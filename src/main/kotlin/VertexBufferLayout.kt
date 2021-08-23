import com.jogamp.opengl.GL4

class VertexBufferElement(val type: Int, val count: Int, val normalized: Boolean, val typeSize: Int)

class VertexBufferLayout {
    val elements: ArrayList<VertexBufferElement> = arrayListOf();
    var stride: Int = 0
        private set

    fun pushFloat(count: Int) {
        elements.add(VertexBufferElement(GL4.GL_FLOAT, count, false, Float.SIZE_BYTES))
        stride += Float.SIZE_BYTES * count
    }

    fun pushInt(count: Int) {
        elements.add(VertexBufferElement(GL4.GL_INT, count, false, Int.SIZE_BYTES))
        stride += Int.SIZE_BYTES * count
    }
}