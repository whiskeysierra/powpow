package org.whiskeysierra.powpow

import de.bht.jvr.core.{SceneNode, GroupNode}
import scala.actors.Actor

class Swarm(private val parent:GroupNode) extends Actor {

    private val seekers = new Array[Seeker](100)
    
    override def act() {
        loop {
            react {
                case PoisonPill => exit()
            }
        }
    }
    
}