import com.jogamp.opengl.GL2ES2
import com.jogamp.opengl.GL4
import java.io.ByteArrayOutputStream

class VFShader(val gl: GL4, val vertexFileName: String, val fragmentFileName: String) {
    val handle: Int
    private val uniformLocations: HashMap<String, Int> = HashMap()

    init {
        handle = createShaderProgram()
    }

    fun use() {
        gl.glUseProgram(handle)
    }

    fun dontUse() {
        gl.glUseProgram(0)
    }

    fun dispose() {
        gl.glDeleteProgram(handle)
    }

    fun setUniform4f(name: String, values: Vector4) {
        gl.glUniform4f(getUniformLocation(name), values.x, values.y, values.z, values.w)
    }

    fun getUniformLocation(name: String): Int {
        if (uniformLocations.containsKey(name)) {
            return uniformLocations[name]!!
        }

        val location = gl.glGetUniformLocation(handle, name)
        if (location == -1) {
            println("Uniform '" + name + "' was not found in the program.")
        }
        uniformLocations[name] = location
        return location
    }

    fun createShaderProgram(): Int {
        val vertexCode = loadShaderResource(vertexFileName)
        val fragmentCode = loadShaderResource(fragmentFileName)
        val programHandle = gl.glCreateProgram()
        val vertexHandle = compileShader(GL4.GL_VERTEX_SHADER, vertexCode)
        val fragmentHandle = compileShader(GL4.GL_FRAGMENT_SHADER, fragmentCode)
        gl.glAttachShader(programHandle, vertexHandle)
        gl.glAttachShader(programHandle, fragmentHandle)
        gl.glLinkProgram(programHandle)
        gl.glValidateProgram(programHandle)
        gl.glDeleteShader(vertexHandle)
        gl.glDeleteShader(fragmentHandle)
        return programHandle
    }

    fun compileShader(type: Int, code: String): Int {
        val shaderHandle = gl.glCreateShader(type)
        val lines = arrayOf(code)
        gl.glShaderSource(shaderHandle, lines.size, lines, intArrayOf(lines[0].length), 0)
        gl.glCompileShader(shaderHandle)

        // check if it worked and forward the error if it didnt
        val compiled = IntArray(1)
        gl.glGetShaderiv(shaderHandle, GL2ES2.GL_COMPILE_STATUS, compiled, 0)
        if (compiled[0] == 0) {
            val logLength = IntArray(1)
            gl.glGetShaderiv(shaderHandle, GL2ES2.GL_INFO_LOG_LENGTH, logLength, 0)
            val log = ByteArray(logLength[0])
            gl.glGetShaderInfoLog(shaderHandle, logLength[0], null as IntArray?, 0, log, 0)
            throw IllegalStateException("Error compiling the shader: " + String(log))
        }
        return shaderHandle
    }

    fun loadShaderResource(name: String): String {
        val buffer = ByteArray(1024)
        var nr: Int
        javaClass.getResourceAsStream(if (name[0] != '/' || name[0] != '.') '/' + name else name).use {
            ByteArrayOutputStream().use { out ->
                while (it!!.read(buffer).also { nr = it } > 0) out.write(buffer, 0, nr)
                return String(out.toByteArray(), charset("UTF-8"))
            }
        }
    }
}