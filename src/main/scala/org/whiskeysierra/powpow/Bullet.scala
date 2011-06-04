package org.whiskeysierra.powpow

import com.bulletphysics.collision.shapes.SphereShape
object Bullet {

    def apply(): Bullet = new Bullet

}

class Bullet extends Physical with Collidable with Energetic {

    val shape = new SphereShape(.1f)
    override val boost = 25f

}
