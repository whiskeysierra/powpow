package org.whiskeysierra.powpow

import com.bulletphysics.collision.shapes.TriangleShape
import javax.vecmath.Vector3f
import de.bht.jvr.math.Vector3

class Seeker extends Physical with Enemy {

    val max = 1f
    val shape = new TriangleShape(new Vector3f(0, 1, 0), new Vector3f(.5f, 0, 0), new Vector3f(-.5f, 0, 0))

}
