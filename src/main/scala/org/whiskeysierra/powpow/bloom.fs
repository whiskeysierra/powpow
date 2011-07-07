uniform sampler2D jvr_Texture0;
uniform sampler2D jvr_Texture1;

varying vec2 texCoord;

vec4 fastblur(float intensity) {
    vec4 color = texture2DLod(jvr_Texture0, texCoord, intensity);
    color.w = 1.0;
    return color;
}

void main (void) {
    gl_FragColor = fastblur(5.5) + texture2D(jvr_Texture1, texCoord);
}
