package com.example.myapplication;

import android.content.Context; // Provides access to the application context
import android.content.Intent; // Allows the creation of intents

import android.graphics.Canvas; // Provides classes and methods for drawing graphics on a canvas
import android.graphics.Color; // ontains constants for defining colors in Android
import android.graphics.Paint; // Provides classes and methods for drawing shapes, text, etc.
import android.util.AttributeSet; // use of attributes in XML files to customize views
import android.view.MotionEvent; // rovides classes and constants to handle touch events
import android.view.View; // base class for all views


import androidx.annotation.Nullable; // Provides annotations that can be used to indicate that a parameter

import java.util.ArrayList; // Provides the ArrayList class for creating dynamic arrays
import java.util.LinkedList; // creating linked lists
import java.util.Queue; // Represents a queue data structure
import java.util.Random; //Provides a random number generator,
import java.util.Stack; // Represents a stack data structure,

/**
 * @author Vanessa and Noor
 *
 * The MazeView class represents the view for the maze game.
 * It handles drawing the maze, player, enemy, and other game elements,
 * as well as user input for controlling the player.
 */

public class Maze extends View {

    /**
     * Enum for directions
     */
    private enum Direction{
        UP, DOWN, LEFT, RIGHT
    }

    /**
     * 2D array representing the maze cells
     */
    private Cell[][] cells;
    /**
     * Cell representing player and exit
     */
    private Cell player, exit;
    /**
     * Number of columns and rows in the maze
     */
    private static final int COLS = 15, ROWS = 15;
    private static final float WALL_THICKNESS = 10;
    /**
     * Size of each cell in pixels
     * Horizontal margin for centering the maze
     * Vertical margin for centering the maze
     */
    private float cellSize, hMargin, vMargin;
    /**
     * Paint object for walls, player, maze exit
     */
    private Paint wallPaint, playerPaint, exitPaint; // last 2 = part 5
    /**
     * Random object for generating random numbers
     */
    private Random random;
    /**
     * Cell representing the enemy's position
     */
    private Cell enemy;
    /**
     * Paint object for enemy
     */
    private Paint enemyPaint;
    /**
     * Time when the enemy last moved
     */
    private long lastMoveTime = 0;
    /**
     * Delay between enemy moves
     */
    private long moveDelay = 400;

    /**
     * Time when the game started (ms)
     */
    private long startTime = 0;
    /**
     * Delay before the enemy starts moving
     */
    private long startDelay = 0;
    /**
     * player starts with 3 lives
     */
    private int playerLives = 3;
    /**
     * Number of mazes played
     */
    private int mazesPlayed = 0;
    /**
     * Player's score
     */
    private int score = 0;

    /**
     * Constructor for Maze class.
     *
     * @param context The context of the view.
     * @param attrs   The attribute set.
     */
    public Maze(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        /**
         * Initialize a new paint for the walls
         */
        wallPaint = new Paint();
        wallPaint.setColor(Color.rgb(21,66,57));
        wallPaint.setStrokeWidth(WALL_THICKNESS);

        /**
         * Initialize a new paint for the player (squirrel)
         */
        playerPaint = new Paint();
        playerPaint.setColor(Color.rgb(139, 69, 19));
        exitPaint = new Paint();
        exitPaint.setColor(Color.rgb(151, 192, 133));

        /**
         * Randomly generated maze
         */
        random = new Random();
        createMaze();
    }

    /**
     * Returns a neighbor cell of the given cell that has not been visited yet.
     *
     * @param cell The cell for which to find a neighbor.
     * @return A neighbor cell that has not been visited, or null if all neighbors have been visited.
     */
    private Cell getNeighbor(Cell cell){
        ArrayList<Cell> neighbors = new ArrayList<>();


        //left neighbor
        if(cell.col > 0){
            if(!cells[cell.col-1][cell.row].visited)
                neighbors.add(cells[cell.col-1][cell.row]);
        }


        //right neighbor
        if(cell.col < COLS-1){
            if(!cells[cell.col+1][cell.row].visited)
                neighbors.add(cells[cell.col+1][cell.row]);
        }


        //top neighbor
        if(cell.row > 0){
            if(!cells[cell.col][cell.row-1].visited)
                neighbors.add(cells[cell.col][cell.row-1]);
        }


        //bottom neighbor
        if(cell.row < ROWS-1){
            if(!cells[cell.col][cell.row+1].visited)
                neighbors.add(cells[cell.col][cell.row+1]);
        }

        /**
         * Check if there are unvisited neighbors
         */
        if(neighbors.size() > 0) {
            /**
             * Choose a random unvisited neighbor
             */
            int index = random.nextInt(neighbors.size());
            return neighbors.get(index);
        }


        return null;
    }

