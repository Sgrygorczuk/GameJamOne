package com.packt.gamejamone;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

class Attack {

    //Attributes
    private String name;
    private int strength;
    private int accuracy;
    private int staminaCost;

    //Box
    private Rectangle hitBox;

    //Texture
    private Texture attackTexture;

    /*
    Input: Texture
    Output: Void
    Purpose: Creates the box and adds the texture
    */
    Attack(Texture attackText){
        hitBox = new Rectangle(0,0,0,0);

        attackTexture = attackText;
    }

    /*
    Input: Dimensions
    Output: Void
    Purpose: Sets size of the box
    */
    void setSize(float width, float height){
        hitBox.width = width;
        hitBox.height = height;
    }

    /*
    Input: Position
    Output: Void
    Purpose: Sets where the box will spawn
    */
    void setPosition(float x, float y){
        hitBox.x = x;
        hitBox.y = y;
    }

    /*
    Input: Flag that tells us if the attack is stationary or moving
    Output: Void
    Purpose: If attack is moving move it to the right
    */
    void updatePosition(boolean mobile){ if(mobile){ hitBox.x += 10; }}

    /*
    Input: Name, STR, ACC, STACost
    Output: Void
    Purpose: Sets up the attributes that will be used in fighting
    */
    void setAttributes(String newName, int newStrength, int newAccuracy, int newStaminaCost){
        name = newName;
        strength = newStrength;
        accuracy = newAccuracy;
        staminaCost = newStaminaCost;
    }

    /*
    Input: Void
    Output: int
    Purpose: Returns the strength of the attack
    */
    int getStrength(){return strength;}

    /*
    Input: Void
    Output: int
    Purpose: Returns the accuracy of the attack
    */
    int getAccuracy(){return accuracy;}

    /*
    Input: Void
    Output: int
    Purpose: Returns the stamina cost, how much user need to have before they can use it
    */
    int getStaminaCost(){return staminaCost;}

    /*
    Input: Void
    Output: Texture
    Purpose: Returns the texture of the attack
    */
    Texture getTexture(){return attackTexture;}

    /*
    Input: SpriteBatch
    Output: Void
    Purpose: Draws the texture
    */
    void draw(SpriteBatch batch){
        batch.draw(attackTexture, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }

}
