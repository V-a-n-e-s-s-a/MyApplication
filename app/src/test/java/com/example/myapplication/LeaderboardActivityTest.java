package com.example.myapplication;

// statements for static methods and classes
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

// android bundle
import android.os.Bundle;

// a view group that displays a list of scrollable items
import android.widget.ListView;

// base class for activities that use the support library action bar features
import androidx.appcompat.app.AppCompatActivity;

// represents a snapshot of a document in a Firestore database
import com.google.firebase.firestore.DocumentSnapshot;

// used for setup and test annotations
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

// annotations for setup and test methods
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

// methods to manipulate lists
import java.util.ArrayList;
// collection that holds elements in a sequential order
import java.util.List;

/**
 * @author Vanessa
 *
 * Test class for LeaderboardActivity class.
 */

@RunWith(MockitoJUnitRunner.class)
public class LeaderboardActivityTest {

    /**
     * Mock object for the AppCompatActivity.
     */
    @Mock
    AppCompatActivity mockActivity;

    /**
     * Mock object for the ListView.
     */
    @Mock
    ListView mockListView;

    /**
     * Mock object for the FirestoreAccess.
     */
    @Mock
    FirestoreAccess mockFirestoreAccess;

    /**
     * Instance of the LeaderboardActivity class to be tested.
     */
    private LeaderboardActivity leaderboardActivity;

    /**
     * Setup method to initialize test resources.
     */
    @Before
    public void setUp() {
        /**
         * Create a new instance of LeaderboardActivity for testing
         */
        leaderboardActivity = new LeaderboardActivity();
        /**
         * Mock the behavior of findViewById in the activity to return the mock ListView
         */
        Mockito.when(mockActivity.findViewById(Mockito.anyInt())).thenReturn(mockListView);
        /**
         * Set the mocked ListView and FirestoreAccess instances in the leaderboardActivity
         */
        leaderboardActivity.listView = mockListView;
        leaderboardActivity.firestoreAccess = mockFirestoreAccess;
    }

    /**
     * Test method for onCreate.
     */
    @Test
    public void testOnCreate() {
        /**
         * Create a new Bundle to simulate the savedInstanceState
         */
        Bundle savedInstanceState = new Bundle();
        /**
         * Call the onCreate method of the leaderboardActivity with the savedInstanceState
         */
        leaderboardActivity.onCreate(savedInstanceState);

        /**
         * Verify that the findViewById method of mockActivity was called with any integer argument
         */
        Mockito.verify(mockActivity).findViewById(Mockito.anyInt());
        /**
         * Verify that the retrievePlayerScores method of mockFirestoreAccess was called with any string and any argument
         */
        Mockito.verify(mockFirestoreAccess).retrievePlayerScores(Mockito.anyString(), Mockito.any());
    }

    /**
     * Test method for onRetrieveSuccess.
     */
    @Test
    public void testOnRetrieveSuccess() {
        /**
         *  Add test document snapshots to the list
         */
        List<DocumentSnapshot> testDocumentSnapshots = new ArrayList<>();

        /**
         * Mock the behavior of FirestoreAccess to directly call onRetrieveSuccess
         */
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                /**
                 * Get the arguments passed to the mocked method
                 */
                Object[] arguments = invocation.getArguments();
                /**
                 * Extract the listener from the arguments
                 */
                FirestoreAccess.OnRetrieveScoresListener listener = (FirestoreAccess.OnRetrieveScoresListener) arguments[1];
                /**
                 * Call the onRetrieveSuccess method of the listener with the testDocumentSnapshots
                 */
                listener.onRetrieveSuccess(testDocumentSnapshots);
                /**
                 * Return null as this method doesn't have a return value
                 */
                return null;
            }
        }).when(mockFirestoreAccess).retrievePlayerScores(Mockito.anyString(), Mockito.any());

        /**
         * Call the method under test
         */
        leaderboardActivity.firestoreAccess = mockFirestoreAccess;
        /**
         * Call the method under test: retrievePlayerScores with a mocked listener
         */
        mockFirestoreAccess.retrievePlayerScores("leaderboard", new FirestoreAccess.OnRetrieveScoresListener() {
            @Override
            public void onRetrieveSuccess(List<DocumentSnapshot> documentSnapshots) {
                /**
                 * Assert that the documentSnapshots contain the expected data
                 */
                assertNotNull(documentSnapshots);
                assertEquals(testDocumentSnapshots.size(), documentSnapshots.size());
            }

            @Override
            public void onRetrieveFailure(Exception e) {
                // Handle failure if needed
            }
        });
    }
}