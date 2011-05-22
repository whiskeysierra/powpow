package org.whiskeysierra.powpow

import scala.actors.Actor

class Gun extends Actor {

    override def act = {
        loop {
            react {
                case Aim(direction) =>
                    // TODO fire
                case PoisonPill => exit
            }
        }
    }
    
}