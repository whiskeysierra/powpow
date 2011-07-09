package org.whiskeysierra.powpow

import de.bht.jvr.core.Context
import de.bht.jvr.core.pipeline.{PipelineState, PipelineCommand}
import javax.media.opengl.{GL2GL3, GL}

class DisableWireframe extends PipelineCommand {

    def execute(context: Context, state: PipelineState) {
        context.getGL.glPolygonMode(GL.GL_FRONT, GL2GL3.GL_FILL)
    }

    def update(state: PipelineState) {

    }

    def reset() {

    }

    def getRenderClone = this

}