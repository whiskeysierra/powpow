package org.whiskeysierra.powpow

import scala.util.Random
import de.bht.jvr.math.Vector3

trait Randomizer {

    private val random = new Random

    def randomDirection = new Vector3(random.nextInt(3) - 1, random.nextInt(3) - 1, 0)

    def randomPosition = new Vector3((2 * random.nextFloat - 1) * Space.MAX_SIZE, (2 * random.nextFloat - 1) * Space.MAX_SIZE, 0)

}