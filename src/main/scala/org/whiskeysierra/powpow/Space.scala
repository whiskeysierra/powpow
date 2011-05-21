package org.whiskeysierra.powpow

import scala.actors.Actor
import com.bulletphysics.collision.broadphase._
import com.bulletphysics.collision.dispatch._
import com.bulletphysics.dynamics._
import com.bulletphysics.dynamics.constraintsolver._
import javax.vecmath._

class Space extends Actor {

    private val config:CollisionConfiguration = new DefaultCollisionConfiguration
    
    private val world = new DiscreteDynamicsWorld(
        new CollisionDispatcher(config),
        new AxisSweep3(new Vector3f(-10000, -10000, -10000), new Vector3f(10000, 10000, 10000)),
        new SequentialImpulseConstraintSolver,
        config
    )

    override def act = {
        loop {
            react {
                case Start =>
                    world.setGravity(new Vector3f(0, -10, 0))
                    world.getDispatchInfo().allowedCcdPenetration = 0
                case PoisonPill => exit
            }
        }
    }
        
}