package org.whiskeysierra.powpow

import javax.media.opengl.{GL2GL3, GL3, GL2ES2, GL}
import de.bht.jvr.core._
import attributes.{AttributeFloat, AttributeVector3}
import uniforms.UniformVector4
import java.util.List
import java.lang.Iterable
import com.google.common.collect.{Iterables, Lists}
import com.google.common.base.{Predicate, Function}
import de.bht.jvr.math.{Vector3, Vector4}

class Swarm(private val parent: GroupNode) extends Actor with ResourceLoader with Randomizer with Clock {

    private val max = 100
    val loop = 1f
    private val reviveRate = 20
    private val cloud = new AttributeCloud(max, GL.GL_POINTS)

    private val seekers: List[Seeker] = Lists.newArrayListWithCapacity(max)

    private val positions: List[Vector3] = Lists.transform(seekers, new Function[Seeker, Vector3] {
        override def apply(s: Seeker) = s.position
    });

    private val directions: List[Vector3] = Lists.transform(seekers, new Function[Seeker, Vector3] {
        override def apply(s: Seeker) = s.direction
    });

    private val deads: Iterable[Seeker] = Iterables.filter(seekers, new Predicate[Seeker] {
        override def apply(s: Seeker) = s.dead
    })

    private val health: Array[Float] = Array.fill(max) {
        0f
    }

    override def act(message: Any) {
        message match {
            case Start =>
                val shape: ShapeNode = new ShapeNode("Swarm")

                val vs = new Shader(load("seekers.vs"), GL2ES2.GL_VERTEX_SHADER)
                val gs = new Shader(load("seekers.gs"), GL3.GL_GEOMETRY_SHADER)
                val fs = new Shader(load("color.fs"), GL2ES2.GL_FRAGMENT_SHADER)

                val program = new ShaderProgram(vs, fs, gs)

                program.setParameter(GL2GL3.GL_GEOMETRY_INPUT_TYPE_ARB, GL.GL_POINTS)
                program.setParameter(GL2GL3.GL_GEOMETRY_OUTPUT_TYPE_ARB, GL.GL_LINE_STRIP)
                program.setParameter(GL2GL3.GL_GEOMETRY_VERTICES_OUT_ARB, 3)

                val material = new ShaderMaterial("AMBIENT", program)

                material.setUniform("AMBIENT", "color", new UniformVector4(Colors.BLUE))

                shape.setGeometry(cloud)
                shape.setMaterial(material)

                for (i <- 0 until max) {
                    val seeker = new Seeker()
                    seeker.kill()
                    seekers.add(seeker)
                }

                parent.addChildNode(shape)
            case Update => update()
            case SeekerWallHit(seeker) =>
                sender ! RemoveBody(seeker.body)
                seeker.kill()
            case SeekerHit(seeker, _) =>
                sender ! RemoveBody(seeker.body)
                seeker.kill()
            case SeekerCollision(seeker, _, _) =>
                sender ! RemoveBody(seeker.body)
                seeker.kill()
            case PoisonPill => exit()
            case _ =>
        }
    }

    private def fromAngle(angle:Float) = new Vector3(math.sin(angle).toFloat, math.cos(angle).toFloat, 0)

    private def update() {
        if (tick()) {
            val it = deads.iterator()
            val position = randomPosition
            val step = math.Pi.toFloat * 2 / reviveRate
            for (i <- 0 until reviveRate) {
                if (it.hasNext) {
                    val seeker = it.next();
                    seeker.revive()
                    seeker.direction = fromAngle(step * i)
                    seeker.position = position
                    sender ! AddBody(seeker.body, Collisions.SEEKER, Collisions.WITH_SEEKER)
                }
            }
        }

        for (i <- 0 until max) {
            health.update(i, seekers.get(i).health)
        }

        cloud.setAttribute("position", new AttributeVector3(positions))
        cloud.setAttribute("direction", new AttributeVector3(directions))
        cloud.setAttribute("health", new AttributeFloat(health))
    }

}