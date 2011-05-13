package org.whiskeysierra.powpow

import de.bht.jvr.util.InputState
trait FrameListener {

    def update(elapsed:Float, input:InputState):Unit
    
}