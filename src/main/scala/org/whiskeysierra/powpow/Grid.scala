package org.whiskeysierra.powpow

import de.bht.jvr.core.{AttributeCloud, GroupNode, Shader, ShaderMaterial, ShaderProgram, ShapeNode}
import de.bht.jvr.core.attributes.AttributeVector3
import de.bht.jvr.math.Vector3
import javax.media.opengl.{GL, GL2, GL3, GL2ES2, GL2GL3}
import scala.actors.Actor
import scala.collection.JavaConversions._

class Grid(private val parent:GroupNode) extends Actor with ResourceLoader {

	override def act = {
		loop {
			react {
				case Start =>
                    val shape:ShapeNode = new ShapeNode("Walls")
                    
                    val vs = new Shader(load("grid.vs"), GL2ES2.GL_VERTEX_SHADER)
                    val gs = new Shader(load("grid.gs"), GL3.GL_GEOMETRY_SHADER)
                    val fs = new Shader(load("grid.fs"), GL2ES2.GL_FRAGMENT_SHADER)
        
                    val program = new ShaderProgram(vs, fs, gs)            
            
                    program.setParameter(GL2GL3.GL_GEOMETRY_INPUT_TYPE_ARB, GL.GL_POINTS)
                    program.setParameter(GL2GL3.GL_GEOMETRY_OUTPUT_TYPE_ARB, GL.GL_LINE_STRIP)
                    program.setParameter(GL2GL3.GL_GEOMETRY_VERTICES_OUT_ARB, 8)
            
                    val material = new ShaderMaterial("AMBIENT", program)
            
                    shape.setGeometry(new AttributeCloud(1))
                    shape.setMaterial(material)

                    sender ! Add(parent, shape)
				case PoisonPill => exit
			}
		}
	}
	
}