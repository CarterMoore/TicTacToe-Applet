import java.applet.Applet;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

/**
 * TicTacToe
 * Created by Carter Moore
 * ICS3U final project
 */

// TODO Create Minimax Algorithm

public class TicTacToe extends Applet implements MouseListener {

    private int size; // Board will be size x size
    private int board[][]; // Store an int on what is in the square, -1 = O, 0 = blank, 1 = X
    private int weight[][];
    private final int LENGTH = 600; // Size of the window (600 x 600)
    private boolean isPvp; // Whether or not game is player vs player
    private int player;

    // Different difficulties
    private enum Difficulty {
        EASY,
        HARD,
        IMPOSSIBLE
    }

    private Difficulty difficulty; // Create variable for difficulty

    // Different states of the game
    private enum GameState {
        MENU,
        SIZE_SELECT,
        DIFFICULTY_SELECT,
        PLAYING,
        WINNER_SCREEN
    }

    // Start at the menu
    private GameState state = GameState.MENU;

    public void init() {
        Random random = new Random();

        player = random.nextInt(2) == 0 ? 1 : -1;

        setSize(LENGTH, LENGTH);
        setBackground(Color.WHITE); // Make the background white
        addMouseListener(this); // Create mouse listener
    }
    
    public void paint(Graphics g) {
        // Create fonts
        Font font = new Font("Arial", Font.BOLD, 30);
        Font font2 = new Font("Arial", Font.BOLD, 70);

        switch (state) {
            case MENU:
                // Draw boxes where single player or two player can be selected
                drawCenteredString(g, font, "Tic Tac Toe - Carter Moore", 30);

                g.drawRect(60, 150, 480, 100);
                drawCenteredString(g, font, "Single Player", 200);

                g.drawRect(60, 300, 480, 100);
                drawCenteredString(g, font, "Two Player", 350);

                break;

            case DIFFICULTY_SELECT:
                // Select the difficulty of AI
                drawCenteredString(g, font, "Select Difficulty", 30);

                g.drawRect(60, 150, 480, 100);
                drawCenteredString(g, font, "Easy", 200);

                g.drawRect(60, 250, 480, 100);
                drawCenteredString(g, font, "Hard", 300);

                g.drawRect(60, 350, 480, 100);
                drawCenteredString(g, font, "Impossible", 400);

                break;

            case SIZE_SELECT:
                // Select the size of the board

                drawCenteredString(g, font, "Select Size", 30);

                g.drawRect(200, 70, 200, 100);
                drawCenteredString(g, font, "3x3", 120);

                g.drawRect(200, 170, 200, 100);
                drawCenteredString(g, font, "4x4", 220);

                g.drawRect(200, 270, 200, 100);
                drawCenteredString(g, font, "5x5", 320);

                g.drawRect(200, 370, 200, 100);
                drawCenteredString(g, font, "6x6", 420);

                break;

            case PLAYING:

                // Draw the board and play game
                for (int i = 1; i < size; i++) { // Draw lines depending on given size
                    g.fillRect(i * (LENGTH / size) - 2, 0, 4, LENGTH);
                    g.fillRect(0, i * (LENGTH / size) - 2, LENGTH, 4);
                }

                Image xImage = getImage(getClass().getResource("X.png")); // Get X image
                Image oImage = getImage(getClass().getResource("O.png")); // Get O image

                // Loop though and draw X or O depending on state of board
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (board[i][j] == 1)
                            g.drawImage(xImage, i * (LENGTH / size), j * (LENGTH / size), this);
                        else if (board[i][j] == -1)
                            g.drawImage(oImage, i * (LENGTH / size), j * (LENGTH / size), this);
                        }
                    }

                // Draw then wait a second to display winner
                if (getWinner() != 0 || !availableSpot()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    state = GameState.WINNER_SCREEN;
                    repaint();
                }

                break;

            case WINNER_SCREEN:

                int winner = getWinner(); // Create winner var so method only needs to be called once

