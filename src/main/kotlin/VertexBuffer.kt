import com.jogamp.common.nio.Buffers
import com.jogamp.opengl.GL4
import java.nio.FloatBuffer

class VertexBuffer(gl: GL4, rawData: FloatArray) : BufferBase(gl, target) {
    private val data : FloatBuffer
    init {
        data = Buffers.newDirectFloatBuffer(rawData)
        gl.glBufferData(target, data.limit() * 4L, data, GL4.GL_STATIC_DRAW)
    }

    companion object{
        const val target = GL4.GL_ARRAY_BUFFER
    }
}