package org.personal.model.classes;
import org.personal.model.Combatant;
import org.personal.model.Team;

public class Zombie extends Combatant {

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
