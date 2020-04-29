package com.packt.gamejamone;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

class AttackBox {
    //Boxes
    private Rectangle attack_one;
    private Rectangle attack_two;
    private Rectangle attack_three;
    private Rectangle frame;
    private Rectangle highlight;

    //Textures
    private Texture attackOneTexture;
    private Texture attackTwoTexture;
    private Texture attackThreeTexture;
    private Texture frameTexture;
    private Texture highlightTexture;

    //Text Strings and Position
    private String textDMG = "NULL";
    private String textCost = "Null";
    private float xTextDMG;
    private float yTextDMG;
    private float xTextCost;
    private float yTextCost;

    //Dimensions
    private final static float FRAME_WIDTH = 200;
    private final static float FRAME_HEIGHT = 60;
    private final static float OFFSET = 10;

    /*
    Input: Textures
    Output: Void
    Purpose: Creates the boxes and adds their textures
    */
    AttackBox(Texture frameT, Texture attackOne, Texture attackTwo, Texture attackThree, Texture highT){
        //Create boxes
        frame = new Rectangle(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        attack_one = new Rectangle(0, 0, (FRAME_WIDTH - 4 * OFFSET)/3, FRAME_HEIGHT - 2 * OFFSET);
        attack_two = new Rectangle(0, 0, (FRAME_WIDTH - 4 * OFFSET)/3, FRAME_HEIGHT - 2 * OFFSET);
        attack_three = new Rectangle(0, 0, (FRAME_WIDTH - 4 * OFFSET)/3, FRAME_HEIGHT - 2 * OFFSET);
        highlight = new Rectangle(0, 0, (FRAME_WIDTH - OFFSET)/3, FRAME_HEIGHT -  OFFSET);

        //Add textures
        frameTexture = frameT;
        attackOneTexture = attackOne;
        attackTwoTexture = attackTwo;
        attackThreeTexture = attackThree;
        highlightTexture = highT;
    }

    /*
    Input: Frame position
    Output: Void
    Purpose: Set the frame of the boxes and then place all the others accordingly
    */
    void setPosition(float x, float y){
        frame.x = x;
        frame.y = y;
        attack_one. x = frame.x + OFFSET;
        attack_one.y = y + OFFSET;
        attack_two.x = attack_one.x + attack_one.width  + OFFSET;
        attack_two.y = y + OFFSET;
        attack_three.x = attack_two.x + attack_two.width + OFFSET;
        attack_three.y = y + OFFSET;
    }

    /*
    Input: Text positions
    Output: Void
    Purpose: Set text position of DMG and COST
    */
    void setTextPosition(float xDMG, float yDMG, float xCost, float yCost){
        xTextDMG = xDMG;
        yTextDMG = yDMG;
        xTextCost = xCost;
        yTextCost = yCost;
    }

    /*
    Input: Text Int
    Output: Void
    Purpose: Set text of DMG and COST
    */
    void setText(int DMG, int cost){
        textDMG = "" + DMG;
        textCost = "" + cost;
    }

    /*
    Input: Character position
    Output: Void
    Purpose: Finds in which region the characters is then sets highlight based on that region
    */
    int updateHighlight(float position){
        position = conversion(position);
        if((position > 5 && position <= 25) || (position > 80 && position <= 100)){
            setHighlight(0);
            return  0;
        }
        else if((position > 25 && position <= 45) || (position > 100 && position <= 119)){
            setHighlight(1);
            return  1;
        }
        else if((position > 45 && position <= 65) || (position > 119 && position <= 140)){
            setHighlight(2);
            return  2;}
        else return 3;
    }

    /*
    Input: Int Choice
    Output: Void
    Purpose: Sets the highlight box to be draw behind box 1,2 or 3 based on where the player is standing
    */
    private void setHighlight(int selection){
        if(selection == 0){
            highlight. x = frame.x + OFFSET/2;
            highlight.y = frame.y + OFFSET/2;
        }
        else if(selection == 1){
            highlight. x = attack_one.x + attack_one.width + OFFSET/2;
            highlight.y = frame.y + OFFSET/2;
        }
        else if(selection == 2){
            highlight. x = attack_two.x + attack_two.width + OFFSET/2;
            highlight.y = frame.y + OFFSET/2;
        }
    }

    /*
    Input: Status input
    Output: Void
    Purpose: Converts from user distance to attack box distance in choosing
    */
    private float conversion(float input){ return input * 200/340; }

    /*
    Input: ShapeRenderer
    Output: Void
    Purpose: Draws wire frame
    */
    void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(frame.x, frame.y, frame.width, frame.height);
        shapeRenderer.rect(attack_one.x, attack_one.y, attack_one.width, attack_one.height);
        shapeRenderer.rect(attack_two.x, attack_two.y, attack_two.width, attack_two.height);
        shapeRenderer.rect(attack_three.x, attack_three.y, attack_three.width, attack_three.height);
        shapeRenderer.rect(highlight.x, highlight.y, highlight.width, highlight.height);
    }

    /*
    Input:
    Output: Void
    Purpose: Draws text
    */
    void drawText(GlyphLayout glyphLayout, BitmapFont bitmapFont, SpriteBatch batch){
        glyphLayout.setText(bitmapFont, textDMG);
        bitmapFont.draw(batch, textDMG, xTextDMG, yTextDMG);
        glyphLayout.setText(bitmapFont, textCost);
        bitmapFont.draw(batch, textCost, xTextCost, yTextCost);
    }

    /*
    Input: SpriteBatch
    Output: Void
    Purpose: Draws textures
    */
    void draw(SpriteBatch batch){
        batch.draw(frameTexture, frame.x, frame.y, frame.width, frame.height);
        batch.draw(highlightTexture, highlight.x, highlight.y, highlight.width, highlight.height);
        batch.draw(attackOneTexture, attack_one.x, attack_one.y, attack_one.width, attack_one.height);
        batch.draw(attackTwoTexture, attack_two.x, attack_two.y, attack_two.width, attack_two.height);
        batch.draw(attackThreeTexture, attack_three.x, attack_three.y, attack_three.width, attack_three.height);
    }

}
