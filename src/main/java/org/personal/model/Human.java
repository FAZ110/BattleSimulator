package org.personal.model;

import org.personal.engine.Arena;

public class Human extends Combatant{

    public Human(String name, int hp, int attackPower, int x, int y) {
        super(name, hp, attackPower, x, y);

    }


    @Override
    public void takeTurn(Arena arena) {

        System.out.println(this.name + "Is moving");
    }
}
