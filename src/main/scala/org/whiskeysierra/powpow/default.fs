uniform sampler2D jvr_Texture0;
varying vec2 texCoord;

void main (void)
{
	gl_FragColor = texture2D(jvr_Texture0, texCoord);
}
