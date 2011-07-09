uniform sampler2D jvr_Texture0;
uniform sampler2D jvr_Texture1;

varying vec2 texCoord;

const float lod = 2.5;
const float boost = 2.0;

void main (void) {
    vec4 color = texture2D(jvr_Texture0, texCoord);
    vec4 glow = vec4(textureLod(jvr_Texture0, texCoord, lod).xyz * boost, 1.0);
    gl_FragColor = color + glow;
}
