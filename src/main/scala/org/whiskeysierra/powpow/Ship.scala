package org.whiskeysierra.powpow

import com.bulletphysics.collision.shapes.SphereShape
import de.bht.jvr.core.{Transform, SceneNode}
import de.bht.jvr.math.Vector3
import javax.vecmath.Vector3f
import scala.actors.Actor

class Ship(private val node:SceneNode) extends Actor with Physical with Collidable {
    
    private val stopped = new Vector3f
    
    val shape = new SphereShape(1f)
    override val boost = 15f

    override def act():Unit = {
        loop {
            react {
                case Start =>
                    sender ! AddBody(body, Collisions.SHIP, Collisions.NOTHING)
                case Move(movement) =>
                    velocity = 1 
                    direction = movement.normalize
                case Stop =>
                    velocity = 0
                case Update => 
                    update
                    sender ! Position(position)
                case PoisonPill => exit
            }
        }
    }
    
    private def angle:Float = {
        if (direction.x == -1) {
            math.Pi.toFloat
        } else {
            2 * math.atan(direction.y / (1 + direction.x)).toFloat
        }
    }
    
    private def update = {
        node.setTransform(
            Transform.translate(position.x, position.y, 0) mul
            Transform.rotateZ(angle)
        )
    }
    
}