#version 330 core

in vec2 passTextureCoords;

out vec4 outColour;

uniform sampler2D tex;

void main()
{
	outColour = texture(tex, passTextureCoords);
}
