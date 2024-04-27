package com.example.myapplication;

import static android.content.ContentValues.TAG; // categorize and identify the source of the log message

import android.util.Log; // output messages to the system log to help with debugging

import androidx.annotation.NonNull; // indicates that a parameter, field, or method return value cannot be null

import com.google.android.gms.tasks.OnFailureListener; // handle failures that occur such as retrieving data from a Firestore database
import com.google.android.gms.tasks.OnSuccessListener; // used in conjunction with tasks that perform asynchronous operations
import com.google.android.gms.tasks.Task; // provides methods for handling success and failure scenarios
import com.google.firebase.firestore.DocumentReference; // represents a reference to a specific document in a Firestore database
import com.google.firebase.firestore.DocumentSnapshot; // epresents a snapshot of a document in a Firestore database
import com.google.firebase.firestore.FirebaseFirestore; // providing the necessary methods and functionality to interact with Firestore

import java.util.ArrayList; // provides methods to add, remove, access, and manipulate elements in the list
import java.util.HashMap; // data structure that stores key-value pairs
import java.util.List; //  ordered collection of elements
import java.util.Map; // represents a collection of key-value pairs

/**
 * FirestoreAccess class provides methods to interact with Firestore
 * database for storing, updating, retrieving, and deleting player
 * email in the leaderboard collection.
 */
public class FirestoreAccess {

    /**
     * Used to interact with the Firestore database
     */
    FirebaseFirestore db;

    /**
     * Initializes a new instance of the FirestoreAccess class.
     * Initializes the Firestore database.
     */
    public FirestoreAccess() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Stores the player's score in the Firestore database.
     *
     * @param email The email of the player.
     * @param score The score of the player.
     */
    public void storePlayerScore(String email, int score) {
        /**
         * Get an instance of the Firestore database
         */
        db = FirebaseFirestore.getInstance();

        /**
         * Create a map to store the player's email and score
         */
        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("score", score);

        /**
         * Add the data to the "leaderboard" collection in Firestore
         */
        db.collection("leaderboard")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                    /**
                     * Log a success message with the ID of the added document
                     *
                     * @param documentReference is the reference to the added document
                     */
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {

                    /**
                     * Log a warning message if adding the document fails
                     * @param e is the exception that occurred during the retrieval process.
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    /**
     * Updates the player's score in the Firestore database.
     *
     * @param email The email of the player.
     * @param newScore The new score of the player.
     * @return a Task representing the asynchronous update operation.
     */
    public Task<Void> updatePlayerScore(String email, int newScore) {
        Map<String, Object> data = new HashMap<>();
        data.put("score", newScore);

        /**
         * Query for the document with the specified email
         */
        return db.collection("leaderboard")
                .whereEqualTo("email", email)
                .get()
                .continueWithTask(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        /**
                         * Update the score of the first document
                         * (assuming there's only one document per email)
                         */
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        return document.getReference().update(data);
                    } else {
                        /**
                         * Handle the case where the document is not found
                         */
                        throw new Exception("Document not found for email: " + email);
                    }
                });
    }

    /**
     * Retrieves the player scores from the Firestore database.
     *
     * @param collection The name of the collection to retrieve scores from.
     * @param listener The listener to handle the retrieval success or failure.
     */
    public void retrievePlayerScores(String collection, OnRetrieveScoresListener listener) {
        db.collection(collection)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    /**
                     * Create a list to store DocumentSnapshot objects
                     */
                    List<DocumentSnapshot> documentSnapshots = new ArrayList<>();

                    /**
                     * Iterate over the query results and add each DocumentSnapshot to the list
                     */
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        documentSnapshots.add(document);
                    }

                    /**
                     * Invoke the listener's onRetrieveSuccess method with the list of DocumentSnapshots
                     */
                    listener.onRetrieveSuccess(documentSnapshots);
                })
                .addOnFailureListener(e -> {
                    /***
                     * Log an error message with the exception
                     */
                    Log.e(TAG, "Error retrieving documents", e);
                    /**
                     * Invoke the listener's onRetrieveFailure method with the exception
                     */
                    listener.onRetrieveFailure(e);
                });
    }

    /**
     * Interface definition for a callback to be invoked when player scores are retrieved.
     */
    public interface OnRetrieveScoresListener {

        /**
         * Called when player scores are successfully retrieved from Firestore.
         *
         * @param documentSnapshots A list of DocumentSnapshot objects containing player score data.
         */
        void onRetrieveSuccess(List<DocumentSnapshot> documentSnapshots);
        /**
         * Called when an error occurs while retrieving player scores from Firestore.
         *
         * @param e The exception that occurred.
         */
        void onRetrieveFailure(Exception e);
    }

    /**
     * Deletes the player's email document from the Firestore database.
     *
     * @param email The email of the player whose score document should be deleted.
     * @return A Task representing the asynchronous delete operation.
     */
    public Task<Void> deletePlayerByEmail(String email) {
        /**
         * Query for the document with the specified email
         */
        return db.collection("leaderboard")
                .whereEqualTo("email", email)
                .get()
                .continueWithTask(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        /**
                         * Delete the document
                         */
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        return document.getReference().delete();
                    } else {
                        /**
                         * Handle the case where the document is not found
                         */
                        throw new Exception("Document not found for email: " + email);
                    }
                });
    }
}
