package org.whiskeysierra.powpow
import de.bht.jvr.core.{CameraNode, Transform}
import de.bht.jvr.util.InputState

class Camera(private val node:CameraNode) {
    
    private var x:Float = 0
    private var y:Float = 0
    private val speed:Float = 0.01f
    
    def update(elapsed:Float, input:InputState):Unit = {
    
        if (input.isDown('W')) {
            y += speed 
        }
        
        if (input.isDown('A')) {
            x -= speed
        }
        
        if (input.isDown('S')) {
            y -= speed
        }
        
        if (input.isDown('D')) {
            x += speed
        }
        
        node.setTransform(Transform.translate(x, y, 3))
        
    }
    
}