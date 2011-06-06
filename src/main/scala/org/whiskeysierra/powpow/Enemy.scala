package org.whiskeysierra.powpow

trait Enemy {

    var health = max
    val max: Float

    def alive = health > 0
    def dead = health <= 0

    def kill() {
        health = 0
    }

    def revive() {
        health = max
    }

}
