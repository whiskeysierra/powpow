package org.whiskeysierra.powpow

import de.bht.jvr.core.{CameraNode, Transform}
import scala.actors.Actor

class Camera(private val node:CameraNode, private val cube:Cube) extends Actor {
    
    override def act() = {
        loop {
            react {
                case _:Update => {
                    node.setTransform(Transform.translate(cube.x, cube.y, 3))
                }
                case PoisonPill => exit
            }
        }
    }
    
}