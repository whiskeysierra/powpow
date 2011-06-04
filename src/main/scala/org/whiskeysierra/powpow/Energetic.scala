package org.whiskeysierra.powpow

trait Energetic {

    def limit = 1f
    private var e = limit

    def energy = e

    def alive = energy > 0
    def dead = energy <= 0

    def revive() {
        e = limit
    }

    def kill() {
        e = 0
    }

}