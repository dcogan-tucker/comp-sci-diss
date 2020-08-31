#version 330 core

in vec2 passTextureCoords;

out vec4 outColour;

uniform sampler2D tex;

void main()
{
	// Sets the colour for the fragment.
	outColour = texture(tex, passTextureCoords);
}
