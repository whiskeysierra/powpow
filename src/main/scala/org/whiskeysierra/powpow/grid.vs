uniform mat4 jvr_ModelViewProjectionMatrix;

void main(void) {
    gl_Position = jvr_ModelViewProjectionMatrix * vec4(0.0, 0.0, 0.0, 1.0);
}
