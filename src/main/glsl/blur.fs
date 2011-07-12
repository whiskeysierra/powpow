uniform sampler2D jvr_Texture0;
uniform sampler2D jvr_Texture1;
uniform mat4 jvr_InverseProjectionViewMatrix;
uniform mat4 jvr_PreviousProjectionViewMatrix;

const int samples = 5;
const float bluriness = 0.01;

varying vec2 texCoord;

void main (void) {
    float zOverW = texture2D(jvr_Texture1, texCoord).x;
    vec4 vertexNDC = vec4(texCoord.x * 2.0 - 1.0, texCoord.y * 2.0 - 1.0, zOverW, 1.0);

    vec4 vertexW = jvr_InverseProjectionViewMatrix * vertexNDC;
    vertexW /= vertexW.w;

    vec4 previousVertexNDC = jvr_PreviousProjectionViewMatrix * vertexW;
    previousVertexNDC /= previousVertexNDC.w;

    vec2 velocity = (vertexNDC.xy - previousVertexNDC.xy) / float(samples);

    vec2 position = texCoord;
    vec4 color = vec4(0.0);
    for (int i = 0; i < samples; i++) {
        color += texture2D(jvr_Texture0, position);
        position += velocity * bluriness;
    }

	gl_FragColor = color / float(samples);
}
