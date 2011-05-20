package org.whiskeysierra.powpow.input

import de.bht.jvr.util.InputState
import java.awt.event.KeyEvent
import org.whiskeysierra.powpow.{Cube, Update, PoisonPill}
import scala.actors.Actor

class Keyboard(private val input:InputState) extends Actor {
    
    private val speed:Float = 90
    
    def act():Unit = {
        loop {
            react {
                case Update(elapsed) => {
                    if (input.isDown(KeyEvent.VK_LEFT)) {
                        sender ! MoveX(-elapsed * speed)
                    }
                    
                    if (input.isDown(KeyEvent.VK_RIGHT)) {
                        sender ! MoveX(elapsed * speed)
                    }
                    
                    if (input.isDown(KeyEvent.VK_UP)) {
                        sender ! MoveY(elapsed * speed)
                    }
                    
                    if (input.isDown(KeyEvent.VK_DOWN)) {
                        sender ! MoveY(-elapsed * speed)
                    }
                }
                case PoisonPill => exit
            }
        }
    }
    
}