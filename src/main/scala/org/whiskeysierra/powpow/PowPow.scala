package org.whiskeysierra.powpow

import com.google.common.io.Resources
import de.bht.jvr.collada14.loader.ColladaLoader
import de.bht.jvr.core.{SceneNode, GroupNode, Shader, ShaderProgram, ShaderMaterial, PointLightNode, CameraNode, Transform, Printer, Finder, ShapeNode}
import de.bht.jvr.core.pipeline.Pipeline
import de.bht.jvr.renderer.{RenderWindow, AwtRenderWindow, Viewer}
import de.bht.jvr.util.{InputState, StopWatch}
import javax.media.opengl.GL2ES2
import java.awt.Color
import java.io.InputStream
import scala.actors.Actor
import scala.util.control.Breaks._

object PowPow {

    def main(args:Array[String]) {
        
        val root:GroupNode = new GroupNode("scene root")

        val vertexShader:Shader = new Shader(open("lighting.vs"), GL2ES2.GL_VERTEX_SHADER)
        val fragmentShader:Shader = new Shader(open("lighting.fs"), GL2ES2.GL_FRAGMENT_SHADER)
        val program:ShaderProgram = new ShaderProgram(vertexShader, fragmentShader)
        
        val material:ShaderMaterial = new ShaderMaterial
        material.setShaderProgram("LIGHTING", program)

        val box:SceneNode = ColladaLoader.load(open("models/box.dae"))
        
        val a:SceneNode = load("axis")
        a.setTransform(Transform.scale(0.01f))
        
        val boxGroup:GroupNode = new GroupNode("Box+Axis")
        boxGroup.addChildNodes(box, a)
        
        Finder.find(box, classOf[ShapeNode], null).setMaterial(material);
        
        val axis:SceneNode = load("axis")
        axis.setTransform(Transform.scale(0.01f))
        
        val light:PointLightNode = new PointLightNode("sun")
        light.setTransform(Transform.translate(3, 0, 3))

        val width:Int = 1000
        val height:Int = 625
        
        val cameraNode:CameraNode = new CameraNode("camera", width.toFloat / height.toFloat, 60)
        cameraNode.setTransform(Transform.rotateDeg(1, 0, 0, -90).mul(Transform.translate(0, 0, 3)))

        root.addChildNodes(axis, boxGroup, light, cameraNode)
        Printer.print(root)

        val pipeline:Pipeline = new Pipeline(root)
        pipeline.clearBuffers(true, true, new Color(0, 0, 0))
        pipeline.switchCamera(cameraNode)
        pipeline.drawGeometry("AMBIENT", null)
        pipeline.doLightLoop(true, true).drawGeometry("LIGHTING", null)

        val input:InputState = new InputState
        val window:RenderWindow = new AwtRenderWindow(pipeline, width, height)
        window.addKeyListener(input)
        
        val cube:Cube = new Cube(boxGroup)
        val camera:Camera = new Camera(cameraNode)
        var keyboard:Keyboard = new Keyboard(input)
        val controller:GameController = GameController.getOrFake
        
        val actors:List[Actor] = List(cube, camera, keyboard, controller)
        actors foreach {_.start}
        val hub:MessageHub = new MessageHub(actors)
        hub.start
        
        val time:StopWatch = new StopWatch
        val viewer:Viewer = new Viewer(window)

        breakable {
            while (viewer.isRunning) {
                val elapsed:Float = time.elapsed
                
                hub ! Update(elapsed)
                
                if (input.isDown('Q')) {
                    break
                }
    
                viewer.display
            }
        }

        hub ! Exit
        
        viewer.close
    }
    
    private def load(model:String):SceneNode = ColladaLoader.load(open("models/" + model + ".dae"))
    
    private def open(fileName:String):InputStream = Resources.getResource(fileName).openStream
    
}