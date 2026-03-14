package org.personal.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.personal.model.CombatantFactory;
import org.personal.model.Team;
import org.personal.model.Combatant;
import org.personal.model.environment.Wall;

import static org.junit.jupiter.api.Assertions.*;

class ArenaTest {

    private Arena arena;

    @BeforeEach
    void setUp() {
        arena = new Arena(5, 5, 1L);
    }

    @Test
    void testMoveFighterSuccess() {
        // Given
        Combatant human = CombatantFactory.createHuman(Team.BLUE, 1, 1);
        arena.spawn(human);
        Position emptyTile = new Position(1, 2);

        // When
        boolean moveSuccessful = arena.moveFighter(human, emptyTile);

        // Then
        assertTrue(moveSuccessful, "Fighter should successfully move to an empty tile");
        assertEquals(1, human.getX());
        assertEquals(2, human.getY());
        assertFalse(arena.isOccupied(1, 1), "Old position should be cleared");
    }

    @Test
    void testMoveFighterFailsOnWall() {
        // Given
        Combatant human = CombatantFactory.createHuman(Team.BLUE, 1, 1);
        arena.spawn(human);
        Position wallPosition = new Position(1, 2);
        arena.spawnObstacle(wallPosition, new Wall());

        // When
        boolean moveSuccessful = arena.moveFighter(human, wallPosition);

        // Then
        assertFalse(moveSuccessful, "Fighter should NOT be able to move into a Wall");
        assertEquals(1, human.getY(), "Fighter's Y coordinate should remain unchanged");
    }
}