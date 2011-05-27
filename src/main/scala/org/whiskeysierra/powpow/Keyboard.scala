package org.whiskeysierra.powpow

import de.bht.jvr.util.InputState
import java.awt.event.KeyEvent
import scala.actors.Actor

class Keyboard(private val input:InputState) extends Actor {
    
    override def act = {
        loop {
            react {
                case Update => {
                    var directionX = 0
                    var directionY = 0
                    
                    if (input.isDown('A')) {
                        directionX -= 1
                    }
                    
                    if (input.isDown('W')) {
                        directionY += 1
                    }
                    
                    if (input.isDown('S')) {
                        directionY -= 1
                    }
                    
                    if (input.isDown('D')) {
                        directionX += 1
                    }
                    
                    val direction = Vector(directionX, directionY)
                    if (Vector.isZero(direction)) {
                        sender ! Stop
                    } else {
                        // keyboard moves with full speed, hence the normalize
                        sender ! Move(direction.normalize)
                    }
                    
                    var aimX = 0f
                    var aimY = 0f
                    
                    if (input.isDown(KeyEvent.VK_LEFT)) {
                        aimX -= 1
                    }
                    
                    if (input.isDown(KeyEvent.VK_UP)) {
                        aimY += 1
                    }
                    
                    if (input.isDown(KeyEvent.VK_DOWN)) {
                        aimY -= 1
                    }
                    
                    if (input.isDown(KeyEvent.VK_RIGHT)) {
                        aimX += 1
                    }
                    
                    val aim = Vector(aimX, aimY)
                    if (Vector.isNotZero(aim)) {
                        sender ! Aim(aim)
                    }
                    
                    if (input.isDown('Q')) {
                        sender ! Quit
                    }
                }
                case PoisonPill => exit
            }
        }
    }
    
}