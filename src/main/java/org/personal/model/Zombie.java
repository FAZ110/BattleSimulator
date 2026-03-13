package org.personal.model;
import org.personal.engine.Arena;

public class Zombie extends Combatant{

    public Zombie(Team team, int hp, int attackPower, int x, int y) {
        super(team, hp, attackPower, x, y);
    }

//    @Override
//    public void takeTurn(Arena arena){
//
//        boolean attacked = attemptAttack(arena);
//
//        if (!attacked){
//            moveRandomly(arena);
//        }
//    }
}
