package org.personal.model;

import org.personal.engine.Arena;
import org.personal.engine.Direction;
import org.personal.engine.Position;

import java.util.*;

public abstract class Combatant {
    protected Team team;
    protected int maxHp;
    protected int hp;
    protected int attackPower;
    protected int speed;
    protected int x;
    protected int y;


    public Combatant(Team team, int hp, int attackPower, int speed, int x, int y) {
        this.team = team;
        this.maxHp = hp;
        this.hp = hp;
        this.attackPower = attackPower;
        this.speed = speed; //priority
        this.x = x;
        this.y = y;
    }

    public void takeTurn(Arena arena) {
        boolean attacked = attemptAttack(arena);

        if(!attacked){
            Combatant prey = findClosestEnemy(arena);

            if (prey != null) {
                moveTowards(arena, prey);
            }else{
                moveRandomly(arena);
            }
        }

    }

    protected boolean attemptAttack(Arena arena) {
        Direction[] allDirections = Direction.values();

        for (Direction direction : allDirections) {
            int targetX = this.x + direction.getDx();
            int targetY = this.y + direction.getDy();

            Combatant target = arena.getFighterAt(targetX, targetY);

            if (target != null && this.team != target.getTeam() && target != this && target.isAlive()){
                target.takeDamage(this.attackPower);

                if(!target.isAlive()){
                    arena.removeFighter(target);
                }
                return true;
            }

        }
        return false;
    }

    protected void moveRandomly(Arena arena) {
        Direction[] allDirections = Direction.values();

        boolean succesfulMove = false;
        int attempts = 0;

        while (!succesfulMove && attempts < 10) {
            int randomIndex = arena.getRandom().nextInt(allDirections.length);
            Direction direction = allDirections[randomIndex];

            int newX = this.x + direction.getDx();
            int newY = this.y + direction.getDy();
            Position newPosition = new Position(newX, newY);

            succesfulMove = arena.moveFighter(this, newPosition);
            attempts++;
        }
    }

    protected Combatant findClosestEnemy(Arena arena) {
        Combatant closestEnemy = null;
        int shortestDistance = Integer.MAX_VALUE;

        for (Combatant other : arena.getAllFighters()){

            if(other.getTeam() != this.team && other.isAlive()){

                int distanceX = Math.abs(this.x - other.getX());
                int distanceY = Math.abs(this.y - other.getY());

                int distance = Math.max(distanceX, distanceY);

                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    closestEnemy = other;
                }
            }
        }
        return closestEnemy;
    }

    protected void moveTowards(Arena arena, Combatant target) {
        Position nextStep = getNextStepTowards(arena, target);

        if (nextStep != null) {
            boolean successfulMove = arena.moveFighter(this, nextStep);
            if (!successfulMove) {
                moveRandomly(arena);
            }
        }else{
            moveRandomly(arena);
        }
    }


    // BFS
    protected Position getNextStepTowards(Arena arena, Combatant target) {
        Position start = new Position(this.x, this.y);
        Position targetPos = new Position(target.getX(), target.getY());

        Queue<Position> frontier  = new LinkedList<>();
        Set<Position> visited = new HashSet<>();
        Map<Position, Position> cameFrom = new HashMap<>();

        frontier.add(start);
        visited.add(start);

        Position fountAdjacent = null;

        while (!frontier.isEmpty()) {
            Position current = frontier.poll();

            int distanceToTarget = Math.max(Math.abs(current.x() - targetPos.x()), Math.abs(current.y() - targetPos.y()));

            if (distanceToTarget == 1){
                fountAdjacent = current;
                break;
            }

            for (Direction direction : Direction.values()) {
                Position next = new Position(current.x() + direction.getDx(), current.y() + direction.getDy());

                if (!visited.contains(next) && arena.isWalkable(next)) {
                    visited.add(next);
                    cameFrom.put(next, current);
                    frontier.add(next);
                }
            }
        }

        if (fountAdjacent == null || fountAdjacent.equals(start)) {
            return null;
        }

        Position step = fountAdjacent;
        while (!cameFrom.get(step).equals(start)) {
            step = cameFrom.get(step);
        }
        return step;
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.hp = 0;
        }

    }

    public void heal(int healAmount){

        if (this.hp +  healAmount > maxHp){
            this.hp = maxHp;
        }else{
            this.hp += healAmount;
        }
    }

    public void setPosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public char getSymbol() {
        return this.getClass().getSimpleName().charAt(0);
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public Team getTeam() { return team; }
    public int getHp() { return hp; }
    public int getSpeed(){return speed;}


}
