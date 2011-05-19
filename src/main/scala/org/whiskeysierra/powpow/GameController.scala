package org.whiskeysierra.powpow

import scala.actors.Actor
import net.java.games.input.Event
import net.java.games.input.EventQueue
import net.java.games.input.Component
import net.java.games.input.ControllerEnvironment
import net.java.games.input.Controller

class GameController(private val cube:Cube) extends Actor with Runnable {

    private var finished:Boolean = false
    
    override def run():Unit = {
        val controllers:Array[Controller] = ControllerEnvironment.getDefaultEnvironment().getControllers
        val controller:Controller = controllers(0)
        
        println(controller.getName)
        
        while (!finished) {
            controller.poll
            val queue:EventQueue = controller.getEventQueue
            val event:Event = new Event
            
            while (queue.getNextEvent(event)) {
                val value:Float = event.getValue
                event.getComponent.getName match {
                    case "x" => cube.moveY(value)
                    case "y" => cube.moveX(value)
                    case "rz" => cube.moveY(value)
                    case "slider" => cube.moveX(value)
                    case _ => 
                }
            }
        }
    }
    
    override def act():Unit = {
        new Thread(this).start
        loop {
            react {
                case Exit =>
                    finished = true
                    exit()
                case _ => 
            }
        }
    }
    
}