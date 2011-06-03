package org.whiskeysierra.powpow

import de.bht.jvr.math.Vector3
import javax.vecmath.Vector3f

object Vector {

    implicit def toVector3(v: Vector3f) = new Vector3(v.x, v.y, v.z)

    implicit def toVector3f(v: Vector3) = new Vector3f(v.x, v.y, v.z)

}
