package com.packt.gamejamone;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

class Fighter {

    //Dimensions
    private final static float HIT_BOX_HEIGHT = 120;
    private final static float HIT_BOX_WIDTH = 60;

    //Movement Bounds
    private float MIN_BOUND = 0;
    private float MAX_BOUND = 480;
    private float BOUND_OFFSET = 10;

    //Keeps track of what the last attack just used
    private int currentAttackSelection = 0;

    //Fighter Stats, modify the attacks
          //Percentage that modifies attack bounds if attack has 15 base attack and fighter has
          //STR 5 then the attack has a 50% bounds MIN = 15 * 0.5, MAX = 15 * 1.5 and then it's random
          //What the attack will be
    private int strength = 0; //RANGE 0-20
          //Speed at which the fighter recovers their Stamina Max Stamina is 100 so STA is 5
          //So recovery will be 99 * 0.05
    //Save what the current attack is like
    private int currentAttackStrength = 0;
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

    //Timers
    //Rate at which the AI takes steps
    private static float WAIT_FLAG_TIME = 0.5f;
    private float waitFlagTimer = WAIT_FLAG_TIME;

    //Rate at which the strength visual is updated
    private static float STR_TIME = .5f;
    private float strTimer = STR_TIME;

    //Rate at which one can take steps
    private static float STEP_TIME = 1f;
    private float stepTimer = STEP_TIME;

    //Array of moves that fighter has
    private Array<Attack> attackArray = new Array<>();

    //Box
    private final Rectangle hitBox;

    //Sprite and textures
    private static final int HEAD_TILE_WIDTH = 60;
    private static final int HEAD_TILE_HEIGHT = 120;
    private TextureRegion[][] hitBoxSpriteSheet;
    private Texture hitBoxTexture;

    //AI Flags
    private int stepCounter = 0;            //Counts how many steps AI took
    private boolean switchStep = false;     //Which direction the AI is walking in

    //Flags
    private boolean textureFlag = false; //Sprite = true, Texture = false
    private int modeFlag = 0;           //Tells us which sprite to display

    /*
    Input: Texture
    Output: Void
    Purpose: Set up box and texture
    */
    Fighter(Texture hitBoxT){
        hitBox = new Rectangle(0,0, HIT_BOX_WIDTH, HIT_BOX_HEIGHT);
        hitBoxTexture = hitBoxT;
    }

    /*
    Input: Void
    Output: Float X
    Purpose: Return X
    */
    float getX(){return hitBox.x;}

    /*
    Input: Void
    Output: Float Y
    Purpose: Return y
    */
    float getY(){return hitBox.y;}

    /*
    Input: Void
    Output: Float Width
    Purpose: Return width
    */
    float getWidth(){return hitBox.width;}

    /*
    Input: Void
    Output: Float Height
    Purpose: Return Height
    */
    float getHeight(){return hitBox.height;}

    /*
    Input: Void
    Output: Float Full Health
    Purpose: Return full health that the character starts with
    */
    int getHealthFull(){ return healthFull;}

    /*
    Input: Void
    Output: Float Current health
    Purpose: Return current health that character has right now
    */
    int getHealthCurrent(){return healthCurrent;}

    /*
    Input: Void
    Output: Float stamina
    Purpose: Return stamina that character has right now
    */
    float getStamina(){return  stamina;}

    /*
    Input: Int attack selection, attack in the array
    Output: Accuracy
    Purpose: Return accuracy which is a combo of attack ACC and character ACC
    */
    int getAccuracy(int attackSelection){
        //If between 0 < X < 100
        if(attackArray.get(attackSelection).getAccuracy() + accuracy > 0 &&
                attackArray.get(attackSelection).getAccuracy() + accuracy <= 100){
            return attackArray.get(attackSelection).getAccuracy() + accuracy;
        }
        //If x > 100
        else if(attackArray.get(attackSelection).getAccuracy() + accuracy > 100){
            return 100;
        }
        //If x < 0
        else {return 0;}
    }

    /*
    Input: Void
    Output: Array<Attack>
    Purpose: Return array so that we can get access to all of the textures
    */
    Array<Attack> getAttackArray(){return  attackArray;}

