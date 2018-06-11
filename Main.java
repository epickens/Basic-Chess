//Elliot Pickens Dec 19 2015
import java.util.Arrays;
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    //1-6 are the standard pieces
    //negative is black, positive is white
    //anything multiplied by 10 has not moved
    //11 is a special for a pawn that moved 2 for its first move and hasn't moved since (for en passant)
    static public int[][] board = {
         {-20, -3, -4, -6, -50, -4, -3, -20},
         {-10, -10, -10, -10, -10, -10, -10, -10},
         {0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0},
         {10, 10, 10, 10, 10, 10, 10, 10},
         {20, 3, 4, 6, 50, 4, 3, 20}
    };
    static boolean gameOver = false;
    static boolean isDraw = false;
    
    public static void main(String [] args) {
        BoardGUI canvas = new BoardGUI();
        Main gameControl = new Main();
        canvas.game = gameControl;
        gameControl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameControl.setSize(450, 525);
        gameControl.add(canvas);
        gameControl.setVisible(true);
        gameControl.addMouseListener(canvas);
        canvas.addMouseListener(canvas);
        canvas.repaint();
        while (gameOver != true && isDraw != true) {
        
        }
    }
    
    //n stands for new position, c stands for current position

    public boolean isValidKingMove(int cX, int cY, int nX, int nY, int[][] board) {
        int sign; 
        if (board[cY][cX] > 0) {
            sign = 1;
        } else {
            sign = -1;
        }
        int dx = Math.abs(nX - cX);
        int dy = Math.abs(nY - cY);
        double forMovement;
        if (dx > 0 && dy == 0) {
            forMovement = (nX - cX) * Math.pow(dx, -1);
        } 
        if (dy > 0 && dx == 0) {
            forMovement = 2 * (nY - cY) * Math.pow(dy, -1);
        } else {
            forMovement = ((nX - cX) * Math.pow(dx, -1) + (nY - cY) * Math.pow(dy, -1) * 2);
        }
        if (nX == cX && nY == cY) {
            return false;
        }
        //castling
        if (dx > 1 && (dx != 2 && (forMovement == 1 && board[nY][nX + 1] != sign * 20 || forMovement == -1 && board[nY][nX - 2] != sign * 20))) 
            return false;
        if (dx > 2) 
            return false;
        if (dy > 1)
            return false;
        if (dy != dx && dy * dx != 0) {
            return false; 
        }
        if (dx * dy != 0 && this.diagnolMovement(forMovement, cX, cY, nX)) {
            boolean retrvl = false;
            int[][] temp = new int[8][8];
            for (int i = 0; i < board.length; i++) {
                temp[i] = Arrays.copyOf(board[i], board[i].length);
            }
            temp[nY][nX] = temp[cY][cX];
            temp[cY][cX] = 0;
            if ((board[nY][nX] == 0 || ((board[cY][cX] < 0 && board[nY][nX] > 0) || (board[cY][cX] > 0 && board[nY][nX] < 0))) && this.kingSafe(temp, sign)) {
                retrvl = true;
            } 
            return retrvl;
        }
        if (dx * dy == 0 && this.axisMovement(forMovement, cX, cY, 1)) {
            boolean retrvl = false;
            int[][] temp = new int[8][8];
            for (int i = 0; i < board.length; i++) {
                temp[i] = Arrays.copyOf(board[i], board[i].length);
            }
            temp[nY][nX] = temp[cY][cX];
            temp[cY][cX] = 0;
            if ((board[nY][nX] == 0 || ((board[cY][cX] < 0 && board[nY][nX] > 0) || (board[cY][cX] > 0 && board[nY][nX] < 0))) && this.kingSafe(temp, sign)) {
                retrvl = true;
            } 
            return retrvl;
        } else {
            return false;
        }
    }
    
    public boolean isValidRookMove(int cX, int cY, int nX, int nY, int[][] board) {
        int sign; 
        if (board[cY][cX] > 0) {
            sign = 1;
        } else {
            sign = -1;
        }
        int dx = Math.abs(nX - cX);
        int dy = Math.abs(nY - cY);
        int moveHistory = board[cY][nY];
        int distance = Math.abs((nX + nY) - (cX + cY));
        double forMovement = ((nX - cX) * Math.pow(dx, -1) + 2 * (nY - cY) * Math.pow(dy, -1));
        if (dx > 0) {
            forMovement = (nX - cX) * Math.pow(dx, -1);
        } else {
            forMovement = 2 * (nY - cY) * Math.pow(dy, -1);
        }
        if (nX == cX && nY == cY) {
            return false;
        }
        if (dy * dx != 0) {
            return false; 
        }
        if (this.axisMovement(forMovement, cX, cY, distance)) {
            boolean retrvl = false;
            int[][] temp = new int[8][8];
            for (int i = 0; i < board.length; i++) {
                temp[i] = Arrays.copyOf(board[i], board[i].length);
            }
            temp[nY][nX] = temp[cY][cX];
            temp[cY][cX] = 0;
            if (this.kingSafe(temp, sign) == false) {
                return false;
            }
            if ((board[nY][nX] == 0 || ((board[cY][cX] < 0 && board[nY][nX] > 0) || (board[cY][cX] > 0 && board[nY][nX] < 0)))) {
                retrvl = true;
            } 
            return retrvl;
        } else {
            return false;
        }
    }
    
    public boolean isValidKnightMove(int cX, int cY, int nX, int nY, int[][] board) {
        int sign; 
        if (board[cY][cX] > 0) {
            sign = 1;
        } else {
            sign = -1;
        }
        int dx = Math.abs(nX - cX);
        int dy = Math.abs(nY - cY);
        if (nX == cX && nY == cY) {
            return false;
        }
        if (dy*dx != 2) {
            return false; 
        }
        int[][] temp = new int[8][8];
        for (int i = 0; i < board.length; i++) {
            temp[i] = Arrays.copyOf(board[i], board[i].length);
        }
        temp[nY][nX] = temp[cY][cX];
        temp[cY][cX] = 0;
        if ((board[nY][nX] == 0 || ((board[cY][cX] < 0 && board[nY][nX] > 0) || (board[cY][cX] > 0 && board[nY][nX] < 0))) && this.kingSafe(temp, sign)) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isValidBishopMove(int cX, int cY, int nX, int nY, int[][] board) {
        int sign; 
        if (board[cY][cX] > 0) {
            sign = 1;
        } else {
            sign = -1;
        }
        int dx = Math.abs(nX - cX);
        int dy = Math.abs(nY - cY);
        double forMovement = ((nX - cX) * Math.pow(dx, -1) + (nY - cY) * Math.pow(dy, -1) * 2);
        if (nX == cX && nY == cY) {
            return false;
        }
        if (dy != dx) {
            return false; 
        }
        if (this.diagnolMovement(forMovement, cX, cY, nX)) {
            boolean retrvl = false;
            int[][] temp = new int[8][8];
            for (int i = 0; i < board.length; i++) {
                temp[i] = Arrays.copyOf(board[i], board[i].length);
            }
            temp[nY][nX] = temp[cY][cX];
            temp[cY][cX] = 0;
            if ((board[nY][nX] == 0 || ((board[cY][cX] < 0 && board[nY][nX] > 0) || (board[cY][cX] > 0 && board[nY][nX] < 0))) && this.kingSafe(temp, sign)) {
                retrvl = true;
            } 
            return retrvl;
        } else {
            return false;
        }
    }
    
    public boolean isValidPawnMove(int cX, int cY, int nX, int nY, int[][] board) {
        int sign; 
        if (board[cY][cX] > 0) {
            sign = 1;
        } else {
            sign = -1;
        }
        int dx = Math.abs(nX - cX);
        int dy = Math.abs(nY - cY);
        int moveHistory = board[cY][nY];
        if ((nX == cX && nY == cY) || ((sign == 1 && nY > cY) || (sign == -1 && nY < cY))) {
            return false;
        }
        if ((dy != 1 && dy != 2) || (dx != 0 && dx != 1)) {
            return false;
        }
        //en passant
        if ((dx > 0 && sign == 1) && (board[nY][nX] == 0 && board[cY][nX] != -11)) {
            return false;
        } 
        if ((dx > 0 && sign == -1) && (board[nY][nX] == 0 && board[cY][nX] != 11)) {
            return false;
        }
        if (Math.abs(board[cY][cX]) == 1 && dy > 1) {
            return false;
        }
        if (dx == 0 && board[nY][nX] != 0) {
            return false;
        }
        int[][] temp = new int[8][8];
        for (int i = 0; i < board.length; i++) {
            temp[i] = Arrays.copyOf(board[i], board[i].length);
        }
        temp[nY][nX] = temp[cY][cX];
        temp[cY][cX] = 0;
        if ((board[nY][nX] == 0 || ((board[cY][cX] < 0 && board[nY][nX] > 0) || (board[cY][cX] > 0 && board[nY][nX] < 0))) && this.kingSafe(temp, sign)) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isValidQueenMove(int cX, int cY, int nX, int nY, int[][] board) {
        int sign; 
        if (board[cY][cX] > 0) {
            sign = 1;
        } else {
            sign = -1;
        }
        int dx = Math.abs(nX - cX);
        int dy = Math.abs(nY - cY);
        int distance = Math.abs((nX + nY) - (cX + cY));
        double forMovement;
        if (dx > 0 && dy == 0) {
            forMovement = (nX - cX) * Math.pow(dx, -1);
        } 
        if (dy > 0 && dx == 0) {
            forMovement = 2 * (nY - cY) * Math.pow(dy, -1);
        } else {
            forMovement = ((nX - cX) * Math.pow(dx, -1) + (nY - cY) * Math.pow(dy, -1) * 2);
        }
        if (nX == cX && nY == cY) {
            return false;
        }
        if (dy != dx && dy * dx != 0) {
            return false; 
        }
        if (dx * dy != 0 && this.diagnolMovement(forMovement, cX, cY, nX)) {
            boolean retrvl = false;
            int[][] temp = new int[8][8];
            for (int i = 0; i < board.length; i++) {
                temp[i] = Arrays.copyOf(board[i], board[i].length);
            }
            temp[nY][nX] = temp[cY][cX];
            temp[cY][cX] = 0;
            if ((board[nY][nX] == 0 || ((board[cY][cX] < 0 && board[nY][nX] > 0) || (board[cY][cX] > 0 && board[nY][nX] < 0))) && this.kingSafe(temp, sign)) {
                retrvl = true;
            } 
            return retrvl;
        }
        if (dx * dy == 0 && this.axisMovement(forMovement, cX, cY, distance)) {
            boolean retrvl = false;
            int[][] temp = new int[8][8];
            for (int i = 0; i < board.length; i++) {
                temp[i] = Arrays.copyOf(board[i], board[i].length);
            }
            temp[nY][nX] = temp[cY][cX];
            temp[cY][cX] = 0;
            if ((board[nY][nX] == 0 || ((board[cY][cX] < 0 && board[nY][nX] > 0) || (board[cY][cX] > 0 && board[nY][nX] < 0))) && this.kingSafe(temp, sign)) {
                retrvl = true;
            } 
            return retrvl;
        } else {
            return false;
        }
    }
    
    //direction represents the quadrant the piece would be moving in
    //I only need nX due to the nature of diagnol movement
    public boolean diagnolMovement(double direction, int cX, int cY, int nX) {
        boolean retrvl = true;
        
        int[][] temp = new int[8][8];
        for (int i = 0; i < board.length; i++) {
            temp[i] = Arrays.copyOf(board[i], board[i].length);
        }
        if (direction == -1.0) {
            for (int j = 1; j+cX < nX; j++) {
                if (temp[cY-j][cX+j] != 0) {
                    retrvl = false;
                    break;
                } 
            }
        }
        if (direction == -3.0) {
            for (int j = 1; cX-j > nX; j++) {
                if (temp[cY-j][cX-j] != 0) {
                    retrvl = false;
                    break;
                } 
            }
        }
        if (direction == 1.0) {
            for (int j = 1; cX-j > nX; j++) {
                if (temp[cY+j][cX-j] != 0) {
                    retrvl = false;
                    break;
                } 
            }
        }
        if (direction == 3.0) {
            for (int j = 1; j+cX < nX; j++) {
                if (temp[cY+j][cX+j] != 0) {
                    retrvl = false;
                    break;
                } 
            }
        }
        return retrvl;
    }
    
    //direction represents the axis with 1 being +x, 2 being +y, 3 being -x, and 4 being -y
    //nd is the distance to the new location, x y does not matter because of the i parameter
    public boolean axisMovement(double direction, int cX, int cY, int nD) {
        boolean retrvl = true;
        int[][] temp = new int[8][8];
        for (int i = 0; i < board.length; i++) {
            temp[i] = Arrays.copyOf(board[i], board[i].length);
        }
        if (direction == 1.0) {
            for (int j = 1; j < nD; j++) {
                if (temp[cY][cX+j] != 0) {
                    retrvl = false;
                    break;
                } 
            }
        }
        if (direction == -2.0) {
            for (int j = 1; j < nD; j++) {
                if (temp[cY-j][cX] != 0) {
                    retrvl = false;
                    break;
                } 
            }
        }
        if (direction == -1.0) {
            for (int j = 1; j < nD; j++) {
                if (temp[cY][cX-j] != 0) {
                    retrvl = false;
                    break;
                } 
            }
        }
        if (direction == 2.0) {
            for (int j = 1; j < nD; j++) {
                if (temp[cY+j][cX] != 0) {
                    retrvl = false;
                    break;
                } 
            }
        }
        return retrvl;
    }
    
    public boolean kingSafe(int[][] board, int sign) {
        int kingX = 0;
        int kingY = 0;
        for (int c = 0; c < 8; c++) {
            for (int r = 0; r < 8; r++) {
                if (board[c][r] == sign * 5 || board[c][r] == sign * 50) {
                    kingY = c;
                    kingX = r;
                    break;
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((board[i][j] < 0 && sign > 0)) {
                    switch(board[i][j]) {
                        case -1: if (this.isValidPawnMove(j, i, kingX, kingY, board)) {return false;} break;
                        case -2: if (this.isValidRookMove(j, i, kingX, kingY, board)) {return false;} break;
                        case -3: if (this.isValidKnightMove(j, i, kingX, kingY, board)) {return false;} break;
                        case -4: if (this.isValidBishopMove(j, i, kingX, kingY, board)) {return false;} break;
                        case -5: if (this.isValidKingMove(j, i, kingX, kingY, board)) {return false;} break;
                        case -6: if (this.isValidQueenMove(j, i, kingX, kingY, board)) {return false;} break;
                        case -10: if (this.isValidPawnMove(j, i, kingX, kingY, board)) {return false;} break;
                        case -11: if (this.isValidPawnMove(j, i, kingX, kingY, board)) {return false;} break;
                        case -20: if (this.isValidRookMove(j, i, kingX, kingY, board)) {return false;} break;
                        case -50: if (this.isValidKingMove(j, i, kingX, kingY, board)) {return false;} break;
                    }
                }
                if ((board[i][j] > 0 && sign < 0)) {
                    switch(board[i][j]) {
                        case 1: if (this.isValidPawnMove(j, i, kingX, kingY, board)) {return false;} break;
                        case 2: if (this.isValidRookMove(j, i, kingX, kingY, board)) {return false;} break;
                        case 3: if (this.isValidKnightMove(j, i, kingX, kingY, board)) {return false;} break;
                        case 4: if (this.isValidBishopMove(j, i, kingX, kingY, board)) {return false;} break;
                        case 5: if (this.isValidKingMove(j, i, kingX, kingY, board)) {return false;} break;
                        case 6: if (this.isValidQueenMove(j, i, kingX, kingY, board)) {return false;} break;
                        case 10: if (this.isValidPawnMove(j, i, kingX, kingY, board)) {return false;} break;
                        case 11: if (this.isValidPawnMove(j, i, kingX, kingY, board)) {return false;} break;
                        case 20: if (this.isValidRookMove(j, i, kingX, kingY, board)) {return false;} break;
                        case 50: if (this.isValidKingMove(j, i, kingX, kingY, board)) {return false;} break;
                    }
                }
            }
        }
        return true; 
    }
    
    public void move(int cX, int cY, int nX, int nY) {
        if (board[cY][cX] == 10) {
            if (Math.abs(nY - cY) == 2) {
                board[nY][nX] = 11;
                board[cY][cX] = 0;
                return;
            } else {
                board[nY][nX] = 1;
                board[cY][cX] = 0;
                return;
            }
        }
        if (board[cY][cX] == -10) {
            if (Math.abs(nY - cY) == 2) {
                board[nY][nX] = -11;
                board[cY][cX] = 0;
                return;
            } else {
                board[nY][nX] = -1;
                board[cY][cX] = 0;
                return;
            }
        }
        if (board[cY][cX] == 1 && board[cY][nX] == -11) {
            board[nY][nX] = 1;
            board[cY][nX] = 0;
            board[cY][cX] = 0;
            return;
        }
        if (board[cY][cX] == -1 && board[cY][nX] == 11) {
            board[nY][nX] = -1;
            board[cY][nX] = 0;
            board[cY][cX] = 0;
            return;
        }
        //always promote to a queen
        if (board[cY][cX] == 1 && nY == 0) {
            board[nY][nX] = 6;
            board[cY][cX] = 0;
            return;
        }
        if (board[cY][cX] == -1 && nY == 8) {
            board[nY][nX] = -6;
            board[cY][cX] = 0;
            return;
        }
        if (board[cY][cX] == 11) {
            board[nY][nX] = 1;
            board[cY][cX] = 0;
            return;
        }
        if (board[cY][cX] == -11) {
            board[nY][nX] = -1;
            board[cY][cX] = 0;
            return;
        }
        if (board[cY][cX] == 20) {
            board[nY][nX] = 2;
            board[cY][cX] = 0;
            return;
        }
        if (board[cY][cX] == -20) {
            board[nY][nX] = -2;
            board[cY][cX] = 0;
            return;
        }
        if (board[cY][cX] == 50 && (nY - cY) == 0 && (board[7][0] == 20 || board[7][7] == 20)) {
            board[nY][nX] = 5;
            board[cY][cX] = 0;
            if (nX - cX > 0) {
                board[7][7] = 0;
                board[nY][nX - 1] = 2;
            } else {
                board[7][0] = 0;
                board[nY][nX + 1] = 2;
            }
            return;
        }
        if (board[cY][cX] == -50 && (nY - cY) == 0 && (board[0][0] == -20 || board[0][7] == -20)) {
            board[nY][nX] = -5;
            board[cY][cX] = 0;
            if (nX - cX > 0) {
                board[0][7] = 0;
                board[nY][nX - 1] = -2;
            } else {
                board[0][0] = 0;
                board[nY][nX + 1] = -2;
            }
            return;
        }
        if (board[cY][cX] == 50) {
            board[nY][nX] = 5;
            board[cY][cX] = 0;
            return;
        }
        if (board[cY][cX] == -50) {
            board[nY][nX] = -5;
            board[cY][cX] = 0;
            return;
        } else {
            board[nY][nX] = board[cY][cX];
            board[cY][cX] = 0;
            return;
        }
    }
}