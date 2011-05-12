attribute vec4 jvr_Vertex;
attribute vec3 jvr_Normal;
attribute vec3 jvr_Tangent;
attribute vec2 jvr_TexCoord;

uniform vec4 jvr_LightSource_Position;
uniform mat3 jvr_NormalMatrix;
uniform mat4 jvr_ModelViewMatrix;
uniform mat4 jvr_ModelViewProjectionMatrix;

varying vec3 light;
varying vec3 eye;

varying vec2 texture;

/**
 * Transforms the given vector v into tangent space defined by t, b and n.
 * 
 * @param t the tangent
 * @param b the bitangent
 * @param n the normal
 * @param v the vector being transformed
 */
vec3 tangentspace(vec3 t, vec3 b, vec3 n, vec3 v) {
    return vec3(dot(v, t), dot(v, b), dot(v, n));
}

void main(void) {
    vec3 n = normalize(jvr_NormalMatrix * jvr_Normal);
    vec3 t = normalize(jvr_NormalMatrix * jvr_Tangent);
    vec3 b = normalize(cross(t, n));
    
    vec3 vertex = vec3(jvr_ModelViewMatrix * jvr_Vertex);
    light = tangentspace(t, b, n, jvr_LightSource_Position.xyz - vertex);
    eye = tangentspace(t, b, n, -vertex);
    
    texture = jvr_TexCoord;

    gl_Position = jvr_ModelViewProjectionMatrix * jvr_Vertex;
}
