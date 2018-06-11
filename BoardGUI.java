import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class BoardGUI extends JPanel implements MouseListener, MouseMotionListener {
    final static int SQUARE_WIDTH = 50;
    final static int SQUARE_HEIGHT = 50;
    int[] pieceList = {1, 10, 11, 2, 20, 3, 4, 5, 50, 6};
    static int cMouseX, cMouseY, nMouseX, nMouseY;
    Main game;
    boolean whiteTurn = true;
    Image whiteKing;
    Image whiteQueen;
    Image whiteRook;
    Image whiteBishop;
    Image whiteKnight;
    Image whitePawn;
    Image blackKing;
    Image blackQueen;
    Image blackRook;
    Image blackBishop;
    Image blackKnight;
    Image blackPawn;

    public void paint(Graphics g) {
        //draw background
        g.setColor(Color.gray);
        g.fillRect(0, 0, getWidth(), getHeight());
        //draw letters & check/checkmate
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        g.setColor(Color.white);
        for (int t = 1; t < 9; t++)
            g.drawString(""+t, 20 + 50 * (t - 1), 425);
        for (int w = 1; w < 9; w++)
            g.drawString(""+(char)(w + 64), 410, 30 + 50 * (w - 1));
        if (game.gameOver) 
            g.drawString("Checkmate!", getWidth() / 2 - 50, 460);
        if (game.isDraw) 
            g.drawString("Draw", getWidth() / 2 - 50, 460);
        //draw checkers
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
                    g.setColor(new Color(245, 245, 220));
                } else {
                    g.setColor(new Color(181, 101, 29));
                }
                g.fillRect(j*50, i*50, SQUARE_WIDTH, SQUARE_HEIGHT);
            }
        }
        //draw pieces
        loadImages();
        for (int c = 0; c < 8; c++) {
            for (int r = 0; r < 8; r++) {
                switch(game.board[c][r]) {
                    case 1: 
                        g.drawImage(whitePawn, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this); 
                        break;
                    case 2: 
                        g.drawImage(whiteRook, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this);
                        break;
                    case 3: 
                        g.drawImage(whiteKnight, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this);
                        break;
                    case 4: 
                        g.drawImage(whiteBishop, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this);
                        break;
                    case 5: 
                        g.drawImage(whiteKing, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this);
                        break;
                    case 6: 
                        g.drawImage(whiteQueen, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this);
                        break;
                    case 10: 
                        g.drawImage(whitePawn, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this);
                        break;
                    case 11: 
                        g.drawImage(whitePawn, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this);
                        break;
                    case 20:
                        g.drawImage(whiteRook, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this);
                        break;
                    case 50: 
                        g.drawImage(whiteKing, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this);
                        break;
                    case -1: 
                        g.drawImage(blackPawn, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this);
                        break;
                    case -2: 
                        g.drawImage(blackRook, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this);
                        break;
                    case -3: 
                        g.drawImage(blackKnight, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this);
                        break;
                    case -4: 
                        g.drawImage(blackBishop, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this);
                        break;
                    case -5: 
                        g.drawImage(blackKing, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this);
                        break;
                    case -6: 
                        g.drawImage(blackQueen, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this);
                        break;
                    case -10: 
                        g.drawImage(blackPawn, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this);
                        break;
                    case -11: 
                        g.drawImage(blackPawn, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this);
                        break;
                    case -20:
                        g.drawImage(blackRook, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this);
                        break;
                    case -50: 
                        g.drawImage(blackKing, r * SQUARE_WIDTH, c * SQUARE_HEIGHT, this);
                        break;
                    default: break;
                }
            }
        }
    }

    public void loadImages() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        whiteKing   = kit.getImage(this.getClass().getResource("whiteKing.png"));
        whiteQueen  = kit.getImage(this.getClass().getResource("whiteQueen.png"));
        whiteRook   = kit.getImage(this.getClass().getResource("whiteRook.png"));
        whiteBishop = kit.getImage(this.getClass().getResource("whiteBishop.png"));
        whiteKnight = kit.getImage(this.getClass().getResource("whiteKnight.png"));
        whitePawn   = kit.getImage(this.getClass().getResource("whitePawn.png"));
        blackKing   = kit.getImage(this.getClass().getResource("blackKing.png"));
        blackQueen  = kit.getImage(this.getClass().getResource("blackQueen.png"));
        blackRook   = kit.getImage(this.getClass().getResource("blackRook.png"));
        blackBishop = kit.getImage(this.getClass().getResource("blackBishop.png"));
        blackKnight = kit.getImage(this.getClass().getResource("blackKnight.png"));
        blackPawn   = kit.getImage(this.getClass().getResource("blackPawn.png"));
    }

    public void mousePressed(MouseEvent e) {
        if(e.getX() < 8 * SQUARE_WIDTH && e.getY() < 8 * SQUARE_HEIGHT) {
            cMouseX = e.getX() / 50;
            cMouseY = e.getY() / 50;
        }
    }
    //checkmate is done by check if the king is in check at the begining and the end of the turn
    //it makes the assumption that players are smart enough to figure out they are in check and move out of it
    public void mouseReleased(MouseEvent e) {
        if(e.getX() < 8 * SQUARE_WIDTH && e.getY() < 8 * SQUARE_HEIGHT) {
            nMouseX = e.getX() / 50;
            nMouseY = e.getY() / 50;
            if (nMouseX > 7 || nMouseY > 7) {
                return;
            }
            if (game.board[cMouseY][cMouseX] == 0) {
                return;
            }
            boolean kingSafeOne;
            if (whiteTurn) {
                kingSafeOne = game.kingSafe(game.board, 1);
            } else {
                kingSafeOne = game.kingSafe(game.board, -1);
            } 
            if ((game.board[cMouseY][cMouseX] < 0 && whiteTurn == false) || (game.board[cMouseY][cMouseX] > 0 && whiteTurn)) {
                for (int piece: pieceList) {
                    if (piece == Math.abs(game.board[cMouseY][cMouseX])) {
                        switch (piece) {
                            case 1:
                                if (game.isValidPawnMove(cMouseX, cMouseY, nMouseX, nMouseY, game.board)) {
                                    game.move(cMouseX, cMouseY, nMouseX, nMouseY);
                                    boolean kingSafeTwo = game.kingSafe(game.board, game.board[nMouseY][nMouseX] / Math.abs(game.board[nMouseY][nMouseX]));
                                    if (whiteTurn) {
                                        whiteTurn = false;
                                    } else {
                                        whiteTurn = true;
                                    }
                                    int numOfPieces = 0;
                                    for (int t = 0; t < 8; t++) 
                                        for (int w = 0; w < 8; w++)
                                            if (game.board[t][w] != 0)
                                                numOfPieces++;
                                    if (numOfPieces == 2) 
                                        game.isDraw = true;
                                    if (kingSafeOne == false && kingSafeTwo == false) {
                                        game.gameOver = true;
                                    }
                                    this.repaint();
                                }
                                break;
                            case 2:
                                if (game.isValidRookMove(cMouseX, cMouseY, nMouseX, nMouseY, game.board)) {
                                    game.move(cMouseX, cMouseY, nMouseX, nMouseY);
                                    boolean kingSafeTwo = game.kingSafe(game.board, game.board[nMouseY][nMouseX] / Math.abs(game.board[nMouseY][nMouseX]));
                                    if (whiteTurn) {
                                        whiteTurn = false;
                                    } else {
                                        whiteTurn = true;
                                    }
                                    int numOfPieces= 0;
                                    for (int t = 0; t < 8; t++) 
                                        for (int w = 0; w < 8; w++)
                                            if (game.board[t][w] != 0)
                                                numOfPieces++;
                                    if (numOfPieces == 2) 
                                        game.isDraw = true;
                                    if (kingSafeOne == false && kingSafeTwo == false) {
                                        game.gameOver = true;
                                    }
                                    this.repaint();
                                }
                                break;
                            case 3:
                                if (game.isValidKnightMove(cMouseX, cMouseY, nMouseX, nMouseY, game.board)) {
                                    game.move(cMouseX, cMouseY, nMouseX, nMouseY);
                                    boolean kingSafeTwo = game.kingSafe(game.board, game.board[nMouseY][nMouseX] / Math.abs(game.board[nMouseY][nMouseX]));
                                    if (whiteTurn) {
                                        whiteTurn = false;
                                    } else {
                                        whiteTurn = true;
                                    }
                                    int numOfPieces = 0;
                                    for (int t = 0; t < 8; t++) 
                                        for (int w = 0; w < 8; w++)
                                            if (game.board[t][w] != 0)
                                                numOfPieces++;
                                    if (numOfPieces == 2) 
                                        game.isDraw = true;
                                    if (kingSafeOne == false && kingSafeTwo == false) {
                                        game.gameOver = true;
                                    }
                                    this.repaint();
                                }
                                break;
                            case 4:
                                if (game.isValidBishopMove(cMouseX, cMouseY, nMouseX, nMouseY, game.board)) {
                                    game.move(cMouseX, cMouseY, nMouseX, nMouseY);
                                    boolean kingSafeTwo = game.kingSafe(game.board, game.board[nMouseY][nMouseX] / Math.abs(game.board[nMouseY][nMouseX]));
                                    if (whiteTurn) {
                                        whiteTurn = false;
                                    } else {
                                        whiteTurn = true;
                                    }
                                    int numOfPieces = 0;
                                    for (int t = 0; t < 8; t++) 
                                        for (int w = 0; w < 8; w++)
                                            if (game.board[t][w] != 0)
                                                numOfPieces++;
                                    if (numOfPieces == 2) 
                                        game.isDraw = true;
                                    if (kingSafeOne == false && kingSafeTwo == false) {
                                        game.gameOver = true;
                                    }
                                    this.repaint();
                                }
                                break;
                            case 5:
                                if (game.isValidKingMove(cMouseX, cMouseY, nMouseX, nMouseY, game.board)) {
                                    game.move(cMouseX, cMouseY, nMouseX, nMouseY);
                                    boolean kingSafeTwo = game.kingSafe(game.board, game.board[nMouseY][nMouseX] / Math.abs(game.board[nMouseY][nMouseX]));
                                    if (whiteTurn) {
                                        whiteTurn = false;
                                    } else {
                                        whiteTurn = true;
                                    }
                                    int numOfPieces = 0;
                                    for (int t = 0; t < 8; t++) 
                                        for (int w = 0; w < 8; w++)
                                            if (game.board[t][w] != 0)
                                                numOfPieces++;
                                    if (numOfPieces == 2) 
                                        game.isDraw = true;
                                    if (kingSafeOne == false && kingSafeTwo == false) {
                                        game.gameOver = true;
                                    }
                                    this.repaint();
                                }
                                break;
                            case 6:
                                if (game.isValidQueenMove(cMouseX, cMouseY, nMouseX, nMouseY, game.board)) {
                                    game.move(cMouseX, cMouseY, nMouseX, nMouseY);
                                    boolean kingSafeTwo = game.kingSafe(game.board, game.board[nMouseY][nMouseX] / Math.abs(game.board[nMouseY][nMouseX]));
                                    if (whiteTurn) {
                                        whiteTurn = false;
                                    } else {
                                        whiteTurn = true;
                                    }
                                    int numOfPieces = 0;
                                    for (int t = 0; t < 8; t++) 
                                        for (int w = 0; w < 8; w++)
                                            if (game.board[t][w] != 0)
                                                numOfPieces++;
                                    if (numOfPieces == 2) 
                                        game.isDraw = true;
                                    if (kingSafeOne == false && kingSafeTwo == false) {
                                        game.gameOver = true;
                                    }
                                    this.repaint();
                                }
                                break;
                            case 10:
                                if (game.isValidPawnMove(cMouseX, cMouseY, nMouseX, nMouseY, game.board)) {
                                    game.move(cMouseX, cMouseY, nMouseX, nMouseY);
                                    boolean kingSafeTwo = game.kingSafe(game.board, game.board[nMouseY][nMouseX] / Math.abs(game.board[nMouseY][nMouseX]));
                                    if (whiteTurn) {
                                        whiteTurn = false;
                                    } else {
                                        whiteTurn = true;
                                    }
                                    int numOfPieces = 0;
                                    for (int t = 0; t < 8; t++) 
                                        for (int w = 0; w < 8; w++)
                                            if (game.board[t][w] != 0)
                                                numOfPieces++;
                                    if (numOfPieces == 2) 
                                        game.isDraw = true;
                                    if (kingSafeOne == false && kingSafeTwo == false) {
                                        game.gameOver = true;
                                    }
                                    this.repaint();
                                }
                                break;
                            case 11:
                                if (game.isValidPawnMove(cMouseX, cMouseY, nMouseX, nMouseY, game.board)) {
                                    game.move(cMouseX, cMouseY, nMouseX, nMouseY);
                                    boolean kingSafeTwo = game.kingSafe(game.board, game.board[nMouseY][nMouseX] / Math.abs(game.board[nMouseY][nMouseX]));
                                    if (whiteTurn) {
                                        whiteTurn = false;
                                    } else {
                                        whiteTurn = true;
                                    }
                                    int numOfPieces = 0;
                                    for (int t = 0; t < 8; t++) 
                                        for (int w = 0; w < 8; w++)
                                            if (game.board[t][w] != 0)
                                                numOfPieces++;
                                    if (numOfPieces == 2) 
                                        game.isDraw = true;
                                    if (kingSafeOne == false && kingSafeTwo == false) {
                                        game.gameOver = true;
                                    }
                                    this.repaint();
                                }
                            case 20:
                                if (game.isValidRookMove(cMouseX, cMouseY, nMouseX, nMouseY, game.board)) {
                                    game.move(cMouseX, cMouseY, nMouseX, nMouseY);
                                    boolean kingSafeTwo = game.kingSafe(game.board, game.board[nMouseY][nMouseX] / Math.abs(game.board[nMouseY][nMouseX]));
                                    if (whiteTurn) {
                                        whiteTurn = false;
                                    } else {
                                        whiteTurn = true;
                                    }
                                    int numOfPieces = 0;
                                    for (int t = 0; t < 8; t++) 
                                        for (int w = 0; w < 8; w++)
                                            if (game.board[t][w] != 0)
                                                numOfPieces++;
                                    if (numOfPieces == 2) 
                                        game.isDraw = true;
                                    if (kingSafeOne == false && kingSafeTwo == false) {
                                        game.gameOver = true;
                                    }
                                    this.repaint();
                                }
                                break;
                            case 50:
                                if (game.isValidKingMove(cMouseX, cMouseY, nMouseX, nMouseY, game.board)) {
                                    game.move(cMouseX, cMouseY, nMouseX, nMouseY);
                                    boolean kingSafeTwo = game.kingSafe(game.board, game.board[nMouseY][nMouseX] / Math.abs(game.board[nMouseY][nMouseX]));
                                    if (whiteTurn) {
                                        whiteTurn = false;
                                    } else {
                                        whiteTurn = true;
                                    }
                                    int numOfPieces = 0;
                                    for (int t = 0; t < 8; t++) 
                                        for (int w = 0; w < 8; w++)
                                            if (game.board[t][w] != 0)
                                                numOfPieces++;
                                    if (numOfPieces == 2) 
                                        game.isDraw = true;
                                    if (kingSafeOne == false && kingSafeTwo == false) {
                                        game.gameOver = true;
                                    }
                                    this.repaint();
                                }
                                break;
                        }
                    }
                }
            }
        }
    }
    
    public void mouseDragged(MouseEvent e) {
        
    }
    public void mouseMoved(MouseEvent e) {

    }
    public void mouseClicked(MouseEvent e) {
        
    }
    public void mouseEntered(MouseEvent e) {

    }
    public void mouseExited(MouseEvent e) {

    }
}