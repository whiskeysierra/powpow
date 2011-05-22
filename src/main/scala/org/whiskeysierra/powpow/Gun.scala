package org.whiskeysierra.powpow

import de.bht.jvr.core.Transform._
import de.bht.jvr.core.{Transform, GroupNode, SceneNode}
import scala.actors.Actor
import scala.collection.mutable.Map
import scala.util.Random

class Gun(private val parent:GroupNode, private val sphere:SceneNode) extends Actor {
    
    private var position = Vector()
    
    private val bullets = Map[Bullet, SceneNode]()
    
    private val angles = for (a <- -20 until 20) yield a.toRadians
    private val random = new Random
    
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
                case Position(position) => this.position = position
                case Aim(direction) =>
                    val bullet = Bullet(position, randomize(direction))
                    val node = new GroupNode("Bullet")
                    node.addChildNodes(sphere)
                    transform(bullet, node)
                    bullets += (bullet -> node)
                    sender ! AddBody(bullet.body, Collisions.BULLET, Collisions.NOTHING)
                    sender ! Add(parent, node)
                case Update =>
                    bullets foreach {
                        case (bullet, node) => transform(bullet, node)
                    }
                case PoisonPill => exit
            }
        }
    }
    
    private def transform(bullet:Bullet, node:SceneNode) = {
        node.setTransform(translate(bullet.position toVector3) mul scale(.5f))
    }
    
}