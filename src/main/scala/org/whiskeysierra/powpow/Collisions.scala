package org.whiskeysierra.powpow

object Collisions {

    val NOTHING:Short = 0
    val WALL:Short = bit(0)
    val BULLET:Short = bit(1)
    val SHIP:Short = bit(2)
    val SEEKER:Short = bit(3)
    val BOMBER:Short = bit(4)
    
    private def bit(s:Short) = 1 << s toShort
    
}