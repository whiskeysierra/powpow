package org.whiskeysierra.powpow

case class Update(elapsed:Float)

case class MoveX(val delta:Float)
case class MoveY(val delta:Float)

case class Position(val x:Float, val y:Float)

case class ShootX(val value:Float)
case class ShootY(val value:Float)

case object PoisonPill
case object Exit
