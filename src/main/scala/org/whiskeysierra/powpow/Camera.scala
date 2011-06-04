package org.whiskeysierra.powpow

import de.bht.jvr.core.{CameraNode, Transform}

class Camera(private val node: CameraNode) extends Actor {

    override def act(message:Any):Unit = message match {
        case Position(position) => {
            // TODO camera and ship should share translation
            node.setTransform(Transform.translate(position.x, position.y, 15))
        }
        case Resize(width, height) => node.setAspectRatio(width.toFloat / height.toFloat)
        case PoisonPill => exit()
        case _ =>
    }

}