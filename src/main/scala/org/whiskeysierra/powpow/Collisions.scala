package org.whiskeysierra.powpow

object Collisions {

    val NOTHING:Short = 0
    val BULLET:Short = bit(0)
    val SHIP:Short = bit(1)
    
    private def bit(s:Short):Short = 1 << s toShort
    
}