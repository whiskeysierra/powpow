
uniform float max;
uniform float size;

void emit(float x, float y) {
    gl_Position = gl_PositionIn[0] + vec4(x, y, 0, 0);
    EmitVertex();
}

void main(void) {
    float x = -max + mod(max, size) + size / 2;
    
    while (x < max) {
        emit(x, max);
        emit(x, -max);
        EndPrimitive();
        x += size;
    }
    
    float y = -max + mod(max, size) + size / 2;
    
    while (y < max) {
        emit(max, y);
        emit(-max, y);
        EndPrimitive();
        y += size;
    }
}
