package com.packt.gamejamone;

public class Attack {

    private String name;
    private int strength;
    private int accuracy;
    private int staminaCost;

    Attack(){ }

    void setAttributes(String newName, int newStrength, int newAccuracy, int newStaminaCost){
        name = newName;
        strength = newStrength;
        accuracy = newAccuracy;
        staminaCost = newStaminaCost;
    }

    int getStrength(){return strength;}

    int getAccuracy(){return accuracy;}

    int getStaminaCost(){return staminaCost;}

}
