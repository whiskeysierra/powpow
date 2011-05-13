package org.whiskeysierra.powpow

import de.bht.jvr.core.{Transform, SceneNode}
import de.bht.jvr.util.InputState
import java.awt.event.KeyEvent

class Cube(private val node:SceneNode) extends FrameListener {
    
    private var angleY:Float = 0
    private var angleX:Float = 0
    private val speed:Float = 90
    
    override def update(elapsed:Float, input:InputState):Unit = {
        
        if (input.isDown(KeyEvent.VK_UP)) {
            angleX += elapsed * speed
        }
        
        if (input.isDown(KeyEvent.VK_LEFT)) {
            angleY -= elapsed * speed
        }
        
        if (input.isDown(KeyEvent.VK_DOWN)) {
            angleX -= elapsed * speed
        }
        
        if (input.isDown(KeyEvent.VK_RIGHT)) {
            angleY += elapsed * speed
        }

        node.setTransform(Transform.rotateYDeg(angleY).mul(Transform.rotateXDeg(angleX)))
    }
    
}