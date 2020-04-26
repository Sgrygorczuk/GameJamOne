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

    private Rectangle bar;
    private Rectangle slider;

    private Texture barTexture;
    private Texture sliderTexture;

    DistanceBar(Texture barT, Texture sliderT) {
        bar = new Rectangle(0, 0, BAR_WIDTH, BAR_HEIGHT);
        slider = new Rectangle(0, 0, SLIDER_WIDTH, SLIDER_HEIGHT);

        barTexture = barT;
        sliderTexture = sliderT;
    }

    void setInitialPosition(float x, float y) {
        bar.x = x;
        bar.y = y;
        slider.x = x;
        slider.y = y - (SLIDER_HEIGHT - BAR_HEIGHT) / 2; //SLIDER_HEIGHT - BAR_HEIGHT offset to center slider on bar
    }

    float getPositionOfSlider(){return (slider.x + slider.width)/2;}

    void updateSliderPositionPlayer(float x) {
        slider.x = bar.x + BAR_WIDTH - conversion(x);
    }

    void updateSliderPositionOpponent(float x) {
        slider.x = bar.x + conversion(x) - 10;
    }


    private float conversion(float input){ return input*BAR_WIDTH/340; }

    void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(bar.x, bar.y, bar.width, bar.height);
        shapeRenderer.rect(slider.x, slider.y, slider.width, slider.height);
    }

    void draw(SpriteBatch batch){
        batch.draw(barTexture, bar.x, bar.y, bar.width, bar.height);
        batch.draw(sliderTexture, slider.x, slider.y - 6);
    }

}
