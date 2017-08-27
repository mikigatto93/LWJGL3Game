#version 330

in vec3 position;
in vec2 textCoords;
in vec3 normals;

uniform mat4 projection;
uniform mat4 modelView;

out vec2 outTextCoords;

void main() {
	
	gl_Position = projection * modelView * vec4(position, 1.0);
	outTextCoords = textCoords;

}