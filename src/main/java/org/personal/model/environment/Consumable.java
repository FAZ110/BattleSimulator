package org.personal.model.environment;

import org.personal.engine.Arena;
import org.personal.model.Combatant;

public interface Consumable {

    void consume(Combatant target, Arena arena);
    char getSymbol();
}
