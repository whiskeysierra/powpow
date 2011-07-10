package org.whiskeysierra.powpow

import com.bulletphysics.collision.shapes.SphereShape
import javax.vecmath.Vector3f
import javax.media.opengl.GL2ES2
import de.bht.jvr.math.Vector4
import de.bht.jvr.core.uniforms.UniformVector4
import de.bht.jvr.core._

class Ship(private val node: SceneNode) extends Actor with Physical with Collidable with ResourceLoader {

    val shape = new SphereShape(.5f)
    override val boost = 7f

    override def act(message:Any) {
        message match {
            case Start =>
                sender ! AddBody(body, Collisions.SHIP, Collisions.WITH_SHIP)

                val vs = new Shader(load("minimal.vs"), GL2ES2.GL_VERTEX_SHADER)
                val fs = new Shader(load("color.fs"), GL2ES2.GL_FRAGMENT_SHADER)

                val program = new ShaderProgram(vs, fs)
                val material = new ShaderMaterial("AMBIENT", program)

                material.setUniform("AMBIENT", "color", new UniformVector4(Colors.YELLOW))

                Finder.find(node, classOf[ShapeNode], null).setMaterial(material)
            case Move(movement) =>
                velocity = movement.length
                direction = movement.normalize
            case Stop =>
                velocity = 0
            case Update =>
                update
                sender ! Position(position)
            case PoisonPill => exit()
            case _ =>
        }
    }

    private def angle: Float = {
        if (direction.x == -1) {
            math.Pi.toFloat
        } else {
            2 * math.atan(direction.y / (1 + direction.x)).toFloat
        }
    }

    private def update = {
        node.setTransform(
            Transform.translate(position.x, position.y, 0) mul
                Transform.rotateZ(angle - math.Pi.toFloat / 4f)
        )
    }

}