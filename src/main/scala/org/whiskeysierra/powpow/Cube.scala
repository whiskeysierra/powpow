package org.whiskeysierra.powpow

import de.bht.jvr.core.{Transform, SceneNode}

import org.whiskeysierra.powpow.input.{MoveY, MoveX}
import scala.actors.Actor

class Cube(private val node:SceneNode) extends Actor {
    
    private var angleX:Float = 0
    private var angleY:Float = 0
    
    override def act():Unit = {
        loop {
            react {
                case MoveX(value) => angleX += value
                case MoveY(value) => angleY += value
                case _:Update => update
                case Exit => exit()
            }
        }
    }
    
    def update():Unit = {
        node.setTransform(Transform.rotateYDeg(angleY).mul(Transform.rotateXDeg(angleX)))
    }
    
}