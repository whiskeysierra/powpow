package org.whiskeysierra.powpow

import com.bulletphysics.collision.broadphase.AxisSweep3
import com.bulletphysics.collision.dispatch.{CollisionConfiguration, CollisionDispatcher, DefaultCollisionConfiguration}
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver
import de.bht.jvr.util.StopWatch
import javax.vecmath.Vector3f
import com.bulletphysics.dynamics._

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

    private val callback = new InternalTickCallback {
        def internalTick(world: DynamicsWorld, timeStep: Float) {
            val n: Int = world.getDispatcher.getNumManifolds

            for (i <- 0 until n) {
                val manifold = world.getDispatcher.getManifoldByIndexInternal(i)
                if (manifold ne null) {
                    val leftBody = manifold.getBody0.asInstanceOf[RigidBody]
                    val rightBody = manifold.getBody1.asInstanceOf[RigidBody]

                    if (manifold.getNumContacts > 0) {
                        val left = leftBody.getUserPointer
                        val right = rightBody.getUserPointer

                        left match {
                            case bullet:Bullet => right match {
                                case wall:Wall => sender ! Miss(bullet)
                                case _ =>
                            }
                            // TODO simplify order matching
                            case wall:Wall => right match {
                                case bullet:Bullet => sender ! Miss(bullet)
                                case _ =>
                            }
                            case _ =>
                        }
                    }

                }
            }
        }
    }

    override def act(message:Any) {
        message match {
            case Start =>
                world.setGravity(new Vector3f)
                world.getDispatchInfo.allowedCcdPenetration = 0f
                world.setInternalTickCallback(callback, null)
            case AddBody(body, bit, mask) => world.addRigidBody(body, bit, mask)
            case RemoveBody(body) => world.removeRigidBody(body)
            case Update => world.stepSimulation(time.elapsed, 10)
            case PoisonPill => exit()
            case _ =>
        }
    }

}
