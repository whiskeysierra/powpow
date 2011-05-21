package org.whiskeysierra.powpow

sealed abstract class Message

case object Start extends Message

case object Update extends Message

// length of movement defines speed, 1.0 is maximum
case class Move(val movement:Vector) extends Message
case class Position(val position:Vector) extends Message
case class Direction(val direction:Vector) extends Message

case class Aim(val direction:Vector) extends Message
case class Fire(val position:Vector) extends Message

case object PoisonPill extends Message
case object Quit extends Message
