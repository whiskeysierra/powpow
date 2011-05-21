package org.whiskeysierra.powpow

import de.bht.jvr.core.{CameraNode, Transform}
import scala.actors.Actor


class Camera(private val node:CameraNode) extends Actor {
    
    override def act() = {
        loop {
            react {
                case Position(x, y) => {
                    node.setTransform(Transform.translate(x, y, 7.5f))
                }
                case PoisonPill => exit
            }
        }
    }
    
}