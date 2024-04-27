package com.example.myapplication;

// necessary classes for ImageButton
import android.widget.ImageButton;

// annotations and classes for JUnit
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

// Mockito annotations and classes for mocking
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

// assertion methods for testing
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;

/**
 * @author Vanessa
 *
 * Test class for MainActivity class.
 */

/**
 * Use the MockitoJUnitRunner to run the test
 */
@RunWith(MockitoJUnitRunner.class)
public class MainActivityTest {

    /**
     * Mock ImageButtons
     */
    @Mock
    private ImageButton mockLoginButton;
    @Mock
    private ImageButton mockCreateAccountButton;

    /**
     * Reference to the MainActivity instance being tested
     */

    private MainActivity mainActivity;

    /**
     * Setup method to initialize mocks and MainActivity.
     */
    @Before
    public void setUp() {
        /**
         * Create a spy of MainActivity
         */
        mainActivity = Mockito.spy(new MainActivity());

        /**
         * Set the mocked ImageButtons in MainActivity
         */
        mainActivity.loginButton = mockLoginButton;
        mainActivity.createAccountButton = mockCreateAccountButton;

        /**
         * Mock the behavior of setOnClickListener method for ImageButtons
         */
        doNothing().when(mockLoginButton).setOnClickListener(Mockito.any());
        doNothing().when(mockCreateAccountButton).setOnClickListener(Mockito.any());
    }

    /**
     * Test the onCreate method to ensure that views are initialized correctly
     */
    @Test
    public void testOnCreate() {
        /**
         * Call onCreate method
         */
        mainActivity.onCreate(null);

        /**
         * Verify that the ImageButtons are initialized
         */
        assertNotNull("Login button is null", mainActivity.loginButton);
        assertNotNull("Create account button is null", mainActivity.createAccountButton);

    }
}