package org.personal.model.classes;

import org.personal.engine.Arena;
import org.personal.engine.Direction;
import org.personal.model.Combatant;
import org.personal.model.Team;
import org.personal.model.behavior.SupportBehavior;

public class Healer extends Combatant {

    private int healingPower;

    public Healer(Team team, int hp, int attackPower, int healingPower, int speed, int x, int y) {
        super(team, hp, attackPower, speed, x, y);
        this.healingPower = healingPower;
        this.setBehavior(new SupportBehavior());
    }

    @Override
    public boolean attemptHeal(Arena arena) {
        Direction[] allDirections = Direction.values();

        for  (Direction direction : allDirections) {
            int targetX = this.x + direction.getDx();
            int targetY = this.y + direction.getDy();

            Combatant target = arena.getFighterAt(targetX, targetY);

            if (target != null && this.getTeam() == target.getTeam() && target.isAlive()) {
                target.heal(this.healingPower);
                arena.logEvent(this.getClass().getSimpleName() + " heals " +
                        target.getClass().getSimpleName() + " for " + this.healingPower);
                return true;
            }
        }

        return false;
    }
}
