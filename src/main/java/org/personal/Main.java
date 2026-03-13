package org.personal;

import org.personal.engine.Arena;
import org.personal.engine.Position;
import org.personal.model.Combatant;
import org.personal.model.Human;

public class Main {
    public static void main(String[] args) {

        System.out.println("Starting Battle simulation...");

        Arena arena = new Arena(5, 5);

        Combatant fighter = new Human("Rick", 5, 2, 0, 0);
        Combatant bandit = new Human("Tom", 10, 1, 3, 3);

        arena.spawn(fighter);
        arena.spawn(bandit);

        System.out.println("INITIAL STATE");
        arena.render();

        int turn = 1;

        while (turn <= 20 && fighter.isAlive() && bandit.isAlive()){
            System.out.println("\n--- TURN " + turn + " ---");

            fighter.takeTurn(arena);
            bandit.takeTurn(arena);
            arena.render();
            turn++;

            try{Thread.sleep(1000);}catch(Exception e){}
        }

    }
}