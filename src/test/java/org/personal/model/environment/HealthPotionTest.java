package org.personal.model.environment;

import org.junit.jupiter.api.Test;
import org.personal.engine.Arena;
import org.personal.engine.Position;
import org.personal.model.CombatantFactory;
import org.personal.model.Team;
import org.personal.model.Combatant;

import static org.junit.jupiter.api.Assertions.*;

class HealthPotionTest {

    @Test
    void testConsumptionHealsFighter() {
        // Given
        Arena arena = new Arena(5, 5, 1L);
        Combatant human = CombatantFactory.createHuman(Team.BLUE, 0, 0);
        human.takeDamage(8); // HP drops from 10 to 2
        arena.spawn(human);

        Position potionPos = new Position(0, 1);
        arena.spawnConsumable(potionPos, new HealthPotion());

        // When
        arena.moveFighter(human, potionPos);

        // Then
        // Assuming potion heals for 5, HP should now be 7 (2 + 5)
        assertEquals(7, human.getHp(), "Human should be healed by the potion's amount");
        assertTrue(!arena.isTileTaken(0, 1) || arena.isOccupied(0,1), "Potion should be removed from the tile");
    }
}