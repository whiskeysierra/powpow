attribute vec3 position;

uniform mat4 jvr_ModelViewProjectionMatrix;

void main(void) {
    gl_Position = jvr_ModelViewProjectionMatrix * vec4(position, 1.0);
}
