package org.whiskeysierra.powpow

import com.bulletphysics.collision.shapes.{CollisionShape, StaticPlaneShape}
import javax.vecmath.Vector3f
import Vector.toVector3

object Wall {
    
    def apply(normal:Vector3f) = {
        val position = new Vector3f(normal)
        position.scale(-15)
        val shape = new StaticPlaneShape(normal, 1)
        val wall = new Wall(shape)
        wall.position = position
        wall
    }
    
}

class Wall(override val shape:CollisionShape) extends Physical with Collidable {
    
    override val mass = 0f
    
}
