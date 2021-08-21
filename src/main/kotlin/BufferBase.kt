import com.jogamp.opengl.GL4

abstract class BufferBase(val gl: GL4, val target: Int) {
    var handle : Int
    init {
        val tempBufferHandleArray = IntArray(1)
        gl.glGenBuffers(1, tempBufferHandleArray, 0)
        handle = tempBufferHandleArray[0]
        gl.glBindBuffer(target, handle)
    }

    fun bind(){
        gl.glBindBuffer(target, handle)
    }
    fun unbind(){
        gl.glBindBuffer(target, 0)
    }
    fun dispose(){
        gl.glDeleteBuffers(1, intArrayOf(handle), 0)
    }
}