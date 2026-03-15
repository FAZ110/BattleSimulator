package org.personal.model.behavior;

import org.personal.engine.Arena;
import org.personal.model.Combatant;

public interface CombatBehavior {
    void executeTurn(Combatant self, Arena arena);
}
