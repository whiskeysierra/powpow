package org.whiskeysierra.powpow

import scala.actors.Actor

final class MessageHub(private val actors:List[Actor]) extends Actor {

    override def act = {
        loop {
            react {
                case Exit => 
                    broadcast(PoisonPill)
                    exit()
                case other => broadcast(other)
            }
        }
    }
    
    private def broadcast(message:Any) = {
        actors foreach {
            _ ! message
        }
    }
    
}