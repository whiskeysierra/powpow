uniform mat4 jvr_ModelViewProjectionMatrix;

attribute vec3 position;
attribute vec3 direction;
attribute float health;

varying vec3 d;
varying float h;

void main(void) {
    d = direction;
    h = health;
    gl_Position = jvr_ModelViewProjectionMatrix * vec4(position, 1.0);
}
