package org.whiskeysierra.powpow

import de.bht.jvr.core.{Transform, GroupNode, SceneNode}
import Transform._
import scala.actors.Actor
import scala.collection.mutable.Map

class Bullets(private val parent:GroupNode, private val sphere:SceneNode) extends Actor {
    
    private var position = Vector()
    
    private val bullets = Map[Bullet, SceneNode]()
    
    override def act = {
        loop {
            react {
                case Position(position) => this.position = position
                case Aim(direction) =>
                    val bullet = Bullet(position, direction)
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