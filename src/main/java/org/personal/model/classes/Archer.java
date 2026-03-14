package org.personal.model.classes;

import org.personal.engine.Arena;
import org.personal.engine.Direction;
import org.personal.engine.Position;
import org.personal.model.Combatant;
import org.personal.model.Team;

public class Archer extends Combatant {

    public Archer(Team team, int hp, int attackPower, int speed, int x, int y) {
        super(team, hp, attackPower, speed, x, y);
    }

    // ARCHER KITES
    @Override
    public void takeTurn(Arena arena){
        Combatant prey = findClosestEnemy(arena);

        if(prey == null){
            moveRandomly(arena);
            return;
        }

        int distanceX = Math.abs(this.x - prey.getX());
        int distanceY = Math.abs(this.y - prey.getY());
        int distance = Math.max(distanceX, distanceY);

        if (distance == 1){
            moveAwayFrom(arena, prey);
        }else if (distance == 2){
            System.out.println("safe distance");
            attemptAttack(arena);
        }else{
            moveTowards(arena, prey);
        }
    }

    private void moveAwayFrom(Arena arena, Combatant target){

        int desiredDx = Integer.compare(this.x, target.getX());
        int desiredDy = Integer.compare(this.y, target.getY());

        int escapeX = this.x + desiredDx;
        int escapeY = this.y + desiredDy;
        Position stepPosition = new Position(escapeX, escapeY);

        boolean successfulMove = arena.moveFighter(this, stepPosition);
        if (!successfulMove){
            if(!attemptAttack(arena)){
                moveRandomly(arena);
            }
        }
    }

    @Override
    public boolean attemptAttack(Arena arena){
        Direction[] allDirections = Direction.values();

        for (Direction direction : allDirections){

            for (int distance=1; distance<=2; distance++){
                int targetX = this.x + (direction.getDx()*distance);
                int targetY = this.y + (direction.getDy()*distance);

                Combatant target = arena.getFighterAt(targetX, targetY);

                if(target != null && this.team != target.getTeam() && this != target && target.isAlive()){
                    target.takeDamage(this.attackPower);
                    System.out.println("Archer attacked");

                    if(!target.isAlive()){
                        arena.removeFighter(target);
                    }
                    return true;
                }
            }


        }
        return false;
    }

}
