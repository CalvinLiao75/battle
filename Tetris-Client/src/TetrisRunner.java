import javax.swing.*;

public class TetrisRunner {
    public static final int WIDTH = 1200, HEIGHT = 675, FPS_DELAY = 10;
    public static final String[] TETROMINOES = {"I", "J", "L", "O", "S", "T", "Z"};

    public static void main(String[] args) {
        JFrame f = new JFrame("Tetris");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TetrisWindow w = new TetrisWindow(f);
        f.add(w);
        f.setSize(WIDTH, HEIGHT);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (w.gameBoard instanceof Tetris2P) ((Tetris2P) w.gameBoard).disconnect();
            }
        });
    }
}
