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
    
    private var x = 0f
    private var y = 0f
    
    private var shootX = 0f
    private var shootY = 0f
    
    private val speed = 0.01f
    
    private def poll():Unit = {
        controller.poll         
        while (queue.getNextEvent(event)) {
            val value:Float = event.getValue
            event.getComponent.getName match {
                case "x" => y = value
                case "y" => x = -value
                case "rz" => shootY = value
                case "slider" => shootX = -value
                case _ => 
            }
        }
    }

    override def act():Unit = {
        loop {
            react {
                case Update =>
                    poll
                    sender ! MoveY(x)
                    sender ! MoveX(y)
                    sender ! ShootY(shootX)
                    sender ! ShootX(shootY)
                case PoisonPill => exit
            }
        }
    }
    
}