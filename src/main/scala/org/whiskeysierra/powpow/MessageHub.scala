package org.whiskeysierra.powpow

final class MessageHub(private val actors: Map[String, Actor]) extends Actor {

    private def broadcast(message: Any) {
        actors.values foreach {a =>
            a.!(this, message)
        }
    }

    override def act(message:Any) {
        message match {
            case Quit =>
                broadcast(PoisonPill)
                exit()
            case other => broadcast(other)
        }
    }

}