package org.whiskeysierra.powpow

import scala.actors.Actor
import de.bht.jvr.renderer.{RenderWindow, Viewer}

class Updater(private val window:RenderWindow) extends Actor {

    private val viewer:Viewer = new Viewer(window)
    
    override def act = {
        loop {
            react {
                case Start => displayAndUpdate
                case Update =>
                    if (viewer.isRunning) displayAndUpdate
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