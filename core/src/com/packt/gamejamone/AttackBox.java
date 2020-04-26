package com.packt.gamejamone;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

class AttackBox {

    private Rectangle attack_one;
    private Rectangle attack_two;
    private Rectangle attack_three;
    private Rectangle frame;
    private Rectangle highlight;

    private Texture attackOneTexture;
    private Texture attackTwoTexture;
    private Texture attackThreeTexture;
    private Texture frameTexture;
    private Texture highlightTexture;

    //Dimensions
    private final static float FRAME_WIDTH = 200;
    private final static float FRAME_HEIGHT = 60;
    private final static float OFFSET = 10;

    AttackBox(Texture frameT, Texture attackOne, Texture attackTwo, Texture attackThree, Texture highT){
        frame = new Rectangle(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        attack_one = new Rectangle(0, 0, (FRAME_WIDTH - 4 * OFFSET)/3, FRAME_HEIGHT - 2 * OFFSET);
        attack_two = new Rectangle(0, 0, (FRAME_WIDTH - 4 * OFFSET)/3, FRAME_HEIGHT - 2 * OFFSET);
        attack_three = new Rectangle(0, 0, (FRAME_WIDTH - 4 * OFFSET)/3, FRAME_HEIGHT - 2 * OFFSET);
        highlight = new Rectangle(0, 0, (FRAME_WIDTH - OFFSET)/3, FRAME_HEIGHT -  OFFSET);

        frameTexture = frameT;
        attackOneTexture = attackOne;
        attackTwoTexture = attackTwo;
        attackThreeTexture = attackThree;
        highlightTexture = highT;
    }

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

    private float conversion(float input){ return input * 200/340; }

    void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(frame.x, frame.y, frame.width, frame.height);
        shapeRenderer.rect(attack_one.x, attack_one.y, attack_one.width, attack_one.height);
        shapeRenderer.rect(attack_two.x, attack_two.y, attack_two.width, attack_two.height);
        shapeRenderer.rect(attack_three.x, attack_three.y, attack_three.width, attack_three.height);
        shapeRenderer.rect(highlight.x, highlight.y, highlight.width, highlight.height);
    }

    void draw(SpriteBatch batch){
        batch.draw(frameTexture, frame.x, frame.y, frame.width, frame.height);
        batch.draw(highlightTexture, highlight.x, highlight.y, highlight.width, highlight.height);
        batch.draw(attackOneTexture, attack_one.x, attack_one.y, attack_one.width, attack_one.height);
        batch.draw(attackTwoTexture, attack_two.x, attack_two.y, attack_two.width, attack_two.height);
        batch.draw(attackThreeTexture, attack_three.x, attack_three.y, attack_three.width, attack_three.height);
    }

}
