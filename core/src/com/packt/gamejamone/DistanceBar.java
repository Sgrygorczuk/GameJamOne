package com.packt.gamejamone;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

class DistanceBar {

    //Dimensions
    private final static float BAR_WIDTH = 200;
    private final static float BAR_HEIGHT = 10;
    private final static float SLIDER_WIDTH = 10;
    private final static float SLIDER_HEIGHT = 14;

    //Boxes
    private Rectangle bar;
    private Rectangle slider;

    //Textures
    private Texture barTexture;
    private Texture sliderTexture;

    /*
    Input: Textures
    Output: Void
    Purpose: Creates the boxes that will be used to represent the bar and slider on top of it
    */
    DistanceBar(Texture barT, Texture sliderT) {
        //Boxes
        bar = new Rectangle(0, 0, BAR_WIDTH, BAR_HEIGHT);
        slider = new Rectangle(0, 0, SLIDER_WIDTH, SLIDER_HEIGHT);
        //Textures
        barTexture = barT;
        sliderTexture = sliderT;
    }

    /*
    Input: Position
    Output: Void
    Purpose: Places the bar somewhere and adjust slider to go with it
    */
    void setInitialPosition(float x, float y) {
        bar.x = x;
        bar.y = y;
        slider.x = x;
        slider.y = y - (SLIDER_HEIGHT - BAR_HEIGHT) / 2; //SLIDER_HEIGHT - BAR_HEIGHT offset to center slider on bar
    }

    /*
    Input: Void
    Output: Float, x value of center of slider
    Purpose: Used to pick which attack users can user
    */
    float getPositionOfSlider(){return (slider.x + slider.width)/2;}

    /*
    Input: X position of character
    Output: Void
    Purpose: Used to move around the slider from left right
    */
    void updateSliderPositionPlayer(float x) {
        slider.x = bar.x + BAR_WIDTH - conversion(x);
    }

    /*
    Input: X position of character
    Output: Void
    Purpose: Used to move around the slider from right to left
    */
    void updateSliderPositionOpponent(float x) {
        slider.x = bar.x + conversion(x) - 10;
    }

    /*
    Input: Input
    Output: Void
    Purpose: Converts the stat value to it's unit equivalentt for the screen
    */
    private float conversion(float input){ return input*BAR_WIDTH/340; }

    /*
    Input: ShapeRenderer
    Output: Void
    Purpose: Draws Wireframe
    */
    void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(bar.x, bar.y, bar.width, bar.height);
        shapeRenderer.rect(slider.x, slider.y, slider.width, slider.height);
    }

    /*
    Input: SpriteBatch
    Output: Void
    Purpose: Draws textures
    */
    void draw(SpriteBatch batch){
        batch.draw(barTexture, bar.x, bar.y, bar.width, bar.height);
        batch.draw(sliderTexture, slider.x, slider.y - 6);
    }

}
