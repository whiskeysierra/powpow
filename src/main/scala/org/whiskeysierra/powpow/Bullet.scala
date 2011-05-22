package org.whiskeysierra.powpow

import com.bulletphysics.collision.shapes.{CollisionShape, SphereShape}
import com.bulletphysics.linearmath.MotionState

object Bullet {
    
    def apply(position:Vector, direction:Vector):Bullet = {
        val radius = .5f
        val shape = new SphereShape(radius)
        val bullet = new Bullet(position, direction, shape)
        bullet.body.setLinearVelocity(direction * 25 toVector3f)
        bullet
    }
    
}

class Bullet(var position:Vector, val direction:Vector, val shape:CollisionShape) extends Physical
