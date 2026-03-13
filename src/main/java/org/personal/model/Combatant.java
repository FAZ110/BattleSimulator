package org.personal.model;

import org.personal.engine.Arena;
import org.personal.engine.Direction;
import org.personal.engine.Position;

import java.util.Random;

public abstract class Combatant {
    protected Team team;
    protected int maxHp;
    protected int hp;
    protected int attackPower;
    protected int x;
    protected int y;

    protected Random random = new Random();


    public Combatant(Team team, int hp, int attackPower, int x, int y) {
        this.team = team;
        this.maxHp = hp;
        this.hp = hp;
        this.attackPower = attackPower;
        this.x = x;
        this.y = y;
    }

    public void takeTurn(Arena arena) {

        if(random.nextBoolean()){
            attemptAttack(arena);
        }else{
            moveRandomly(arena);
        }

    }

    protected boolean attemptAttack(Arena arena) {
        Direction[] allDirections = Direction.values();

        for (Direction direction : allDirections) {
            int targetX = this.x + direction.getDx();
            int targetY = this.y + direction.getDy();

            Combatant target = arena.getFighterAt(targetX, targetY);

            if (target != null && this.team != target.getTeam() && target != this && target.isAlive()){
                target.takeDamage(this.attackPower);

                if(!target.isAlive()){
                    arena.removeFighter(target);
                }
                return true;
            }

        }
        return false;
    }

    protected void moveRandomly(Arena arena) {
        Direction[] allDirections = Direction.values();

        boolean succesfulMove = false;
        int attempts = 0;

        while (!succesfulMove && attempts < 10) {
            int randomIndex = random.nextInt(allDirections.length);
            Direction direction = allDirections[randomIndex];

            int newX = this.x + direction.getDx();
            int newY = this.y + direction.getDy();
            Position newPosition = new Position(newX, newY);

            succesfulMove = arena.moveFighter(this, newPosition);
            attempts++;
        }


    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }

    }

    public void heal(int healAmount){

        if (this.hp +  healAmount > maxHp){
            this.hp = maxHp;
        }else{
            this.hp += healAmount;
        }
    }

    public void setPosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public char getSymbol() {
        return this.getClass().getSimpleName().charAt(0);
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public Team getTeam() { return team; }


}
