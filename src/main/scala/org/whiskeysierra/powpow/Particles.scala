package org.whiskeysierra.powpow

import java.lang.Iterable
import java.util.List
import com.google.common.base.{Predicate, Function}
import com.google.common.collect.{Iterables, Lists}
import javax.media.opengl.{GL2GL3, GL3, GL2ES2, GL}
import de.bht.jvr.core._
import attributes.{AttributeVector4, AttributeFloat, AttributeVector3}
import de.bht.jvr.math.{Vector3, Vector4}

class Particles(private val parent: GroupNode) extends Actor with ResourceLoader with Randomizer {

    private val max = 100
    val loop = 1f
    private val emitRate = 5
    private val cloud = new AttributeCloud(max, GL.GL_POINTS)

    private val particles: List[Particle] = Lists.newArrayListWithCapacity(max)

    private val positions: List[Vector3] = Lists.transform(particles, new Function[Particle, Vector3] {
        override def apply(p: Particle) = p.position
    })

    private val directions: List[Vector3] = Lists.transform(particles, new Function[Particle, Vector3] {
        override def apply(p: Particle) = p.direction
    })

    private val colors: List[Vector4] = Lists.transform(particles, new Function[Particle, Vector4] {
        override def apply(p: Particle) = p.color
    })

    private val deads: Iterable[Particle] = Iterables.filter(particles, new Predicate[Particle] {
        override def apply(s: Particle) = s.dead
    })

    private val energy: Array[Float] = Array.fill(max) {
        0f
    }

    override def act(message: Any) {
        message match {
            case Start =>
                val shape: ShapeNode = new ShapeNode("Swarm")

                val vs = new Shader(load("particles.vs"), GL2ES2.GL_VERTEX_SHADER)
                val gs = new Shader(load("particles.gs"), GL3.GL_GEOMETRY_SHADER)
                val fs = new Shader(load("particles.fs"), GL2ES2.GL_FRAGMENT_SHADER)

                val program = new ShaderProgram(vs, fs, gs)

                program.setParameter(GL2GL3.GL_GEOMETRY_INPUT_TYPE_ARB, GL.GL_POINTS)
                program.setParameter(GL2GL3.GL_GEOMETRY_OUTPUT_TYPE_ARB, GL.GL_LINE_STRIP)
                program.setParameter(GL2GL3.GL_GEOMETRY_VERTICES_OUT_ARB, 2)

                val material = new ShaderMaterial("AMBIENT", program)

                shape.setGeometry(cloud)
                shape.setMaterial(material)

                for (i <- 0 until max) {
                    val particle = new Particle()
                    particle.energy = 0
                    particles.add(particle)
                }

                parent.addChildNode(shape)
            case Update => update()
            case BulletWallHit(bullet) => emit(bullet.position, Colors.RED)
            case BomberHit(bomber, bullet) => emit(bullet.position, Colors.GREEN)
            case SeekerHit(seeker, bullet) => emit(bullet.position, Colors.BLUE)
            case SeekerWallHit(seeker) => emit(seeker.position, Colors.BLUE)
            case BombWallHit(bomb) => emit(bomb.position, Colors.GREEN)
            case ParticleWallHit(particle) =>
                sender ! RemoveBody(particle.body)
                particle.energy = 0
            case PoisonPill => exit()
            case _ =>
        }
    }

    private def emit(position:Vector3, color:Vector4) {
        for (i <- 0 until emitRate) {
            val it = deads.iterator()
            if (it.hasNext) {
                val particle = it.next();
                particle.energy = 25f
                particle.direction = randomDirection
                particle.position = position
                particle.color = color
                sender ! AddBody(particle.body, Collisions.PARTICLE, Collisions.WITH_PARTICLE)
            }
        }
    }

    private def update() {
        for (i <- 0 until max) {
            val particle = particles.get(i)
            particle.energy -= 1f
            energy.update(i, particle.energy)
            if (particle.dead) {
                sender ! RemoveBody(particle.body)
            }
        }

        cloud.setAttribute("position", new AttributeVector3(positions))
        cloud.setAttribute("direction", new AttributeVector3(directions))
        cloud.setAttribute("energy", new AttributeFloat(energy))
        cloud.setAttribute("color", new AttributeVector4(colors))
    }

}