package org.personal.model.classes;
import org.personal.model.Combatant;
import org.personal.model.Team;
import org.personal.model.behavior.AggressiveBehavior;

public class Zombie extends Combatant {

    public Zombie(Team team, int hp, int attackPower, int speed, int x, int y) {
        super(team, hp, attackPower, speed, x, y);
        this.setBehavior(new AggressiveBehavior());
    }


}
