
uniform mat4 jvr_ModelViewProjectionMatrix;

attribute vec4 jvr_Vertex;

void main(void) {
    gl_Position = jvr_ModelViewProjectionMatrix * jvr_Vertex;
}
