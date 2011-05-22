package org.whiskeysierra.powpow

import de.bht.jvr.core.{Transform, SceneNode}
import scala.actors.Actor

class Player(private val node:SceneNode) extends Actor {
    
    private val axis = Vector(1)
    private val speed = 0.2f
    
    private var direction = Vector()
    private var position = Vector()

    override def act():Unit = {
        loop {
            react {
                case Move(movement) => 
                    direction = movement.normalize
                    position += movement * speed
                case Update => 
                    update
                    sender ! Position(position)
                case PoisonPill => exit
            }
        }
    }
    
    private def update = {
        val angle = math.acos(axis dot direction).toFloat
        node.setTransform(
            Transform.translate(position.x, position.y, 0) mul
            Transform.rotateZ(angle)
        )
    }
    
}