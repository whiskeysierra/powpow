package org.whiskeysierra.powpow

import de.bht.jvr.core.GroupNode

class Swarm(private val parent: GroupNode) extends Actor {

    private val seekers = new Array[Seeker](100)

    override def act(message:Any):Unit = message match {
        case PoisonPill => exit()
        case _ =>
    }

}