package org.personal.model;

public class CombatantFactory {

    public static Combatant createHuman(Team team, int x, int y){
        return new Human(team, 10, 3, x, y);
    }

    public static Combatant createZombie(Team team, int x, int y){
        return new Zombie(team, 10, 2, x, y);
    }

    public static Combatant createArcher(Team team, int x, int y){
        return new Archer(team, 5, 4, x, y);
    }
}
