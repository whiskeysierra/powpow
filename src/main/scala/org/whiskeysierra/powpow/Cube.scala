package org.whiskeysierra.powpow

import de.bht.jvr.core.{Transform, SceneNode}
import javax.vecmath.Vector2f
import scala.actors.Actor

class Cube(private val node:SceneNode) extends Actor {
    
    private var x:Float = 1
    private var y:Float = 1
    
    private var last:Vector2f = new Vector2f
    
    override def act():Unit = {
        loop {
            react {
                case MoveX(value) => x += value
                case MoveY(value) => y += value
                case _:Update => 
                    sender ! Position(x, y)
                    update
                case PoisonPill => exit
            }
        }
    }
    
    def update():Unit = {
        val current = new Vector2f(x, y)
        val direction = new Vector2f(x, y)
        
        direction.sub(last)
        
        var angle = 0f
        
        if (direction.length > 0) {
            last = new Vector2f(x, y)
            direction.normalize
            angle = Math.acos(direction.dot(new Vector2f(1, 0))).toFloat
            
//            if (y < 0) {
//                angle = Math.Pi.toFloat * 2f - angle
//            }
            node.setTransform(
                    Transform.scale(1.0f).mul(
                            Transform.translate(x, y, 0).mul(
                                    Transform.rotateZ(angle)
                            )))
        }
        
        
    }
    
}