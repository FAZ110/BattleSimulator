package org.personal;

import org.personal.engine.Arena;
import org.personal.engine.ConsoleLogger;
import org.personal.engine.Position;
import org.personal.engine.Simulation;
import org.personal.model.*;

import java.util.Random;

public class Main {
    public static void main(String[] args) {

        System.out.println("Starting Battle simulation...");

        int height = 10;
        int width = 10;
        long seed = 42L;

        Arena arena = new Arena(height, width, seed);

        arena.addObserver(new ConsoleLogger());
        arena.generateEnvironment(7,3);
        Simulation sim = new Simulation(arena, 50);

        Random random = new Random(seed);

        for (int i=0 ; i<3; i++){

            int randomX, randomY;

            do{
                randomX = random.nextInt(width/2);
                randomY = random.nextInt(height);
            }while (arena.isTileTaken(randomX, randomY));

            sim.addFighter(CombatantFactory.createHuman(Team.BLUE, randomX, randomY));
            sim.addFighter(CombatantFactory.createMage(Team.BLUE, randomX, randomY));
        }

        for (int i=0 ; i<3; i++){

            int randomX, randomY;

            do{
                randomX = random.nextInt(width/2, width);
                randomY = random.nextInt(height);
            }while (arena.isTileTaken(randomX, randomY));

            sim.addFighter(CombatantFactory.createZombie(Team.RED, randomX,randomY));
            sim.addFighter(CombatantFactory.createArcher(Team.RED, randomX,randomY));

        }

        sim.start();



    }
}