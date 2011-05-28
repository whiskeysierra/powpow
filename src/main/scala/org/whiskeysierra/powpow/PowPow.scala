package org.whiskeysierra.powpow

import com.google.common.io.Resources
import de.bht.jvr.collada14.loader.ColladaLoader
import de.bht.jvr.core.{SceneNode, GroupNode, PointLightNode, CameraNode, Transform, Printer}
import de.bht.jvr.core.pipeline.Pipeline
import de.bht.jvr.util.Color
import de.bht.jvr.util.awt.InputState
import java.io.InputStream
import scala.actors.Actor

object PowPow extends ResourceLoader {

    def main(args:Array[String]) {
        
        val root:GroupNode = new GroupNode("scene root")

        val boxNode:SceneNode = loadModel("box")
        
        val boxAxis:SceneNode = loadModel("axis")
        boxAxis.setTransform(Transform.scale(0.01f))
        
        val box:GroupNode = new GroupNode("Box+Axis")
        box.addChildNodes(boxNode, boxAxis)
        
        val axis:SceneNode = loadModel("axis")
        axis.setTransform(Transform.scale(0.01f))
        
        val sphere:SceneNode = loadModel("sphere")
        
//        val grid:SceneNode = loadModel("grid")
//        grid.setTransform(Transform.translate(0, 0, -50) mul Transform.rotateYDeg(90))
        
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
        
        val hub:Actor = new MessageHub(Map(
            "displayer" -> new Displayer(pipeline, input),
            "space" -> new Space,
            "ship" -> new Ship(box),
            "camera" -> new Camera(cameraNode),
            "gun" -> new Gun(bullets),
            "keyboard" -> new Keyboard(input),
            "controller1" -> GameController(0),
            "controller2" -> GameController(1)
        )).start
        
        hub ! Start
    }
    
    private def loadModel(model:String):SceneNode = ColladaLoader.load(open("models/" + model + ".dae"))
    
}