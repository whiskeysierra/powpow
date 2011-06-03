package org.whiskeysierra.powpow

import de.bht.jvr.math.Vector3
import net.java.games.input.{Controller, ControllerEnvironment, EventQueue, Event}
import scala.actors.Actor

object GameController {

    private val controllers: Array[Controller] = ControllerEnvironment.getDefaultEnvironment.getControllers filter {
        c => c.getType == Controller.Type.GAMEPAD || c.getType == Controller.Type.STICK
    }

    def apply(index: Int): GameController = {
        if (index < controllers.length) {
            val controller: Controller = controllers(index)
            println("Found: " + controller)

            controller.getName match {
                case WingManRumblePad.name => new JInputGameController(controller, WingManRumblePad)
                case CordlessRumblePad2.name => new JInputGameController(controller, CordlessRumblePad2)
            }
        } else {
            println("Using Fake Controller #" + index)
            FakeGameController
        }
    }

}

private trait Template {

    val name: String

    val leftX = "x"
    val leftY = "y"
    val rightX: String
    val rightY: String

    override def toString = name

}

private object WingManRumblePad extends Template {

    val name = "Logitech Inc. WingMan RumblePad"

    val rightX = "rz"
    val rightY = "slider"

}

private object CordlessRumblePad2 extends Template {

    val name = "Logitech Logitech Cordless RumblePad 2"

    val rightX = "z"
    val rightY = "rz"

}

trait GameController extends Actor

private object FakeGameController extends GameController {
    override def act() {
        Unit
    }
}

private class JInputGameController(val controller: Controller, val template: Template) extends GameController {

    private var queue: EventQueue = controller.getEventQueue
    private val event: Event = new Event

    private var movementX = 0f
    private var movementY = 0f
    private var aimX = 0f
    private var aimY = 0f

    private val speed = 0.01f

    private def poll() {
        controller.poll
        while (queue.getNextEvent(event)) {
            val value: Float = event.getValue
            event.getComponent.getName match {
                case template.leftX => movementX = value
                case template.leftY => movementY = -value
                case template.rightX => aimX = value
                case template.rightY => aimY = -value
                case _ =>
            }
        }
    }

    override def act() {
        loop {
            react {
                case Update =>
                    poll()

                    val movement = new Vector3(movementX, movementY, 0)
                    if (movement.length > 0.1) {
                        sender ! Move(movement)
                    } else {
                        sender ! Stop
                    }

                    val aim = new Vector3(aimX, aimY, 0)
                    if (aim.length > 0.1) {
                        sender ! Aim(aim.normalize)
                    }
                case PoisonPill => exit()
            }
        }
    }

}