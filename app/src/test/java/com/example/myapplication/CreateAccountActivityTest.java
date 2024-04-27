package com.example.myapplication;

// static methods for Mockito
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// messaging object that is used to request an action from another app component
import android.content.Intent;
// android bundle
import android.os.Bundle;

// firebase authentication
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// used for setup and test annotations
import org.junit.Before;
import org.junit.Test;

// used for mocking objects
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author Vanessa
 *
 * Test class for CreateAccountActivity class.
 */

public class CreateAccountActivityTest {

    /**
     * The CreateAccountActivity instance being tested.
     */
    private CreateAccountActivity createAccountActivity;
    /**
     * Mock FirebaseAuth instance for testing.
     */
    @Mock
    private FirebaseAuth mockAuth;
    /**
     * Mock FirestoreAccess instance for testing.
     */
    @Mock
    private FirestoreAccess mockFirestoreAccess;

    /**
     * Setup method to initialize mocks and CreateAccountActivity.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        createAccountActivity = new CreateAccountActivity() {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                // Do nothing to prevent super.onCreate from being called
            }
        };

        /**
         * Set the mocked FirebaseAuth instance for authentication in CreateAccountActivity
         */
        createAccountActivity.authentication = mockAuth;
        /**
         * Set the mocked FirestoreAccess instance for Firestore access in CreateAccountActivity
         */
        createAccountActivity.firestoreAccess = mockFirestoreAccess;

        /**
         * Mock FirebaseUser to return a non-null value
         */
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);

        /**
         * do nothing when called with any String and any Integer argument.
         */
        doNothing().when(mockFirestoreAccess).storePlayerScore(any(String.class), any(Integer.class));
    }

    /**
     * Test for successful createAccount
     */
    @Test
    public void testCreateAccount_Success() {
        /**
         * Setup email and password
         */
        String email = "test@example.com";
        String password = "password";

        /**
         * Run the createAccount method
         */
        createAccountActivity.createAccount(email, password);

        /**
         * Verify that the createUserWithEmailAndPassword method was called
         * with the provided email and password
         */
        verify(mockAuth).createUserWithEmailAndPassword(email, password);

        /**
         * Check if createAccountActivity started a new activity
         */
        verify(createAccountActivity).startActivity(any(Intent.class));
        /**
         * createAccountActivity is finished
         */
        verify(createAccountActivity).finish();
    }

}