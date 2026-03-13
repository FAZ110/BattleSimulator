package org.personal.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void testEquality() {
        // Given
        Position p1 = new Position(2, 3);
        Position p2 = new Position(2, 3);
        Position p3 = new Position(4, 1);

        // When
        boolean areIdenticalEqual = p1.equals(p2);
        boolean areDifferentEqual = p1.equals(p3);

        // Then
        assertTrue(areIdenticalEqual, "Positions with identical coordinates should be equal");
        assertFalse(areDifferentEqual, "Positions with different coordinates should not be equal");
    }

    @Test
    void testHashCode() {
        // Given
        Position p1 = new Position(5, 5);
        Position p2 = new Position(5, 5);

        // When
        int hash1 = p1.hashCode();
        int hash2 = p2.hashCode();

        // Then
        assertEquals(hash1, hash2, "HashCodes must match for identical positions");
    }
}