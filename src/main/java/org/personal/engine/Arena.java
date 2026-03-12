package org.personal.engine;

import org.personal.model.Combatant;

import java.util.HashMap;
import java.util.Map;

public class Arena {
    private int height;
    private int width;
    private Map<Position, Combatant> grid;


    public Arena(int height, int width) {
        this.height = height;
        this.width = width;
        this.grid = new HashMap<Position, Combatant>();
    }

    public void spawn(Combatant fighter){
        Position position = new Position(fighter.getX(), fighter.getY());
        grid.put(position, fighter);
    }

    public boolean isOccupied(int x, int y){
        return grid.containsKey(new Position(x, y));
    }

    public Combatant getAtPosition(int x, int y){
        return grid.get(new Position(x, y));
    }

    public boolean isWithinBounds(int x, int y){
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void moveFighter(Combatant fighter, Position newPosition){
        if (!isWithinBounds(newPosition.x(), newPosition.y())){
            System.out.println(fighter.getName() +  " cannot move out of bounds");
            return;
        }

        if (isOccupied(newPosition.x(), newPosition.y())){
            System.out.println(fighter.getName() + "'s new position is occupied");
            return;
        }

        grid.remove(new Position(fighter.getX(), fighter.getY()));
        grid.put(newPosition, fighter);

        fighter.setPosition(newPosition.x(), newPosition.y());

    }

    public void render(){

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){

                if(isOccupied(x, y)){
                    Combatant fighter = getAtPosition(x, y);
                    System.out.print("[" + fighter.getName().charAt(0) + "]" );
                }else{
                    System.out.print("[ ]");
                }
            }
            System.out.println();
        }
        System.out.println("=============================================");
    }


}
