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

// TODO Create Minimax Algorithm, Create screen at the end showing who won and play again option

public class TicTacToe extends Applet implements MouseListener {
    
    private int size; // Board will be size x size
    private int board[][]; // Store an int on what is in the square, -1 = O, 0 = blank, 1 = X
    private final int LENGTH = 600; // Size of the window (600 x 600)
    private boolean isPvp; // Whether or not game is player vs player
    private int player = 1; // Which player has their move (X starts)
    private int difficulty = 2; // Difficulty of bot. 1 = Easy, 2 = Medium, 3 = Impossible
    private int gameState = 0; // 0 is title screen, 1 is difficulty selection, 2 is size selection, 3 is playing, 4 is winner screen


    public void init() {
        setSize(LENGTH, LENGTH);
        addMouseListener(this); // Create mouse listener
    }
    
    public void paint(Graphics g) {

        Font font = new Font("Arial", Font.BOLD, 30);

        if (gameState == 0) {
            // Draw boxes where single player or two player can be selected
            g.setFont(font);
    
            g.drawString("Tic Tac Toe - Carter Moore", 100, 30);
    
            g.drawRect(60, 150, 480, 100);
            g.drawString("Single Player", 200, 200);
    
            g.drawRect(60, 300, 480, 100);
            g.drawString("Two Player", 225, 350);

        }else if (gameState == 1) {
            // Select the difficulty of AI
            g.setFont(font);
    
            g.drawString("Select Difficulty", 180, 30);
    
            g.drawRect(60, 150, 480, 100);
            g.drawString("Easy", 275, 200);
    
            g.drawRect(60, 250, 480, 100);
            g.drawString("Medium", 250, 300);
    
            g.drawRect(60, 350, 480, 100);
            g.drawString("Impossible", 240, 400);

        }else if (gameState == 2) {
            // Select the size of the board
            g.setFont(font);
            
            g.drawString("Select Size", 230, 30);
            
            g.drawRect(200, 70, 200, 100);
            g.drawString("3x3", 280, 120);
            
            g.drawRect(200, 170, 200, 100);
            g.drawString("4x4", 280, 220);
            
            g.drawRect(200, 270, 200, 100);
            g.drawString("5x5", 280, 320);
            
            g.drawRect(200, 370, 200, 100);
            g.drawString("6x6", 280, 420);

        }else if (gameState == 3) {
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
            
        }else if (gameState == 4) {
            
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

        System.out.println(winner);
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
            if (getWinner() == 0)
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
    
    private void initBoard(int s) {
        // Create the board with size given by user
        size = s;
        board = new int[s][s];
    
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = 0;
            }
        }
        
        gameState = 3;
        
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

        if (gameState == 0) {
            // Find if single player or two player has been clicked
            if (x >= 60 && x <= 540 && y >= 150 && y <= 250) {
                isPvp = false;
                gameState = 1;
                repaint();
            }else if (x >= 60 && x <= 540 && y >= 300 && y <= 400) {
                isPvp = true;
                gameState = 2;
                repaint();
            }

        }else if (gameState == 1) {
            // Find difficulty selected
            if (x >= 60 && x <= 540 && y >= 150 && y <= 250) {
                difficulty = 1;
                gameState = 2;
                repaint();
            }else if (x >= 60 && x <= 540 && y >= 250 && y <= 350) {
                difficulty = 2;
                gameState = 2;
                repaint();
            }

        }else if (gameState == 2) {
            // Find board size selected
            if (x >= 200 && x <= 400 && y >= 70 && y <= 170) {
                initBoard(3);
                repaint();
            }else if (x >= 200 && x <= 400 && y >= 170 && y <= 270) {
                initBoard(4);
                repaint();
            }else if (x >= 200 && x <= 400 && y >= 270 && y <= 370) {
                initBoard(5);
                repaint();
            }else if (x >= 200 && x <= 400 && y >= 370 && y <= 470) {
                initBoard(6);
                repaint();
            }
            
        }else if (gameState == 3) {
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
        }
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
