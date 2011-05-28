
void emit(float x, float y) {
    gl_Position = gl_PositionIn[0] + vec4(vec2(x, y), 0, 0);
    EmitVertex();
}

void main(void) {
    const float s = 20.0;
    const float m = 75.0;
    emit(s, m);
    emit(s, -m);
    EndPrimitive();
    emit(m, -s);
    emit(-m, -s);
    EndPrimitive();
    emit(-s, -m);
    emit(-s, m);
    EndPrimitive();
    emit(-m, s);
    emit(m, s);
    EndPrimitive();
}
