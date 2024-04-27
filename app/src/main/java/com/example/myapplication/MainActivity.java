package com.example.myapplication;

import android.content.Intent; // communicate between components of an Android application
import android.os.Bundle; // used for passing data between activities
import android.view.View; // represents a basic building block for user interface components
import android.widget.ImageButton; // represents a button with an image that can be clicked

import androidx.appcompat.app.AppCompatActivity; // base class for activities that use the support library action bar features

/**
 * @author Vanessa
 *
 * MainActivity is a class used to display the home screen
 * where player can Login or Create Account.
 *
 * MainActivity is a subclass of AppCompatActivity
 */

public class MainActivity extends AppCompatActivity {
    /**
     * ImageButton used for navigating to Create Account screen
     */
    ImageButton createAccountButton;
    /**
     * ImageButton used for navigating to Login screen
     */
    ImageButton loginButton;

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
         * In this case home_screen.xml
         */
        setContentView(R.layout.home_screen);

        /**
         * Find views by their IDs
         */
        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);

        /**
         * Set click listener for the login button
         */
        loginButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Click event
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                /**
                 * Navigates to LoginActivity
                 */
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close this activity
            }
        });

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
                 * Navigates to CreateAccountActivity
                 */
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intent);
                finish(); // Close this activity
            }
        });

    }
}
