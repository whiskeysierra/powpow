package org.whiskeysierra.powpow

import de.bht.jvr.core.{SceneNode, GroupNode, Transform}
import de.bht.jvr.math.Vector3
import de.bht.jvr.util.StopWatch
import scala.actors.Actor

class Squadron(private val parent:GroupNode, private val sphere:SceneNode) extends Actor with Randomizer {

    private val bombers = new Array[Bomber](1)
    private val dead = bombers.view.filter({_.isDead})
    
    private val time = new StopWatch
    private var elapsed = 0f

    override def act = {
        loop {
            react {
                case Start => 
                    for (i <- 0 until bombers.length) {
                        bombers.update(i, new Bomber(new GroupNode(sphere)))
                    }
                    
                        val bomber = dead.head
                        println("Bomber! " + bomber)
                        bomber.direction = new Vector3(1, 1, 0)
                        bomber.revive
                        sender ! AddBody(bomber.body, Collisions.BOMBER, Collisions.WITH_BOMBER)
                        sender ! Add(parent, bomber.node)
                case Update =>
//                    elapsed += time.elapsed
//                    if (elapsed > 1) {
//                        elapsed = 0
//                        if (!dead.isEmpty) {
//                            val bomber = dead.head
//                            println("Bomber! " + bomber)
//                            bomber.direction = new Vector3(1, 1, 0)
//                            bomber.revive
//                            sender ! AddBody(bomber.body, Collisions.BOMBER, Collisions.WITH_BOMBER)
//                            sender ! Add(parent, bomber.node)
//                        }
//                    }
                    for (bomber <- bombers.filter({_.isAlive})) {
                        bomber.node.setTransform(Transform.translate(bomber.position))
                    }
                case PoisonPill => exit
            }
        }
    }
    
}