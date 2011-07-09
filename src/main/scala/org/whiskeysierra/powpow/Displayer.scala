package org.whiskeysierra.powpow

import com.jogamp.opengl.util.FPSAnimator
import de.bht.jvr.core.Context
import de.bht.jvr.core.pipeline.Pipeline
import de.bht.jvr.util.awt.InputState
import javax.media.opengl.awt.GLCanvas
import javax.swing.JFrame
import java.awt.Dimension
import javax.media.opengl._

class Displayer(val pipeline: Pipeline, private val input: InputState) extends Actor with GLEventListener {

    private val frame: JFrame = new JFrame("PowPow")
    private val canvas: GLCanvas = new GLCanvas
    private val animator: FPSAnimator = new FPSAnimator(canvas, 60)

    private var gl: GL2GL3 = null
    private var context: Context = null

    override def init(drawable: GLAutoDrawable) {
        if (drawable.getGL eq null) {
            return
        } else {
            gl = drawable.getGL.getGL2GL3
            gl.setSwapInterval(1);
            context = new Context(gl)

            gl.glEnable(GL.GL_LINE_SMOOTH)
            gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST)
        }
    }

    override def display(drawable: GLAutoDrawable) {
        if (animator.isAnimating) {
            pipeline.update()
            pipeline.render(context)
            sender ! Update
        }
    }

    override def reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int) {

    }

    override def dispose(drawable: GLAutoDrawable) {

    }

    override def act(message: Any) {
        message match {
            case Start =>
                canvas.addGLEventListener(this)
                canvas.addKeyListener(input)
                frame.getContentPane.add(canvas)
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
                frame.setMinimumSize(new Dimension(600, 600))
                frame.setResizable(false)
                frame.setVisible(true)
                canvas.requestFocusInWindow
                animator.start
            case PoisonPill =>
                animator.stop
                frame.setVisible(false)
                frame.dispose()
                exit()
            case _ =>
        }
    }

}