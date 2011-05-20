package org.whiskeysierra.powpow

import scala.actors.Actor
import de.bht.jvr.core.{CameraNode, Transform}
import de.bht.jvr.util.InputState

class Camera(private val node:CameraNode, private val cube:Cube) extends Actor {
    
    override def act() = {
        loop {
            react {
                case _:Update => {
                    node.setTransform(Transform.translate(cube.x, cube.y, 3))
                }
                case Exit => exit
            }
        }
    }
    
}