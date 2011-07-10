package org.whiskeysierra.powpow

import com.bulletphysics.dynamics.RigidBody
import de.bht.jvr.math.Vector3

case object Start
case object Update
case class Move(movement: Vector3)
case object Stop
case class Position(position: Vector3)
case class Aim(direction: Vector3)
case class AddBody(body: RigidBody, bit: Short, mask: Short)
case class RemoveBody(body: RigidBody)
case class BulletWallHit(bullet: Bullet)
case class SeekerHit(seeker: Seeker, bullet: Bullet)
case class SeekerWallHit(seeker: Seeker)
case class BomberHit(bomber: Bomber, bullet: Bullet)
case class ParticleWallHit(particle: Particle)
case class BomberKill(bomber: Bomber)
case class BombWallHit(bomb: Bomb)
case class SeekerCollision(seeker: Seeker, ship: Ship, point: Vector3)
case class BomberCollision(bomber: Bomber, ship: Ship, point: Vector3)
case class BombCollision(bomb: Bomb, ship: Ship, point: Vector3)
case object PoisonPill
case object Quit
