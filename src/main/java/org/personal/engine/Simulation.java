package org.personal.engine;

import org.personal.model.Combatant;
import org.personal.model.Team;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private Arena arena;
    private List<Combatant> roster;
    private List<Combatant> allParticipants; //stats
    private int maxTurns;


    public Simulation(Arena arena, int maxTurns) {
        this.arena = arena;
        this.roster = new ArrayList<>();
        this.allParticipants = new ArrayList<>();
        this.maxTurns = maxTurns;

    }

    public void addFighter(Combatant fighter){
        arena.spawn(fighter);
        roster.add(fighter);
        allParticipants.add(fighter);
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

        System.out.println("Simulation finished ");
        printPostMatchReport();

    }

    public void playOneTurn(){
        if (isSimOver()){
            System.out.println("Simulation over");
            return;
        }

        roster.sort(java.util.Comparator.comparingInt(Combatant::getSpeed).reversed());

        for (Combatant fighter : roster){
            if(fighter.isAlive()){
                fighter.takeTurn(arena);
            }
        }
        roster.removeIf(fighter -> !fighter.isAlive());
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


    private void printPostMatchReport() {
        System.out.println("\n=============================================");
        System.out.println("            POST MATCH STATISTICS            ");
        System.out.println("=============================================");

        // Sort fighters by Kills (Descending), and then by Damage Dealt
        allParticipants.sort((f1, f2) -> {
            if (f1.getKills() != f2.getKills()) {
                return Integer.compare(f2.getKills(), f1.getKills());
            }
            return Integer.compare(f2.getDamageDealt(), f1.getDamageDealt());
        });

        System.out.printf("%-15s %-10s %-10s %-10s %-10s\n", "Fighter", "Kills", "Dmg Dealt", "Dmg Taken", "Healed");
        System.out.println("-------------------------------------------------------------");

        for (Combatant f : allParticipants) {
            // Give them a " (DEAD)" tag if they didn't survive
            String status = f.isAlive() ? "" : " (RIP)";
            String name = f.getTeam() + " " + f.getClass().getSimpleName() + status;

            System.out.printf("%-15s %-10d %-10d %-10d %-10d\n",
                    name, f.getKills(), f.getDamageDealt(), f.getDamageTaken(), f.getHealingReceived());
        }
        System.out.println("=============================================\n");
    }

}
