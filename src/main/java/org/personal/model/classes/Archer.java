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
    public boolean attemptAttack(Arena arena) {
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                if (dx == 0 && dy == 0) continue; // Don't shoot ourselves!

                int targetX = this.x + dx;
                int targetY = this.y + dy;

                Combatant target = arena.getFighterAt(targetX, targetY);

                if (target != null && this.team != target.getTeam() && target.isAlive()) {
                    int hpBefore = target.getHp();
                    target.takeDamage(this.attackPower);

                    this.damageDealt += (hpBefore - target.getHp());
                    arena.logEvent(this.getClass().getSimpleName() + " attacks " + target.getClass().getSimpleName() + " for " + this.attackPower + " damage!");

                    if (!target.isAlive()) {
                        this.kills++;
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
