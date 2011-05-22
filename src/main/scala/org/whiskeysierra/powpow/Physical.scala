package org.whiskeysierra.powpow

import com.bulletphysics.collision.dispatch.CollisionObject
import com.bulletphysics.collision.shapes.CollisionShape
import com.bulletphysics.dynamics.{RigidBodyConstructionInfo, RigidBody}
import com.bulletphysics.linearmath.{Transform, MotionState}
import javax.vecmath.{Vector3f, Matrix4f}

trait Physical {

    var position:Vector
    
    private val rigidBody = createBody
    
    def body:RigidBody = rigidBody
    def shape:CollisionShape
    
    private def createBody:RigidBody = {
        val mass = 1
        val inertia = new Vector3f
        shape.calculateLocalInertia(mass, inertia)
        
        var self = this
        val state = new MotionState {
            
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
    
        val info = new RigidBodyConstructionInfo(mass, state, shape, inertia)
        info.restitution = 0
        info.linearDamping = 0
        info.angularDamping = 0
        val body = new RigidBody(info)
        val matrix = new Matrix4f
        matrix.set(position toVector3f)
        body.proceedToTransform(new Transform(matrix))
        body.setActivationState(CollisionObject.DISABLE_DEACTIVATION)
        body
    }
    
}