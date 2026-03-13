package org.personal.model.environment;

import org.personal.model.Combatant;

public interface Consumable {

    void consume(Combatant target);
    char getSymbol();
}
