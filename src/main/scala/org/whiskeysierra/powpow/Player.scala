package org.whiskeysierra.powpow

import de.bht.jvr.core.{Transform, SceneNode}
import scala.actors.Actor

class Player(private val node:SceneNode) extends Actor {
    
    private val TwoPi = Math.Pi.toFloat * 2
        
    private val axis = Vector(1)
    private val speed = 0.2f
    
    private var current = Vector()
    private var last = Vector()

    private var angle:Float = 0
    
    override def act():Unit = {
        loop {
            react {
                case Move(movement) => current += movement * speed
                case Update => 
                    sender ! Position(current)
                    
                    if (current equals last) {
                        // standing still, no need to change the angle
                    } else {
                        val direction = current - last
                        angle = math.acos(axis dot (direction.normalize)).toFloat
                        if (angle.isNaN) angle = 0
                        sender ! Direction(direction)
                    }
                    
                    last = current.copy
                    update
                    
                case PoisonPill => exit
            }
        }
    }
    
    def update():Unit = {
        node.setTransform(
            Transform.translate(current.x, current.y, 0) mul
            Transform.rotateZ(angle)
        )
    }
    
}