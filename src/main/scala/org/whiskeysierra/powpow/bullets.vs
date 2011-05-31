uniform mat4 jvr_ModelViewProjectionMatrix;

attribute vec3 position;
attribute vec3 direction;
attribute float energy;

varying vec3 bulletDirection;
varying float bulletEnergy;

void main(void) {
    bulletDirection = direction;
    bulletEnergy = energy;
    gl_Position = jvr_ModelViewProjectionMatrix * vec4(position, 1.0);
}
