package org.whiskeysierra.powpow

import com.bulletphysics.collision.shapes.SphereShape

class Bomb extends Physical with Enemy {

    val shape = new SphereShape(.5f)
    val max = Float.MaxValue

    override val boost = 4f

}