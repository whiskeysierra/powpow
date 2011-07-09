package org.whiskeysierra.powpow

import com.bulletphysics.collision.shapes.BoxShape
import javax.vecmath.Vector3f

/**
 * A small object which is the result of a Bullet hitting an enemy or the wall.
 */
class Particle extends Physical {

    val shape = new BoxShape(new Vector3f(.2f, .2f, .2f))
    var energy = 25f
    override val boost = 2f

    var color = Colors.RED

    def alive = energy > 0
    def dead = energy <= 0

}