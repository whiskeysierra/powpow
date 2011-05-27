package org.whiskeysierra.powpow

import com.google.common.collect.{Iterables, Lists}
import com.google.common.base.{Function, Predicate}
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
import Vector.toVector3f

class Gun(private val parent:GroupNode, private val sphere:SceneNode) extends Actor with ResourceLoader {
    
    private var position = new Vector3
    
    private val max = 1000
    private val cloud = new AttributeCloud(max, GL.GL_POINTS)
    
    private val bullets:List[Bullet] = Lists.newArrayListWithCapacity(max)
    
    private val positions:List[Vector3] = Lists.transform(bullets, new Function[Bullet, Vector3] {
        override def apply(b) = b.position  
    });
    
    private val energies:Array[Float] = Array.fill(max) {0}
    
    private val inactives:Iterable[Bullet] = Iterables.filter(bullets, new Predicate[Bullet] {
        override def apply(b:Bullet) = b.energy <= 0f
    })
    
    private val angles = for (a <- -20 until 20) yield a.toRadians
    private val random = new Random
    private val collisions = Collisions.WALL | Collisions.SEEKER | Collisions.BOMBER toShort
    
    private def cos(a:Float) = math.cos(a).toFloat
    private def sin(a:Float) = math.sin(a).toFloat
    
    private def randomize(direction:Vector3) = {
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

                    for (i <- 0 until max) {
                        val bullet = Bullet.apply()
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
                            matrix.set(position)
                            bullet.body.proceedToTransform(new com.bulletphysics.linearmath.Transform(matrix))
                            bullet.body.setLinearVelocity(bullet.direction mul 25)
                            bullet.energy = 1
                        }
                    }
                case Update => update
                case PoisonPill => exit
            }
        }
    }
    
    private def update = {
        
        for (i <- 0 until max) {
            energies.update(i, bullets(i).energy)
        }
        
        cloud.setAttribute("position", new AttributeVector3(positions))
        cloud.setAttribute("energy", new AttributeFloat(energies))
        
        bullets foreach {bullet =>
            bullet.energy -= 0.01f
        }
    }
    
}
