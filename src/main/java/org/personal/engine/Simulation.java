package org.personal.engine;

import org.personal.model.Combatant;
import org.personal.model.Team;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private Arena arena;
    private List<Combatant> roster;
    private int maxTurns;


    public Simulation(Arena arena, int maxTurns) {
        this.arena = arena;
        this.roster = new ArrayList<>();
        this.maxTurns = maxTurns;

    }

    public void addFighter(Combatant fighter){
        arena.spawn(fighter);
        roster.add(fighter);
    }

    public void start(){
        System.out.println("Simulation started");

        arena.render();
        int turn = 1;

        while (turn <= maxTurns && !isSimOver()){

            roster.sort(java.util.Comparator.comparingInt(Combatant::getSpeed).reversed());

            for (Combatant fighter : roster){
                if(fighter.isAlive()){
                    fighter.takeTurn(arena);
                }

            }
            roster.removeIf(fighter -> !fighter.isAlive());
            arena.render();
            turn++;

            try{Thread.sleep(500);}catch(Exception e){}

        }
    }


    private boolean isSimOver(){
        if(roster.isEmpty()) return true;

        Team firstTeamFound = roster.getFirst().getTeam();

        for (Combatant fighter : roster){
            if(!fighter.getTeam().equals(firstTeamFound)){
                return false;
            }
        }
        return true;

    }

}
