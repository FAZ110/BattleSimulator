package org.personal.model;

import org.personal.model.classes.*;

public class CombatantFactory {

    public static Combatant createHuman(Team team, int x, int y){
        return new Human(team, 10, 3, 4, x, y);
    }

    public static Combatant createZombie(Team team, int x, int y){
        return new Zombie(team, 10, 2, 2, x, y);
    }

    public static Combatant createArcher(Team team, int x, int y){
        return new Archer(team, 5, 4, 6, x, y);
    }

    public static Combatant createMage(Team team, int x, int y){return  new Mage(team, 3, 3, 5, x, y);}

    public static Combatant createHealer(Team team, int x, int y){return new Healer(team, 3, 1, 2, 4, x, y);}
}
