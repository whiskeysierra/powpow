package org.whiskeysierra.powpow

import de.bht.jvr.math.Vector3
import javax.vecmath.Vector3f

object Vector {
    
    def apply():Vector3 = apply(0, 0)
    
    def apply(x:Float, y:Float) = new Vector3(x, y, 0)
    
    def apply(v:Vector3f) = new Vector3(v.x, v.y, v.z)
    
    def isZero(v:Vector3) = v.x == 0 && v.y == 0
    
    def isNotZero(v:Vector3) = !isZero(v)
    
    def copy(v:Vector3) = new Vector3(v.x, v.y, v.z)
    
    implicit def toVector3f(v:Vector3) = new Vector3f(v.x, v.y, v.z)
    
}
