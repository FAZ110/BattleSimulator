package org.personal.model.classes;

import org.personal.engine.Arena;
import org.personal.engine.Direction;
import org.personal.engine.Position;
import org.personal.model.Combatant;
import org.personal.model.Team;
import org.personal.model.behavior.KitingBehavior;

public class Archer extends Combatant {

    public Archer(Team team, int hp, int attackPower, int speed, int x, int y) {
        super(team, hp, attackPower, speed, x, y);
        this.setBehavior(new KitingBehavior());
    }


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
                    arena.logEvent(this.getClass().getSimpleName() + " attacks " + target.getClass().getSimpleName() + " for " + this.attackPower + " damage!");

                    if(!target.isAlive()){
                        arena.logEvent(target.getClass().getSimpleName() + " has died!");
                        arena.removeFighter(target);
                    }
                    return true;
                }
            }


        }
        return false;
    }

}