                // Set the screen to white
                g.setColor(Color.WHITE);
                g.drawRect(0, 0, LENGTH, LENGTH);
                // Make text blue
                g.setColor(Color.BLUE);
                g.setFont(font2);
                if (winner == 0)  { // Tie
                    drawCenteredString(g, font2, "Tie!", 200);
                }else if (isPvp && winner == 1) { // Player 1 wns in pvp
                    drawCenteredString(g, font2, "Player 1 wins!", 200);
                }else if (isPvp && winner == -1) { // Player 2 wins in pvp
                    drawCenteredString(g, font2, "Player 2 wins", 200);
                }else if (winner == 1) { // Player wins
                    drawCenteredString(g, font2, "You win!", 200);
                }else if (winner == -1) { // AI wins
                    drawCenteredString(g, font2, "You Lose!", 200);
                }

                // Draw play again button
                g.setColor(Color.BLACK);
                g.drawRect(60, 400, 480, 100);
                drawCenteredString(g, font, "Play again", 450);

                break;
        }


    }

    private void drawCenteredString(Graphics g, Font font, String s, int y) {
        // Draw a string that is horizontally centered
        FontMetrics metrics = g.getFontMetrics(font);

        int x = (LENGTH - metrics.stringWidth(s)) / 2;

        g.setFont(font);

        g.drawString(s, x, y);
    }

    private int getWinner() {
        // Determine who has won
        int winner = 0;

        // Set all win possibilities as true and set to false if no win
        boolean diagonalW1 = true, diagonalW2 = true;
            for (int i = 0; i < size; i++) {
            boolean horizontalW = true, verticalW = true;
                for (int j = 1; j < size; j++) {
                if (board[i][0] != board[i][j])  // Check if win horizontal win
                    horizontalW = false;
                
                if (board[0][i] != board[j][i])  // Check if vertical win
                    verticalW = false;
            }
            
            if (horizontalW && board[i][0] != 0) // Make sure a player won
                winner = board[i][0];
            
            if (verticalW && board[0][i] != 0)  // Make sure a player won
                winner = board[0][i];
            
            if (board[0][0] != board[i][i]) // Check for diagonal win
                diagonalW1 = false;
                    if (board[0][size-1] != board[i][size-i-1]) // Check for diagonal win
                diagonalW2 = false;
        }
        
        if (diagonalW1 && board[0][0] != 0) // Make sure a player won
            winner = board[0][0];
        if (diagonalW2 && board[0][size-1] != 0) // Make sure a player won
            winner = board[0][size-1];

        return winner; // Return who won, -1 = O, 0 = nobody, 1 = X
    }
    
    private void botMove(Difficulty difficulty) {
    
        Random random = new Random();

        switch (difficulty) {
            case EASY:
                // Randomize where bot goes
                int x = random.nextInt(size);
                int y = random.nextInt(size);

                // Randomize until available space is found
                // Make sure a move can be made. Will be caught in infinite loop if not checked
                while (availableSpot() && board[x][y] != 0) {
                    x = random.nextInt(size);
                    y = random.nextInt(size);
                }
                board[x][y] = -1;
                break;

            case HARD:
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (board[i][j] == 0) {
                            int temp = board[i][j]; // Create a temporary variable to store state of space
                            board[i][j] = -1; // Simulate if moving in that spot will result in a win
                            if (getWinner() == -1) // If so, move there
                                return;
                            else
                                board[i][j] = temp; // Put space back to original value
                        }
                    }
                }

                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (board[i][j] == 0) {
                            int temp = board[i][j]; // Create a temporary variable to store state of space
                            board[i][j] = 1; // Simulate if player moving in that spot will result in a loss
                            if (getWinner() == 1) { // If so, move there
                                board[i][j] = -1;
                                return;
                            }else
                                board[i][j] = temp; // Put space back to original value
                        }
                    }
                }

                int moveX = 0;
                int moveY = 0;
                int w = 0;

                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (weight[i][j] > w && board[i][j] == 0) {
                            w = weight[i][j];
                            moveX = i;
                            moveY = j;
                        }
                    }
                }
                if (board[moveX][moveY] == 0)
                    board[moveX][moveY] = -1;
                break;

        }
    }

    private void initPriority() {

        // Corners
        weight[0][0] = 2;
        weight[0][size - 1] = 2;
        weight[size - 1][0] = 2;
        weight[size - 1][size - 1] = 2;

        // Edges
        for(int i = 1; i < size-1; i++) {
            weight[i][0] = 1;
            weight[0][i] = 1;
            weight[size - 1][i] = 1;
            weight[i][size - 1] = 1;
        }

        // Center
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (weight[i][j] == 0)
                    weight[i][j] = 3;
            }
        }
    }

    private void initBoard() {
        // Create the board with size given by user
        board = new int[size][size];
        weight = new int[size][size];
        initPriority();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = 0;
            }
        }

    }
    
    private boolean availableSpot () {
        // Check if there are any moves left to make
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0)
                    return true;
            }
        }
        return false;
    }
    
    public void mouseClicked (MouseEvent e) {
        // Get x and y coordinates of click
        int x = e.getX();
        int y = e.getY();

        switch (state) {

            case MENU:
                // Find if single player or two player has been clicked
                if (x >= 60 && x <= 540 && y >= 150 && y <= 250) {
                    isPvp = false;
                    state = GameState.DIFFICULTY_SELECT;
                    repaint();
                }else if (x >= 60 && x <= 540 && y >= 300 && y <= 400) {
                    isPvp = true;
                    state = GameState.SIZE_SELECT;
                    repaint();
                }

                break;

            case DIFFICULTY_SELECT:
                // Find difficulty selected
                if (x >= 60 && x <= 540 && y >= 150 && y <= 250) {
                    difficulty = Difficulty.EASY;
                    state = GameState.SIZE_SELECT;
                    repaint();
                }else if (x >= 60 && x <= 540 && y >= 250 && y <= 350) {
                    difficulty = Difficulty.HARD;
                    state = GameState.SIZE_SELECT;
                    repaint();
                }

                break;

            case SIZE_SELECT:
                // Find board size selected
                if (x >= 200 && x <= 400 && y >= 70 && y <= 170) {
                    size = 3;
                }else if (x >= 200 && x <= 400 && y >= 170 && y <= 270) {
                    size = 4;
                }else if (x >= 200 && x <= 400 && y >= 270 && y <= 370) {
                    size = 5;
                }else if (x >= 200 && x <= 400 && y >= 370 && y <= 470) {
                    size = 6;
                }

                initBoard();
                state = GameState.PLAYING;
                repaint();
                break;

            case PLAYING:
                // Calculate which squares are clicked from the coordinates
                // e.g. x = 100, size = 3, (int) (100 / (600 / 3)) = 0
                int moveX = x / (LENGTH / size);
                int moveY = y / (LENGTH / size);

                // If the game is two player, swap between X and O every turn
                if (board[moveX][moveY] == 0 && getWinner() == 0 && isPvp) {
                    board[moveX][moveY] = player;
                    player *= -1; // Player will be 1(x) or -1(o)
                }
                // If the game is single player call the botMove procedure after every turn
                else if (board[moveX][moveY] == 0 && getWinner() == 0) {
                    board[moveX][moveY] = 1;
                    if (getWinner() == 0) { // Make sure there is still no winner after move is
                        botMove(difficulty);
                    }
                }


                // Draw the updated board
                repaint();

                break;

            case WINNER_SCREEN:
                if (x >= 60 && x <= 540 && y >= 400 && y <= 500) {
                    state = GameState.MENU;
                    repaint();
                }
        }
    }
    
    public void mousePressed (MouseEvent e) {
        
    }
    
    public void mouseReleased (MouseEvent e) {
        
    }
    
    public void mouseEntered (MouseEvent e) {
        
    }
    
    public void mouseExited (MouseEvent e) {
        
    }
}
