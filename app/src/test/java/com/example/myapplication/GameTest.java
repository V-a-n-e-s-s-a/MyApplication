package com.example.myapplication;

import static org.mockito.Mockito.*; // Static imports for Mockito methods

import org.junit.Test; // JUnit Test annotation

/**
 * @author Vanessa and Noor
 *
 * Test class for GameTest class.
 */

public class GameTest {

    /**
     * Test for updateScore
     */
    @Test
    public void testUpdateScore() {
        /**
         * Create a mock FirestoreAccess object
         */
        FirestoreAccess firestoreAccess = mock(FirestoreAccess.class);

        /**
         * Create a Game instance with the mock FirestoreAccess
         */
        Game game = new Game("test@example.com", 0, firestoreAccess);

        /**
         * Call the updateScore method
         */
        game.updateScore();

        /**
         * Verify that updatePlayerScore was called with the correct arguments
         */
        verify(firestoreAccess).updatePlayerScore(eq("test@example.com"), anyInt());

        /**
         * Verify that storePlayerScore was called with the correct arguments
         */
        verify(firestoreAccess).storePlayerScore(eq("test@example.com"), anyInt());
    }
}