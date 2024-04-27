package com.example.myapplication;

import android.content.Context;  // Provides access to application-specific resources and classes
import android.graphics.Canvas;  // Used for drawing graphics on Android
import android.graphics.Color;   // Represents colors in Android
import android.graphics.Paint;   // Used for drawing on Canvas in Android
import android.util.AttributeSet; // Contains attribute values for initializing views
import android.view.MotionEvent; // Represents motion events like touch gestures

import org.junit.Before;          // Used for setup tasks before each test method
import org.junit.Test;            // Marks a method as a test method
import org.junit.runner.RunWith;  // Allows specifying a custom test runner
import org.mockito.Mock;          // Creates a mock object
import org.mockito.MockitoAnnotations; // Initializes mocks based on annotations
import org.mockito.runners.MockitoJUnitRunner; // Deprecated in Mockito 2.1.0

import static org.mockito.Mockito.*; // Allows using static Mockito methods without class prefix

/**
 * @author Vanessa and Noor
 *
 * Test class for Maze class.
 */

@RunWith(MockitoJUnitRunner.class)
public class MazeTest {
    /**
     * Mocked Context for simulating Android application context.
     */
    @Mock
    Context mockContext;
    /**
     * Mocked AttributeSet for simulating view attribute set.
     */
    @Mock
    AttributeSet mockAttributeSet;
    /**
     * Mocked Canvas for simulating drawing operations.
     */
    @Mock
    Canvas mockCanvas;
    /**
     * Mocked MotionEvent for simulating touch events.
     */
    @Mock
    MotionEvent mockMotionEvent;

    /**
     * Set up method to initialize Mockito annotations for each test method.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test the initialization of the Maze view.
     */
    @Test
    public void testMazeViewInitialization() {
        Maze mazeView = new Maze(mockContext, mockAttributeSet);

    }

    /**
     * Test the onDraw method of the Maze view.
     */
    @Test
    public void testOnDraw() {
        Maze mazeView = new Maze(mockContext, mockAttributeSet);
        mazeView.onDraw(mockCanvas);
        /**
         * Assert drawing logic drawRect, drawLine, and drawCircle
         *
         *  with any float values for left, top, right, bottom, and any Paint object
         *
         *  with any float values for startX, startY, stopX, stopY, and any Paint object
         *
         *  with any float values for centerX, centerY, radius, and any Paint object
         */
        verify(mockCanvas).drawRect(anyFloat(), anyFloat(), anyFloat(), anyFloat(), any(Paint.class));
        verify(mockCanvas).drawLine(anyFloat(), anyFloat(), anyFloat(), anyFloat(), any(Paint.class));
        verify(mockCanvas).drawCircle(anyFloat(), anyFloat(), anyFloat(), any(Paint.class));
        /**
         * Verify background color
         */
        verify(mockCanvas).drawColor(Color.rgb(151, 192, 133));

    }

    /**
     * Test the onTouchEvent method of the Maze view.
     */
    @Test
    public void testOnTouchEvent() {
        /**
         * Create a new instance of the Maze view with mocked context and attribute set
         */
        Maze mazeView = new Maze(mockContext, mockAttributeSet);
        /**
         * Mock a MotionEvent with the action set to ACTION_DOWN
         */
        when(mockMotionEvent.getAction()).thenReturn(MotionEvent.ACTION_DOWN);
        /**
         * Simulate a touch event on the Maze view using the mocked MotionEvent
         */
        mazeView.onTouchEvent(mockMotionEvent);

    }
}