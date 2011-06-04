package org.whiskeysierra.powpow

abstract class Actor {

    private var s:Actor = null

    def start() = Unit
    def exit() = Unit

    final def sender = s

    def act(message:Any)

    def !(sender:Actor, message:Any):Unit = {
        s = sender
        this.!(message)
    }

    def !(message:Any):Unit = {
        act(message)
    }

}
