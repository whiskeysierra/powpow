package org.whiskeysierra.powpow

import com.bulletphysics.collision.broadphase.AxisSweep3
import com.bulletphysics.collision.dispatch.{CollisionConfiguration, CollisionDispatcher, DefaultCollisionConfiguration}
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver
import de.bht.jvr.util.StopWatch
import com.bulletphysics.dynamics._
import com.sun.xml.internal.ws.developer.MemberSubmissionAddressing.Validation
import com.bulletphysics.collision.narrowphase.PersistentManifold
import javax.vecmath.Vector3f
import de.bht.jvr.math.Vector3

object Space {

    val MAX_SIZE = 15f

}

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

                        // TODO simplify order matching
                        left match {
                            case bullet:Bullet => right match {
                                case wall:Wall => sender ! BulletWallHit(bullet)
                                case bomber:Bomber => sender ! BomberHit(bomber, bullet)
                                case seeker:Seeker => sender ! SeekerHit(seeker, bullet)
                                case _ =>
                            }
                            case wall:Wall => right match {
                                case bullet:Bullet => sender ! BulletWallHit(bullet)
                                case seeker:Seeker => sender ! SeekerWallHit(seeker)
                                case particle:Particle => sender ! ParticleWallHit(particle)
                                case bomb:Bomb => sender ! BombWallHit(bomb)
                                case _ =>
                            }
                            case bomber:Bomber => right match {
                                case bullet:Bullet => sender ! BomberHit(bomber, bullet)
                                case ship:Ship => sender ! BomberCollision(bomber, ship, pointOfContact(manifold))
                                case _ =>
                            }
                            case seeker:Seeker => right match {
                                case bullet:Bullet => sender ! SeekerHit(seeker, bullet)
                                case ship:Ship => sender ! SeekerCollision(seeker, ship, pointOfContact(manifold))
                                case _:Wall => sender ! SeekerWallHit(seeker)
                                case _ =>
                            }
                            case particle:Particle => right match {
                                case wall:Wall => sender ! ParticleWallHit(particle)
                                case _ =>
                            }
                            case bomb:Bomb => right match {
                                case wall:Wall => sender ! BombWallHit(bomb)
                                case ship:Ship => sender ! BombCollision(bomb, ship, pointOfContact(manifold))
                                case _ =>
                            }
                            case ship:Ship => right match {
                                case bomber:Bomber => sender ! BomberCollision(bomber, ship, pointOfContact(manifold))
                                case seeker:Seeker => sender ! SeekerCollision(seeker, ship, pointOfContact(manifold))
                                case bomb:Bomb => sender ! BombCollision(bomb, ship, pointOfContact(manifold))
                                case _ =>
                            }
                            case _ =>
                        }
                    }

                }
            }
        }
    }

    private def pointOfContact(manifold: PersistentManifold):Vector3 = {
        val v = new Vector3f()
        manifold.getContactPoint(0).getPositionWorldOnA(v)
        return new Vector3(v.x, v.y, 0)
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
