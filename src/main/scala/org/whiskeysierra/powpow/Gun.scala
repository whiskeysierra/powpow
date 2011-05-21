package org.whiskeysierra.powpow

import scala.actors.Actor

class Gun extends Actor {

    override def act = {
        loop {
            react {
                case ShootX(value) => println("ShootX: " + value)
                case ShootY(value) => println("ShootY: " + value)
                case PoisonPill => exit
            }
        }
    }
    
}