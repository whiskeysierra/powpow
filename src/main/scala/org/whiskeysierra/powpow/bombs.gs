const vec3 zAxis = vec3(0.0, 0.0, 1.0);

varying in vec3 d[];
varying in float e[];

void lineVertex(vec4 p) {
    gl_Position = p;
    EmitVertex();
}

void main(void) {
    if (e[0] > 0.0) {
	    vec4 bottom = gl_PositionIn[0];
	    vec4 direction = vec4(d[0], 0.0);
	    vec4 top = normalize(direction) * 1.0 + bottom;

	    lineVertex(top);
	    lineVertex(bottom);

	    EndPrimitive();
    }
}