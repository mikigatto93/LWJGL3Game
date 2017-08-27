#version 330

in vec2 outTextCoords;

uniform sampler2D textureSampler;
uniform int useColor;
uniform vec3 color;

out vec4 fragColor;


void main() {
	
	if (useColor == 1) {
		fragColor = vec4(color, 1.0);
	} else {
		fragColor = texture(textureSampler, outTextCoords);
	}
}