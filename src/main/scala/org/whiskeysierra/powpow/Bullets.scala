package org.whiskeysierra.powpow

import de.bht.jvr.core.{Transform, GroupNode, SceneNode}
import Transform._
import scala.actors.Actor

class Bullets(private val node:GroupNode, private val sphere:SceneNode) extends Actor {
    
    private var position = Vector()
    
    override def act = {
        loop {
            react {
                case Position(position) => this.position = position
                case Aim(direction) =>
                    val bullet = new GroupNode("Bullet")
                    bullet.addChildNodes(sphere)
                    bullet.setTransform(translate(direction + position toVector3) mul scale(.5f))
                    sender ! Add(node, bullet)
                case PoisonPill => exit
            }
        }
    }
    
}