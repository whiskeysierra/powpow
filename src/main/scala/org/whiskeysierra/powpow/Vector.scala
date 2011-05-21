package org.whiskeysierra.powpow

object Vector {
    def apply(x:Float=0, y:Float=0) = new Vector(x, y)
    def unapply(v:Vector):Option[(Float, Float)] = Some((v.x, v.y))
}

final class Vector(var x:Float, var y:Float) {

    def +(v:Vector) = Vector(x + v.x, y + v.y)
    
    def -(v:Vector) = Vector(x - v.x, y - v.y)
    
    def *(s:Float) = Vector(x * s, y * s)
    
    def /(s:Float) = this * (1 / s)
    
    def length = math.sqrt(math.pow(x, 2) + math.pow(y, 2)).toFloat
    
    def normalize = if (isZero) Vector() else this / length
    
    def dot(v:Vector) = x * v.x + y * v.y
    
    def copy = Vector(x, y)
    
    def isZero = x == 0 && y == 0

    private def eq(a:Float, b:Float) = a - b < 0.1f
    
    override def equals(that:Any):Boolean = that match {
        case Vector(x, y) => eq(this.x, x) && eq(this.y, y)
        case other => false
    }
    
    override def hashCode = x.hashCode ^ y.hashCode
    
    override def toString = "[%.2f, %.2f]".format(x, y)
    
}