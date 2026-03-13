package org.personal;

import org.personal.engine.Arena;
import org.personal.engine.Position;
import org.personal.engine.Simulation;
import org.personal.model.*;

import java.util.Random;

public class Main {
    public static void main(String[] args) {

        System.out.println("Starting Battle simulation...");

        int height = 20;
        int width = 20;

        Arena arena = new Arena(height, width);
        arena.generateEnvironment(5,3);
        Simulation sim = new Simulation(arena, 50);

        Random random = new Random();

        for (int i=0 ; i<25; i++){

            int randomX, randomY;

            do{
                randomX = random.nextInt(width/2);
                randomY = random.nextInt(height);
            }while (arena.isTileTaken(randomX, randomY));

            sim.addFighter(CombatantFactory.createHuman(Team.BLUE, randomX, randomY));
        }

        for (int i=0 ; i<10; i++){

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