package org.whiskeysierra.powpow

import scala.actors.Actor

class Gun extends Actor {

    private var position = Vector()
    private var direction = Vector(0, 0)
    
    override def act = {
        loop {
            react {
                case Position(position) => this.position = position
                case Aim(direction) => this.direction = direction
                case Update =>
                    sender ! Fire(position)
                case PoisonPill => exit
            }
        }
    }
    
}