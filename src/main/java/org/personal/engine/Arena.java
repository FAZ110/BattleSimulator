package org.personal.engine;

import org.personal.model.Combatant;
import org.personal.model.Team;
import org.personal.model.environment.Consumable;
import org.personal.model.environment.HealthPotion;
import org.personal.model.environment.Obstacle;
import org.personal.model.environment.Wall;

import java.util.*;

public class Arena {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private int height;
    private int width;
    private Map<Position, Combatant> grid;
    private Map<Position, Obstacle> obstacles;
    private Map<Position, Consumable> consumables;

    private Random random;
    private List<GameObserver> observers;


    public Arena(int height, int width, long seed) {
        this.height = height;
        this.width = width;
        this.grid = new HashMap<>();
        this.obstacles = new HashMap<>();
        this.consumables = new HashMap<>();
        this.random = new Random(seed);
        this.observers = new ArrayList<>();
    }

    public void addObserver(GameObserver observer) {
        this.observers.add(observer);
    }

    public void logEvent(String message){
        for (GameObserver observer : observers) {
            observer.onEvent(message);
        }
    }




    public void spawn(Combatant fighter){
        Position position = new Position(fighter.getX(), fighter.getY());
        grid.put(position, fighter);
    }

    public void spawnObstacle(Position pos, Obstacle obstacle) {
        obstacles.put(pos, obstacle);
    }

    public void spawnConsumable(Position pos, Consumable consumable) {
        consumables.put(pos, consumable);
    }

    public void generateEnvironment(int numberOfWalls, int numberOfConsumables){
        System.out.println("Generating environment...");


        for(int i = 0; i < numberOfWalls; i++){
            int randX, randY;

            do{
//                System.out.println("Generating wall...");
                randX = this.random.nextInt(width);
                randY = this.random.nextInt(height);
            }while (isTileTaken(randX, randY));

            spawnObstacle(new Position(randX, randY), new Wall());
//            System.out.println("Wall placed");

        }

        for(int i = 0; i < numberOfConsumables; i++){
            int randX, randY;

            do{
                randX = this.random.nextInt(width);
                randY = this.random.nextInt(height);
            }while (isTileTaken(randX, randY));

            spawnConsumable(new Position(randX, randY), new HealthPotion());

        }
    }


    public boolean isOccupied(int x, int y){
        return grid.containsKey(new Position(x, y));
    }

    public boolean isTileTaken(int x, int y){
        Position pos = new Position(x, y);
        return grid.containsKey(pos) || obstacles.containsKey(pos) || consumables.containsKey(pos);
    }
    public boolean isWithinBounds(int x, int y){
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void removeFighter(Combatant fighter){
        grid.remove(new Position(fighter.getX(), fighter.getY()));
    }

    public Combatant getFighterAt(int x, int y){return  grid.get(new Position(x, y));}

    public java.util.Collection<Combatant> getAllFighters(){return grid.values();}


    public boolean isWalkable(Position pos){
        if (!isWithinBounds(pos.x(), pos.y())) return false;
        if (obstacles.containsKey(pos)) return false;
        if (isOccupied(pos.x(), pos.y())) return false;
        return true;
    }

    public boolean moveFighter(Combatant fighter, Position newPosition){
        if (!isWithinBounds(newPosition.x(), newPosition.y())){
//            System.out.println(fighter.getSymbol() +  " cannot move out of bounds");
            return false;
        }

        if(obstacles.containsKey(newPosition)){
            return false;
        }

        if (isOccupied(newPosition.x(), newPosition.y())){
//            System.out.println(fighter.getSymbol() + "'s new position is occupied");
            return false;
        }

        grid.remove(new Position(fighter.getX(), fighter.getY()));
        grid.put(newPosition, fighter);

        fighter.setPosition(newPosition.x(), newPosition.y());

        if (consumables.containsKey(newPosition)){
            Consumable item = consumables.get(newPosition);
            item.consume(fighter, this);
            consumables.remove(newPosition);
        }
        return true;

    }

    public void render(){

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                Position currentPos = new Position(x, y);

                if(isOccupied(x, y)){
                    Combatant fighter = getFighterAt(x, y);

                    String color = RESET;
                    if (fighter.getTeam() == Team.RED) color = RED;
                    if (fighter.getTeam() == Team.BLUE) color = BLUE;

                    System.out.print("[" + color + fighter.getSymbol() + RESET + "]" );
                }
                else if (obstacles.containsKey(currentPos)) {
                    Obstacle obs = obstacles.get(currentPos);
                    System.out.print("[" + obs.getSymbol() + "]");
                }
                else if (consumables.containsKey(currentPos)) {
                    Consumable cons = consumables.get(currentPos);
                    System.out.print("[\u001B[32m" + cons.getSymbol() + "\u001B[0m]");
                }

                else{
                    System.out.print("[ ]");
                }
            }
            System.out.println();
        }
        System.out.println("=============================================");
    }

    public Random getRandom(){
        return this.random;
    }


}
