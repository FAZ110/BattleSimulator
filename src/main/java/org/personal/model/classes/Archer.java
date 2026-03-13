package org.personal.model.classes;

import org.personal.engine.Arena;
import org.personal.engine.Direction;
import org.personal.model.Combatant;
import org.personal.model.Team;

public class Archer extends Combatant {

    public Archer(Team team, int hp, int attackPower, int x, int y) {
        super(team, hp, attackPower, x, y);
    }


//    @Override
//    public void takeTurn(Arena arena){
//        boolean didAttack = attemptAttack(arena);
//
//        if (!didAttack){
//            moveRandomly(arena);
//        }
//        moveRandomly(arena);
//    }

    @Override
    public boolean attemptAttack(Arena arena){
        Direction[] allDirections = Direction.values();

        for (Direction direction : allDirections){

            for (int distance=1; distance<=2; distance++){
                int targetX = this.x + (direction.getDx()*distance);
                int targetY = this.y + (direction.getDy()*distance);

                Combatant target = arena.getFighterAt(targetX, targetY);

                if(target != null && this.team != target.getTeam() && this != target && target.isAlive()){
                    target.takeDamage(this.attackPower);
                    System.out.println("Archer attacked");

                    if(!target.isAlive()){
                        arena.removeFighter(target);
                    }
                    return true;
                }
            }


        }
        return false;
    }

}
