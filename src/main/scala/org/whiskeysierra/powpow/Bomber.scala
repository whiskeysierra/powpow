package org.whiskeysierra.powpow

import com.bulletphysics.collision.shapes.SphereShape
import de.bht.jvr.core.GroupNode

class Bomber(val node: GroupNode) extends Physical with Enemy {

    val max = 10f
    override val mass = 100f
    val shape = new SphereShape(1)

    def hit() {
        health -= 1
    }

}
