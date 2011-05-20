package org.whiskeysierra.powpow.input

import net.java.games.input.{Controller, ControllerEnvironment, Component, EventQueue, Event}
import org.whiskeysierra.powpow.{Exit, Update, PoisonPill}
import scala.actors.Actor

trait GameController extends Actor {
}

object GameController {
    
    private val controllers:Array[Controller] = ControllerEnvironment.getDefaultEnvironment().getControllers
    
    def isPresent():Boolean = {
        return controllers.length > 0
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
    
    private var x:Float = 0
    private var y:Float = 0
    
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
                case _:Update =>
                    poll
                    sender ! MoveY(x)
                    sender ! MoveX(y)
                case PoisonPill => exit
            }
        }
    }
    
}