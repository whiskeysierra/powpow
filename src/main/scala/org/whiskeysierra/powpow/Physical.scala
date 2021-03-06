package org.whiskeysierra.powpow

import com.bulletphysics.collision.dispatch.CollisionObject
import com.bulletphysics.collision.shapes.CollisionShape
import com.bulletphysics.linearmath.{Transform, MotionState}
import de.bht.jvr.math.Vector3
import Vector.toVector3f
import Vector.toVector3
import com.bulletphysics.dynamics.{RigidBodyConstructionInfo, RigidBody}
import javax.vecmath.{Vector3f, Matrix4f}

private object Physical {

    val ZERO = new Vector3f

}

trait Physical {

    val shape: CollisionShape

    val mass = 1f
    val boost = 1f

    private var p = new Vector3

    final def position = p

    final def position_=(p: Vector3) {
        this.p = p
        val matrix = new Matrix4f
        matrix.set(position)
        body.proceedToTransform(new Transform(matrix))
    }

    private var d = new Vector3

    final def direction = d

    final def direction_=(d: Vector3) {
        this.d = d
        body.setLinearVelocity(direction mul velocity * boost)
    }

    private var v = 1f

    final def velocity = v

    final def velocity_=(v: Float) {
        this.v = v
        body.setLinearVelocity(Physical.ZERO)
    }

    private var b: RigidBody = null

    def body: RigidBody = if (b == null) createAndSetBody else b

    private def createAndSetBody: RigidBody = {
        b = createBody()
        b
    }

    private def createBody(): RigidBody = {
        val inertia = new Vector3f
        shape.calculateLocalInertia(mass, inertia)

        val state = new MotionState {

            override def getWorldTransform(transform: Transform) = {
                val matrix = new Matrix4f
                matrix.set(position)
                // TODO add direction as rotation
                transform.set(matrix)
                transform
            }

            override def setWorldTransform(transform: Transform) {
                val matrix = new Matrix4f
                transform.getMatrix(matrix)
                val translation = new Vector3f
                // TODO extract rotation into direction
                matrix.get(translation)
                // FIXME as soon as jBullet supports RigidBody.setLinearFactor(Vector3f)
                translation.z = 0f
                position = translation
            }

        }

        val info = new RigidBodyConstructionInfo(mass, state, shape, inertia)
        info.friction = 0f
        info.restitution = .25f
        info.linearDamping = 0f
        info.angularDamping = 0f

        val body = new RigidBody(info)
        body.setActivationState(CollisionObject.DISABLE_DEACTIVATION)
        body.setUserPointer(this)
        body
    }

    override def toString = {
        "%s [%s, %s, %f]" format (super.toString, position, direction, velocity)
    }

}