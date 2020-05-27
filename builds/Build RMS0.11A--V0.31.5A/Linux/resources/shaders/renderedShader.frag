#version 150

in vec2 textureCoords;
out vec4 out_Color;

uniform sampler2D te;

void main(void){
    out_Color = texture(te, textureCoords);
}