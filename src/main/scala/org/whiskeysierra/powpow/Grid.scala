package org.whiskeysierra.powpow

import de.bht.jvr.core.{AttributeCloud, GroupNode, Shader, ShaderMaterial, ShaderProgram, ShapeNode}
import de.bht.jvr.core.attributes.AttributeVector3
import de.bht.jvr.core.uniforms.{UniformFloat, UniformVector4}
import de.bht.jvr.math.{Vector3, Vector4}
import javax.media.opengl.{GL, GL2, GL3, GL2ES2, GL2GL3}
import scala.actors.Actor
import scala.collection.JavaConversions._

class Grid(private val parent:GroupNode) extends Actor with ResourceLoader {

	override def act = {
		loop {
			react {
				case Start =>
                    walls
                    background
				case PoisonPill => exit
			}
		}
	}
	
	private def grid(max:Float, size:Float, z:Float=0, alpha:Float=1) = {
        val shape:ShapeNode = new ShapeNode
        
        val vs = new Shader(load("grid.vs"), GL2ES2.GL_VERTEX_SHADER)
        val gs = new Shader(load("grid.gs"), GL3.GL_GEOMETRY_SHADER)
        val fs = new Shader(load("color.fs"), GL2ES2.GL_FRAGMENT_SHADER)

        val program = new ShaderProgram(vs, fs, gs)            

        program.setParameter(GL2GL3.GL_GEOMETRY_INPUT_TYPE_ARB, GL.GL_POINTS)
        program.setParameter(GL2GL3.GL_GEOMETRY_OUTPUT_TYPE_ARB, GL.GL_LINE_STRIP)
        // TODO calculate maximum
        program.setParameter(GL2GL3.GL_GEOMETRY_VERTICES_OUT_ARB, 1000)

        val material = new ShaderMaterial("AMBIENT", program)
        material.setUniform("AMBIENT", "z", new UniformFloat(z))
        material.setUniform("AMBIENT", "max", new UniformFloat(max))
        material.setUniform("AMBIENT", "size", new UniformFloat(size))
        material.setUniform("AMBIENT", "color", new UniformVector4(new Vector4(1, 1, 1, alpha)))

        shape.setGeometry(new AttributeCloud(1))
        shape.setMaterial(material)

        sender ! Add(parent, shape)
	}
	
	private def walls = grid(75, 50)
	private def background = grid(500, 50, -50, 0.2f)
	
}