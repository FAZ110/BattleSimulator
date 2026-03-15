package org.personal.model.classes;

import org.personal.engine.Arena;
import org.personal.engine.Direction;
import org.personal.engine.Position;
import org.personal.model.Combatant;
import org.personal.model.Team;
import org.personal.model.behavior.KitingBehavior;

public class Mage extends Combatant {

    public Mage(Team team, int hp, int attackPower, int speed, int x, int y){
        super(team, hp, attackPower, speed, x, y);
        this.setBehavior(new KitingBehavior());
    }

    @Override
    public boolean attemptAttack(Arena arena) {
        Position bestPosition = null;
        int bestNumberOfTargets = 0;

        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {

                int targetX = this.getX() + dx;
                int targetY = this.getY() + dy;

                int numberOfTargets = enemiesAround(arena, new Position(targetX, targetY));
                if (numberOfTargets > bestNumberOfTargets) {
                    bestNumberOfTargets = numberOfTargets;
                    bestPosition = new Position(targetX, targetY);
                }
            }
        }

        if (bestPosition != null){
            for (int dx = -1; dx <= 1; dx++){
                for (int dy = -1; dy <= 1; dy++){

                    int targetX = bestPosition.x() + dx;
                    int targetY = bestPosition.y() + dy;

                    Combatant target = arena.getFighterAt(targetX, targetY);

                    if (target != null && target.getTeam() != this.getTeam()){
                        int hpBefore = target.getHp();
                        target.takeDamage(this.attackPower);

                        this.damageDealt += (hpBefore - target.getHp());
                        arena.logEvent(this.getClass().getSimpleName() + " attacks " + target.getClass().getSimpleName() + " for " + this.attackPower + " damage!");

                        if(!target.isAlive()){
                            this.kills++;
                            arena.logEvent(target.getClass().getSimpleName() + " has died!");
                            arena.removeFighter(target);
                        }
                    }



                }
            }
            return true;
        }
        return false;

    }

    private int enemiesAround(Arena arena, Position position) {
        int targets = 0;

        for (int dx = -1; dx <= 1; dx++){
            for (int dy = -1; dy <= 1; dy++){

                int targetX = position.x() + dx;
                int targetY = position.y() + dy;

                Combatant target = arena.getFighterAt(targetX, targetY);

                if (target != null && target.getTeam() != this.getTeam() && target.isAlive()){
                    targets++;
                }

            }
        }
        return targets;
    }
}

