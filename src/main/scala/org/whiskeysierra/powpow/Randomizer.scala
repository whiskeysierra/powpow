package org.whiskeysierra.powpow

import de.bht.jvr.math.Vector3
import scala.util.Random

trait Randomizer {

	private val random = new Random
	
	def randomVector = new Vector3(random.nextInt(1) + 1, random.nextInt(1) + 1, 0)
	
}