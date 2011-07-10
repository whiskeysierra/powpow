const vec3 zAxis = vec3(0.0, 0.0, 1.0);

varying in vec3 d[];
varying in float e[];

const vec4 y = vec4(0.0, 1.0, 0.0, 0.0);

void lineVertex(vec4 p) {
    gl_Position = p;
    EmitVertex();
}

void main(void) {
    if (e[0] > 0.0) {
	    vec4 bottom = gl_PositionIn[0];

	    lineVertex(bottom + vec4( 1.0, 0.0, 0.0, 0.0));
	    lineVertex(bottom + vec4(-1.0, 0.0, 0.0, 0.0));
	    EndPrimitive();

	    lineVertex(bottom + vec4(0.0,  1.0, 0.0, 0.0));
	    lineVertex(bottom + vec4(0.0, -1.0, 0.0, 0.0));
	    EndPrimitive();

	    lineVertex(bottom + vec4( 0.7, 0.7, 0.0, 0.0));
	    lineVertex(bottom + vec4(-0.7, -0.7, 0.0, 0.0));
	    EndPrimitive();

	    lineVertex(bottom + vec4(-0.7,  0.7, 0.0, 0.0));
	    lineVertex(bottom + vec4( 0.7, -0.7, 0.0, 0.0));
	    EndPrimitive();
    }
}