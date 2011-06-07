package org.whiskeysierra.powpow

import de.bht.jvr.core.{SceneNode, GroupNode, Transform}
import collection.mutable.HashSet

class Squadron(private val parent: GroupNode, private val sphere: SceneNode) extends Actor with Randomizer with Clock {

    private val bombers = new HashSet[Bomber]
    private val max = 10

    val loop = 1f

    override def act(message:Any) {
        message match {
            case Start =>
                sphere.setTransform(Transform.scale(2))
            case Update =>
                if (tick()) {
                    if (bombers.size < max) {
                        val bomber = new Bomber(new GroupNode(sphere))
                        bomber.position = randomPosition
                        bomber.direction = randomDirection
                        sender ! AddNode(parent, bomber.node)
                        sender ! AddBody(bomber.body, Collisions.BOMBER, Collisions.WITH_BOMBER)
                        bombers.add(bomber)
                    }
                }
                for (bomber <- bombers) {
                    bomber.node.setTransform(Transform.translate(bomber.position))
                }
            case BomberHit(bomber, _) =>
                bomber.hit()
                if (bomber.dead) {
                    sender ! RemoveNode(parent, bomber.node)
                    sender ! RemoveBody(bomber.body)
                    bombers.remove(bomber)
                }
            case PoisonPill => exit()
            case _ =>
        }
    }

}