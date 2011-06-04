package org.whiskeysierra.powpow

import com.bulletphysics.collision.broadphase.AxisSweep3
import com.bulletphysics.collision.dispatch.{CollisionConfiguration, CollisionDispatcher, DefaultCollisionConfiguration}
import com.bulletphysics.dynamics.{DiscreteDynamicsWorld, DynamicsWorld, InternalTickCallback, RigidBody}
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver
import de.bht.jvr.util.StopWatch
import javax.vecmath.Vector3f


class Space extends Actor {

    private val config: CollisionConfiguration = new DefaultCollisionConfiguration
    private val limit = 50f

    private val world = new DiscreteDynamicsWorld(
        new CollisionDispatcher(config),
        new AxisSweep3(new Vector3f(-limit, -limit, -limit), new Vector3f(limit, limit, limit)),
        new SequentialImpulseConstraintSolver,
        config
    )

    private val time = new StopWatch

    override def act(message:Any) {
        message match {
            case Start =>
                world.setGravity(new Vector3f)
                world.getDispatchInfo.allowedCcdPenetration = 0f
                world.setInternalTickCallback(new InternalTickCallback {
                    override def internalTick(world: DynamicsWorld, elapsed: Float) {
                        handleCollisions()
                    }
                }, null)
            case AddBody(body, bit, mask) => world.addRigidBody(body, bit, mask)
            case RemoveBody(body) => world.removeRigidBody(body)
            case Update => update()
            case PoisonPill => exit()
            case _ =>
        }
    }

    private def update() {
        world.stepSimulation(time.elapsed, 10)
        handleCollisions()
    }

    private def handleCollisions() {
        val dispatcher = world.getDispatcher
        val n: Int = dispatcher.getNumManifolds

        for (i <- 0 until n) {
            val manifold = dispatcher.getManifoldByIndexInternal(i)
            val leftBody = manifold.getBody0.asInstanceOf[RigidBody]
            val rightBody = manifold.getBody1.asInstanceOf[RigidBody]
            val left = leftBody.getUserPointer
            val right = rightBody.getUserPointer

            left match {
                case b: Bullet => right match {
                    case _: Wall => b.energy -= 0.01f
                    case _ =>
                }
                case _ =>
            }
        }
    }

}