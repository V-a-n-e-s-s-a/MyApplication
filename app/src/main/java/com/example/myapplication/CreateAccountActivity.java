package com.example.myapplication;

import android.os.Bundle; // used for passing data between activities
import android.view.View; // represents a basic building block for user interface components
import android.widget.EditText; // provides a text entry for users to enter text
import android.widget.ImageButton; // represents a button with an image that can be clicked
import android.widget.Toast; // provides simple feedback about an operation in a small popup

import androidx.annotation.NonNull; // indicates that a parameter, field, or method return value cannot be null
import androidx.appcompat.app.AppCompatActivity; // base class for activities that use the support library action bar features

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener; // listener called when a task completes
import com.google.android.gms.tasks.Task; // represents an asynchronous operation
import com.google.firebase.auth.AuthResult; // represents the result os a successful authentication
import com.google.firebase.auth.FirebaseAuth; // entry point of the Firebase Authentication SDK
import com.google.firebase.auth.FirebaseAuthUserCollisionException; // exception thrown when email already in use

/**
 * @author Vanessa
 *
 * CreateAccountActivity is an interface class that displays the
 * create account screen, allows player to create a game account
 * (username and password), and checks if the username is taken already.
 *
 * CreateAccountActivity is a subclass of AppCompatActivity
 */

public class CreateAccountActivity extends AppCompatActivity {
    /**
     * EditText view used for entering the email address
     */
    EditText editTextEmail;
    /**
     * EditText view used for entering the password
     */
    EditText editTextPassword;
    /**
     * ImageButton used as the create account button
     */
    ImageButton createAccountButton;
    /**
     * An instance of FirebaseAuth used for Firebase authentication
     */
    FirebaseAuth authentication;

    /**
     * Instance of FirestoreAccess for accessing Firestore database
     */
    FirestoreAccess firestoreAccess;

    /**
     * A method that is called when the activity is being created
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * Ensure that the superclass's initialization code is executed
         * before custom initialization code.
         */
        super.onCreate(savedInstanceState);
        /**
         * Initializing variables and setting up any necessary event listeners
         * In this case create_account_screen.xml
         */
        setContentView(R.layout.create_account_screen);

        /**
         * Initialize Firebase Authentication
         */
        authentication = FirebaseAuth.getInstance();

        /**
         * Initialize FirestoreAccess
         */
        firestoreAccess = new FirestoreAccess();

        /**
         * Find views by their IDs
         */
        editTextEmail = findViewById(R.id.CreateAccountEmail);
        editTextPassword = findViewById(R.id.CreateAccountPassword);
        createAccountButton = findViewById(R.id.CreateAccountButton);

        /**
         * Set click listener for the create account button
         */
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Click event
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                /**
                 * Get user entered email and password
                 */
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                /**
                 * Call createAccount method
                 */
                createAccount(email, password);
            }
        });
    }

    /**
     * Creates account with the provided email and password
     *
     * @param email is user's email
     * @param password is the user's password
     */
    protected void createAccount(String email, String password) {
        /**
         * Create account with given email and password
         */
        authentication.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    /**
                     * Makes sure a return value cannot be null
                     * @param task is an object that represents the result of an authentication task
                     */
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        /**
                         * Account creation success
                         */
                        if (task.isSuccessful()) {

                            /**
                             * Creates a new instance of the Game class with
                             * the specified email and initializes score to 0,
                             * and the FirestoreAccess instance firestoreAccess
                             * (initialize new game object for a player).
                             */
                            Game game = new Game(email, 0, firestoreAccess);
                            /**
                             * Update the score in Firestore
                             */
                            game.updateScore();

                            /**
                             * The account was successfully created
                             */
                            showToast("Account created successfully");

                            /**
                             * Navigates to Maze
                             */
                            setContentView(R.layout.maze);
                        }
                        else {
                            /**
                             * User must enter a new email
                             */
                            Exception exception = task.getException();
                            if (exception instanceof FirebaseAuthUserCollisionException) {
                                showToast("Email already in use");
                            }
                            /**
                             * Account creation failed
                             */
                            else {
                                showToast("Account creation failed: " + exception.getMessage());
                            }
                        }
                    }
                });
    }

    /**
     * Displays a toast message
     * @param message is the message to display
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
