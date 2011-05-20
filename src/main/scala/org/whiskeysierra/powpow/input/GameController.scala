package org.whiskeysierra.powpow.input

import net.java.games.input.{Controller, ControllerEnvironment, Component, EventQueue, Event}
import org.whiskeysierra.powpow.{Cube, Exit, Update}
import scala.actors.Actor

trait GameController {
    def start:Actor
    def !(msg:Any):Unit
}

object GameController {
    
    private val controllers:Array[Controller] = ControllerEnvironment.getDefaultEnvironment().getControllers
    
    def isPresent():Boolean = {
        return controllers.length > 0
    }
    
    def getOrFake(cube:Cube):GameController = {
        if (isPresent) {
            return new JInputGameController(cube)
        } else {
            return new GameController {
                def start:Actor = null
                def !(msg:Any):Unit = Unit
            }
        }
    }
    
    def getController():Controller = {
        return controllers(0)
    }
    
}

private class JInputGameController(private val cube:Cube) extends Actor with GameController {

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
                    cube ! MoveY(x)
                    cube ! MoveX(y)
                case Exit =>
                    exit
            }
        }
    }
    
}