    /**
     * Removes the wall between the current cell and the next cell.
     *
     * @param current The current cell.
     * @param next    The next cell.
     */
    private void removeWall(Cell current, Cell next){
        // Remove top wall of current cell and bottom wall of next cell
        if(current.col == next.col && current.row == next.row+1){
            current.topWall = false;
            next.bottomWall = false;
        }

        // Remove bottom wall of current cell and top wall of next cell
        if(current.col == next.col && current.row == next.row-1){
            current.bottomWall = false;
            next.topWall = false;
        }

        // Remove left wall of current cell and right wall of next cell
        if(current.col == next.col+1 && current.row == next.row){
            current.leftWall = false;
            next.rightWall = false;
        }

        // Remove right wall of current cell and left wall of next cell
        if(current.col == next.col-1 && current.row == next.row){
            current.rightWall = false;
            next.leftWall = false;
        }
    }

    /**
     * Creates the maze using a depth-first search algorithm.
     * If the player reaches the exit, the score is incremented by 1000 and a new maze is created.
     * If the player completes 3 mazes, the game ends and goes to the LeaderboardActivity.
     */
    private void createMaze() {
        /**
         * Game no longer in progress, go to Leaderboard screen
         */
        if (mazesPlayed == 3) {
            Intent intent = new Intent(Maze.this.getContext(), LeaderboardActivity.class);
            getContext().startActivity(intent);
        }

        /**
         * Creates a new stack to store cells and initializes the current and next cells.
         */
        Stack<Cell> stack = new Stack<>();
        Cell current, next;

        /**
         * Initialize cells
         */
        cells = new Cell[COLS][ROWS];
        for(int x=0; x<COLS; x++){
            for(int y=0; y<ROWS; y++){
                cells[x][y] = new Cell(x, y);
            }
        }


        /**
         * Initialize player and exit positions
         */
        player = cells[1][14];
        exit = cells[COLS-1][ROWS-1];


        /**
         * Initialize enemy at the starting position
         */
        enemy = cells[0][0];
        enemyPaint = new Paint();
        /**
         * Orange color for enemy (slime)
         */
        enemyPaint.setColor(Color.rgb(255, 165, 0));

        /**
         * Create maze
         */
        current = cells[0][0];
        current.visited = true;


        do {
            next = getNeighbor(current);
            if (next != null) {
                removeWall(current, next);
                stack.push(current);
                current = next;
                current.visited = true;
            }
            else
                current = stack.pop();
        }while(!stack.empty());

    }

    /**
     * Draws the maze on the canvas.
     *
     * @param canvas The canvas on which to draw the maze.
     */
    @Override
    protected void onDraw(Canvas canvas) {

        int width = getWidth();
        int height = getHeight();

        /**
         * Draw maze walls
         */
        canvas.drawColor(Color.rgb(151, 192, 133));;

        /**
         * Calculate cell size based on view dimensions
         */
        if(width/height < COLS/ROWS)
        {
            cellSize = width/(COLS+1);
        }
        else{
            cellSize = height/(ROWS+1);
        }

        /**
         * Translates the canvas by the horizontal and vertical margins to center the maze.
         */
        hMargin = (width-COLS*cellSize)/2;
        vMargin = (height-ROWS*cellSize)/2;
        canvas.translate(hMargin, vMargin);

        /**
         * Draws each wall with wall paint
         */
        for(int x=0; x<COLS; x++){
            for(int y=0; y<ROWS; y++){
                if(cells[x][y].topWall)
                    canvas.drawLine(x*cellSize, y*cellSize, (x+1)*cellSize, y*cellSize, wallPaint
                    );


                if(cells[x][y].leftWall)
                    canvas.drawLine(x*cellSize, y*cellSize, x*cellSize, (y+1)*cellSize, wallPaint
                    );


                if(cells[x][y].bottomWall)
                    canvas.drawLine(x*cellSize, (y+1)*cellSize, (x+1)*cellSize, (y+1)*cellSize, wallPaint
                    );


                if(cells[x][y].rightWall)
                    canvas.drawLine((x+1)*cellSize, y*cellSize, (x+1)*cellSize, (y+1)*cellSize, wallPaint
                    );
            }
        }
        /**
         * Moves the enemy towards the player's position.
         * If the player is in a neighboring cell, the enemy moves directly towards the player.
         * If the player is not in a neighboring cell, the enemy moves closer to the player's last known position.
         */
        moveEnemyTowardsPlayer();

        /**
         * Draws the player(squirrel)
         */
        float margin = cellSize/10;
        canvas.drawRect((player.col)*cellSize+margin, (player.row)*cellSize+margin, (player.col+1)*cellSize-margin, (player.row+1)*cellSize-margin, playerPaint);

        /**
         * Draws the exit(acorn)
         */
        canvas.drawRect((exit.col)*cellSize+margin, (exit.row)*cellSize+margin, (exit.col+1)*cellSize-margin, (exit.row+1)*cellSize-margin, exitPaint);

        /**
         * Draws the enemy (slime)
         */
        float enemyCircleRadius = cellSize / 3; // Adjust the size as needed
        canvas.drawCircle((enemy.col + 0.5f) * cellSize, (enemy.row + 0.5f) * cellSize, enemyCircleRadius, enemyPaint);

        /**
         * Draw player lives
         */
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(cellSize / 2+10);

        /**
         * Draw player score
         */
        Paint scoreTextPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(cellSize / 2 + 10);

        /**
         * Display the score
         */
        String scoreText = "Score: " + score;
        float scoreTextWidth = textPaint.measureText(scoreText);
        canvas.drawText(scoreText, getWidth() - scoreTextWidth - hMargin-50, vMargin - scoreTextPaint.getTextSize()+50, textPaint);

        /**
         * Display the lives
         */
        String livesText = "Lives: " + playerLives;
        float textWidth = textPaint.measureText(livesText);
        canvas.drawText(livesText, getWidth() - textWidth - hMargin-50, vMargin - textPaint.getTextSize()+150, textPaint);

    }

