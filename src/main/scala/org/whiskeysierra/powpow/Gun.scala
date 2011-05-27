package org.whiskeysierra.powpow

import com.google.common.collect.{Iterables, Lists}
import de.bht.jvr.core.{AttributeCloud, GroupNode, SceneNode, Shader, ShaderMaterial, ShaderProgram, ShapeNode}
import de.bht.jvr.core.attributes.{AttributeFloat, AttributeVector3}
import de.bht.jvr.math.Vector3
import java.lang.Iterable
import java.util.List 
import javax.media.opengl.{GL, GL2, GL3, GL2ES2, GL2GL3}
import scala.actors.Actor
import scala.collection.JavaConversions._
import scala.util.Random
import javax.vecmath.{Vector3f, Matrix4f}

class Gun(private val parent:GroupNode, private val sphere:SceneNode) extends Actor with ResourceLoader {
    
    private var position = Vector()
    
    private val cloud = new AttributeCloud(1000, GL.GL_POINTS)
    
    private val bullets:List[Bullet] = Lists.newArrayListWithCapacity(cloud.getNumPoints)
    
    private val positions:List[Vector3] = Lists.transform(bullets, Bullet.Position)
    private val energies:List[java.lang.Float] = Lists.transform(bullets, Bullet.Energy)
    private val inactives:Iterable[Bullet] = Iterables.filter(bullets, Bullet.Inactive)
    
    private val angles = for (a <- -20 until 20) yield a.toRadians
    private val random = new Random
    private val collisions = Collisions.WALL | Collisions.SEEKER | Collisions.BOMBER toShort
    
    private def cos(a:Float) = math.cos(a).toFloat
    private def sin(a:Float) = math.sin(a).toFloat
    
    private def randomize(direction:Vector) = {
        val angle = angles(random.nextInt(angles.length))
        Vector(
            cos(angle) * direction.x - sin(angle) * direction.y,
            sin(angle) * direction.x + cos(angle) * direction.y
        )
    }
    
    override def act = {
        loop {
            react {
                case Start =>
                    val shape:ShapeNode = new ShapeNode("Gun")
                    
                    val vs = new Shader(open("bullets.vs"), GL2ES2.GL_VERTEX_SHADER)
                    val gs = new Shader(open("bullets.gs"), GL3.GL_GEOMETRY_SHADER)
                    val fs = new Shader(open("bullets.fs"), GL2ES2.GL_FRAGMENT_SHADER)
        
                    val program = new ShaderProgram(vs, fs, gs)            
            
                    program.setParameter(GL2GL3.GL_GEOMETRY_INPUT_TYPE_ARB, GL.GL_POINTS)
                    program.setParameter(GL2GL3.GL_GEOMETRY_OUTPUT_TYPE_ARB, GL2.GL_QUADS)
                    program.setParameter(GL2GL3.GL_GEOMETRY_VERTICES_OUT_ARB, 4)
            
                    val material = new ShaderMaterial("AMBIENT", program)
            
                    shape.setGeometry(cloud)
                    shape.setMaterial(material)

                    for (i <- 0 until cloud.getNumPoints) {
                        val bullet = Bullet()
                        bullets add bullet
                        sender ! AddBody(bullet.body, Collisions.BULLET, collisions)
                    }
                    
                    update
                    
                    sender ! Add(parent, shape)
                case Position(position) => this.position = position
                case Aim(direction) =>
                    var inactive = inactives.iterator
                    for (i <- 0 until 5) {
                        if (inactive.hasNext) {
                            val bullet = inactive.next
                            bullet.position = position
                            bullet.direction = randomize(direction)
                            
                            val matrix = new Matrix4f
                            matrix.set(position toVector3f)
                            bullet.body.proceedToTransform(new com.bulletphysics.linearmath.Transform(matrix))
                            bullet.body.setLinearVelocity(bullet.direction * 25 toVector3f)
                            bullet.energy = 1
                        }
                    }
                case Update => 
                    update
                    bullets foreach {bullet =>
                        bullet.energy -= 0.01f
                    }
                case PoisonPill => exit
            }
        }
    }
    
    private def update = {
        cloud.setAttribute("position", new AttributeVector3(positions))
    }
    
}
