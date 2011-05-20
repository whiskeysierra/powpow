package org.whiskeysierra.powpow.input

import de.bht.jvr.util.InputState
import java.awt.event.KeyEvent
import org.whiskeysierra.powpow.{Cube, Update}
import scala.actors.Actor

class Keyboard(private val cube:Cube, private val input:InputState) extends Actor {
    
    private val speed:Float = 90
    
    def act():Unit = {
        loop {
            react {
                case Update(elapsed) => {
                    if (input.isDown(KeyEvent.VK_LEFT)) {
                        cube ! MoveY(-elapsed * speed)
                    }
                    
                    if (input.isDown(KeyEvent.VK_RIGHT)) {
                        cube ! MoveY(elapsed * speed)
                    }
                    
                    if (input.isDown(KeyEvent.VK_UP)) {
                        cube ! MoveX(elapsed * speed)
                    }
                    
                    if (input.isDown(KeyEvent.VK_DOWN)) {
                        cube ! MoveX(-elapsed * speed)
                    }
                }
            }
        }
    }
    
}