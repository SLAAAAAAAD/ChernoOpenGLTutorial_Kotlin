import com.jogamp.common.nio.Buffers
import com.jogamp.opengl.GL4
import java.nio.IntBuffer

class IndexBuffer(gl: GL4, rawData: IntArray) : BufferBase(gl, target)  {
    private val data: IntBuffer
    init {
        data = Buffers.newDirectIntBuffer(rawData)
        gl.glBufferData(target, data.limit() * 4L, data, GL4.GL_STATIC_DRAW)
    }

    companion object{
        const val target = GL4.GL_ELEMENT_ARRAY_BUFFER
    }
}