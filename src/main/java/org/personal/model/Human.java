package org.personal.model;

import org.personal.engine.Arena;
import org.personal.engine.Direction;
import org.personal.engine.Position;

import java.util.Random;

public class Human extends Combatant{

    private Random random =  new Random();

    public Human(String name, int hp, int attackPower, int x, int y) {
        super(name, hp, attackPower, x, y);

    }


    @Override
    public void takeTurn(Arena arena) {

        if(random.nextBoolean()){
            attemptAttack(arena);
        }else{
            moveRandomly(arena);
        }

    }

    private void attemptAttack(Arena arena) {
        Direction[] allDirections = Direction.values();

        for (Direction direction : allDirections) {
            int targetX = this.x + direction.getDx();
            int targetY = this.y + direction.getDy();

            Combatant target = arena.getFighterAt(targetX, targetY);

            if (target != null && target != this && target.isAlive()){
                System.out.println(this.name + " punches " + target.getName() + "!");
                target.takeDamage(this.attackPower);

                if(!target.isAlive()){
                    System.out.println(target.getName() + " is dead!");
                    arena.removeFighter(target);
                }
                return;
            }

        }
    }

    private void moveRandomly(Arena arena) {
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
            if (succesfulMove){
                System.out.println(this.name + " moved to " + newPosition);
            }
            attempts++;
        }




    }
}
