package org.whiskeysierra.powpow

import scala.actors.Actor
import de.bht.jvr.renderer.{RenderWindow, Viewer}

class Updater(private val window:RenderWindow) extends Actor {

    private val viewer:Viewer = new Viewer(window)
    
    override def act = {
        loop {
            react {
                case Start =>
                    sender ! Update
                case Update =>
                    if (viewer.isRunning) {
                        sender ! Update
                        viewer.display
                    }
                case PoisonPill =>
                    viewer.close
                    exit
            }
        }
    }
    
}