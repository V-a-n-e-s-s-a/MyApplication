package com.example.myapplication;

// used for setup and test annotations
import org.junit.Before;
import org.junit.Test;

// used for mocking objects
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

// used for Mockito methods
import static org.mockito.Mockito.*;

// messaging object that is used to request an action from another app component
import android.content.Intent;
// android bundle
import android.os.Bundle;

// firebase authentication
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

/**
 * @author Vanessa
 *
 * Test class for LoginActivity class.
 */

public class LoginActivityTest {

    /**
     * The LoginActivity instance being tested.
     */
    private LoginActivity loginActivity;
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
     * Setup method to initialize mocks and LoginActivity.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        loginActivity = new LoginActivity() {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                // Do nothing to prevent super.onCreate from being called
            }
        };

        /**
         * Set the mocked FirebaseAuth instance for authentication in LoginActivity
         */
        loginActivity.authentication = mockAuth;
        /**
         * Set the mocked FirestoreAccess instance for Firestore access in LoginActivity
         */
        loginActivity.firestoreAccess = mockFirestoreAccess;

        /**
         * Mock FirebaseUser to return a non-null value
         */
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);

        /**
         * do nothing when called with any String
         */
        doNothing().when(mockFirestoreAccess).deletePlayerByEmail(any(String.class));
        /**
         * do nothing when called with any String and any Integer argument.
         */
        doNothing().when(mockFirestoreAccess).storePlayerScore(any(String.class), any(Integer.class));
    }

    /**
     * Test for successful login.
     */
    @Test
    public void testLoginSuccess() {

        /**
         * Setup email and password
         */
        String email = "test@example.com";
        String password = "password";

        /**
         * Run the login method
         */
        loginActivity.login(email, password);

        /**
         * Verify that signInWithEmailAndPassword method is called
         * with the correct email and password
         */
        verify(mockAuth).signInWithEmailAndPassword(email, password);
        /**
         * Verify that deletePlayerByEmail method is called with the correct email
         */
        verify(mockFirestoreAccess).deletePlayerByEmail(email);

    }

    /**
     * Test for unsuccessful login due to invalid credentials.
     */
    @Test
    public void testLoginFailure_invalidCredentials() {
        /**
         * Setup email and password
         */
        String email = "test@example.com";
        String password = "incorrectPassword";

        /**
         * Mock signInWithEmailAndPassword to throw an exception
         */
        when(mockAuth.signInWithEmailAndPassword(email, password))
                .thenThrow(new FirebaseAuthInvalidCredentialsException("invalid", "Invalid email or password"));

        /**
         * Run the login method
         */
        loginActivity.login(email, password);

        /**
         * Verify that the signInWithEmailAndPassword method was called
         * with the provided email and password
         */
        verify(mockAuth).signInWithEmailAndPassword(email, password);
        /**
         * Verify that the deletePlayerByEmail method of mockFirestoreAccess was never called,
         * indicating that the player's email is not deleted due to the login failure
         */
        verify(mockFirestoreAccess, never()).deletePlayerByEmail(any(String.class));

        /**
         * Check if loginActivity started a new activity
         */
        verify(loginActivity).startActivity(any(Intent.class));
        /**
         * loginActivity is finished
         */
        verify(loginActivity).finish();
    }

}