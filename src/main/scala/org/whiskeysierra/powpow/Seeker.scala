package org.whiskeysierra.powpow

import com.bulletphysics.collision.shapes.SphereShape

class Seeker extends Physical with Enemy {

    val max = 1f
    val shape = new SphereShape(.5f)

    override val boost = 2f

}
