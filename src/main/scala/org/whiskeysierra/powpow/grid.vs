uniform mat4 jvr_ModelViewProjectionMatrix;

uniform float z;

void main(void) {
    gl_Position = jvr_ModelViewProjectionMatrix * vec4(0.0, 0.0, z, 1.0);
}
