package org.whiskeysierra.powpow

import scala.actors.Actor

final class MessageHub(private val actors:Map[String, Actor]) extends Actor {
    
    private def each(f: (Actor) => Unit) {
      actors.values foreach {
        f(_)
      }
    }
    
    private def broadcast(message:Any) {
      each {
        _ ! message
      }
    }

    override def start() = {
        each {_.start()}
        super.start()
    }
    
    override def act() {
        loop {
            react {
                case Quit => 
                    broadcast(PoisonPill)
                    exit()
                case other => broadcast(other)
            }
        }
    }
    
    private def deliver(name:String, message:Any) {
        actors.get(name) match {
            case Some(actor) => actor ! message
            case None => throw new IllegalStateException("Unknown actor name: " + name)
        }
    }
    
}