    /*
    Input: Int attack selection, attack in the array, delta for timing
    Output: int strength
    Purpose: Returns the strength of current attack, the strength is a combo of attack and character
            with it being a random chance with a range based on character stats. Its updated every
            tick or if character changes stances.
    */
    int getStrength(int attackSelection, float delta){
        strTimer -= delta;
        if (strTimer <= 0 || currentAttackSelection != attackSelection) {
            strTimer = STR_TIME;
            //Gets bounds
            int attackMin = attackArray.get(attackSelection).getStrength() * strength/10;
            int attackMax = attackArray.get(attackSelection).getStrength() + attackMin;
            //
            currentAttackStrength = MathUtils.random(attackMin, attackMax);
            currentAttackSelection = attackSelection;
            return MathUtils.random(attackMin, attackMax);
        }
        return currentAttackStrength;
    }

    /*
    Input: Int attack selection, attack in the array
    Output: Boolean
    Purpose: Returns if the character has enough stamina to use the attack
    */
    boolean getSpendStamina(int attackSelection){ return stamina > attackArray.get(attackSelection).getStaminaCost(); }

    /*
    Input: Int attack selection, attack in the array
    Output: Int
    Purpose: Returns the cost of the selected attack
    */
    int getCurrentAttackCost(int attackSelection){
        if(attackArray.size > 0){ return attackArray.get(attackSelection).getStaminaCost();}
        else return 101;
    }

    /*
    Input: Position
    Output: Void
    Purpose: sets where the fighter is placed
    */
    void setPosition(float x, float y){
        hitBox.x = x;
        hitBox.y = y;
    }

    /*
    Input: Int Health
    Output: Void
    Purpose: sets up the health of character
    */
    void setHealth(int health){
        healthCurrent = health;
        healthFull = health;
    }

    /*
    Input: Int Stamina
    Output: Void
    Purpose: Set stamina to 0
    */
    void setStamina(){ stamina = 0; }

    /*
    Input: Stats
    Output: Void
    Purpose: Sets up the stats that will dictate how well the fighter fights
    */
    void setStats(int STR, int STA, int ACC, int DEF, int HP, float SPD){
        strength = STR;
        staminaRegeneration = STA;
        accuracy = ACC;
        defense = DEF;
        setHealth(HP);
        speed = SPD;
    }

    /*
    Input: Texture
    Output: Void
    Purpose: Used to set up a character that has a sprite sheet
    */
    void setTexture(Texture texture){
        textureFlag = true;
        hitBoxSpriteSheet = new TextureRegion(texture).split(HEAD_TILE_WIDTH, HEAD_TILE_HEIGHT);
    }

    /*
    Input: Bounds, and delta for timing
    Output: Void
    Purpose: Updates the bounds and resets the frame to standing
    */
    void updateBoundsAndAnimation(float minBound, float maxBound, float delta){
        updateBounds(minBound, maxBound);
        waitFlagTimer -= delta;
        if (waitFlagTimer <= 0) {
            waitFlagTimer = WAIT_FLAG_TIME;
            modeFlag = 0;
        }
    }

    /*
    Input: Boolean action
    Output: Void
    Purpose: Moves character left or right
    */
    void updatePlayer(boolean action) {
        //False - Left, true = Right
        if (!action) { updatePosition(false); }
        else{ updatePosition(true);}
    }

    /*
    Input: Delta timing
    Output: Void
    Purpose: Moves the AI left and right based on how many steps in each direction it has taken
    */
    void updateAI(float delta){
        //Restart the step counter and reverse movement direction
        if(stepCounter == 3){
            switchStep = !switchStep;
            stepCounter = 0;
        }

        //Counts down between each move and moves in a direction
        stepTimer -= delta;
        if (stepTimer <= 0) {
            stepTimer = STEP_TIME;
            if (switchStep) { updatePosition(false); }
            else { updatePosition(true); }
            stepCounter++;
        }
    }

