package org.whiskeysierra.powpow

trait Enemy {

    val max: Float
    var health = max

    def alive = health > 0
    def dead = health <= 0

    def kill() {
        health = 0
    }

    def revive() {
        health = max
    }

}
