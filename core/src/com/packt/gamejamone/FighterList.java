package com.packt.gamejamone;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

class FighterList {

    private Fighter player;
    private Fighter opponent;

    FighterList(Texture playerT, Texture opponentT){
        createPlayer(playerT);
        createOpponentFighter(opponentT);
    }

    private void createOpponentFighter(Texture texture){
        opponent = new Fighter(true, texture);
        opponent.setPosition(360,120);
        opponent.setStats(10, 2, 60, 2, 200, 20);
        opponent.setAI(2);

        Attack attack = new Attack();
        attack.setAttributes("Short Attack", 5, 10, 15);
        player.addAttack(attack);

        attack.setAttributes("Medium Attack", 15, 10, 5);
        player.addAttack(attack);

        attack.setAttributes("Long Attack", 1, 10, 1);
        player.addAttack(attack);
    }

    private void createPlayer(Texture texture){
        player = new Fighter(false, texture);
        player.setStats(5,5,30,5,120,10);
        player.setPosition(120 - player.getWidth(),120);

        Attack attack = new Attack();
        attack.setAttributes("Short Attack", 5, 10, 15);
        player.addAttack(attack);

        Attack attackTwo = new Attack();
        attackTwo.setAttributes("Medium Attack", 15, 1, 20);
        player.addAttack(attackTwo);

        Attack attackThree = new Attack();
        attackThree.setAttributes("Long Attack", 1, 15, 30);
        player.addAttack(attackThree);
    }

    int getPlayerHealthFull(){ return player.getHealthFull();}

    int getPlayerHealthCurrent(){return player.getHealthCurrent();}

    float getPlayerStamina(){return player.getStamina();}

    float getPlayerCurrentAttackCost(int attackSelection){return  player.getCurrentAttackCost(attackSelection);}

    void updateStaminaPay(float cost){player.updateStamina(cost);}

    float getPlayerAccuracy(int attackSelection){return player.getAccuracy(attackSelection);}

    float getPlayerStrength(int attackSelection){return player.getStrength(attackSelection);}

    void updatePlayer(int action){
        player.update(action, 0, opponent.getX());
    }

    void damagePlayer(int damage){player.updateHealth(damage);}

    int getOpponentHealthFull(){ return opponent.getHealthFull();}

    float getOpponentStamina(){return opponent.getStamina();}

    float getOpponentHealthCurrent(){return opponent.getHealthCurrent();}

    void damageOpponent(int damage){opponent.updateHealth(damage);}

    float getDistanceBetweenFighters() {
        return (opponent.getX() - player.getX() - player.getWidth());
    }

    void updateOpponent(){
        opponent.update(0, player.getX() + player.getWidth(), 480);
    }

    void info(){
        player.printStats();
        opponent.printStats();
    }

    void updateStamina(){
        player.updateStamina();
        opponent.updateStamina();
    }

    void drawDebug(ShapeRenderer shapeRenderer, boolean playerFlag) {
        if(playerFlag){player.drawDebug(shapeRenderer);}
        else{opponent.drawDebug(shapeRenderer);}
    }

    void draw(SpriteBatch batch){
        player.draw(batch);
        opponent.draw(batch);
    }
}
