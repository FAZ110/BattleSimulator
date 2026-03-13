package org.personal;

import org.personal.engine.Arena;
import org.personal.engine.Position;
import org.personal.engine.Simulation;
import org.personal.model.Combatant;
import org.personal.model.Human;
import org.personal.model.Team;
import org.personal.model.Zombie;

public class Main {
    public static void main(String[] args) {

        System.out.println("Starting Battle simulation...");

        Arena arena = new Arena(15, 15);
        Simulation sim = new Simulation(arena, 10);

        for (int i=0 ; i<20; i++){
            sim.addFighter(new Human(Team.BLUE, 10, 3, 2, 2));
        }

        for (int i=0 ; i<20; i++){
            sim.addFighter(new Zombie(Team.RED, 8, 2, 12, 12));

        }

        sim.start();



    }
}