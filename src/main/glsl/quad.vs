uniform mat4 jvr_ModelViewProjectionMatrix;

attribute vec4 jvr_Vertex;
attribute vec2 jvr_TexCoord;

varying vec2 texCoord;

void main(void) {
    texCoord = jvr_TexCoord;
    gl_Position = jvr_ModelViewProjectionMatrix * jvr_Vertex;
}