package org.whiskeysierra.powpow

import scala.actors.Actor

final class MessageHub(private val actors:Map[String, Actor]) extends Actor {

    override def act = {
        loop {
            react {
                case position:Position => deliver("camera", position)
                case Quit => 
                    broadcast(PoisonPill)
                    exit()
                case other => broadcast(other)
            }
        }
    }
    
    private def deliver(name:String, message:Any) ={
        actors.get(name) match {
            case Some(actor) => actor ! message
            case None => throw new IllegalStateException("Unknown actor name: " + name)
        }
    }
    
    private def broadcast(message:Any) = {
        actors foreach {
            case (name, actor) => actor ! message
        }
    }
    
}