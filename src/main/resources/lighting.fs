
uniform vec4 jvr_LightSource_Diffuse;
uniform vec4 jvr_LightSource_Specular;
uniform float jvr_Material_Shininess;

varying vec3 light;
varying vec3 eye;

varying vec2 texture;

void main(void) {
    // normal is a constant in tangent space 
    vec3 n = vec3(0.0, 0.0, 1.0);

    // "transforms" the texture coord to the "grid coordinate system"    
    vec2 point = fract(texture / 0.2);
    vec2 center = vec2(0.5, 0.5);
    
    if (distance(center, point) < 0.5) {
        n = normalize(n * 0.5 - vec3(point - center, 0.0));
    }
    
    vec3 l = normalize(light);
    vec3 e = normalize(eye);
    
    /* diffuse intensity */
    float intensity = dot(l, n);

    vec3 color = intensity * jvr_LightSource_Diffuse.rgb;
    
    /* specular highlight */
    if (intensity > 0.0) {
        vec3 r = reflect(-l, n);
        float specular = max(dot(r, e), 0.0);
        // less than 0 means out of sight
        if (specular > 0.0) {
            // specular part
            // TODO use jvr_Material_Shininess instead of 50.0
            color += jvr_LightSource_Specular.rgb * intensity * pow(specular , 50.0);
        }
    }
    
    gl_FragColor = vec4(color, 1);
}
