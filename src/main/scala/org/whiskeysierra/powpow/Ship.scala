package org.whiskeysierra.powpow

import com.bulletphysics.collision.shapes.SphereShape
import de.bht.jvr.core.{Transform, SceneNode}
import scala.actors.Actor
import javax.vecmath.Vector3f

class Ship(private val node:SceneNode, var position:Vector) extends Actor with Physical {
    
    private val axis = Vector(1)
    private val stopped = new Vector3f
    private val speed = 15
    
    private var direction = Vector()
    
    def this(node:SceneNode) = this(node, Vector())
    
    override def shape = new SphereShape(1f)

    override def act():Unit = {
        loop {
            react {
                case Start =>
                    sender ! AddBody(body, Collisions.SHIP, Collisions.NOTHING)
                case Move(movement) => 
                    direction = movement.normalize
                    body.setLinearVelocity(movement * speed toVector3f)
                case Stop =>
                    body.setLinearVelocity(stopped)
                case Update => 
                    update
                    sender ! Position(position)
                case PoisonPill => exit
            }
        }
    }
    
    private def update = {
        val angle = math.acos(axis dot direction).toFloat
        node.setTransform(
            Transform.translate(position.x, position.y, 0) mul
            Transform.rotateZ(angle)
        )
    }
    
}