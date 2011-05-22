package org.whiskeysierra.powpow

import com.bulletphysics.collision.dispatch.CollisionObject
import com.bulletphysics.collision.shapes.SphereShape
import com.bulletphysics.dynamics.{RigidBodyConstructionInfo, RigidBody}
import com.bulletphysics.linearmath.{Transform, MotionState}
import javax.vecmath.{Vector3f, Matrix4f}

object Bullet {
    
    def apply(position:Vector, direction:Vector):Bullet = {
        val radius = .5f
        val shape = new SphereShape(radius)
        val mass = 1
        val inertia = new Vector3f
        shape.calculateLocalInertia(mass, inertia)
        val bullet = new Bullet(position, direction)
        val info = new RigidBodyConstructionInfo(mass, bullet, shape, inertia)
        info.restitution = 0
        info.linearDamping = 0
        info.angularDamping = 0
        val body = new RigidBody(info)
        val matrix = new Matrix4f
        matrix.set(position toVector3f)
        body.proceedToTransform(new Transform(matrix))
        body.setLinearVelocity(direction toVector3f);
        body.setActivationState(CollisionObject.DISABLE_DEACTIVATION)
        
        bullet.body = body
        
        return bullet
    }
    
}

class Bullet(var position:Vector, val direction:Vector) extends MotionState {

    var body:RigidBody = null
    
    override def getWorldTransform(transform:Transform) = {
        val matrix = new Matrix4f
        matrix.set(position toVector3f)
        transform.set(matrix)
        transform
    }

    override def setWorldTransform(transform:Transform) = {
        val matrix = new Matrix4f
        transform.getMatrix(matrix)
        val translation = new Vector3f
        matrix.get(translation)
        position = Vector(translation)
    }
    
}