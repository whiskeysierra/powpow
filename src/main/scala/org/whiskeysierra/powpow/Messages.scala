package org.whiskeysierra.powpow

import de.bht.jvr.core.{GroupNode, SceneNode}

sealed abstract class Message

case object Start extends Message

case object Update extends Message
case class Add(val parent:GroupNode, val child:SceneNode) extends Message
case class Remove(val parent:GroupNode, val orphan:SceneNode) extends Message

// length of movement defines speed, 1.0 is maximum
case class Move(val movement:Vector) extends Message
case class Position(val position:Vector) extends Message
case class Direction(val direction:Vector) extends Message

case class Aim(val direction:Vector) extends Message

case object PoisonPill extends Message
case object Quit extends Message
