package org.personal.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.personal.engine.Arena;

import static org.junit.jupiter.api.Assertions.*;

class CombatantTest {

    private Arena arena;

    @BeforeEach
    void setUp() {
        // Given a fresh 10x10 arena before every test
        arena = new Arena(10, 10);
    }

    // --- attemptAttack() Tests ---

    @Test
    void testAttemptAttackSucceedsOnAdjacentEnemy() {
        // Given
        Combatant human = CombatantFactory.createHuman(Team.BLUE, 2, 2);
        Combatant zombie = CombatantFactory.createZombie(Team.RED, 2, 3); // Right next to human
        int initialZombieHp = zombie.getHp();

        arena.spawn(human);
        arena.spawn(zombie);

        // When
        boolean didAttack = human.attemptAttack(arena);

        // Then
        assertTrue(didAttack, "Should return true when an enemy is successfully attacked");
        assertTrue(zombie.getHp() < initialZombieHp, "Zombie should have taken damage");
    }

    @Test
    void testAttemptAttackIgnoresFriendlyFire() {
        // Given
        Combatant human1 = CombatantFactory.createHuman(Team.BLUE, 2, 2);
        Combatant human2 = CombatantFactory.createHuman(Team.BLUE, 2, 3); // Friendly!
        int initialHuman2Hp = human2.getHp();

        arena.spawn(human1);
        arena.spawn(human2);

        // When
        boolean didAttack = human1.attemptAttack(arena);

        // Then
        assertFalse(didAttack, "Should return false because there are no enemies to attack");
        assertEquals(initialHuman2Hp, human2.getHp(), "Friendly unit should not take damage");
    }

    // --- takeTurn() Tests ---

    @Test
    void testTakeTurnAttacksInsteadOfMoving() {
        // Given
        Combatant human = CombatantFactory.createHuman(Team.BLUE, 2, 2);
        Combatant zombie = CombatantFactory.createZombie(Team.RED, 2, 3);
        int initialZombieHp = zombie.getHp();

        arena.spawn(human);
        arena.spawn(zombie);

        // When
        human.takeTurn(arena);

        // Then
        assertTrue(zombie.getHp() < initialZombieHp, "Zombie should take damage");
        assertEquals(2, human.getX(), "Human X should not change (they chose to attack, not move)");
        assertEquals(2, human.getY(), "Human Y should not change");
    }

    @Test
    void testTakeTurnMovesTowardsDistantEnemy() {
        // Given
        Combatant human = CombatantFactory.createHuman(Team.BLUE, 0, 0);
        Combatant zombie = CombatantFactory.createZombie(Team.RED, 0, 5); // Far away

        arena.spawn(human);
        arena.spawn(zombie);

        // When
        human.takeTurn(arena);

        // Then
        // Because the zombie is at Y=5, the human should step south to Y=1
        assertEquals(0, human.getX(), "Human X should remain 0");
        assertEquals(1, human.getY(), "Human Y should increase by 1 towards the zombie");
    }

    // --- moveRandomly() Test ---

    @Test
    void testMoveRandomlyChangesPosition() {
        // Given
        Combatant human = CombatantFactory.createHuman(Team.BLUE, 5, 5);
        arena.spawn(human);

        // When
        human.moveRandomly(arena);

        // Then
        // We can't predict exactly where they went, but we know they shouldn't be at 5,5 anymore
        boolean moved = (human.getX() != 5) || (human.getY() != 5);
        assertTrue(moved, "Fighter should have moved to a new adjacent tile");

        // Let's also verify they only moved a maximum of 1 tile away!
        int distanceX = Math.abs(5 - human.getX());
        int distanceY = Math.abs(5 - human.getY());
        assertTrue(distanceX <= 1 && distanceY <= 1, "Fighter should only move 1 tile away maximum");
    }
}