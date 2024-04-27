package com.example.myapplication;

import static android.content.ContentValues.TAG; // TAG for logging

import android.util.Log; // Android logging utility

import androidx.annotation.NonNull; // Annotation for non-null parameters

import com.google.android.gms.tasks.OnFailureListener; // Listener for task failure
import com.google.android.gms.tasks.OnSuccessListener; // Listener for task success

/**
 * @author Vanessa and Noor
 *
 * Game represents a game instance with a player's email and score.
 */
public class Game {
    /**
     * Player's email
     */
    private String email;
    /**
     * Player's score
     */
    private int score;
    /**
     * instance of firestoreAccess
     */
    private FirestoreAccess firestoreAccess;

    /**
     * Constructs a new Game instance.
     *
     * @param email           The player's email.
     * @param score           The player's score.
     * @param firestoreAccess The FirestoreAccess instance to access Firestore.
     */
    public Game(String email, int score, FirestoreAccess firestoreAccess) {
        this.email = email;
        this.score = score;
        this.firestoreAccess = firestoreAccess;
    }

    /**
     * Updates the player's score by adding 1000 points and stores the updated score in Firestore.
     */
    public void updateScore() {
        // Update the score by adding 1000
        int newScore = score + 1000;

        /**
         * Update player's score in Firestore
         */
        firestoreAccess.updatePlayerScore(email, newScore)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Player score updated successfully");
                        score = newScore;
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating player score", e);
                    }
                });

        /**
         * Store the updated score in Firestore with email
         */
        firestoreAccess.storePlayerScore(email, newScore);

    }

}
