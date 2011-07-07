uniform sampler2D jvr_Texture0;

varying vec2 texCoord;

void main (void) {
   	vec4 color = texture2D(jvr_Texture0, texCoord);
   	
   	color.x = 1.0 - color.x;
   	color.y = 1.0 - color.y;
   	color.z = 1.0 - color.z;
   	color.w = 1.0;
   	
	gl_FragColor = color;
}
