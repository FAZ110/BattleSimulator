package org.personal.model.behavior;

import org.personal.engine.Arena;
import org.personal.model.Combatant;

public class SupportBehavior implements CombatBehavior{

    @Override
    public void executeTurn(Combatant self, Arena arena) {

        Combatant patient = self.findBestHealTarget(arena);
        if(patient != null){
            int distanceX = Math.abs(self.getX() - patient.getX());
            int distanceY = Math.abs(self.getY() - patient.getY());
            int distance = Math.max(distanceX, distanceY);

            if (distance == 1){
                self.attemptHeal(arena);
            }else {
                self.moveTowards(arena, patient);
            }
        }else{
            Combatant enemy = self.findClosestEnemy(arena);

            if (enemy != null){
                self.moveAwayFrom(arena, enemy);
            }else{
                self.moveRandomly(arena);
            }
        }
    }
}
