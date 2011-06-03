package org.whiskeysierra.powpow

trait Enemy {
    
    var health = 0f
    val max:Float

    def isAlive = health > 0
    def isDead = !isAlive
    def revive() {
      health = max
    }
    
}
