package org.personal.model;

import org.personal.engine.Arena;
import org.personal.engine.Direction;
import org.personal.engine.Position;
import org.personal.model.behavior.CombatBehavior;

import java.util.*;

public abstract class Combatant {
    protected Team team;
    protected int maxHp;
    protected int hp;
    protected int attackPower;
    protected int speed;
    protected int x;
    protected int y;
    protected CombatBehavior behavior;


    protected int damageDealt = 0;
    protected int damageTaken = 0;
    protected int kills = 0;
    protected int healingReceived = 0;



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
        if (this.behavior != null) {
            this.behavior.executeTurn(this, arena);
        }

    }

    public boolean attemptAttack(Arena arena) {
        Direction[] allDirections = Direction.values();

        for (Direction direction : allDirections) {
            int targetX = this.x + direction.getDx();
            int targetY = this.y + direction.getDy();

            Combatant target = arena.getFighterAt(targetX, targetY);

            if (target != null && this.team != target.getTeam() && target != this && target.isAlive()){
                int hpBefore = target.getHp();
                target.takeDamage(this.attackPower);

                this.damageDealt += (hpBefore - target.getHp());

                arena.logEvent(this.getClass().getSimpleName() + " attacks " + target.getClass().getSimpleName() + " for " + this.attackPower + " damage!");

                if(!target.isAlive()){
                    this.kills++;
                    arena.logEvent(target.getClass().getSimpleName() + " has died!");
                    arena.removeFighter(target);
                }
                return true;
            }

        }
        return false;
    }

    public void moveRandomly(Arena arena) {
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

    public Combatant findClosestEnemy(Arena arena) {
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

    public void moveTowards(Arena arena, Combatant target) {
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

    public void moveAwayFrom(Arena arena, Combatant target){

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

    public Combatant findBestHealTarget(Arena arena) {
        Combatant bestTarget = null;
        double highestUrgency = -9999;

        for (Combatant target : arena.getAllFighters()) {
            if(target.getTeam() == this.team && target.isAlive() && target != this && target.getHp() < target.getMaxHp()){

                int distanceX = Math.abs(this.x - target.getX());
                int distanceY = Math.abs(this.y - target.getY());
                int distance = Math.max(distanceX, distanceY);

                int missingHp =  target.getMaxHp() - target.getHp();
                double urgency = missingHp - (distance*1.5);

                if (urgency > highestUrgency) {
                    highestUrgency = urgency;
                    bestTarget = target;
                }
            }
        }
        return bestTarget;


    }


    public boolean attemptHeal(Arena arena) {
        return false;
    }

    public boolean isAlive() {
        return this.hp > 0;
    }

    public void takeDamage(int damage) {

        int actualDamage = Math.min(this.hp, damage);
        this.hp -= actualDamage;
        this.damageTaken += actualDamage;

    }

    public void heal(int healAmount){
        int actualHeal = Math.min(maxHp - this.hp, healAmount); // Don't over-heal
        this.hp += actualHeal;
        this.healingReceived += actualHeal;
    }

    public void setBehavior(CombatBehavior behavior) {
        this.behavior = behavior;
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
    public int getMaxHp() { return maxHp; }
    public int getSpeed(){return speed;}

    public int getDamageDealt() { return damageDealt; }
    public int getDamageTaken() { return damageTaken; }
    public int getKills() { return kills; }
    public int getHealingReceived() { return healingReceived; }


}
