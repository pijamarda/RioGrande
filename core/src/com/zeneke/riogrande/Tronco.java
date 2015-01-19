package com.zeneke.riogrande;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by fgimenez on 17/01/2015.
 */
public class Tronco {
    Texture texture;
    int x;
    int y;
    int xOrigin;
    int yOrigin;
    int width;
    int height;

    Tronco()
    {
        this.xOrigin = 700;
        this.yOrigin = -256;
    }

    Tronco(int xOrigin, int yOrigin)
    {
        this.xOrigin = xOrigin;
        this.yOrigin = -yOrigin;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        if (xOrigin > 600)
            x = xOrigin - texture.getWidth() + 10;
        else
            x = xOrigin;
        y = yOrigin;
        width = texture.getWidth();
        height = texture.getHeight();
    }

    public Texture getTexture()
    {
        return texture;
    }

    public int getX(){return x;}
    public int getY(){return y;}
}