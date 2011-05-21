package org.whiskeysierra.powpow

sealed abstract class Message

case object Update extends Message

case class MoveX(val value:Float) extends Message
case class MoveY(val value:Float) extends Message

case class Position(val x:Float, val y:Float) extends Message
case class Direction(val angle:Float) extends Message

case class ShootX(val value:Float) extends Message
case class ShootY(val value:Float) extends Message

case object PoisonPill extends Message
case object Quit extends Message
