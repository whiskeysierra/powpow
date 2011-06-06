package org.whiskeysierra.powpow

import de.bht.jvr.util.StopWatch

trait Clock {

    private val watch = new StopWatch
    private var elapsed = 0f

    def loop:Float

    def tick():Boolean = {
        elapsed += watch.elapsed()
        if (elapsed > loop) {
            elapsed = 0f
            true
        } else {
            false
        }
    }

}