    /**
     * Moves the player in the specified direction if there is no wall.
     *
     * @param direction The direction in which to move the player.
     */
    private void movePlayer(Direction direction){
        switch (direction){
            case UP:
                if(!player.topWall)
                    player = cells[player.col][player.row-1];
                break;
            case DOWN:
                if(!player.bottomWall)
                    player = cells[player.col][player.row+1];
                break;
            case LEFT:
                if(!player.leftWall)
                    player = cells[player.col-1][player.row];
                break;
            case RIGHT:
                if(!player.rightWall)
                    player = cells[player.col+1][player.row];
        }

        /**
         * Check if player is at the exit
         *
         * Redraw
         */
        checkExit();
        invalidate();
    }


    /**
     * Checks if the player has reached the exit.
     * If the player reaches the exit, the score is incremented by 1000 and a new maze is created.
     */
    public void checkExit(){
        if (player == exit) {
            score += 1000;
            mazesPlayed++;
            createMaze();
        }
    }

    /**
     * Handles touch events on the maze view.
     *
     * @param event The MotionEvent representing the touch event.
     * @return True if the touch event is handled, false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        /**
         * Move the enemy towards the player's trail
         */
        followPlayerTrail();
        if(event.getAction() == MotionEvent.ACTION_DOWN)
            return true;

        /**
         * Check for touch down event
         */
        if(event.getAction() == MotionEvent.ACTION_MOVE)
        {
            /**
             * Handle touch move event
             */
            float x = event.getX();
            float y = event.getY();

            /**
             * Calculates the center coordinates of the player's cell and computes the distance between
             * the touch event coordinates and the player's center.
             */
            float playerCenterX = hMargin + (player.col+0.5f)*cellSize;
            float playerCenterY = vMargin + (player.row+0.5f)*cellSize;


            float dx = x - playerCenterX;
            float dy = y - playerCenterY;


            float absDx = Math.abs(dx);
            float absDy = Math.abs(dy);


            if(absDx > cellSize || absDy > cellSize){
                if(absDx > absDy){
                    /**
                     * Move in x-direction
                     */
                    if(dx > 0)
                    /**
                     * move to the right
                     */
                        movePlayer(Direction.RIGHT);
                    else
                    /**
                     * move to the left
                     */
                        movePlayer(Direction.LEFT);


                }
                else{
                    /**
                     * move in y-direction
                     */
                    if(dy > 0)
                    /**
                     * move down
                     */
                        movePlayer(Direction.DOWN);
                    else
                    /**
                     * move up
                     */
                        movePlayer(Direction.UP);
                }
            }
            return true;
        }

