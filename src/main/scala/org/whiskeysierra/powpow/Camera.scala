package org.whiskeysierra.powpow

import de.bht.jvr.core.{CameraNode, Transform}

class Camera(private val node: CameraNode) extends Actor {

    override def act(message:Any) {
        message match {
            case Position(position) => {
                // TODO camera and ship should share translation
                node.setTransform(Transform.translate(position.x, position.y, 25))
            }
            case PoisonPill => exit()
            case _ =>
        }
    }

}