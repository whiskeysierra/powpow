uniform mat4 jvr_ModelViewProjectionMatrix;

attribute vec3 position;
attribute vec3 direction;

varying vec3 bulletDirection;

void main(void) {
    bulletDirection = direction;
    gl_Position = jvr_ModelViewProjectionMatrix * vec4(position, 1.0);
}
