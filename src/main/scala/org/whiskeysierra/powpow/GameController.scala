package org.whiskeysierra.powpow

import net.java.games.input.{AbstractController, Controller, ControllerEnvironment, Component, EventQueue, Event}
import scala.actors.Actor


object GameController {
    
    private val controllers:Array[Controller] = ControllerEnvironment.getDefaultEnvironment().getControllers filter {
        _.getType == Controller.Type.GAMEPAD
    }
    
    private def find(index:Int):Controller = {
        if (index < controllers.length) {
            val controller:Controller = controllers(index)
            println("Found: " + controller)
            return controller
        } else {
            return new AbstractController("Fake Controller #" + index, Array(), Array(), Array()) {
                override def getNextDeviceEvent(event:Event):Boolean = false
            }
        }
    }
    
    def apply(index:Int):GameController = {
        val controller:Controller = find(index)
        println("Using " + controller)
        return new GameController(controller)
    }
    
}

class GameController(private val controller:Controller) extends Actor {

    private var queue:EventQueue = controller.getEventQueue
    private val event:Event = new Event
    
    private var x = 0f
    private var y = 0f
    
    private val speed = 0.01f
    
    private def poll():Unit = {
        controller.poll         
        while (queue.getNextEvent(event)) {
            val value:Float = event.getValue
            event.getComponent.getName match {
                case "x" => y = value
                case "y" => x = -value
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
                case PoisonPill => exit
            }
        }
    }
    
}