        return super.onTouchEvent(event);
    }

    /**
     * Represents a cell in the maze.
     */
    private class Cell{
        /**
         * All walls initialized to true
         */
        boolean
                topWall = true,
                leftWall = true,
                bottomWall = true,
                rightWall = true,
                visited = false;
        /**
         * column and row index of cell
         */
        int col, row;

        /**
         * Constructs a new Cell object with the specified column and row.
         *
         * @param col The column index of the cell.
         * @param row The row index of the cell.
         */
        public Cell(int col, int row) {
            this.col = col;
            this.row = row;
        }
    }

    /**
     * Moves the enemy towards the player's position.
     * If the enemy reaches the player, the player loses a life and a new maze is created.
     * If the player loses all lives, the game ends.
     */
    private void moveEnemyTowardsPlayer() {
        long currentTime = System.currentTimeMillis();

        if (startTime == 0) {
            startTime = currentTime;
        }

        /**
         * Delayed start, do not move yet
         */
        if (currentTime - startTime < startDelay) {
            return; // Delayed start, do not move yet
        }

        /**
         * Delay between moves, do not move yet
         */
        if (currentTime - lastMoveTime < moveDelay) {
            return;
        }

        lastMoveTime = currentTime;


        /**
         * Move the enemy towards the player, ignoring walls
         */
        if (player.col < enemy.col) {
            enemy = cells[enemy.col - 1][enemy.row]; // Move left
        } else if (player.col > enemy.col) {
            enemy = cells[enemy.col + 1][enemy.row]; // Move right
        } else if (player.row < enemy.row) {
            enemy = cells[enemy.col][enemy.row - 1]; // Move up
        } else if (player.row > enemy.row) {
            enemy = cells[enemy.col][enemy.row + 1]; // Move down
        }

        /**
         * Redraw the view
         */
        invalidate();
    }

    /**
     * Moves the enemy towards the player's position by following the shortest path.
     * If the enemy catches the player, the player loses a life and a new maze is created.
     */
    private void followPlayerTrail() {
        /**
         * Create a map of distances from the enemy to each cell
         */
        int[][] distanceMap = new int[COLS][ROWS];
        for (int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {
                distanceMap[x][y] = Integer.MAX_VALUE;
            }
        }

        /**
         * Queue for Dijkstra's algorithm
         */
        Queue<Cell> queue = new LinkedList<>();
        queue.add(enemy);
        distanceMap[enemy.col][enemy.row] = 0;


        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            int distance = distanceMap[current.col][current.row] + 1;


            if (current == player) {
                /**
                 * Found the player, backtrack to find the shortest path
                 */
                while (current != enemy) {
                    Cell next = current;
                    if (current.col > 0 && distanceMap[current.col - 1][current.row] == distance - 1) {
                        next = cells[current.col - 1][current.row];
                    } else if (current.col < COLS - 1 && distanceMap[current.col + 1][current.row] == distance - 1) {
                        next = cells[current.col + 1][current.row];
                    } else if (current.row > 0 && distanceMap[current.col][current.row - 1] == distance - 1) {
                        next = cells[current.col][current.row - 1];
                    } else if (current.row < ROWS - 1 && distanceMap[current.col][current.row + 1] == distance - 1) {
                        next = cells[current.col][current.row + 1];
                    }
                    /**
                     * Move the enemy along the shortest path
                     */
                    current = next;
                    distance--;
                }
                /**
                 * Move the enemy along the shortest path
                 */
                enemy = current;
                break;
            }


            /**
             * Check neighborss
             */
            if (current.col > 0 && !current.leftWall && distance < distanceMap[current.col - 1][current.row]) {
                distanceMap[current.col - 1][current.row] = distance;
                queue.add(cells[current.col - 1][current.row]);
            }
            if (current.col < COLS - 1 && !current.rightWall && distance < distanceMap[current.col + 1][current.row]) {
                distanceMap[current.col + 1][current.row] = distance;
                queue.add(cells[current.col + 1][current.row]);
            }
            if (current.row > 0 && !current.topWall && distance < distanceMap[current.col][current.row - 1]) {
                distanceMap[current.col][current.row - 1] = distance;
                queue.add(cells[current.col][current.row - 1]);
            }
            if (current.row < ROWS - 1 && !current.bottomWall && distance < distanceMap[current.col][current.row + 1]) {
                distanceMap[current.col][current.row + 1] = distance;
                queue.add(cells[current.col][current.row + 1]);
            }
        }

        /**
         * Check if enemy caught the player
         */
        if (player == enemy) {
            playerLives--; // Decrement player lives
            mazesPlayed++; // Increment mazes played

            /**
             * If player lost all their lives, show leaderboard
             */
            if(playerLives == 3)
            {
                Intent intent = new Intent(Maze.this.getContext(), LeaderboardActivity.class);
                getContext().startActivity(intent);
            }
            else{
                /**
                 * Create the maze
                 *
                 * Redraw
                 */
                createMaze();
                invalidate();
            }

        }
        /**
         * Redraw the view
         */
        invalidate();
    }


}

