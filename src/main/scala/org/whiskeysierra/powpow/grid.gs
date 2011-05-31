
uniform float maximum;
uniform float size;

void emit(float x, float y) {
    gl_Position = gl_PositionIn[0] + vec4(x, y, 0, 0);
    EmitVertex();
}

void main(void) {
    float x = -maximum + mod(maximum, size) + size / 2.0;
    
    while (x < maximum) {
        emit(x, maximum);
        emit(x, -maximum);
        EndPrimitive();
        x += size;
    }
    
    float y = -maximum + mod(maximum, size) + size / 2.0;
    
    while (y < maximum) {
        emit(maximum, y);
        emit(-maximum, y);
        EndPrimitive();
        y += size;
    }
}
