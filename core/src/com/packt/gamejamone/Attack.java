package com.packt.gamejamone;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Attack {

    private String name;
    private int strength;
    private int accuracy;
    private int staminaCost;

    private Rectangle hitBox;

    private Texture attackTexture;

    Attack(Texture attackText){
        hitBox = new Rectangle(0,0,0,0);

        attackTexture = attackText;
    }

    void setSize(float width, float height){
        hitBox.width = width;
        hitBox.height = height;
    }

    void setPosition(float x, float y){
        hitBox.x = x;
        hitBox.y = y;
    }

    void updatePosition(boolean mobile){
        if(mobile){
            hitBox.x += 10;
        }
    }

    void setAttributes(String newName, int newStrength, int newAccuracy, int newStaminaCost){
        name = newName;
        strength = newStrength;
        accuracy = newAccuracy;
        staminaCost = newStaminaCost;
    }

    int getStrength(){return strength;}

    int getAccuracy(){return accuracy;}

    int getStaminaCost(){return staminaCost;}

    void drawDebug(ShapeRenderer shapeRenderer){
        shapeRenderer.rect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }

    void draw(SpriteBatch batch){
        batch.draw(attackTexture, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }

}
