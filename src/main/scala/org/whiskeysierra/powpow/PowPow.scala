package org.whiskeysierra.powpow

import de.bht.jvr.collada14.loader.ColladaLoader
import de.bht.jvr.util.Color
import de.bht.jvr.util.awt.InputState
import javax.media.opengl.GL2ES2
import de.bht.jvr.core._
import pipeline.{PipelineCommand, Pipeline}
import de.bht.jvr.math.Matrix4
import uniforms.UniformMatrix4

object PowPow extends ResourceLoader {

    def main(args: Array[String]) {

        val root: GroupNode = new GroupNode("scene root")

        val box: SceneNode = loadModel("box")

        val camera = new CameraNode("camera", 1, 60)
        val bullets = new GroupNode("Bullets")
        val grid = new GroupNode("Grid")
        val swarm = new GroupNode("Swarm")
        val squadron = new GroupNode("Squadron")
        val explosions = new GroupNode("Particles")
        val bombs = new GroupNode("Bombs")

        root.addChildNodes(box, bullets, grid, swarm, squadron, explosions, bombs, camera)

        val glow = new ShaderMaterial("GlowPass", new ShaderProgram(
            new Shader(load("quad.vs"), GL2ES2.GL_VERTEX_SHADER),
            new Shader(load("bloom.fs"), GL2ES2.GL_FRAGMENT_SHADER)
        ))

        val blur = new ShaderMaterial("BlurPass", new ShaderProgram(
            new Shader(load("quad.vs"), GL2ES2.GL_VERTEX_SHADER),
            new Shader(load("blur.fs"), GL2ES2.GL_FRAGMENT_SHADER)
        ))

        val pipeline: Pipeline = new Pipeline(root)
        val commandClass = classOf[PipelineCommand]
        val name = "addCommand"
        val addCommand = classOf[Pipeline].getDeclaredMethod(name, commandClass)
        addCommand.setAccessible(true)

        pipeline.switchCamera(camera)

        pipeline.createFrameBufferObject("GlowMap", true, 1, 1.0f, 0)
        pipeline.createFrameBufferObject("SceneMap", true, 1, 1.0f, 0)

        pipeline.switchFrameBufferObject("GlowMap")
        pipeline.clearBuffers(true, true, new Color(0, 0, 0))

        addCommand.invoke(pipeline, new EnableWireframe)
        pipeline.drawGeometry("AMBIENT", null)
        addCommand.invoke(pipeline, new DisableWireframe)

        pipeline.switchFrameBufferObject("SceneMap")
        pipeline.clearBuffers(true, true, new Color(0, 0, 0))

        pipeline.bindColorBuffer("jvr_Texture0", "GlowMap", 0)
        pipeline.drawQuad(glow, "GlowPass")

        pipeline.switchFrameBufferObject(null)
        pipeline.clearBuffers(true, true, new Color(0, 0, 0))

        val projectionViewMatrix = getProjectionViewMatrix(root, camera)

        val inverse = pipeline.setUniform("jvr_InverseProjectionViewMatrix",
            new UniformMatrix4(projectionViewMatrix))

        val previous = pipeline.setUniform("jvr_PreviousProjectionViewMatrix",
            new UniformMatrix4(projectionViewMatrix.inverse))

        pipeline.bindColorBuffer("jvr_Texture0", "SceneMap", 0)
        pipeline.bindDepthBuffer("jvr_Texture1", "SceneMap")
        pipeline.drawQuad(blur, "BlurPass")

        val input: InputState = new InputState

        val hub: Actor = new MessageHub(Map(
            "displayer" -> new Displayer(pipeline, root, inverse, previous, input),
            "space" -> new Space,
            "grid" -> new Grid(grid),
            "ship" -> new Ship(box),
            "camera" -> new Camera(camera),
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

    private def getProjectionViewMatrix(root: SceneNode, camera:CameraNode): Matrix4 =
        camera.getProjectionMatrix.mul(camera.getEyeWorldTransform(root).getInverseMatrix)

}