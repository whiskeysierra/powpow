package org.whiskeysierra.powpow.prototype;

import de.bht.jvr.collada14.loader.ColladaLoader;
import de.bht.jvr.core.*;
import de.bht.jvr.core.pipeline.Pipeline;
import de.bht.jvr.core.uniforms.UniformVector4;
import de.bht.jvr.math.Vector4;
import de.bht.jvr.renderer.AwtRenderWindow;
import de.bht.jvr.renderer.RenderWindow;
import de.bht.jvr.renderer.Viewer;
import de.bht.jvr.util.Color;
import de.bht.jvr.util.InputState;
import de.bht.jvr.util.StopWatch;

import java.io.File;

public class BlurExample {


    public static void main(String[] args) throws Exception {
        GroupNode root = new GroupNode("scene root");

        SceneNode teapot = ColladaLoader.load(new File("src/main/resources/models/teapot.dae"));
        teapot.setTransform(Transform.translate(0f, -0.6f, 0f));
        GroupNode teapotRotor = new GroupNode();
        teapotRotor.addChildNode(teapot);

        ShaderMaterial material = new ShaderMaterial("GlowPass", new ShaderProgram(
                new File("src/main/scala/org/whiskeysierra/powpow/quad.vs"),
                new File("src/main/scala/org/whiskeysierra/powpow/blur.fs")));

        ShaderProgram ambient = new ShaderProgram(
                new File("src/main/scala/org/whiskeysierra/powpow/minimal.vs"),
                new File("src/main/scala/org/whiskeysierra/powpow/color.fs"));

        ShapeNode shape = Finder.find(teapot, ShapeNode.class, "Teapot01_Shape");

        ShaderMaterial teapotMat = new ShaderMaterial();
        teapotMat.setShaderProgram("AMBIENT", ambient);

        shape.setMaterial(teapotMat);

        CameraNode camera = new CameraNode("camera", 4f / 3f, 60);
        camera.setTransform(Transform.translate(0, 0, 3));

        root.addChildNodes(teapotRotor, camera);
        Printer.print(root);

        Pipeline pipeline = new Pipeline(root);

        pipeline.setUniform("color", new UniformVector4(new Vector4(0.0f, 1.0f, 0.0f, 1.0f)));

        /* Alles in GlowMap rendern*/
        pipeline.createFrameBufferObject("GlowMap", true, 1, 1.0f, 0);
        pipeline.switchFrameBufferObject("GlowMap");

        pipeline.clearBuffers(true, true, new Color(0, 0, 0));
        pipeline.switchCamera(camera);
        pipeline.drawGeometry("AMBIENT", null);

        pipeline.switchFrameBufferObject(null);
        pipeline.clearBuffers(true, true, new Color(0, 0, 0));

        /* Alles auf Screen*/
        pipeline.bindColorBuffer("jvr_Texture0", "GlowMap", 0); // bind color buffer from fbo to uniform
        // render quad with dof shader
        pipeline.drawQuad(material, "GlowPass");

        InputState input = new InputState();
        RenderWindow win = new AwtRenderWindow(pipeline, 800, 600);
        win.addKeyListener(input);

        StopWatch time = new StopWatch();
        Viewer v = new Viewer(win);

        float angleY = 0;
        float angleX = 0;
        float speed = 90;

        while (v.isRunning()) {
            float elapsed = time.elapsed();

            if (input.isOneDown('W', java.awt.event.KeyEvent.VK_UP))
                angleX += elapsed * speed;
            if (input.isOneDown('S', java.awt.event.KeyEvent.VK_DOWN))
                angleX -= elapsed * speed;
            if (input.isOneDown('D', java.awt.event.KeyEvent.VK_RIGHT))
                angleY += elapsed * speed;
            if (input.isOneDown('A', java.awt.event.KeyEvent.VK_LEFT))
                angleY -= elapsed * speed;

            teapotRotor.setTransform(Transform.rotateYDeg(angleY).mul(Transform.rotateXDeg(angleX)));

            if (input.isDown('Q'))
                System.exit(0);

            v.display();
        }

    }
}