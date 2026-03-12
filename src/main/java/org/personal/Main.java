package org.personal;

import org.personal.engine.Arena;
import org.personal.engine.Position;
import org.personal.model.Combatant;
import org.personal.model.Human;

public class Main {
    public static void main(String[] args) {

        System.out.println("Starting Battle simulation...");

        Arena arena = new Arena(5, 5);

        Combatant man = new Human("Rick", 5, 2, 1, 3);
        arena.spawn(man);

        arena.render();

        arena.moveFighter(man, new Position(2,3));
        arena.render();
    }
}