    /*
    Input: Int Damage
    Output: Returns flag that tells if we hit or not, used to show signs
    Purpose: Takes a roll for a hit, if we hit takes damage,
    */
    boolean updateHealth(int damage){
        int diceRoll = MathUtils.random(defense + (int) speed,100);
        //Block if you roll an over 85 you dodge
        if(diceRoll < 85) {
            //Take damage of the hit minus your defense
            if (healthCurrent - damage + defense > 0) {
                healthCurrent -= damage + defense;
            } else {
                healthCurrent = 0;
            }
            return true;
        }
        else {
            return false;
        }
    }

    /*
    Input: Void
    Output: Void
    Purpose: Naturally increase the stamina based on the Reg attribute
    */
    void updateStamina(){
        if(stamina < 100){ stamina += staminaRegeneration;}
        else {stamina = 100;}
    }

    /*
    Input: Void
    Output: Void
    Purpose: Lowers stamina when the attack is used and change it to the spire we need to display
    */
    void updateStamina(float staminaCost, int attackSelection) {
        if(stamina - staminaCost > 0){
            modeFlag = attackSelection + 2;
            stamina -= staminaCost;
        }
    }

    /*
    Input: Attack
    Output: Void
    Purpose: adds and attack to the array of fighters abilities
    */
    void addAttack(Attack newAttack){ attackArray.add(newAttack);}

    /*
    Input: Direction in which the character wants to go in
    Output: Void
    Purpose: Moves the character as long as they don't go past their bounds
    */
    private void updatePosition(boolean direction) {
        modeFlag = 1;
        //direction false = left, true = right
        if (direction && hitBox.x + hitBox.width < MAX_BOUND) {hitBox.x += speed;}
        else if(!direction && hitBox.x > MIN_BOUND){hitBox.x -= speed; }

        if(hitBox.x < MIN_BOUND){hitBox.x += BOUND_OFFSET;}
        if(hitBox.x + hitBox.width > MAX_BOUND){hitBox.x -= BOUND_OFFSET;}
    }

    /*
    Input: Bounds
    Output: Void
    Purpose: Updates the bounds
    */
    private void updateBounds(float minBound, float maxBound){
        MIN_BOUND = minBound + BOUND_OFFSET;
        MAX_BOUND = maxBound - BOUND_OFFSET;
    }

    /*
    Input: Position
    Output: Void
    Purpose: Sets all attacks to be at a certain position again
    */
    void restartAttackPosition(float x, float y){
        for(Attack attack : attackArray){
            attack.setPosition(x, y);
        }
    }

    /*
    Input: ShapeRenderer
    Output: Void
    Purpose: Draw wireframe
    */
    void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }

    /*
    Input: Which attackSelection, is it moving mobile, SpriteBatch
    Output: Void
    Purpose: Draw Attack
    */
    void drawAttack(int attackSelection, boolean mobile, SpriteBatch batch){
        attackArray.get(attackSelection).draw(batch);
        attackArray.get(attackSelection).updatePosition(mobile);
    }

    /*
    Input: SpriteBatch
    Output: Void
    Purpose: Draw texture
    */
    void draw(SpriteBatch batch){
        if(textureFlag) {
            //Stand
            if(modeFlag == 0){
                batch.draw(hitBoxSpriteSheet[0][0], hitBox.x, hitBox.y, hitBox.width, hitBox.height);
            }
            //Move
            else if(modeFlag == 1){
                batch.draw(hitBoxSpriteSheet[0][1], hitBox.x, hitBox.y, hitBox.width, hitBox.height);
            }
            //ShortRange
            else if(modeFlag == 2){
                batch.draw(hitBoxSpriteSheet[0][2], hitBox.x, hitBox.y, hitBox.width, hitBox.height);
            }
            //Medium
            else if(modeFlag == 3){
                batch.draw(hitBoxSpriteSheet[0][3], hitBox.x, hitBox.y, hitBox.width, hitBox.height);
            }
            //Long
            else if(modeFlag == 4){
                batch.draw(hitBoxSpriteSheet[0][4], hitBox.x, hitBox.y, hitBox.width, hitBox.height);
            }
        }
        else {
            batch.draw(hitBoxTexture, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
        }
    }

}
