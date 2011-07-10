package org.whiskeysierra.powpow

abstract class Actor {

    private var s:Actor = null

    final def sender = s

    def exit() = Unit

    def act(message:Any)

    final def !(sender:Actor, message:Any) {
        s = sender
        this ! message
    }

    final def !(message:Any) {
        act(message)
    }

}
