package org.whiskeysierra.powpow

import com.google.common.io.Resources
import de.bht.jvr.collada14.loader.ColladaLoader
import de.bht.jvr.core.{Transform, SceneNode, Printer, PointLightNode, GroupNode, CameraNode}
import de.bht.jvr.core.pipeline.Pipeline
import de.bht.jvr.renderer.{Viewer, RenderWindow, AwtRenderWindow}
import de.bht.jvr.util.{StopWatch, InputState}
import java.awt.Color
import java.awt.event.KeyEvent

import scala.util.control.Breaks._

object PowPow {

    def main(args:Array[String]) {
        
        val root:GroupNode = new GroupNode("scene root")
        val box:SceneNode = ColladaLoader.load(Resources.getResource("box.dae").openStream);

        val light:PointLightNode = new PointLightNode("sun")
        light.setTransform(Transform.translate(3, 0, 3))

        val camera:CameraNode = new CameraNode("camera", 4f / 3f, 60)
        camera.setTransform(Transform.translate(0, 0, 3))

        root.addChildNodes(box, light, camera)

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
                
                if (input.isOneDown('S', KeyEvent.VK_DOWN)) {
                    angleX -= elapsed * speed
                }
                
                if (input.isOneDown('D', KeyEvent.VK_RIGHT)) {
                    angleY += elapsed * speed
                }
                
                if (input.isOneDown('A', KeyEvent.VK_LEFT)) {
                    angleY -= elapsed * speed
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