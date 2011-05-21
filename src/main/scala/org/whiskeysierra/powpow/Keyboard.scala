package org.whiskeysierra.powpow

import de.bht.jvr.util.{InputState, StopWatch}
import java.awt.event.KeyEvent
import scala.actors.Actor

class Keyboard(private val input:InputState) extends Actor {
    
    private val time:StopWatch = new StopWatch
    
    private val speed:Float = 5
    
    def act():Unit = {
        loop {
            react {
                case Update => {
                    val elapsed = time.elapsed
                    
                    if (input.isDown('A')) {
                        sender ! MoveX(-elapsed * speed)
                    }
                    
                    if (input.isDown('W')) {
                        sender ! MoveY(elapsed * speed)
                    }
                    
                    if (input.isDown('S')) {
                        sender ! MoveY(-elapsed * speed)
                    }
                    
                    if (input.isDown('D')) {
                        sender ! MoveX(elapsed * speed)
                    }
                    
                    if (input.isDown(KeyEvent.VK_LEFT)) {
                        sender ! ShootX(-elapsed * speed)
                    }
                    
                    if (input.isDown(KeyEvent.VK_UP)) {
                        sender ! ShootY(elapsed * speed)
                    }
                    
                    if (input.isDown(KeyEvent.VK_DOWN)) {
                        sender ! ShootY(-elapsed * speed)
                    }
                    
                    if (input.isDown(KeyEvent.VK_RIGHT)) {
                        sender ! ShootX(elapsed * speed)
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