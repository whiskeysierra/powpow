uniform mat4 jvr_ModelViewProjectionMatrix;

attribute vec3 position;
attribute vec3 direction;
attribute float energy;

varying vec3 d;
varying float e;

void main(void) {
    d = direction;
    e = energy;
    gl_Position = jvr_ModelViewProjectionMatrix * vec4(position, 1.0);
}
