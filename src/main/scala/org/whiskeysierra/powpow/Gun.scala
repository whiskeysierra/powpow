package org.whiskeysierra.powpow

import scala.actors.Actor

class Gun extends Actor {

    private var position = Vector()
    
    override def act = {
        loop {
            react {
                case Position(position) => this.position = position
                case Aim(direction) =>
                    sender ! Fire(position)
                case PoisonPill => exit
            }
        }
    }
    
}