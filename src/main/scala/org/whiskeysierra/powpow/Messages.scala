package org.whiskeysierra.powpow

import com.bulletphysics.dynamics.RigidBody
import de.bht.jvr.core.{GroupNode, SceneNode}
import de.bht.jvr.math.Vector3

sealed abstract class Message

case object Start extends Message

case object Update extends Message
case class Resize(val width:Int, val height:Int) extends Message

case class Add(val parent:GroupNode, val child:SceneNode) extends Message
case class Remove(val parent:GroupNode, val orphan:SceneNode) extends Message

case class Move(val movement:Vector3) extends Message
case object Stop extends Message
case class Position(val position:Vector3) extends Message

case class Aim(val direction:Vector3) extends Message

case class AddBody(body:RigidBody, bit:Short, mask:Short) extends Message
case class RemoveBody(body:RigidBody) extends Message

case class Collision(left:Collidable, right:Collidable) extends Message

case object PoisonPill extends Message
case object Quit extends Message
