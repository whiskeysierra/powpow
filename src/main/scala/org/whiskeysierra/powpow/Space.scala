package org.whiskeysierra.powpow

import com.bulletphysics.collision.broadphase.AxisSweep3
import com.bulletphysics.collision.dispatch.{CollisionConfiguration, CollisionDispatcher, DefaultCollisionConfiguration}
import com.bulletphysics.dynamics.DiscreteDynamicsWorld
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver
import de.bht.jvr.util.StopWatch
import javax.vecmath.Vector3f

import scala.actors.Actor

class Space extends Actor {

    private val config:CollisionConfiguration = new DefaultCollisionConfiguration
    private val limit = 50f
    
    private val world = new DiscreteDynamicsWorld(
        new CollisionDispatcher(config),
        new AxisSweep3(new Vector3f(-limit, -limit, -limit), new Vector3f(limit, limit, limit)),
        new SequentialImpulseConstraintSolver,
        config
    )
    
    private val time = new StopWatch

    override def act = {
        loop {
            react {
                case Start =>
                    world.setGravity(new Vector3f)
                    world.getDispatchInfo().allowedCcdPenetration = 0
                case AddBody(body, bit, mask) => world.addRigidBody(body, bit, mask)
                case RemoveBody(body) => world.removeRigidBody(body)
                case Update => world.stepSimulation(time.elapsed, 10);
                case PoisonPill => exit
            }
        }
    }
        
}