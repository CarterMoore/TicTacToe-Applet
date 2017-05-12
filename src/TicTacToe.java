import java.applet.Applet;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

/**
 * TicTacToe-Applet
 * Created by Carter Moore
 */

public class TicTacToe extends Applet implements MouseListener{
    
    private int size = 3; // Dimensions of the board will be able to change
    private int board[][] = new int[size][size]; // Store an int on what is in the square, -1 = O, 0 = blank, 1 = X
    private final int LENGTH = 600;
    
    public void init() {
        addMouseListener(this);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = 0;
            }
        }
    }
    
    public void paint(Graphics g) {
        for (int i = 1; i < size; i++) { // Draw lines depending on given size
            g.fillRect(i * (LENGTH / size) - 2, 0, 4, LENGTH);
            g.fillRect(0, i * (LENGTH / size) - 2, LENGTH, 4);
        }
        
        Image xImage = getImage(getClass().getResource("X.png"));
        Image oImage = getImage(getClass().getResource("O.png"));
        
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
        int win = 0;
        
        boolean diagonalW1 = true, diagonalW2 = true;
        for (int i = 0; i < size; i++) {
            boolean horizontalW = true, verticalW = true;
            for (int j = 1; j < size; j++) {
                if (board[i][0] != board[i][j]) { // Check if win horizontal win
                    horizontalW = false;
                    break;
                }
                
                if (board[0][i] != board[j][i]) { // Check if vertical win
                    verticalW = false;
                    break;
                }
            }
            
            if (horizontalW && board[i][0] != 0) { // Make sure a player won
                win = board[i][0];
                break;
            }
            
            if (verticalW && board[0][i] != 0) { // Make sure a player won
                win = board[0][i];
                break;
            }
            
            if (board[0][0] != board[i][i]) // Check for diagonal win
                diagonalW1 = false;
            if (board[0][size-1] != board[i][size-i-1]) // Check for diagonal win
                diagonalW2 = false;
        }
        
        if (diagonalW1 && board[0][0] != 0) // Make sure a player won
            win = board[0][0];
        if (diagonalW2 && board[0][size-1] != 0) // Make sure a player won
            win = board[0][size-1];
        
        return win; // Return who won, -1 = O, 0 = nobody, 1 = X
    }
    
    private void botMove(int difficulty) {
    
        Random random = new Random();
        
        ArrayList<Integer> availableX = new ArrayList<>();
        ArrayList<Integer> availableY = new ArrayList<>();
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == 0) {
                    availableX.add(i);
                    availableY.add(j);
                }
                    
            }
        }
        
        if (difficulty == 1) { // Easiest difficulty, random
            
            int x = random.nextInt(availableX.size());
            int y = random.nextInt(availableY.size());
    
            System.out.println(availableX.get(x));
            System.out.println(availableY.get(y));
            
            board[availableX.get(x)][availableY.get(y)] = -1;
            
        }
        
    }
    
    public void mouseClicked (MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
    
        System.out.println(x + " " + y);
    
        loop:
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (x <= (i + 1) * (LENGTH / size) && y <= (j + 1) * (LENGTH / size) && board[i][j] == 0) {
                    board[i][j] = 1;
                    break loop;
                }
            }
        }
        if (getWinner() == 0)
            botMove(1);
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
