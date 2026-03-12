package org.personal.model;

import org.personal.engine.Arena;
import org.personal.engine.Position;

public abstract class Combatant {
    protected String name;
    protected int hp;
    protected int attackPower;
    protected int x;
    protected int y;


    public Combatant(String name, int hp, int attackPower, int x, int y) {
        this.name= name;
        this.hp = hp;
        this.attackPower = attackPower;
        this.x = x;
        this.y = y;
    }

    public abstract void takeTurn(Arena arena);

    public boolean isAlive() {
        return this.hp > 0;
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println(this.name + " takes " + damage + " damage! (HP: " + this.hp + ")");

    }

    public void setPosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }


}
