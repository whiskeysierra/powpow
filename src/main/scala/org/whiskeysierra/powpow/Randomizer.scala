package org.whiskeysierra.powpow

import scala.util.Random
import de.bht.jvr.math.Vector3

trait Randomizer {

    private val random = new Random

    def randomDirection = new Vector3(random.nextInt(3) - 1, random.nextInt(3) - 1, 0)

    def randomPosition = new Vector3((2 * random.nextFloat - 1) * Space.MAX_SIZE, (2 * random.nextFloat - 1) * Space.MAX_SIZE, 0)

    private def randomMax = if (random.nextBoolean()) Space.MAX_SIZE else -Space.MAX_SIZE

    def randomCorner = new Vector3(randomMax * .7f, randomMax * .7f, 0)

    private def cos(a:Float) = math.cos(a.toDouble).toFloat
    private def sin(a:Float) = math.sin(a.toDouble).toFloat

    def randomAngle(max:Float):Float = ((random.nextFloat() - 0.5f) * 0.5f * max).toFloat

    def spread(direction:Vector3, angle:Float):Vector3 = new Vector3(
        cos(angle) * direction.x - sin(angle) * direction.y,
        sin(angle) * direction.x + cos(angle) * direction.y,
        0
    )

}