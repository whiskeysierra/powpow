package org.whiskeysierra.powpow

import de.bht.jvr.math.Vector3
import de.bht.jvr.util.awt.InputState
import java.awt.event.KeyEvent

class Keyboard(private val input: InputState) extends Actor {

    override def act(message:Any) {
        message match {
            case Update => {
                var directionX = 0f
                var directionY = 0f

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

                val direction = new Vector3(directionX, directionY, 0)
                if (direction.length == 0) {
                    // FIXME stop if and only if we are using the keyboard
                    //sender ! Stop
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

                val aim = new Vector3(aimX, aimY, 0)
                if (aim.length > 0) {
                    sender ! Aim(aim)
                }

                if (input.isDown('Q')) {
                    sender ! Quit
                }
            }
            case PoisonPill => exit()
            case _ =>
        }
    }

}