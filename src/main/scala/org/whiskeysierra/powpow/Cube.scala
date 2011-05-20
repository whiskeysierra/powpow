package org.whiskeysierra.powpow

import de.bht.jvr.core.{Transform, SceneNode}
import javax.vecmath.Vector2f
import org.whiskeysierra.powpow.input.{MoveY, MoveX}
import scala.actors.Actor

class Cube(private val node:SceneNode) extends Actor {
    
    var x:Float = 1
    var y:Float = 1
    
    private var last:Vector2f = new Vector2f
    
    private val speed = 0.01f
    
    override def act():Unit = {
        loop {
            react {
                case MoveX(value) => x += value * speed
                case MoveY(value) => y += value * speed
                case _:Update => update
                case Exit => exit()
            }
        }
    }
    
    def update():Unit = {
        val current = new Vector2f(x, y)
        
        if (last.epsilonEquals(current, 0.01f)) {
            return
        }
        
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