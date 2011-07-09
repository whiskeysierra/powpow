package org.whiskeysierra.powpow

import de.bht.jvr.collada14.loader.ColladaLoader
import de.bht.jvr.util.Color
import de.bht.jvr.util.awt.InputState
import javax.media.opengl.GL2ES2
import de.bht.jvr.core._
import pipeline.{PipelineState, PipelineCommand, PipelineCommandPtr, Pipeline}
import uniforms.UniformFloat
import java.io.File
import java.lang.reflect.Field

object PowPow extends ResourceLoader {

    def main(args: Array[String]) {

        val root: GroupNode = new GroupNode("scene root")

        val box: SceneNode = loadModel("box")

        // TODO this should be somewhere else
        val width: Int = 600
        val height: Int = 600

        val cameraNode: CameraNode = new CameraNode("camera", width.toFloat / height.toFloat, 60)
        val bullets: GroupNode = new GroupNode("Bullets")
        val grid: GroupNode = new GroupNode("Grid")
        val swarm: GroupNode = new GroupNode("Swarm")
        val squadron: GroupNode = new GroupNode("Squadron")

        root.addChildNodes(box, bullets, grid, swarm, squadron, cameraNode)

        Printer.print(root)

        val vs = new Shader(load("quad.vs"), GL2ES2.GL_VERTEX_SHADER)
        val fs = new Shader(load("blur.fs"), GL2ES2.GL_FRAGMENT_SHADER)
        val program = new ShaderProgram(vs, fs)

        val material = new ShaderMaterial("GlowPass", program)

        val pipeline: Pipeline = new Pipeline(root)
        val addCommand = classOf[Pipeline].getDeclaredMethod("addCommand", classOf[PipelineCommand]);
        addCommand.setAccessible(true)

        pipeline.createFrameBufferObject("GlowMap", true, 1, 1.0f, 0)
        pipeline.switchFrameBufferObject("GlowMap")

        pipeline.clearBuffers(true, true, new Color(0, 0, 0))
        pipeline.switchCamera(cameraNode)
        addCommand.invoke(pipeline, new EnableWireframe)
        pipeline.drawGeometry("AMBIENT", null)
        addCommand.invoke(pipeline, new DisableWireframe)

        pipeline.switchFrameBufferObject(null)

        pipeline.clearBuffers(true, true, new Color(0, 0, 0))
        pipeline.bindColorBuffer("jvr_Texture0", "GlowMap", 0)
        pipeline.drawQuad(material, "GlowPass")

        val input: InputState = new InputState

        val hub: Actor = new MessageHub(Map(
            "displayer" -> new Displayer(pipeline, input),
            "space" -> new Space,
            "grid" -> new Grid(grid),
            "ship" -> new Ship(box),
            "camera" -> new Camera(cameraNode),
            "gun" -> new Gun(bullets),
            "swarm" -> new Swarm(swarm),
            "squadron" -> new Squadron(squadron, loadModel("sphere")),
            "keyboard" -> new Keyboard(input),
            "controller1" -> GameController(0),
            "controller2" -> GameController(1)
        ))

        hub.start()
        hub ! Start
    }

    private def loadModel(model: String): SceneNode = ColladaLoader.load(open("models/" + model + ".dae"))

}