const vec3 zAxis = vec3(0.0, 0.0, 1.0);

varying in vec3 bulletDirection[];

void lineVertex(vec4 p) {
    gl_Position = p;
    EmitVertex();
}

void main(void) {
    vec4 bottom = gl_PositionIn[0];
    vec4 direction = vec4(bulletDirection[0], 0.0);
    vec4 top = normalize(direction) * 0.5 + bottom;
    vec4 orthogonal = vec4(normalize(cross(zAxis, bulletDirection[0])) * 0.25, 0.0);
    vec4 left = bottom + orthogonal;
    vec4 right = bottom + -orthogonal;
    
    lineVertex(left);
    lineVertex(top);
    lineVertex(right);
    
    EndPrimitive();
}