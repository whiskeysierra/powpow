package org.whiskeysierra.powpow

import com.bulletphysics.collision.shapes.{CollisionShape, SphereShape}
import com.google.common.base.{Function, Predicate}
import de.bht.jvr.math.Vector3
import Vector.toVector3f

object Bullet {
    
    val Position = new Function[Bullet, Vector3] {
        override def apply(bullet:Bullet):Vector3 = bullet.position
    }
    
    val Energy = new Function[Bullet, java.lang.Float] {
        override def apply(bullet:Bullet):java.lang.Float = bullet.energy
    }
    
    val Inactive = new Predicate[Bullet] {
        override def apply(bullet:Bullet):Boolean = bullet.energy <= 0f
    }
    
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
