import com.jogamp.opengl.GL2ES2
import com.jogamp.opengl.GL4
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLEventListener
import java.io.ByteArrayOutputStream

object Context : GLEventListener {
    lateinit var vb : VertexBuffer
    lateinit var ib: IndexBuffer
    val vertexArrayHandles = IntArray(1)
    var programHandle = -1

    var timer = 0.0f

    override fun reshape(glautodrawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int) {
    }

    override fun init(glautodrawable: GLAutoDrawable) {
        val gl = glautodrawable.gl.gL4
        gl.swapInterval = 1
        println(gl.glGetString(GL4.GL_VERSION))

        val vertices = floatArrayOf(
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f, 0.5f,
            -0.5f, 0.5f
        )

        val indices = intArrayOf(
            0, 1, 2,
            2, 3, 0
        )

        gl.glGenVertexArrays(1, vertexArrayHandles, 0)
        gl.glBindVertexArray(vertexArrayHandles[0])

        vb = VertexBuffer(gl, vertices)

        gl.glEnableVertexAttribArray(0)
        gl.glVertexAttribPointer(0, 2, GL4.GL_FLOAT, false, 8, 0)

        ib = IndexBuffer(gl, indices)

        programHandle = Util.createShaderProgram(
            gl,
            "chernoShader.vert",
            "chernoShader.frag"
        )
    }

    override fun dispose(glautodrawable: GLAutoDrawable) {
        val gl = glautodrawable.gl.gL4
        gl.glDeleteProgram(programHandle)
        vb.dispose()
        ib.dispose()
    }

    override fun display(glautodrawable: GLAutoDrawable) {
        val gl = glautodrawable.gl.gL4

        gl.glClearColor(0.1f, 0.0f, 0.1f, 1.0f)
        gl.glClear(GL4.GL_COLOR_BUFFER_BIT)

        gl.glUseProgram(programHandle)

        val red = (Math.cos(2f * Math.PI * timer * 4) + 1f).toFloat() / 2
        gl.glUniform4f(gl.glGetUniformLocation(programHandle, "uColor"), red, 0.05f, 0.7f, 1.0f)
        timer = (timer + 1f / 360f) % 1f

        gl.glEnableVertexAttribArray(0)
        gl.glBindVertexArray(vertexArrayHandles[0])
        vb.bind()
        ib.bind()

        gl.glDrawElements(GL4.GL_TRIANGLES, 6, GL4.GL_UNSIGNED_INT, 0)

        gl.glDisableVertexAttribArray(0)
        vb.unbind()
        ib.unbind()
        gl.glUseProgram(0)
    }
}

object Util {
    fun createShaderProgram(gl: GL2ES2, vertexShader: String, fragmentShader: String): Int {
        val vertexCode = loadShaderResource(vertexShader)
        val fragmentCode = loadShaderResource(fragmentShader)
        val programHandle = gl.glCreateProgram()
        val vertexHandle = compileShader(gl, GL4.GL_VERTEX_SHADER, vertexCode)
        val fragmentHandle = compileShader(gl, GL4.GL_FRAGMENT_SHADER, fragmentCode)
        gl.glAttachShader(programHandle, vertexHandle)
        gl.glAttachShader(programHandle, fragmentHandle)
        gl.glLinkProgram(programHandle)
        gl.glValidateProgram(programHandle)
        gl.glDeleteShader(vertexHandle)
        gl.glDeleteShader(fragmentHandle)
        return programHandle
    }

    fun compileShader(gl: GL2ES2, type: Int, code: String): Int {
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