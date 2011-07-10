package org.whiskeysierra.powpow

import de.bht.jvr.collada14.loader.ColladaLoader
import de.bht.jvr.util.Color
import de.bht.jvr.util.awt.InputState
import javax.media.opengl.GL2ES2
import de.bht.jvr.core._
import pipeline.{PipelineCommand, Pipeline}

object PowPow extends ResourceLoader {

    def main(args: Array[String]) {

        val root: GroupNode = new GroupNode("scene root")

        val box: SceneNode = loadModel("box")

        val cameraNode = new CameraNode("camera", 1, 60)
        val bullets = new GroupNode("Bullets")
        val grid = new GroupNode("Grid")
        val swarm = new GroupNode("Swarm")
        val squadron = new GroupNode("Squadron")
        val explosions = new GroupNode("Particles")
        val bombs = new GroupNode("Bombs")

        root.addChildNodes(box, bullets, grid, swarm, squadron, explosions, bombs, cameraNode)

        val vs = new Shader(load("quad.vs"), GL2ES2.GL_VERTEX_SHADER)
        val fs = new Shader(load("bloom.fs"), GL2ES2.GL_FRAGMENT_SHADER)
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
            "particles" -> new Particles(explosions),
            "bombs" -> new Bombs(bombs),
            "keyboard" -> new Keyboard(input),
            "controller1" -> GameController(0),
            "controller2" -> GameController(1)
        ))

        hub ! Start
    }

    private def loadModel(model: String): SceneNode = ColladaLoader.load(load("models/" + model + ".dae"))

}