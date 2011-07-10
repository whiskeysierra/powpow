package org.whiskeysierra.powpow

final class MessageHub(private val actors: Map[String, Actor]) extends Actor {

    private def each(f: (Actor) => Unit) {
        actors.values foreach {
            f(_)
        }
    }

    private def broadcast(message: Any) {
        each {a =>
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

    private def deliver(name: String, message: Any) {
        actors.get(name) match {
            case Some(actor) => actor ! message
            case None => throw new IllegalStateException("Unknown actor name: " + name)
        }
    }

}