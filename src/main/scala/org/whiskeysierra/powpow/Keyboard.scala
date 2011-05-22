package org.whiskeysierra.powpow

import de.bht.jvr.util.InputState
import java.awt.event.KeyEvent
import scala.actors.Actor

class Keyboard(private val input:InputState) extends Actor {
    
    override def act = {
        loop {
            react {
                case Update => {
                    val direction = Vector()
                    
                    if (input.isDown('A')) {
                        direction.x -= 1
                    }
                    
                    if (input.isDown('W')) {
                        direction.y += 1
                    }
                    
                    if (input.isDown('S')) {
                        direction.y -= 1
                    }
                    
                    if (input.isDown('D')) {
                        direction.x += 1
                    }
                    
                    if (direction.isZero) {
                        sender ! Stop
                    } else {
                        // keyboard moves with full speed, hence the normalize
                        sender ! Move(direction.normalize)
                    }
                    
                    val aim = Vector()
                    
                    if (input.isDown(KeyEvent.VK_LEFT)) {
                        aim.x -= 1
                    }
                    
                    if (input.isDown(KeyEvent.VK_UP)) {
                        aim.y += 1
                    }
                    
                    if (input.isDown(KeyEvent.VK_DOWN)) {
                        aim.y -= 1
                    }
                    
                    if (input.isDown(KeyEvent.VK_RIGHT)) {
                        aim.x += 1
                    }
                    
                    if (aim.isNotZero) {
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