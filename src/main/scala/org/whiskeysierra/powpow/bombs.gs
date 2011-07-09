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

	    /*
	    vec4 direction = vec4(d[0], 0.0);
	    vec4 d = normalize(direction);
	    vec4 top = d * 1.0 + bottom;

	    lineVertex(top);
	    lineVertex(bottom);
	    EndPrimitive();

	    vec4 center = d * 0.5 + bottom;
	    vec4 left = vec4(cross(center, y), 1.0) + bottom;
        vec4 right = -left + bottom;

        lineVertex(left);
        lineVertex(right);
        EndPrimitive();
        */
    }
}