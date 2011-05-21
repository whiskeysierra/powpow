package org.whiskeysierra.powpow

import de.bht.jvr.core.{Transform, GroupNode, SceneNode}
import Transform._
import scala.actors.Actor

class Bullets(private val node:GroupNode, private val sphere:SceneNode) extends Actor {
    
    private var direction = Vector()
    
    override def act = {
        loop {
            react {
                case Aim(direction) => this.direction = direction
                case Fire(position) =>
//                    val bullet = new GroupNode("Bullet")
//                    bullet.addChildNodes(sphere)
//                    bullet.setTransform(scale(.5f) mul translate(direction.normalize * .2f toVector3))
//                    node.addChildNodes(bullet)
                    println("Shooting at " + direction)
                case PoisonPill => exit
            }
        }
    }
    
}