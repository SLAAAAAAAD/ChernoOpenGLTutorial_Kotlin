import com.jogamp.opengl.GL4

class VertexArray(val gl: GL4) {
    val handle: Int

    init {
        val tempHandleArray = IntArray(1)
        gl.glGenVertexArrays(1, tempHandleArray, 0)
        handle = tempHandleArray[0]
        gl.glBindVertexArray(handle)
    }

    fun addBuffer(vb: VertexBuffer, layout: VertexBufferLayout) {
        bind()
        vb.bind()
        var offset = 0L
        layout.elements.forEachIndexed { i, vbe ->
            gl.glEnableVertexAttribArray(i)
            gl.glVertexAttribPointer(i, vbe.count, vbe.type, vbe.normalized, layout.stride, offset)
            offset += vbe.count * vbe.typeSize
        }
    }

    fun bind() {
        gl.glBindVertexArray(handle)
    }

    fun unbind(){
        gl.glBindVertexArray(0)
    }

    fun dispose() {
        gl.glDeleteVertexArrays(1, intArrayOf(handle), 0)
    }
}