package org.whiskeysierra.powpow.input

import de.bht.jvr.util.InputState
import java.awt.event.KeyEvent
import org.whiskeysierra.powpow.{Cube, Exit, Update}
import scala.actors.Actor

class Keyboard(private val cube:Cube, private val input:InputState) extends Actor {
    
    private val speed:Float = 90
    
    def act():Unit = {
        loop {
            react {
                case Update(elapsed) => {
                    if (input.isDown(KeyEvent.VK_LEFT)) {
                        cube ! MoveX(-elapsed * speed)
                    }
                    
                    if (input.isDown(KeyEvent.VK_RIGHT)) {
                        cube ! MoveX(elapsed * speed)
                    }
                    
                    if (input.isDown(KeyEvent.VK_UP)) {
                        cube ! MoveY(elapsed * speed)
                    }
                    
                    if (input.isDown(KeyEvent.VK_DOWN)) {
                        cube ! MoveY(-elapsed * speed)
                    }
                }
                case Exit => exit
            }
        }
    }
    
}