package org.whiskeysierra.powpow

import de.bht.jvr.core.{AttributeCloud, GroupNode, Shader, ShaderMaterial, ShaderProgram, ShapeNode}
import de.bht.jvr.core.uniforms.{UniformFloat, UniformVector4}
import de.bht.jvr.math.Vector4
import javax.media.opengl.{GL, GL3, GL2ES2, GL2GL3}
import javax.vecmath.Vector3f

class Grid(private val parent: GroupNode) extends Actor with ResourceLoader {

    val size = 50

    override def act(message:Any) {
        message match {
            case Start =>
                generateWalls()
                grid(75, size)
                grid(100, size, -50, 0.2f)
            case PoisonPill => exit()
            case _ =>
        }
    }

    private def normals = Array(
        new Vector3f(0, 1, 0),
        new Vector3f(0, -1, 0),
        new Vector3f(1, 0, 0),
        new Vector3f(-1, 0, 0)
    )

    private def generateWalls() {
        for (normal <- normals) {
            val wall = Wall(normal)
            sender ! AddBody(wall.body, Collisions.WALL, Collisions.WITH_WALL)
        }
    }

    private def grid(max: Float, size: Float, z: Float = 0, alpha: Float = 1) {
        val shape: ShapeNode = new ShapeNode

        val vs = new Shader(load("grid.vs"), GL2ES2.GL_VERTEX_SHADER)
        val gs = new Shader(load("grid.gs"), GL3.GL_GEOMETRY_SHADER)
        val fs = new Shader(load("color.fs"), GL2ES2.GL_FRAGMENT_SHADER)

        val program = new ShaderProgram(vs, fs, gs)

        program.setParameter(GL2GL3.GL_GEOMETRY_INPUT_TYPE_ARB, GL.GL_POINTS)
        program.setParameter(GL2GL3.GL_GEOMETRY_OUTPUT_TYPE_ARB, GL.GL_LINE_STRIP)
        program.setParameter(GL2GL3.GL_GEOMETRY_VERTICES_OUT_ARB, math.floor(max / size) * 8 toInt)

        val material = new ShaderMaterial("AMBIENT", program)
        material.setUniform("AMBIENT", "z", new UniformFloat(z))
        material.setUniform("AMBIENT", "maximum", new UniformFloat(max))
        material.setUniform("AMBIENT", "size", new UniformFloat(size))
        material.setUniform("AMBIENT", "color", new UniformVector4(new Vector4(1, 1, 1, alpha)))

        shape.setGeometry(new AttributeCloud(1))
        shape.setMaterial(material)

        sender ! Add(parent, shape)
    }

}