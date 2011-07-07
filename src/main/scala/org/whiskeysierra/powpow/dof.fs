uniform sampler2D jvr_Texture0;
uniform sampler2D jvr_Texture1;
uniform float intensity;
varying vec2 texCoord;

float linearizeDepth()
{
  float n = 0.1;
  float f = 100.0;
  float z = texture2D(jvr_Texture0, texCoord).x;
  return (2.0 * n) / (f + n - z * (f - n));
}

vec4 blur(float z)
{   	
   	int iteration = 4;
   	
   	vec4 final_color = vec4(0,0,0,1);
   	
   	for(int x=-iteration/2; x<iteration/2+1; x++)
   	{
   		for(int y=-iteration/2; y<iteration/2+1; y++)
   		{
   			vec2 offset = intensity * z * vec2(float(x)/1024.0, float(y)/1024.0);
   			vec2 texC = texCoord+offset;
   			if(texC.x>1.0 || texC.x<0.0 || texC.y>1.0 || texC.y<0.0)
   			{
   				texC = texCoord;
   			}
   			final_color += texture2D(jvr_Texture1, texC);
   		}
   	}

	final_color /= float(iteration + 1) * float(iteration + 1); 
   	
   	final_color.w = 1.0;
	return final_color;
}

vec4 fastblur(float z)
{    	
   	// use mipmapping levels
	vec4 final_color = texture2DLod(jvr_Texture1, texCoord, z * intensity);
   	final_color.w = 1.0;
	return final_color;
}

void main (void)
{
	float z = linearizeDepth();
	//vec4 final_color = fastblur(z);
	vec4 final_color = blur(z);
	
	gl_FragColor = final_color;
}
