import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TetrisKeyListener implements KeyListener {
    private TetrisWindow window;

    public TetrisKeyListener(TetrisWindow window) {
        this.window = window;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if ((window.gameState.equals("1p") || window.gameState.equals("2p")) && window.gameBoard.gameState.equals("game")) {
            if (e.getKeyCode() == KeyEvent.VK_UP) window.gameBoard.currentHand.rotate();
            else if (e.getKeyCode() == KeyEvent.VK_DOWN) window.gameBoard.movingDown = true;
            else if (e.getKeyCode() == KeyEvent.VK_LEFT) window.gameBoard.movingLeft = true;
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT) window.gameBoard.movingRight = true;
            else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                window.gameBoard.movingLeft = false;
                window.gameBoard.movingRight = false;
                window.gameBoard.currentHand.jumpScore();
                window.gameBoard.currentHand.addToBoard();
            } else if (e.getKeyCode() == KeyEvent.VK_C) {
                window.gameBoard.movingLeft = false;
                window.gameBoard.movingRight = false;
                window.gameBoard.hold();
            }
        }

        if (window.gameState.equals("1p") && window.gameBoard.gameState.equals("game")) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                window.gameState = "paused";
            }
        }
        else if ((window.gameState.equals("1p") || window.gameState.equals("2p")) && window.gameBoard.gameState.equals("game_over")) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                window.gameState = "menu";
            }
        }
        else if (window.gameState.equals("paused")) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                window.gameState = "1p";
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (window.gameState.equals("1p") || window.gameState.equals("2p")) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                window.gameBoard.movingLeft = false;
                window.gameBoard.movingDelay = 50;
                if (!window.gameBoard.oneMoveDone) window.gameBoard.currentHand.moveLeft();
                window.gameBoard.oneMoveDone = false;
            }
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                window.gameBoard.movingRight = false;
                window.gameBoard.movingDelay = 50;
                if (!window.gameBoard.oneMoveDone) window.gameBoard.currentHand.moveRight();
                window.gameBoard.oneMoveDone = false;
            }
            else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                window.gameBoard.movingDown = false;
                window.gameBoard.movingDelay = 50;
                if (!window.gameBoard.oneMoveDone) window.gameBoard.currentHand.moveDown();
                window.gameBoard.oneMoveDone = false;
            }
        }
    }
}
