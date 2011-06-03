package org.whiskeysierra.powpow

import com.bulletphysics.dynamics.RigidBody
import de.bht.jvr.core.{GroupNode, SceneNode}
import de.bht.jvr.math.Vector3

sealed abstract class Message

case object Start extends Message

case object Update extends Message
case class Resize(width:Int, height:Int) extends Message

case class Add(parent:GroupNode, child:SceneNode) extends Message
case class Remove(parent:GroupNode, orphan:SceneNode) extends Message

case class Move(movement:Vector3) extends Message
case object Stop extends Message
case class Position(position:Vector3) extends Message

case class Aim(direction:Vector3) extends Message

case class AddBody(body:RigidBody, bit:Short, mask:Short) extends Message
case class RemoveBody(body:RigidBody) extends Message

case class Miss(bullet:Bullet) extends Message
case class SeekerHit(seeker:Seeker, bullet:Bullet) extends Message
case class BomberHit(bomber:Bomber, bullet:Bullet) extends Message

case object PoisonPill extends Message
case object Quit extends Message
