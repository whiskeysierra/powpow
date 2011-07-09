package org.whiskeysierra.powpow

import com.bulletphysics.dynamics.RigidBody
import de.bht.jvr.core.{GroupNode, SceneNode}
import de.bht.jvr.math.Vector3

case object Start

case object Update

case class Resize(width: Int, height: Int)

case class Move(movement: Vector3)

case object Stop

case class Position(position: Vector3)

case class Aim(direction: Vector3)

case class AddBody(body: RigidBody, bit: Short, mask: Short)

case class RemoveBody(body: RigidBody)

case class Miss(bullet: Bullet)

case class Bounce(seeker: Seeker)

case class SeekerHit(seeker: Seeker, bullet: Bullet)

case class BomberHit(bomber: Bomber, bullet: Bullet)

case object PoisonPill

case object Quit
