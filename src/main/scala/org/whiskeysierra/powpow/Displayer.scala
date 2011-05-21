package org.whiskeysierra.powpow

import scala.actors.Actor
import de.bht.jvr.renderer.{RenderWindow, Viewer}

class Displayer(private val window:RenderWindow) extends Actor {

    private val viewer:Viewer = new Viewer(window)
    
    override def act = {
        loop {
            react {
                case Start => displayAndUpdate
                case Update =>
                    if (viewer.isRunning) displayAndUpdate
                case Add(parent, child) => parent.addChildNodes(child)
                case Remove(parent, orphan) => parent.removeChildNode(orphan)
                case PoisonPill =>
                    viewer.close
                    exit
            }
        }
    }
    
    private def displayAndUpdate = {
        viewer.display
        sender ! Update
    }
    
}