package org.whiskeysierra.powpow

import de.bht.jvr.math.Vector3
import net.java.games.input._

object GameController {

    private val controllers: Array[Controller] = ControllerEnvironment.getDefaultEnvironment.getControllers filter {
        c => c.getType == Controller.Type.GAMEPAD || c.getType == Controller.Type.STICK
    }
    
    def isAbsent = controllers.isEmpty

    def apply(index: Int): GameController = {
        if (index < controllers.length) {
            val controller: Controller = controllers(index)
            controller.getName match {
                case "Logitech Inc. WingMan RumblePad" => new JInputGameController(controller, new Template("x", "y", "rz", "slider"))
                case "WingMan RumblePad" => new JInputGameController(controller, new Template("X axis", "Y axis", "Rudder", "Extra"))
                case "Logitech Cordless RumblePad 2" => new JInputGameController(controller, new Template("x", "y", "z", "rz"))
                case "Logitech Cordless RumblePad 2 USB" => new JInputGameController(controller, new Template("X Axis", "Y Axis", "Z Axis", "Z Rotation"))
                case _ =>
                    System.err.println("No compatible game controller found")
                    FakeGameController
            }
        } else {
            FakeGameController
        }
    }

}

case class Template(leftX:String, leftY:String, rightX:String, rightY:String)

trait GameController extends Actor

private object FakeGameController extends GameController {
    override def act(message:Any) {
        Unit
    }
}

private class JInputGameController(val controller: Controller, val template: Template) extends GameController with Clock {

    private val queue: EventQueue = controller.getEventQueue
    private val event: Event = new Event

    private var movementX = 0f
    private var movementY = 0f
    private var aimX = 0f
    private var aimY = 0f

    override val loop = .5f
    private var rumbling = false

    private def enableRumbling() {
        if (rumbling) {
            return
        } else {
            rumbling = true
            for (rumbler <- controller.getRumblers) {
                rumbler.rumble(.5f)
            }
        }
    }

    private def disableRumbling() {
        if (rumbling) {
            for (rumbler <- controller.getRumblers) {
                rumbler.rumble(0)
            }
            rumbling = false
        }
    }

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

    override def act(message:Any) {
        message match {
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

                if (tick()) {
                    disableRumbling();
                }
            case _:BomberCollision => enableRumbling()
            case _:SeekerCollision => enableRumbling()
            case _:BombCollision => enableRumbling()
            case PoisonPill => exit()
            case _ =>
        }
    }

}