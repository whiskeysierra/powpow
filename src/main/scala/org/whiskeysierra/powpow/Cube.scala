package org.whiskeysierra.powpow

import scala.actors.Actor
import de.bht.jvr.core.{Transform, SceneNode}
import de.bht.jvr.util.InputState
import java.awt.event.KeyEvent

class Cube(private val node:SceneNode) {
    
    private var moveX:Float = 0
    private var angleY:Float = 0
    private var moveY:Float = 0
    private var angleX:Float = 0
    private val speed:Float = 1
    
    def moveX(value:Float):Unit = {
        moveX = value
    }
    
    def moveY(value:Float):Unit = {
        moveY = value
    }
    
    def update(elapsed:Float, input:InputState):Unit = {
        
//        if (input.isDown(KeyEvent.VK_UP)) {
//            angleX += elapsed * speed
//        }
//        
//        if (input.isDown(KeyEvent.VK_LEFT)) {
//            angleY -= elapsed * speed
//        }
//        
//        if (input.isDown(KeyEvent.VK_DOWN)) {
//            angleX -= elapsed * speed
//        }
//        
//        if (input.isDown(KeyEvent.VK_RIGHT)) {
//            angleY += elapsed * speed
//        }

        
        angleX += moveX * speed
        angleY += moveY * speed
        
        node.setTransform(Transform.rotateYDeg(angleY).mul(Transform.rotateXDeg(angleX)))
    }
    
}