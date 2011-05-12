package org.whiskeysierra.powpow

import de.bht.jvr.core.ShapeNode
import de.bht.jvr.core.Finder
import de.bht.jvr.core.Printer
import de.bht.jvr.core.Transform
import java.awt.event.KeyEvent
import de.bht.jvr.renderer.Viewer
import de.bht.jvr.util.StopWatch
import java.awt.Color
import de.bht.jvr.renderer.AwtRenderWindow
import de.bht.jvr.renderer.RenderWindow
import de.bht.jvr.util.InputState
import de.bht.jvr.core.pipeline.Pipeline
import de.bht.jvr.core.CameraNode
import de.bht.jvr.core.PointLightNode
import de.bht.jvr.math.Vector3
import de.bht.jvr.core.uniforms.UniformVector3
import de.bht.jvr.core.ShaderMaterial
import de.bht.jvr.core.ShaderProgram
import javax.media.opengl.GL2ES2
import com.google.common.io.{Resources}
import de.bht.jvr.collada14.loader.ColladaLoader
import de.bht.jvr.core.{SceneNode, GroupNode, Shader}

import java.io.InputStream
import scala.util.control.Breaks._

object PowPow {
    
    def open(fileName:String):InputStream = Resources.getResource(fileName).openStream

    def main(args:Array[String]) {
        
        val root:GroupNode = new GroupNode("scene root")
        val box:SceneNode = ColladaLoader.load(open("box.dae"))

        val vertexShader:Shader = new Shader(open("lighting.vs"), GL2ES2.GL_VERTEX_SHADER)
        val fragmentShader:Shader = new Shader(open("lighting.fs"), GL2ES2.GL_FRAGMENT_SHADER)
        val program:ShaderProgram = new ShaderProgram(vertexShader, fragmentShader)
        
        val material:ShaderMaterial = new ShaderMaterial
        material.setUniform("LIGHTING", "toonColor", new UniformVector3(new Vector3(1, 1, 1)))
        material.setShaderProgram("LIGHTING", program)

        Finder.find(box, classOf[ShapeNode], null).setMaterial(material);
        
        val light:PointLightNode = new PointLightNode("sun")
        light.setTransform(Transform.translate(3, 0, 3))

        val camera:CameraNode = new CameraNode("camera", 4f / 3f, 60)
        camera.setTransform(Transform.translate(0, 0, 3))

        root.addChildNodes(box, light, camera)
        Printer.print(root)

        val pipeline:Pipeline = new Pipeline(root)
        pipeline.clearBuffers(true, true, new Color(0, 0, 0))
        pipeline.switchCamera(camera)
        pipeline.drawGeometry("AMBIENT", null)
        pipeline.doLightLoop(true, true).drawGeometry("LIGHTING", null)

        val input:InputState = new InputState
        val window:RenderWindow = new AwtRenderWindow(pipeline, 800, 600)
        window.addKeyListener(input)

        val time:StopWatch = new StopWatch
        val viewer:Viewer = new Viewer(window)

        var angleY:Float = 0
        var angleX:Float = 0
        val speed:Float = 90

        breakable {
            while (viewer.isRunning) {
                val elapsed:Float = time.elapsed
    
                if (input.isOneDown('W', KeyEvent.VK_UP)) {
                    angleX += elapsed * speed
                }
                
                if (input.isOneDown('A', KeyEvent.VK_LEFT)) {
                    angleY -= elapsed * speed
                }
                
                if (input.isOneDown('S', KeyEvent.VK_DOWN)) {
                    angleX -= elapsed * speed
                }
                
                if (input.isOneDown('D', KeyEvent.VK_RIGHT)) {
                    angleY += elapsed * speed
                }
    
                box.setTransform(Transform.rotateYDeg(angleY).mul(Transform.rotateXDeg(angleX)))
    
                if (input.isDown('Q')) {
                    break
                }
    
                viewer.display
            }
        }
        
        viewer.close
    }
    
}