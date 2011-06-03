package org.whiskeysierra.powpow

import com.bulletphysics.collision.shapes.SphereShape
object Bullet {

    def apply(): Bullet = new Bullet

}

class Bullet extends Physical with Collidable {

    val shape = new SphereShape(.5f)
    override val boost = 25f
    var energy = 1f

}
