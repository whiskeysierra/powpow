package org.whiskeysierra.powpow

import de.bht.jvr.core.{Transform, SceneNode}
import javax.vecmath.Vector2f
import org.whiskeysierra.powpow.input.{MoveY, MoveX}
import scala.actors.Actor

class Cube(private val node:SceneNode) extends Actor {
    
    var x:Float = 0
    var y:Float = 0
    
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
        var angle = 0f
        
        if (current.length > 0) {
            current.normalize
            angle = Math.acos(current.dot(new Vector2f(1, 0))).toFloat
            
            if (y < 0) {
                angle = Math.Pi.toFloat * 2f - angle
            }
        }
        
        node.setTransform(
            Transform.translate(x, y, 0).mul(
            Transform.rotateZ(angle)
        ))
    }
    
}