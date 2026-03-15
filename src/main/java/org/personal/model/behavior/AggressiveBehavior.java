package org.personal.model.behavior;

import org.personal.engine.Arena;
import org.personal.model.Combatant;


public class AggressiveBehavior implements CombatBehavior {

    @Override
    public void executeTurn(Combatant self, Arena arena){

        boolean attacked = self.attemptAttack(arena);

        if (!attacked){
            Combatant prey = self.findClosestEnemy(arena);
            if (prey != null) {
                self.moveTowards(arena, prey);
            }else{
                self.moveRandomly(arena);
            }
        }
    }
}
