package org.whiskeysierra.powpow

import scala.actors.Actor
import org.whiskeysierra.powpow.input.Keyboard
import com.google.common.io.Resources
import de.bht.jvr.collada14.loader.ColladaLoader
import de.bht.jvr.core.{SceneNode, GroupNode, Shader, ShaderProgram, ShaderMaterial, PointLightNode, CameraNode, Transform, Printer, Finder, ShapeNode}
import de.bht.jvr.core.pipeline.Pipeline
import de.bht.jvr.renderer.{RenderWindow, AwtRenderWindow, Viewer}
import de.bht.jvr.util.{InputState, StopWatch}
import javax.media.opengl.GL2ES2
import java.awt.Color
import java.io.InputStream
import scala.util.control.Breaks._

object PowPow {

    def main(args:Array[String]) {
        
        val root:GroupNode = new GroupNode("scene root")

        val vertexShader:Shader = new Shader(open("lighting.vs"), GL2ES2.GL_VERTEX_SHADER)
        val fragmentShader:Shader = new Shader(open("lighting.fs"), GL2ES2.GL_FRAGMENT_SHADER)
        val program:ShaderProgram = new ShaderProgram(vertexShader, fragmentShader)
        
        val material:ShaderMaterial = new ShaderMaterial
        material.setShaderProgram("LIGHTING", program)

        val box:SceneNode = ColladaLoader.load(open("box.dae"))
        Finder.find(box, classOf[ShapeNode], null).setMaterial(material);
        
        val bottom:SceneNode = ColladaLoader.load(open("box.dae"))
        bottom.setTransform(Transform.translate(0, -1, 0).mul(Transform.scale(10, 0.01f, 5)))

        val light:PointLightNode = new PointLightNode("sun")
        light.setTransform(Transform.translate(3, 0, 3))

        val width:Int = 1000
        val height:Int = 625
        
        val camera:CameraNode = new CameraNode("camera", width.toFloat / height.toFloat, 60)
        camera.setTransform(Transform.rotateDeg(1, 0, 0, -90).mul(Transform.translate(0, 0, 3)))

        root.addChildNodes(box, bottom, light, camera)
        Printer.print(root)

        val pipeline:Pipeline = new Pipeline(root)
        pipeline.clearBuffers(true, true, new Color(0, 0, 0))
        pipeline.switchCamera(camera)
        pipeline.drawGeometry("AMBIENT", null)
        pipeline.doLightLoop(true, true).drawGeometry("LIGHTING", null)

        val input:InputState = new InputState
        val window:RenderWindow = new AwtRenderWindow(pipeline, width, height)
        window.addKeyListener(input)
        
        val cam:Camera = new Camera(camera)
        val cube:Cube = new Cube(box)
        
        var inputActor:Actor = null
        
        if (GameController.isPresent) {
            inputActor = new GameController(cube)
        } else {
            inputActor = new Keyboard(cube, input)
        }
        
        cube.start
        inputActor.start
        
        val time:StopWatch = new StopWatch
        val viewer:Viewer = new Viewer(window)

        breakable {
            while (viewer.isRunning) {
                val elapsed:Float = time.elapsed
                cam.update(elapsed, input)
                
                val update:Update = Update(elapsed)
                
                inputActor ! update
                cube ! update
    
                if (input.isDown('Q')) {
                    break
                }
    
                viewer.display
            }
        }
        
        cube ! Exit
        inputActor ! Exit
        
        viewer.close
    }
    
    private def open(fileName:String):InputStream = Resources.getResource(fileName).openStream
    
}