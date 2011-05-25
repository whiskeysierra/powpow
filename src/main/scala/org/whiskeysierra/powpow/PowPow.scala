package org.whiskeysierra.powpow

import com.google.common.io.Resources
import de.bht.jvr.collada14.loader.ColladaLoader
import de.bht.jvr.core.{SceneNode, GroupNode, Shader, ShaderProgram, ShaderMaterial, PointLightNode, CameraNode, Transform, Printer, Finder, ShapeNode}
import de.bht.jvr.core.pipeline.Pipeline
import de.bht.jvr.util.Color
import de.bht.jvr.renderer.{RenderWindow, AwtRenderWindow, Viewer}
import de.bht.jvr.util.{InputState, StopWatch}
import javax.media.opengl.GL2ES2
import java.io.InputStream
import scala.actors.Actor

object PowPow extends ResourceLoader {

    def main(args:Array[String]) {
        
        val root:GroupNode = new GroupNode("scene root")

        val vertexShader:Shader = new Shader(open("lighting.vs"), GL2ES2.GL_VERTEX_SHADER)
        val fragmentShader:Shader = new Shader(open("lighting.fs"), GL2ES2.GL_FRAGMENT_SHADER)
        val program:ShaderProgram = new ShaderProgram(vertexShader, fragmentShader)
        
        val material:ShaderMaterial = new ShaderMaterial
        material.setShaderProgram("LIGHTING", program)

        val boxNode:SceneNode = loadModel("box")
        
        val boxAxis:SceneNode = loadModel("axis")
        boxAxis.setTransform(Transform.scale(0.01f))
        
        val box:GroupNode = new GroupNode("Box+Axis")
        box.addChildNodes(boxNode, boxAxis)
        
        Finder.find(boxNode, classOf[ShapeNode], null).setMaterial(material);
        
        val axis:SceneNode = loadModel("axis")
        axis.setTransform(Transform.scale(0.01f))
        
        val sphere:SceneNode = loadModel("sphere")
        
        val light:PointLightNode = new PointLightNode("sun")
        light.setTransform(Transform.translate(3, 0, 3))

        val width:Int = 1000
        val height:Int = 625
        
        val cameraNode:CameraNode = new CameraNode("camera", width.toFloat / height.toFloat, 60)
        
        val bullets:GroupNode = new GroupNode("Bullets")

        root.addChildNodes(axis, box, sphere, bullets, light, cameraNode)
        
        Printer.print(root)

        val pipeline:Pipeline = new Pipeline(root)
        pipeline.clearBuffers(true, true, new Color(0, 0, 0))
        pipeline.switchCamera(cameraNode)
        pipeline.drawGeometry("AMBIENT", null)
        pipeline.doLightLoop(true, true).drawGeometry("LIGHTING", null)

        val input:InputState = new InputState
        val window:RenderWindow = new AwtRenderWindow(pipeline, width, height)
        window.addKeyListener(input)
        
        val hub:Actor = new MessageHub(Map(
            "displayer" -> new Displayer(window),
            "space" -> new Space,
            "ship" -> new Ship(box),
            "camera" -> new Camera(cameraNode),
            "gun" -> new Gun(bullets, sphere),
            "keyboard" -> new Keyboard(input),
            "controller1" -> GameController(0),
            "controller2" -> GameController(1)
        )).start
        
        hub ! Start
    }
    
    private def loadModel(model:String):SceneNode = ColladaLoader.load(open("models/" + model + ".dae"))
    
}