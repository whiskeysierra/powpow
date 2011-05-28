package org.whiskeysierra.powpow
import Vector.toVector3f

import com.bulletphysics.collision.shapes.{CollisionShape, SphereShape}
import de.bht.jvr.math.Vector3

object Bullet {
    
    def apply():Bullet = new Bullet
    
}

class Bullet extends Physical with Collidable {
    
    val shape = new SphereShape(.5f)
    override val boost = 25f
    var energy = 1f
 
}
