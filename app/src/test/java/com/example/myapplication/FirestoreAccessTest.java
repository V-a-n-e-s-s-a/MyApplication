package com.example.myapplication;

// classes for asynchronous operations
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

// classes for interacting with Firestore database
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

// used for setup and test annotations
import org.junit.Before;
import org.junit.Test;

// annotations for setup and test methods
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

// statements for static methods and classes
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

// static methods for matching arguments and defining behavior
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.util.List;

/**
 * @author Vanessa
 *
 * Test class for FirestoreAccessTest class.
 */
public class FirestoreAccessTest {

    @InjectMocks
    private FirestoreAccess firestoreAccess;
    /**
     * Mock object for the FirestoreAccess.
     */
    @Mock
    private FirebaseFirestore firebaseFirestore;

    // Mock object for the CollectionReference.
    @Mock
    private CollectionReference collectionReference;

    // Mock object for the Query.
    @Mock
    private Query query;

    /**
     * Setup method to initialize test resources.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test method for storePlayerScore.
     */
    @Test
    public void testStorePlayerScore() {
        /**
         * Mock any method calls or behaviors as needed
         */
        when(firebaseFirestore.collection("leaderboard")).thenReturn(null);

        /**
         * Call the method to be tested
         */
        firestoreAccess.storePlayerScore("test@example.com", 100);
    }

    /**
     * Test method for updatePlayerScore.
     */
    @Test
    public void testUpdatePlayerScore() {
        /**
         * Create a mock QuerySnapshot
         */
        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);

        /**
         * Create a mock Query
         */
        Query query = Mockito.mock(Query.class);
        when(query.get()).thenReturn(Tasks.forResult(querySnapshot)); // Use Tasks.forResult() to create a completed Task

        /**
         * Create a mock CollectionReference
         */
        CollectionReference collectionReference = Mockito.mock(CollectionReference.class);
        when(collectionReference.whereEqualTo(anyString(), any())).thenReturn(query);

        /**
         * Create FirestoreAccess instance
         */
        FirestoreAccess firestoreAccess = new FirestoreAccess();

        /**
         * Call the method under test
         */
        Task<Void> task = firestoreAccess.updatePlayerScore("test@example.com", 200);
    }

    /**
     * Test method for retrievePlayerScores.
     */
    @Test
    public void testRetrievePlayerScores() {
        /**
         * Mock any method calls or behaviors as needed
         */
        when(firebaseFirestore.collection(anyString())).thenReturn(collectionReference);
        when(collectionReference.get()).thenReturn(Tasks.forResult(Mockito.mock(QuerySnapshot.class)));

        /**
         * Create a listener for the retrieval success
         */
        FirestoreAccess.OnRetrieveScoresListener listener = new FirestoreAccess.OnRetrieveScoresListener() {

            /**
             * Called when player scores are successfully retrieved from Firestore.
             *
             * @param documentSnapshots A list of DocumentSnapshot objects containing player score data.
             */
            @Override
            public void onRetrieveSuccess(List<DocumentSnapshot> documentSnapshots) {
                /**
                 * Verify that the documentSnapshots list is not null
                 */
                assertNotNull(documentSnapshots);

                /**
                 * Verify that the documentSnapshots list contains the expected number of items
                 */
                assertEquals(3, documentSnapshots.size());

                /**
                 * Verify the contents of specific DocumentSnapshot objects
                 */
                DocumentSnapshot firstDocument = documentSnapshots.get(0);
                assertEquals("test1@example.com", firstDocument.get("email"));
                assertEquals(100, firstDocument.get("score"));

                DocumentSnapshot secondDocument = documentSnapshots.get(1);
                assertEquals("test2@example.com", secondDocument.get("email"));
                assertEquals(200, secondDocument.get("score"));

                DocumentSnapshot thirdDocument = documentSnapshots.get(2);
                assertEquals("test3@example.com", thirdDocument.get("email"));
                assertEquals(300, thirdDocument.get("score"));
            }

            /**
             * Callback method invoked when an error occurs while retrieving player scores from Firestore.
             *
             * @param e The exception that occurred.
             */
            @Override
            public void onRetrieveFailure(Exception e) {
                /**
                 * Verify that an exception was received
                 */
                assertNotNull(e);

                /**
                 * Verify the message of the exception
                 */
                assertEquals("Some expected message", e.getMessage());
            }
        };

        /**
         * Call the method to be tested
         */
        firestoreAccess.retrievePlayerScores("leaderboard", listener);
    }

    /**
     * Test method for deletePlayerByEmail.
     */
    @Test
    public void testDeletePlayerByEmail() {
        /**
         * Create a mocked Task<DocumentSnapshot> object
         */
        Task<DocumentSnapshot> task = Tasks.forResult(null);

        /**
         * Mock the behavior of FirebaseFirestore and CollectionReference
         */
        when(firebaseFirestore.collection(any(String.class))).thenReturn(collectionReference);
        when(collectionReference.whereEqualTo(any(String.class), any(Object.class))).thenReturn(query);

        /**
         * Call the method under test
         */
        firestoreAccess.deletePlayerByEmail("test@example.com");

    }

}