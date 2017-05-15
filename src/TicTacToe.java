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

// TODO Create Minimax Algorithm, Create menu screen where players can choose to play each other or a bot, and difficulty can be chosen

public class TicTacToe extends Applet implements MouseListener{
    
    private int size = 3; // Board will be size x size
    private int board[][] = new int[size][size]; // Store an int on what is in the square, -1 = O, 0 = blank, 1 = X
    private final int LENGTH = 600; // Size of the window (600 x 600)
    private boolean isPvp = false; // Whether or not game is player vs player
    private int player = 1; // Which player has their move (X starts)
    private int difficulty = 2; // Difficulty of bot. 1 = Easy, 2 = Medium, 3 = Impossible
    
    public void init() {
        addMouseListener(this); // Create mouse listener
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = 0; // Set all board spaces as 0
            }
        }
    }
    
    public void paint(Graphics g) {
        
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
    }
    
    private int getWinner() {
        
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
    
    private void botMove(int difficulty) {
    
        Random random = new Random();

        if (difficulty == 1) { // Easiest difficulty, random
            
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            
            // Randomize until available space is found
            // Make sure a move can be made. Will be caught in infinite loop if not checked
            while (availableSpot() && board[x][y] != 0) {
                x = random.nextInt(size);
                y = random.nextInt(size);
            }
            
            board[x][y] = -1;
            
        }
        
        else if (difficulty == 2) { // Medium difficulty
    
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
            // If bot cannot win or block a win from a move, choose random spot
            botMove(1);
            
        }else { // Impossible difficulty, uses minimax algorithm
            
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
        
        // Calculate which squares are clicked from the coordinates
        // e.g. x = 100, size = 3, 100 / (600 / 3) = 0
        int moveX = x / (LENGTH / size);
        int moveY = y / (LENGTH / size);
        
        // If the game is two player, swap between X and O every turn
        if (board[moveX][moveY] == 0 && getWinner() == 0 && isPvp) {
            board[moveX][moveY] = player;
            player *= -1;
        }
        // If the game is single player call the botMove procedure after every turn
        else if (board[moveX][moveY] == 0 && getWinner() == 0) {
            board[moveX][moveY] = 1;
            botMove(difficulty);
        }
        
        // Draw the updated board
        repaint();
    }
    
    //<editor-fold desc="Unused mouse functions">
    public void mousePressed (MouseEvent e) {
        
    }
    
    public void mouseReleased (MouseEvent e) {
        
    }
    
    public void mouseEntered (MouseEvent e) {
        
    }
    
    public void mouseExited (MouseEvent e) {
        
    }
    //</editor-fold>
}
