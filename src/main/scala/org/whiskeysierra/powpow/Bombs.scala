package org.whiskeysierra.powpow

import java.lang.Iterable
import java.util.List
import com.google.common.base.{Predicate, Function}
import com.google.common.collect.{Iterables, Lists}
import javax.media.opengl.{GL2GL3, GL3, GL2ES2, GL}
import de.bht.jvr.core._
import attributes.{AttributeFloat, AttributeVector3}
import uniforms.UniformVector4
import de.bht.jvr.math.Vector3

class Bombs(private val parent: GroupNode) extends Actor with ResourceLoader {

    private val max = 100
    val loop = 1f
    private val emitRate = 12
    private val cloud = new AttributeCloud(max, GL.GL_POINTS)

    private val bombs: List[Bomb] = Lists.newArrayListWithCapacity(max)

    private val positions: List[Vector3] = Lists.transform(bombs, new Function[Bomb, Vector3] {
        override def apply(p: Bomb) = p.position
    })

    private val directions: List[Vector3] = Lists.transform(bombs, new Function[Bomb, Vector3] {
        override def apply(p: Bomb) = p.direction
    })

    private val deads: Iterable[Bomb] = Iterables.filter(bombs, new Predicate[Bomb] {
        override def apply(s: Bomb) = s.dead
    })

    private val health: Array[Float] = Array.fill(max) {
        0f
    }

    override def act(message: Any) {
        message match {
            case Start =>
                val shape: ShapeNode = new ShapeNode("Swarm")

                val vs = new Shader(load("bombs.vs"), GL2ES2.GL_VERTEX_SHADER)
                val gs = new Shader(load("bombs.gs"), GL3.GL_GEOMETRY_SHADER)
                val fs = new Shader(load("color.fs"), GL2ES2.GL_FRAGMENT_SHADER)

                val program = new ShaderProgram(vs, fs, gs)

                program.setParameter(GL2GL3.GL_GEOMETRY_INPUT_TYPE_ARB, GL.GL_POINTS)
                program.setParameter(GL2GL3.GL_GEOMETRY_OUTPUT_TYPE_ARB, GL.GL_LINE_STRIP)
                program.setParameter(GL2GL3.GL_GEOMETRY_VERTICES_OUT_ARB, 8)

                val material = new ShaderMaterial("AMBIENT", program)

                material.setUniform("AMBIENT", "color", new UniformVector4(Colors.GREEN))

                shape.setGeometry(cloud)
                shape.setMaterial(material)

                for (i <- 0 until max) {
                    val bomb = new Bomb()
                    bomb.kill()
                    bombs.add(bomb)
                }

                parent.addChildNode(shape)
            case Update => update()
            case BomberKill(bomber) =>
                val step = math.Pi.toFloat * 2 / emitRate
                for (i <- 0 until emitRate) {
                    val it = deads.iterator()
                    if (it.hasNext) {
                        val bomb = it.next();
                        bomb.revive()
                        bomb.direction = fromAngle(step * i)
                        bomb.position = bomber.position
                        sender ! AddBody(bomb.body, Collisions.BOMB, Collisions.WITH_BOMB)
                    }
                }
            case BombWallHit(bomb) =>
                bomb.kill()
                sender ! RemoveBody(bomb.body)
            case BombCollision(bomb, _, _) =>
                bomb.kill()
                sender ! RemoveBody(bomb.body)
            case PoisonPill => exit()
            case _ =>
        }
    }

    private def fromAngle(angle:Float) = new Vector3(math.sin(angle).toFloat, math.cos(angle).toFloat, 0)

    private def update() {
        for (i <- 0 until max) {
            val bomb = bombs.get(i)
            health.update(i, bomb.health)
            if (bomb.dead) {
                sender ! RemoveBody(bomb.body)
            }
        }

        cloud.setAttribute("position", new AttributeVector3(positions))
        cloud.setAttribute("direction", new AttributeVector3(directions))
        cloud.setAttribute("energy", new AttributeFloat(health))
    }

}