import com.jogamp.newt.event.KeyEvent
import com.jogamp.opengl.GLCapabilities
import com.jogamp.opengl.GLProfile
import com.jogamp.opengl.awt.GLCanvas
import java.awt.BorderLayout
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame

class ChernoOpenGLTutorial {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val glprofile = GLProfile.get(GLProfile.GL4ES3)
            val glcapabilities = GLCapabilities(glprofile)
            val glcanvas = GLCanvas(glcapabilities)
            val jframe = JFrame("OpenGL")
            var stopped = false

            glcanvas.addGLEventListener(Context)

            jframe.contentPane.add(glcanvas, BorderLayout.CENTER)
            jframe.setSize(800, 600)
            jframe.isVisible = true
            jframe.addWindowListener(object : WindowAdapter() {
                override fun windowClosing(windowevent: WindowEvent) {
                    jframe.dispose()
                    stopped = true
                    System.exit(0)
                }
            })

            while (!stopped) {
                glcanvas.display()
                stopped = !jframe.isVisible
            }
            glcanvas.destroy()
        }
    }

}
