package org.whiskeysierra.powpow

case object Update

case class MoveX(val delta:Float)
case class MoveY(val delta:Float)

case class Position(val x:Float, val y:Float)

case class ShootX(val value:Float)
case class ShootY(val value:Float)

case object PoisonPill
case object Quit
