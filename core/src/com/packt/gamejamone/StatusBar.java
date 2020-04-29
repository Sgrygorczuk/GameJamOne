package com.packt.gamejamone;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

class StatusBar {

    //Size of the frame and offset that will be used to make the inner bar
    private final static float FRAME_BAR_WIDTH = 200;
    private float BAR_OFFSET;

    //Inner Bar Value converts from whatever the character stats are to pixels that display it
    //Such that 300 HP doesn't equal 300 pixel but is fit within the 200 width of the frame
    private int innerBarValue;

    //Text string and position
    private String text = "NULL";
    private float xText;
    private float yText;

    //Boxes that keep track of the frame and inner moving bar
    private Rectangle statusBarFrame;
    private Rectangle movingBarFrame;

    //Textures for the boxes
    private Texture statusBarFrameTexture;
    private Texture movingBarFrameTexture;

    /*
    Input: height, offset used for creating boxes, full for telling if inner box is filled in or not
        and textures for the boxes
    Output: Void
    Purpose: Updates the dimensions of the screen
    */
    StatusBar(float height, float offset, boolean full, Texture statusBar, Texture movingBar){
        //Get offset
        this.BAR_OFFSET = offset;
        //Based on full set width of inner bar to 0 or the new calduaced width
        float width = 0;
        if(full){width = FRAME_BAR_WIDTH - 2 * BAR_OFFSET;}
        //Create the boxes
        statusBarFrame = new Rectangle(0,0, FRAME_BAR_WIDTH, height);
        movingBarFrame = new Rectangle(0,0, width,
                height - 2 * BAR_OFFSET);
        //Connect the textures to the boxes
        statusBarFrameTexture = statusBar;
        movingBarFrameTexture = movingBar;
    }

    /*
    Input: inner Bar Value
    Output: Void
    Purpose:Inner Bar Value converts from whatever the character stats are to pixels that display it
        Such that 300 HP doesn't equal 300 pixel but is fit within the 200 width of the frame
    */
    void setHealth(int innerBarValue){
        this.innerBarValue = innerBarValue;
    }

    /*
    Input: Position of the frame
    Output: Void
    Purpose: Set the frame and moving bar on screen
    */
    void setPosition (float x, float y){
        statusBarFrame.x = x;
        statusBarFrame.y = y;
        movingBarFrame.x = x + BAR_OFFSET;
        movingBarFrame.y = y + BAR_OFFSET;
    }

    /*
    Input: Current player health and full player health
    Output: Void
    Purpose: Sets up the text that will be drawn on screen
    */
    void setHealthText(int currentHealth, int fullHealth){
        text = currentHealth + "/" + fullHealth;
    }

    /*
    Input: Current player stamina
    Output: Void
    Purpose: Sets up the text that will be drawn on screen
    */
    void setStaminaText(int stamina){
        text = stamina + "%";
    }

    /*
    Input: Current player stamina
    Output: Void
    Purpose: Sets up the text that will be drawn on screen
    */
    void setTextPosition(float x, float y){
        xText = x;
        yText = y;
    }

    /*
    Input: Current player health
    Output: Void
    Purpose: Sets up the health to shrink and move in the right direction
        so that it looks like its cutting part of health on the left end
    */
    void setPlayerHealth(float health) {
        health = conversion(health);
        movingBarFrame.width = health;
        movingBarFrame.x = FRAME_BAR_WIDTH + BAR_OFFSET - health;
    }

    /*
    Input: Current opponent health
    Output: Void
    Purpose: Sets up the health to shrink cutting of a chunk from the right side
    */
    void setOpponentHealth(float health){
        health = conversion(health);
        movingBarFrame.width = health;
    }

    /*
    Input: Current player stamina
    Output: Void
    Purpose: Sets up the player stamina, grows from right to left
    */
    void setStaminaPlayer(float STA){
        STA = conversion(STA);
        if(STA < FRAME_BAR_WIDTH - 2 * BAR_OFFSET){
            movingBarFrame.width = STA;
            movingBarFrame.x = statusBarFrame.x + FRAME_BAR_WIDTH - BAR_OFFSET - STA;
        }
        else{
            movingBarFrame.width = FRAME_BAR_WIDTH - 2 * BAR_OFFSET;
            movingBarFrame.x = statusBarFrame.x + BAR_OFFSET;
        }
    }

    /*
    Input: Current opponent stamina
    Output: Void
    Purpose: Sets up the opponent stamina, grows from left ot right
    */
    void setStaminaOpponent(float STA){
        STA = conversion(STA);
        movingBarFrame.width = Math.min(STA, FRAME_BAR_WIDTH - 2 * BAR_OFFSET);
    }

    /*
    Input: Current status of inner bar
    Output: Void
    Purpose: Translate the user stats to width of the inner bar so it can shrink or grow correctly
    */
    private float conversion(float input){ return  (input * (FRAME_BAR_WIDTH - 2 * BAR_OFFSET))/innerBarValue; }

    /*
    Input: ShapeRenderer
    Output: Void
    Purpose: Draws wireframe
    */
    void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(statusBarFrame.x, statusBarFrame.y, statusBarFrame.width, statusBarFrame.height);
        shapeRenderer.rect(movingBarFrame.x, movingBarFrame.y, movingBarFrame.width, movingBarFrame.height);
    }

    /*
    Input: gridlayout, bitmap and SpriteBatch
    Output: Void
    Purpose: Draws text
    */
    void drawText(GlyphLayout glyphLayout, BitmapFont bitmapFont, SpriteBatch batch){
        glyphLayout.setText(bitmapFont, text);
        bitmapFont.draw(batch, text, xText, yText);
    }

    /*
    Input: SpriteBatch
    Output: Void
    Purpose: Draws textures
    */
    void draw(SpriteBatch batch){
        batch.draw(statusBarFrameTexture, statusBarFrame.x, statusBarFrame.y, statusBarFrame.width, statusBarFrame.height);
        batch.draw(movingBarFrameTexture, movingBarFrame.x, movingBarFrame.y, movingBarFrame.width, movingBarFrame.height);
    }

}
