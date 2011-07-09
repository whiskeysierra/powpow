package org.whiskeysierra.powpow

import collection.mutable.HashSet
import javax.media.opengl.GL2ES2
import de.bht.jvr.core._
import uniforms.UniformVector4

class Squadron(private val parent: GroupNode, private val sphere: SceneNode) extends Actor with Randomizer with Clock
    with ResourceLoader {

    private val bombers = new HashSet[Bomber]
    private val max = 10

    val loop = 3f

    override def act(message:Any) {
        message match {
            case Start =>
                sphere.setTransform(Transform.scale(3))

                val vs = new Shader(load("minimal.vs"), GL2ES2.GL_VERTEX_SHADER)
                val fs = new Shader(load("color.fs"), GL2ES2.GL_FRAGMENT_SHADER)

                val program = new ShaderProgram(vs, fs)
                val material = new ShaderMaterial("AMBIENT", program)

                material.setUniform("AMBIENT", "color", new UniformVector4(Colors.GREEN))

                Finder.find(sphere, classOf[ShapeNode], null).setMaterial(material)

            case Update =>
                if (tick()) {
                    if (bombers.size < max) {
                        val bomber = new Bomber(new GroupNode(sphere))
                        bomber.revive()
                        bomber.position = randomPosition
                        bomber.direction = randomDirection
                        parent.addChildNode(bomber.node)
                        sender ! AddBody(bomber.body, Collisions.BOMBER, Collisions.WITH_BOMBER)
                        bombers.add(bomber)
                    }
                }
                for (bomber <- bombers) {
                    bomber.node.setTransform(Transform.translate(bomber.position))
                }
            case BomberHit(bomber, _) =>
                bomber.hit()
                if (bomber.dead) {
                    parent.removeChildNode(bomber.node)
                    sender ! RemoveBody(bomber.body)
                    bombers.remove(bomber)
                    sender ! BomberKill(bomber)
                }
            case BomberCollision(bomber) =>
                parent.removeChildNode(bomber.node)
                sender ! RemoveBody(bomber.body)
                bombers.remove(bomber)
                sender ! BomberKill(bomber)
            case PoisonPill => exit()
            case _ =>
        }
    }

}