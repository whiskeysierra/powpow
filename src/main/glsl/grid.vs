uniform mat4 jvr_ModelViewProjectionMatrix;

attribute vec3 center;

void main(void) {
    gl_Position = jvr_ModelViewProjectionMatrix * vec4(center, 1.0);
}
