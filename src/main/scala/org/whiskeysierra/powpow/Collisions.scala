package org.whiskeysierra.powpow

object Collisions {

    val NOTHING: Short = 0
    val WALL: Short = bit(0)
    val BULLET: Short = bit(1)
    val SHIP: Short = bit(2)
    val SEEKER: Short = bit(3)
    val BOMBER: Short = bit(4)
    val BOMB: Short = bit(5)
    val PARTICLE: Short = bit(6)

    val WITH_WALL: Short = (SHIP | BULLET | SEEKER | BOMBER | BOMB | PARTICLE).toShort
    val WITH_SHIP: Short = (WALL | BOMBER | SEEKER | BOMB).toShort
    val WITH_BULLET: Short = (WALL | SEEKER | BOMBER).toShort
    val WITH_SEEKER: Short = (WALL | SHIP | BULLET).toShort
    val WITH_BOMBER: Short = (WALL | SHIP | BULLET).toShort
    val WITH_BOMB: Short = (WALL | SHIP).toShort
    val WITH_PARTICLE: Short = (WALL).toShort

    private def bit(s: Short) = (1 << s).toShort

}
