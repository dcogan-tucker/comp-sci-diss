#version 460 core

in vec3 position;
in vec2 textureCoords;

out vec2 passTextureCoords;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
	// Transform the 3D vertex positions so they can be shown on the 2D screen.
	gl_Position = projection * view * model * vec4(position, 1.0);
	passTextureCoords = textureCoords;
}
