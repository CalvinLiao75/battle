import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class Tetris1P extends GameBoard {
    public int score = 0;
    private int level = 1;

    private GameButton
        pauseButton = new GameButton(window, 10, 10, 50, 50, "1p") {
            @Override
            public void click() {
                super.click();
                if (gameState.equals("game")) window.gameState = "paused";
            }
        },
        menuButton1P = new GameButton(window, 420, 290, 360, 100, "1p") {
            @Override
            public void click() {
                super.click();
                if (gameState.equals("game_over")) window.gameState = "menu";
            }
        };

    public Tetris1P(TetrisWindow window) {
        super(window);
        gameState = "game";
    }

    public void tick() {
        if (gameState.equals("game")) super.tick();
    }

    public void nextCycle() {
        super.nextCycle();

        level = (super.linesBroken / 10) + 1;
        tetrominoDelay = Math.pow(0.8 - ((level - 1) * 0.007), level - 1);
    }

    public int breakLines() {
        int lines = super.breakLines();
        if (lines == 1) score += 40 * level;
        if (lines == 2) score += 100 * level;
        if (lines == 3) score += 300 * level;
        if (lines == 4) score += 1200 * level;
        return lines;
    }

    public void paintGame(Graphics g) {
        if (super.gameState.equals("game_over")) {
            paintGameOver(g);
            return;
        }
        // Background
        
        Graphics2D G2D = (Graphics2D) g;

		// 設定漸層色物件
		// new GradientPaint(起始x, 起始y, 起始顏色, 終止x, 終止y, 中止顏色);
		GradientPaint gr1 = new GradientPaint(500, 510, Color.green, 500, 600,
				Color.CYAN);
		// 設定繪圖樣式
		G2D.setPaint(gr1);
        //g.setColor(Color.green);
        g.fillRect(0, 0, TetrisRunner.WIDTH, TetrisRunner.HEIGHT);


     




        // Draw the Tetrion
        int startingX = (TetrisRunner.WIDTH / 2) - ((TETRION_WIDTH / 2) * BLOCK_WIDTH);
        int startingY = (TetrisRunner.HEIGHT) - (TETRION_HEIGHT * BLOCK_WIDTH) - 30;

        g.setColor(Color.black);
        g.fillRect(startingX - 10, startingY - 10, (TETRION_WIDTH * BLOCK_WIDTH) + 20, (TETRION_HEIGHT * BLOCK_WIDTH) + 20);

        for (int row = 0; row < Tetrion.length; row ++) {
            for (int col = 0; col < Tetrion[0].length; col ++) {
                if (Tetrion[row][col] == null) new Block(Block.EMPTY).draw(g, startingX + (BLOCK_WIDTH * col), startingY + (BLOCK_WIDTH * row), BLOCK_WIDTH);
                else Tetrion[row][col].draw(g, startingX + (BLOCK_WIDTH * col), startingY + (BLOCK_WIDTH * row), BLOCK_WIDTH);
            }
        }

        currentHand.draw(g, startingX, startingY);

        // Score
        g.setColor(Color.black);
        g.fillRect(startingX - 200, startingY + ((TETRION_HEIGHT / 2) * BLOCK_WIDTH), 150, 50);

        g.setFont(new Font("Sans Serif", Font.BOLD, 20));
        g.drawString("成績", startingX - 200, startingY + ((TETRION_HEIGHT / 2) * BLOCK_WIDTH) - 15);

        g.setFont(new Font("Sans Serif", Font.BOLD, 30));
        g.setColor(Color.white);

        NumberFormat scoreFormat = NumberFormat.getNumberInstance(Locale.US);
        g.drawString(scoreFormat.format(score), startingX - 190, startingY + ((TETRION_HEIGHT / 2) * BLOCK_WIDTH) + 35);

        // Level
        g.setColor(Color.black);
        g.fillRect(startingX - 200, startingY + ((TETRION_HEIGHT / 2) * BLOCK_WIDTH) + 100, 150, 50);

        g.setFont(new Font("Sans Serif", Font.BOLD, 20));
        g.drawString("等級", startingX - 200, startingY + ((TETRION_HEIGHT / 2) * BLOCK_WIDTH) + 85);

        g.setFont(new Font("Sans Serif", Font.BOLD, 30));
        g.setColor(Color.white);
        g.drawString(level + "", startingX - 190, startingY + ((TETRION_HEIGHT / 2) * BLOCK_WIDTH) + 135);

        // Lines
        g.setColor(Color.black);
        g.fillRect(startingX - 200, startingY + ((TETRION_HEIGHT / 2) * BLOCK_WIDTH) + 200, 150, 50);

        g.setFont(new Font("Sans Serif", Font.BOLD, 20));
        g.drawString("行數", startingX - 200, startingY + ((TETRION_HEIGHT / 2) * BLOCK_WIDTH) + 185);

        g.setFont(new Font("Sans Serif", Font.BOLD, 30));
        g.setColor(Color.white);
        g.drawString(linesBroken + "", startingX - 190, startingY + ((TETRION_HEIGHT / 2) * BLOCK_WIDTH) + 235);

        // Hold
        g.setColor(Color.black);
        g.fillRect(startingX - 200, startingY + 15, 150, 150);

        g.setFont(new Font("Sans Serif", Font.BOLD, 20));
        g.drawString("保留", startingX - 200, startingY);

        if (super.hold != null) {
            int width = super.hold.orientations[0].length;
            int offset = ((4 - width) * 35) / 2;
            for (int row = 0; row < width; row++) {
                for (int col = 0; col < width; col++) {
                    if (super.hold.orientations[0][row][col] == null) continue;
                    super.hold.orientations[0][row][col].draw(g, startingX - 195 + (col * 35) + offset, startingY + 20 + (row * 35) + offset, 35);
                }
            }
        }

        // Queue
        g.setColor(Color.black);
        g.fillRect(startingX + (TETRION_WIDTH * BLOCK_WIDTH) + 20, startingY + 15, 160, 450);

        g.setFont(new Font("Sans Serif", Font.BOLD, 20));
        g.drawString("下一塊", startingX + (TETRION_WIDTH * BLOCK_WIDTH) + 20, startingY);

        for (int i = 0; i < super.queue.length; i ++) {
            Tetromino t = super.queue[i];
            int width = t.getOrientation().length;
            int offset = ((4 - width) * 35) / 2;
            for (int row = 0; row < width; row++) {
                for (int col = 0; col < width; col++) {
                    if (t.getOrientation()[row][col] == null) continue;
                    t.getOrientation()[row][col].draw(g, startingX + (TETRION_WIDTH * BLOCK_WIDTH) + 30 + (col * 35) + offset, startingY + 20 + (150 * i) + (row * 35) + offset, 35);
                }
            }
        }

        // Pause Button
        if (pauseButton.isHighlighted()) {
            g.setColor(new Color(50,50,50));
        } else {
            g.setColor(Color.black);
        }

        g.fillRect(10, 10, 50, 50);

        g.setFont(new Font("Sans Serif", Font.BOLD, 30));
        g.setColor(Color.white);
        g.drawString("||", 23, 45);
    }

    private void paintGameOver(Graphics g) {
        // Background
        g.setColor(Color.white);
        g.fillRect(0, 0, TetrisRunner.WIDTH, TetrisRunner.HEIGHT);

        // Menu
        g.setColor(new Color(50,50,50));
        g.fillRect(380, 80, 400, TetrisRunner.HEIGHT - 160);

        g.setColor(new Color(25,25,25));
        g.fillRect(400, 100, 400, TetrisRunner.HEIGHT - 160);

        // Game Over
        g.setFont(new Font("Sans Serif", Font.BOLD, 50));
        g.setColor(Color.red);
        GraphicsUtils.centerString(g, "GAME OVER", TetrisRunner.WIDTH / 2, 55);

        // Points
        g.setFont(new Font("Sans Serif", Font.BOLD, 50));
        g.setColor(Color.white);

        NumberFormat scoreFormat = NumberFormat.getNumberInstance(Locale.US);

        g.setFont(new Font("Sans Serif", Font.BOLD, 40));
        GraphicsUtils.centerString(g, "YOUR SCORE", TetrisRunner.WIDTH / 2, 150);

        g.setFont(new Font("Sans Serif", Font.BOLD, 75));
        GraphicsUtils.centerString(g, scoreFormat.format(score), TetrisRunner.WIDTH / 2, 220);

        // Buttons
        if (menuButton1P.isHighlighted()) {
            g.setColor(new Color(37,223,37));
        } else {
            g.setColor(new Color(27,142,27));
        }

        g.fillRect(420, 290, 360, 100);

        g.setFont(new Font("Sans Serif", Font.PLAIN, 40));
        g.setColor(Color.white);
        GraphicsUtils.centerString(g, "MENU", TetrisRunner.WIDTH / 2, 355);
    }

    public void lose() {
        window.gameBoard.gameState = "game_over";
    }

}
