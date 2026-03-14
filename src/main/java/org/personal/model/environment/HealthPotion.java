package org.personal.model.environment;

import org.personal.engine.Arena;
import org.personal.model.Combatant;

public class HealthPotion implements Consumable{

    private int healAmount = 5;

    @Override
    public char getSymbol() {
        return '+';
    }

    public void consume(Combatant target,  Arena arena) {
        arena.logEvent(target.getClass().getSimpleName() + " drank a potion and healed " + healAmount + " HP!");
        target.heal(healAmount);
    }


}
