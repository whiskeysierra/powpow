package org.whiskeysierra.powpow

import net.java.games.input.{AbstractController, Controller, ControllerEnvironment, Component, EventQueue, Event}
import scala.actors.Actor


object GameController {
    
    private val controllers:Array[Controller] = ControllerEnvironment.getDefaultEnvironment().getControllers filter {
        _.getType == Controller.Type.GAMEPAD
    }
    
    def apply(index:Int):GameController = {
        if (index < controllers.length) {
            val controller:Controller = controllers(index)
            println("Found: " + controller)
            return new JInputGameController(controller)
        } else {
            println("Using Fake Controller #" + index)
            return FakeGameController
        }
    }
    
}

trait GameController extends Actor

private object FakeGameController extends GameController {
    override def act = Unit
}

private class JInputGameController(private val controller:Controller) extends GameController {

    private var queue:EventQueue = controller.getEventQueue
    private val event:Event = new Event
    
    private val movement = Vector()
    private val aim = Vector()
    
    private val speed = 0.01f
    
    private def poll():Unit = {
        controller.poll         
        while (queue.getNextEvent(event)) {
            val value:Float = event.getValue
            event.getComponent.getName match {
                case "x" => movement.x = value
                case "y" => movement.y = -value
                case "z" => aim.x = value
                case "rz" => aim.y = -value
                case _ =>
            }
        }
    }

    override def act():Unit = {
        loop {
            react {
                case Update =>
                    poll
                    if (movement.length > 0.1) {
                        sender ! Move(movement)
                    } else {
                        sender ! Stop
                    }
                    if (aim.length > 0.1) {
                        sender ! Aim(aim.normalize)
                    }
                case PoisonPill => exit
            }
        }
    }
    
}