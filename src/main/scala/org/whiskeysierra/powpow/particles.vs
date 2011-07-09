uniform mat4 jvr_ModelViewProjectionMatrix;

attribute vec3 position;
attribute vec3 direction;
attribute float energy;
attribute vec4 color;

varying vec3 d;
varying float e;
varying vec4 c;

void main(void) {
    d = direction;
    e = energy;
    c = color;
    gl_Position = jvr_ModelViewProjectionMatrix * vec4(position, 1.0);
}
