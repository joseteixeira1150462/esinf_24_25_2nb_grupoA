package esinf.utils;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * The PairTest class contains unit tests for the Pair class.
 * It verifies the correct functionality of methods provided by the Pair class.
 */
public class PairTest {

    /**
     * Tests the getFirst() method of the Pair class.
     * Verifies that the first element of the pair is correctly returned.
     * Specifically, checks that the method returns the integer value 10 for the given pair.
     */
    @Test
    public void testGetFirst() {
        Pair<Integer, String> pair = new Pair<>(10, "ten");
        assertEquals(10, pair.getFirst(), "getFirst() should return the first value of the pair");
    }

    /**
     * Tests the getFirst() method when the first value of the pair is null.
     * This ensures that the method correctly returns null when the first element is set to null.
     */
    @Test
    public void testGetFirstWithNull() {
        Pair<Integer, String> pair = new Pair<>(null, "null");
        assertNull(pair.getFirst(), "getFirst() should return null as the first value is null");
    }
}