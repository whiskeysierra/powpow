package org.whiskeysierra.powpow

trait Enemy {

    var health = 0f
    val max: Float

    def alive = health > 0
    def dead = health <= 0

    def revive() {
        health = max
    }

}
