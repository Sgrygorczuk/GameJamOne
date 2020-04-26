package com.packt.gamejamone;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import javax.swing.text.TabExpander;

class Fighter {

    //Dimensions
    private final static float HIT_BOX_HEIGHT = 120;
    private final static float HIT_BOX_WIDTH = 60;

    //Movement Bounds
    private float MIN_BOUND = 0;
    private float MAX_BOUND = 480;
    private float BOUND_OFFSET = 10;

    //Fighter Stats, modify the attacks
          //Percentage that modifies attack bounds if attack has 15 base attack and fighter has
          //STR 5 then the attack has a 50% bounds MIN = 15 * 0.5, MAX = 15 * 1.5 and then it's random
          //What the attack will be
    private int strength = 0; //RANGE 0-20
          //Speed at which the fighter recovers their Stamina Max Stamina is 100 so STA is 5
          //So recovery will be 99 * 0.05
    private int staminaRegeneration = 0; //RANGE 0-100
         //Accuracy gives you the hit chance of the opponent, between 0-100, works as a modifier
         //of the base stat of an attack so if an attack has accuracy of 30% and player has ACC 80
         //(80+30)/2
    //Actual amount of stamina
    private int stamina = 0;
    private int accuracy = 0; //RANGE 0-20
        //Defense will be a modifier on the attack of the enemy, if they hit for 15 and your defense is
        //5 it'd subtract that amount and you take damage with chance of blocking
    private int defense = 0; //RANGE 0-20
        //Flat health
    private int healthFull = 0; //RANGE 50-150
    private int healthCurrent = 0; //0 - healthFull
        //The amount of spaces the user moves
    private float speed = 0; //RANGE 1-5

    //AI behavior 0 - Close Up, 1 Mid Range, 2 Far Range
    private int behavior = 3;

    //Array of moves that fighter has
    private Array<Attack> attackArray = new Array<>();

    private final Rectangle hitBox;

    private Texture hitBoxTexture;

    private boolean computerFlag; //False = Player, True = AI

    Fighter(boolean computer, Texture hitBoxT){
        computerFlag = computer;
        hitBox = new Rectangle(0,0, HIT_BOX_WIDTH, HIT_BOX_HEIGHT);

        hitBoxTexture = hitBoxT;
    }

    float getX(){return hitBox.x;}

    float getWidth(){return hitBox.width;}

    int getHealthFull(){ return healthFull;}

    int getHealthCurrent(){return healthCurrent;}

    float getStamina(){return  stamina;}

    float getAccuracy(int attackSelection){
           return attackArray.get(attackSelection).getAccuracy() + accuracy;
    }

    float getStrength(int attackSelection){
        int attackMin = attackArray.get(attackSelection).getStrength() * strength/10;
        int attackMax = attackArray.get(attackSelection).getStrength() + attackMin;
        return MathUtils.random(attackMin, attackMax);
    }

    int getCurrentAttackCost(int attackSelection){
        if(attackArray.size > 0){
            return attackArray.get(attackSelection).getStaminaCost();}
        else return 101;
    }

    void setPosition(float x, float y){
        hitBox.x = x;
        hitBox.y = y;
    }

    void setStats(int STR, int STA, int ACC, int DEF, int HP, float SPD){
        strength = STR;
        staminaRegeneration = STA;
        accuracy = ACC;
        defense = DEF;
        healthFull = HP;
        healthCurrent = HP;
        speed = SPD;
    }

    void printStats(){
        System.out.println("STR" + strength + " STAREG" + staminaRegeneration + " STA" + stamina +
                " DEF" + defense + " HP" + healthFull + " SPD" + speed);
    }

    void setAI(int BEH){ behavior = BEH; }

    void update(int action, float minBound, float maxBound) {
        updateBounds(minBound, maxBound);
        //Human Actions
        if (!computerFlag) {
            //0 - Left, 1 = Right, 2 = Perform Action
            if (action == 0) {
                updatePosition(false);
            } else if (action == 1) {
                updatePosition(true);
            }
        }
        //AI Actions
        else {
            if (behavior == 0) {
                updatePosition(false);
            } else if (behavior == 1) {
                //In this behavior the enemy is trying to stay in the mid range so in middle of the player and end of screen
                //|  Player - Enemy Zone  |  Enemy - End of Screen Zone   |
                //|        120            |          20                   |
                //Move Right
                if (hitBox.x - minBound < maxBound - hitBox.x + hitBox.width) {
                    updatePosition(true);
                } else {
                    updatePosition(false);
                }
            } else {
                updatePosition(true);
            }
        }
    }

    void updateHealth(int damage){
        int diceRoll = MathUtils.random(defense + (int) speed,100);
        //Block if you roll an over 85 you dodge
        if(diceRoll < 85) {
            //Take damage of the hit minus your defense
            if (healthCurrent - damage + defense > 0) {
                healthCurrent -= damage + defense;
            } else {
                healthCurrent = 0;
            }
        }
    }

    void updateStamina(){
        if(stamina < 101){ stamina += staminaRegeneration;}
        else {stamina = 100;}
    }

    void updateStamina(float staminaCost){
        if(stamina - staminaCost > 0){stamina -= staminaCost;}
    }

    void addAttack(Attack newAttack){ attackArray.add(newAttack);}

    private void updatePosition(boolean direction) {
        //direction false = left, true = right
        if (direction && hitBox.x + hitBox.width < MAX_BOUND) {hitBox.x += speed;}
        else if(!direction && hitBox.x > MIN_BOUND){hitBox.x -= speed; }

        if(hitBox.x < MIN_BOUND){hitBox.x += BOUND_OFFSET;}
        if(hitBox.x + hitBox.width > MAX_BOUND){hitBox.x -= BOUND_OFFSET;}
    }

    private void updateBounds(float minBound, float maxBound){
        MIN_BOUND = minBound + BOUND_OFFSET;
        MAX_BOUND = maxBound - BOUND_OFFSET;
    }

    void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }

    void draw(SpriteBatch batch){
        batch.draw(hitBoxTexture, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }

}
