package com.packt.gamejamone;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class StatusBar {

    private final static float FRAME_BAR_WIDTH = 200;
    private float BAR_OFFSET;

    private int health;

    private String text = "NULL";
    private float xText;
    private float yText;

    private Rectangle statusBarFrame;
    private Rectangle movingBarFrame;

    private Texture statusBarFrameTexture;
    private Texture movingBarFrameTexture;

    StatusBar(float height, float offset, boolean full, Texture statusBar, Texture movingBar){
        this.BAR_OFFSET = offset;
        float width = 0;
        if(full){width = FRAME_BAR_WIDTH - 2 * BAR_OFFSET;}
        statusBarFrame = new Rectangle(0,0, FRAME_BAR_WIDTH, height);
        movingBarFrame = new Rectangle(0,0, width,
                height - 2 * BAR_OFFSET);

        statusBarFrameTexture = statusBar;
        movingBarFrameTexture = movingBar;
    }

    void setHealth(int health){
        this.health = health;
    }

    void setPosition (float x, float y){
        statusBarFrame.x = x;
        statusBarFrame.y = y;
        movingBarFrame.x = x + BAR_OFFSET;
        movingBarFrame.y = y + BAR_OFFSET;
    }

    void setHealthText(int currentHealth, int fullHealth){
        text = currentHealth + "/" + fullHealth;
    }

    void setStaminaText(int stamina){
        text = stamina + "%";
    }

    void setTextPosition(float x, float y){
        xText = x;
        yText = y;
    }

    void setPositionPlayerStamina(){
        movingBarFrame.x = statusBarFrame.x + FRAME_BAR_WIDTH - BAR_OFFSET;
    }

    void setPlayerHealth(float health) {
        health = conversion(health);
        movingBarFrame.width = health;
        movingBarFrame.x = FRAME_BAR_WIDTH + BAR_OFFSET - health;
    }

    void setOpponentHealth(float health){
        health = conversion(health);
        movingBarFrame.width = health;
    }

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

    void setStaminaOpponent(float STA){
        STA = conversion(STA);
        if(STA < FRAME_BAR_WIDTH - 2 * BAR_OFFSET){movingBarFrame.width = STA;}
        else{movingBarFrame.width = FRAME_BAR_WIDTH - 2 * BAR_OFFSET;}
    }

    private float conversion(float input){
        return  (input * (FRAME_BAR_WIDTH - 2 * BAR_OFFSET))/health;
    }

    void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(statusBarFrame.x, statusBarFrame.y, statusBarFrame.width, statusBarFrame.height);
        shapeRenderer.rect(movingBarFrame.x, movingBarFrame.y, movingBarFrame.width, movingBarFrame.height);
    }

    void drawText(GlyphLayout glyphLayout, BitmapFont bitmapFont, SpriteBatch batch){
        glyphLayout.setText(bitmapFont, text);
        bitmapFont.draw(batch, text, xText, yText);
    }

    void draw(SpriteBatch batch){
        batch.draw(statusBarFrameTexture, statusBarFrame.x, statusBarFrame.y, statusBarFrame.width, statusBarFrame.height);
        batch.draw(movingBarFrameTexture, movingBarFrame.x, movingBarFrame.y, movingBarFrame.width, movingBarFrame.height);
    }

}
