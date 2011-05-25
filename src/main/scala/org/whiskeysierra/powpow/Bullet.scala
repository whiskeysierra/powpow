package org.whiskeysierra.powpow

import com.bulletphysics.collision.shapes.{CollisionShape, SphereShape}
import com.google.common.base.{Function, Predicate}
import de.bht.jvr.math.Vector3

object Bullet {
    
    val Position = new Function[Bullet, Vector3] {
        override def apply(bullet:Bullet):Vector3 = bullet.position.toVector3
    }
    
    val Energy = new Function[Bullet, java.lang.Float] {
        override def apply(bullet:Bullet):java.lang.Float = bullet.energy
    }
    
    val Inactive = new Predicate[Bullet] {
        override def apply(bullet:Bullet):Boolean = bullet.energy <= 0f
    }
    
    def apply(position:Vector=Vector(), direction:Vector=Vector()):Bullet = {
        val radius = .5f
        val shape = new SphereShape(radius)
        val bullet = new Bullet(position, direction, shape)
        bullet.body.setLinearVelocity(direction * 25 toVector3f)
        bullet
    }
    
}

class Bullet(var position:Vector, var direction:Vector, val shape:CollisionShape) extends Physical {
    
    var energy:Float = 0
 
    override def toString = "Bullet(%s, %s, %f)" format (position, direction, energy)
    
}
