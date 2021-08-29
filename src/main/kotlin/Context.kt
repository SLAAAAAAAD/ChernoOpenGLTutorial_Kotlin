import com.jogamp.opengl.GL2ES2
import com.jogamp.opengl.GL4
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.GLEventListener
import java.io.ByteArrayOutputStream

object Context : GLEventListener {
    lateinit var vb: VertexBuffer
    lateinit var ib: IndexBuffer
    lateinit var va: VertexArray
    lateinit var layout: VertexBufferLayout
    lateinit var shaderProgram: VFShader
    val vertexArrayHandles = IntArray(1)

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


        va = VertexArray(gl)
        vb = VertexBuffer(gl, vertices)
        layout = VertexBufferLayout()
        layout.addFloat(2)
        va.addBuffer(vb, layout)
        ib = IndexBuffer(gl, indices)

        shaderProgram = VFShader(gl, "chernoShader.vert", "chernoShader.frag")

    }

    override fun dispose(glautodrawable: GLAutoDrawable) {
        shaderProgram.dispose()
        va.dispose()
        vb.dispose()
        ib.dispose()
    }

    override fun display(glautodrawable: GLAutoDrawable) {
        val gl = glautodrawable.gl.gL4

        gl.glClearColor(0.1f, 0.0f, 0.1f, 1.0f)
        gl.glClear(GL4.GL_COLOR_BUFFER_BIT)

        shaderProgram.use()

        val red = (Math.cos(2f * Math.PI * timer * 4) + 1f).toFloat() / 2
        shaderProgram.setUniform4f("uColor", Vector4(red, 0.05f, 0.7f, 1.0f))
        timer = (timer + 1f / 360f) % 1f

        va.bind()
        vb.bind()
        ib.bind()

        gl.glDrawElements(GL4.GL_TRIANGLES, 6, GL4.GL_UNSIGNED_INT, 0)

        va.unbind()
        vb.unbind()
        ib.unbind()
        shaderProgram.dontUse()
    }
}