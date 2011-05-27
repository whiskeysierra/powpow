package org.whiskeysierra.powpow

import com.bulletphysics.collision.shapes.{CollisionShape, SphereShape}
import de.bht.jvr.math.Vector3
import Vector.toVector3f

object Bullet {
    
    def apply():Bullet = new Bullet
    
}

class Bullet extends Physical {
    
    val shape = new SphereShape(.5f)
    override val mass = 1f
    override val boost = 25f
    var energy = 1f
 
    override def toString = "Bullet(%s, %s, %f)" format (position, direction, energy)
    
}
