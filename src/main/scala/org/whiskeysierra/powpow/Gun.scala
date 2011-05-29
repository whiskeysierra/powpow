package org.whiskeysierra.powpow

import Vector.toVector3f
import com.google.common.base.{Function, Predicate}

import com.google.common.collect.{Iterables, Lists}
import de.bht.jvr.core.{AttributeCloud, GroupNode, Shader, ShaderMaterial, ShaderProgram, ShapeNode}
import de.bht.jvr.core.attributes.{AttributeFloat, AttributeVector3}
import de.bht.jvr.math.Vector3
import javax.media.opengl.{GL, GL2, GL3, GL2ES2, GL2GL3}
import javax.vecmath.{Vector3f, Matrix4f}
import java.lang.Iterable
import java.util.List
import scala.actors.Actor
import scala.collection.JavaConversions._
import scala.util.Random

class Gun(private val parent:GroupNode) extends Actor with ResourceLoader {
    
    private var position = new Vector3
    
    private val rateOfFire = 5
    private val spreading = 30 // in degrees
    
    private val angles = {
    	val size = spreading / (rateOfFire - 1)
    	for (i <- 0 until rateOfFire) 
    		yield (size * i - spreading / 2).toRadians
    }
    
    private val max = 1000
    private val cloud = new AttributeCloud(max, GL.GL_POINTS)
    
    private val bullets:List[Bullet] = Lists.newArrayListWithCapacity(max)
    
    private val positions:List[Vector3] = Lists.transform(bullets, new Function[Bullet, Vector3] {
        override def apply(b:Bullet) = b.position  
    });
    
    private val directions:List[Vector3] = Lists.transform(bullets, new Function[Bullet, Vector3] {
        override def apply(b:Bullet) = b.direction  
    });
    
    private val energies:Array[Float] = Array.fill(max) {0}
    
    private val inactives:Iterable[Bullet] = Iterables.filter(bullets, new Predicate[Bullet] {
        override def apply(b:Bullet) = b.energy <= 0f
    })
    
    override def act = {
        loop {
            react {
                case Start =>
                    val shape:ShapeNode = new ShapeNode("Gun")
                    
                    val vs = new Shader(load("bullets.vs"), GL2ES2.GL_VERTEX_SHADER)
                    val gs = new Shader(load("bullets.gs"), GL3.GL_GEOMETRY_SHADER)
                    val fs = new Shader(load("bullets.fs"), GL2ES2.GL_FRAGMENT_SHADER)
        
                    val program = new ShaderProgram(vs, fs, gs)            
            
                    program.setParameter(GL2GL3.GL_GEOMETRY_INPUT_TYPE_ARB, GL.GL_POINTS)
                    program.setParameter(GL2GL3.GL_GEOMETRY_OUTPUT_TYPE_ARB, GL.GL_LINE_STRIP)
                    program.setParameter(GL2GL3.GL_GEOMETRY_VERTICES_OUT_ARB, 3)
            
                    val material = new ShaderMaterial("AMBIENT", program)
            
                    shape.setGeometry(cloud)
                    shape.setMaterial(material)

                    for (i <- 0 until max) {
                        val bullet = Bullet()
                        bullets add bullet
                        sender ! AddBody(bullet.body, Collisions.BULLET, Collisions.WITH_BULLET)
                    }
                    
                    update
                    
                    sender ! Add(parent, shape)
                case Position(position) => this.position = position
                case Aim(direction) =>
                    var inactive = inactives.iterator
                    for (i <- 0 until rateOfFire) {
                        if (inactive.hasNext) {
                            val bullet = inactive.next
                            bullet.position = position
                            bullet.direction = spread(direction, i)
                            bullet.energy = 1
                        }
                    }
                case Update => update
                case PoisonPill => exit
            }
        }
    }
    
    private def cos(a:Float) = math.cos(a).toFloat
    private def sin(a:Float) = math.sin(a).toFloat
    
    private def spread(direction:Vector3, i:Int):Vector3 = {
		val angle = angles(i)
		return new Vector3(
            cos(angle) * direction.x - sin(angle) * direction.y,
            sin(angle) * direction.x + cos(angle) * direction.y,
            0
        )
    }
    
    private def update = {
//        for (i <- 0 until max) {
//            energies.update(i, bullets(i).energy)
//        }
        
        cloud.setAttribute("position", new AttributeVector3(positions))
        cloud.setAttribute("direction", new AttributeVector3(directions))
//        cloud.setAttribute("energy", new AttributeFloat(energies))
        
        bullets foreach {
            bullet => bullet.energy -= 0.01f
        }
    }
    
}
