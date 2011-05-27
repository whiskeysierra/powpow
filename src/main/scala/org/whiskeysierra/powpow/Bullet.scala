package org.whiskeysierra.powpow

import com.bulletphysics.collision.shapes.{CollisionShape, SphereShape}
import de.bht.jvr.math.Vector3
import Vector.toVector3f

object Bullet {
    
    def apply():Bullet = apply(Vector(), Vector())
    
    def apply(position:Vector3, direction:Vector3):Bullet = {
        val radius = .5f
        val shape = new SphereShape(radius)
        val bullet = new Bullet(position, direction, shape)
        bullet.body.setLinearVelocity(direction mul 25)
        bullet
    }
    
}

class Bullet(var position:Vector3, var direction:Vector3, val shape:CollisionShape) extends Physical {
    
    var energy:Float = 0
 
    override def toString = "Bullet(%s, %s, %f)" format (position, direction, energy)
    
}
