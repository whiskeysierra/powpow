package org.whiskeysierra.powpow

import net.java.games.input.{Controller, ControllerEnvironment, Component, EventQueue, Event}
import org.whiskeysierra.powpow.input.{MoveY, MoveX}
import scala.actors.Actor

object GameController {
    
    private val controllers:Array[Controller] = ControllerEnvironment.getDefaultEnvironment().getControllers
    
    def isPresent():Boolean = {
        return controllers.length > 0
    }
    
    def getController():Controller = {
        return controllers(0)
    }
    
}

class GameController(private val cube:Cube) extends Actor {

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
                case "x" => x = value
                case "y" => y = value
                case _ => 
            }
        }
    }

    override def act():Unit = {
        loop {
            react {
                case _:Update =>
                    poll
                    cube ! MoveY(x)
                    cube ! MoveX(y)
                case Exit =>
                    exit
            }
        }
    }
    
}