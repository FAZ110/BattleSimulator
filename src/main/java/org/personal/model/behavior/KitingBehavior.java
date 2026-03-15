package org.personal.model.behavior;

import org.personal.engine.Arena;
import org.personal.model.Combatant;

public class KitingBehavior implements CombatBehavior {

    @Override
    public void executeTurn(Combatant self, Arena arena){
        Combatant prey = self.findClosestEnemy(arena);

        if (prey == null) {
            self.moveRandomly(arena);
            return;
        }

        int distanceX = Math.abs(self.getX() - prey.getX());
        int distanceY = Math.abs(self.getY() - prey.getY());
        int distance = Math.max(distanceX, distanceY);


        if (distance == 1) {
            self.moveAwayFrom(arena, prey);
        } else if (distance == 2) {
            self.attemptAttack(arena);
        } else {
            self.moveTowards(arena, prey);
        }

    }
}
