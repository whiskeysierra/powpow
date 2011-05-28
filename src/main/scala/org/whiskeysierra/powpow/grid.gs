
uniform float max;
uniform float size;

void emit(float x, float y) {
    gl_Position = gl_PositionIn[0] + vec4(vec2(x, y), 0, 0);
    EmitVertex();
}

void main(void) {
    float x = size / 2.0;
    while (x < max) {
        emit(x, max);
        emit(x, -max);
        EndPrimitive();
        x += size;
    }
    
    x = -size / 2.0;
    while (x > -max) {
        emit(x, max);
        emit(x, -max);
        EndPrimitive();
        x -= size;
    }
    
    float y = size / 2.0;
    while (y < max) {
        emit(max, y);
        emit(-max, y);
        EndPrimitive();
        y += size;
    }

    y = -size / 2.0;
    while (y > -max) {
        emit(max, y);
        emit(-max, y);
        EndPrimitive();
        y -= size;
    }
}
