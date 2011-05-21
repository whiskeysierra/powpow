package org.whiskeysierra.powpow

import net.java.games.input.{Controller, ControllerEnvironment, Component, EventQueue, Event}
import scala.actors.Actor

trait GameController extends Actor {
}

object GameController {
    
    private val controllers:Array[Controller] = ControllerEnvironment.getDefaultEnvironment().getControllers
    
    def isPresent():Boolean = {
        return ! controllers.isEmpty
    }
    
    def getOrFake:GameController = {
        if (isPresent) {
            return new JInputGameController
        } else {
            return new GameController {
                override def act = Unit
            }
        }
    }
    
    def getController():Controller = {
        return controllers(0)
    }
    
}

private class JInputGameController extends GameController {

    private val controller:Controller = GameController.getController
    
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