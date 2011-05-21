package org.whiskeysierra.powpow

import de.bht.jvr.core.GroupNode
import scala.actors.Actor

class Bullets(private val node:GroupNode) extends Actor {
    
    private var direction = Vector()
    
    override def act = {
        loop {
            react {
                case Aim(direction) => this.direction = direction
                case Fire(position) =>
//                    println("Firing at " + position + " to " + direction)
                case PoisonPill => exit
            }
        }
    }
    
}