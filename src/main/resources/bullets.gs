float hs = 0.5;

void quadVertex(float dx, float dy) {
    gl_Position = gl_PositionIn[0] + vec4(dx, dy, 0, 0);
    EmitVertex();    
}

void main(void) {
    quadVertex(-hs, -hs);
    quadVertex( hs, -hs);
    quadVertex(-hs,  hs);
    quadVertex( hs,  hs);
    EndPrimitive();
}