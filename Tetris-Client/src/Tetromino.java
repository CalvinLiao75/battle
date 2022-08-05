import java.awt.*;
import java.util.Arrays;
//方塊形狀(包含底部)    移動聲音設置
public class Tetromino {
    private Block[][] orientation1;
    private Block[][] orientation2;
    private Block[][] orientation3;
    private Block[][] orientation4;
    
    public Block[][][] orientations;

    public String type;
    private TetrisWindow window;

    private int currentOrientation = 0;
    private int milliseconds = 0;
    private int timeBeforePlaced = 1000;
    private int absoluteTimeBeforePlaced = 3000;//最低點有3秒可以反應
    private int x;
    private int y;

    public Tetromino(String type, TetrisWindow window) {
        this.window = window;

        if (!Arrays.asList(TetrisRunner.TETROMINOES).contains(type)) {
            throw new RuntimeException("Invalid Tetromino type \"" + type + "\"");
        }
        this.type = type;
        switch (type) {
            case "I":      //青色一條
                this.orientation1 = new Block[][]{
                        {null, null, null, null},
                        {new Block(Block.CYAN), new Block(Block.CYAN), new Block(Block.CYAN), new Block(Block.CYAN)},
                        {null, null, null, null},
                        {null, null, null, null}
                };
                this.orientation2 = new Block[][]{
                        {null, null, new Block(Block.CYAN), null},
                        {null, null, new Block(Block.CYAN), null},
                        {null, null, new Block(Block.CYAN), null},
                        {null, null, new Block(Block.CYAN), null}
                };
                this.orientation3 = new Block[][]{
                        {null, null, null, null},
                        {null, null, null, null},
                        {new Block(Block.CYAN), new Block(Block.CYAN), new Block(Block.CYAN), new Block(Block.CYAN)},
                        {null, null, null, null}
                };
                this.orientation4 = new Block[][]{
                        {null, new Block(Block.CYAN), null, null},
                        {null, new Block(Block.CYAN), null, null},
                        {null, new Block(Block.CYAN), null, null},
                        {null, new Block(Block.CYAN), null, null}
                };
                break;
            case "J":  //藍色L型
   
                this.orientation1 = new Block[][]{
                        {new Block(Block.BLUE), null, null},
                        {new Block(Block.BLUE), new Block(Block.BLUE), new Block(Block.BLUE)},
                        {null, null, null}
                };
                this.orientation2 = new Block[][]{
                        {null, new Block(Block.BLUE), new Block(Block.BLUE)},
                        {null, new Block(Block.BLUE), null},
                        {null, new Block(Block.BLUE), null}
                };
                this.orientation3 = new Block[][]{
                        {null, null, null},
                        {new Block(Block.BLUE), new Block(Block.BLUE), new Block(Block.BLUE)},
                        {null, null, new Block(Block.BLUE)}
                };
                this.orientation4 = new Block[][]{
                        {null, new Block(Block.BLUE), null},
                        {null, new Block(Block.BLUE), null},
                        {new Block(Block.BLUE), new Block(Block.BLUE), null}
                };
                break;
            case "L":     //橘色L
                this.orientation1 = new Block[][]{
                        {null, null, new Block(Block.ORANGE)},
                        {new Block(Block.ORANGE), new Block(Block.ORANGE), new Block(Block.ORANGE)},
                        {null, null, null}
                };
                this.orientation2 = new Block[][]{
                        {null, new Block(Block.ORANGE), null},
                        {null, new Block(Block.ORANGE), null},
                        {null, new Block(Block.ORANGE), new Block(Block.ORANGE)}
                };
                this.orientation3 = new Block[][]{
                        {null, null, null},
                        {new Block(Block.ORANGE), new Block(Block.ORANGE), new Block(Block.ORANGE)},
                        {new Block(Block.ORANGE), null, null}
                };
                this.orientation4 = new Block[][]{
                        {new Block(Block.ORANGE), new Block(Block.ORANGE), null},
                        {null, new Block(Block.ORANGE), null},
                        {null, new Block(Block.ORANGE), null}
                };
                break;
            case "O": //正方形
                this.orientation1 = new Block[][]{
                        {new Block(Block.YELLOW), new Block(Block.YELLOW)},
                        {new Block(Block.YELLOW), new Block(Block.YELLOW)}
                };
                this.orientation2 = new Block[][]{
                        {new Block(Block.YELLOW), new Block(Block.YELLOW)},
                        {new Block(Block.YELLOW), new Block(Block.YELLOW)}
                };
                this.orientation3 = new Block[][]{
                        {new Block(Block.YELLOW), new Block(Block.YELLOW)},
                        {new Block(Block.YELLOW), new Block(Block.YELLOW)}
                };
                this.orientation4 = new Block[][]{
                        {new Block(Block.YELLOW), new Block(Block.YELLOW)},
                        {new Block(Block.YELLOW), new Block(Block.YELLOW)}
                };
                break;
            case "S":   //s型
                this.orientation1 = new Block[][]{
                        {null, new Block(Block.GREEN), new Block(Block.GREEN)},
                        {new Block(Block.GREEN), new Block(Block.GREEN), null},
                        {null, null, null}
                };
                this.orientation2 = new Block[][]{
                        {null, new Block(Block.GREEN), null},
                        {null, new Block(Block.GREEN), new Block(Block.GREEN)},
                        {null, null, new Block(Block.GREEN)}
                };
                this.orientation3 = new Block[][]{
                        {null, null, null},
                        {null, new Block(Block.GREEN), new Block(Block.GREEN)},
                        {new Block(Block.GREEN), new Block(Block.GREEN), null}
                };
                this.orientation4 = new Block[][]{
                        {new Block(Block.GREEN), null, null},
                        {new Block(Block.GREEN), new Block(Block.GREEN), null},
                        {null, new Block(Block.GREEN), null}
                };
                break;
            case "T":     //T型
                this.orientation1 = new Block[][]{
                        {null, new Block(Block.PURPLE), null},
                        {new Block(Block.PURPLE), new Block(Block.PURPLE), new Block(Block.PURPLE)},
                        {null, null, null}
                };
                this.orientation2 = new Block[][]{
                        {null, new Block(Block.PURPLE), null},
                        {null, new Block(Block.PURPLE), new Block(Block.PURPLE)},
                        {null, new Block(Block.PURPLE), null}
                };
                this.orientation3 = new Block[][]{
                        {null, null, null},
                        {new Block(Block.PURPLE), new Block(Block.PURPLE), new Block(Block.PURPLE)},
                        {null, new Block(Block.PURPLE), null}
                };
                this.orientation4 = new Block[][]{
                        {null, new Block(Block.PURPLE), null},
                        {new Block(Block.PURPLE), new Block(Block.PURPLE), null},
                        {null, new Block(Block.PURPLE), null}
                };
                break;
            case "Z":    //z型
                this.orientation1 = new Block[][]{
                        {new Block(Block.RED), new Block(Block.RED), null},
                        {null, new Block(Block.RED), new Block(Block.RED)},
                        {null, null, null}
                };
                this.orientation2 = new Block[][]{
                        {null, null, new Block(Block.RED)},
                        {null, new Block(Block.RED), new Block(Block.RED)},
                        {null, new Block(Block.RED), null}
                };
                this.orientation3 = new Block[][]{
                        {null, null, null},
                        {new Block(Block.RED), new Block(Block.RED), null},
                        {null, new Block(Block.RED), new Block(Block.RED)}
                };
                this.orientation4 = new Block[][]{
                        {null, new Block(Block.RED), null},
                        {new Block(Block.RED), new Block(Block.RED), null},
                        {new Block(Block.RED), null, null}
                };
                break;
        }
        orientations = new Block[][][]{ orientation1, orientation2, orientation3, orientation4 };

        int offset;
        int width = getOrientation()[0].length;
        if (width % 2 == 0) {
            offset = (width / 2);
        } else {
            offset = ((width / 2) + 1);
        }
        x = (GameBoard.TETRION_WIDTH / 2) - offset;
        y = 0;
    }

    public void tick() {
        while (y > getMaxY()) {
            y --;
        }

        if (timeBeforePlaced <= 0 || absoluteTimeBeforePlaced <= 0) {
            addToBoard();
        }
        else if (y == getMaxY()) {
            timeBeforePlaced -= TetrisRunner.FPS_DELAY;
            absoluteTimeBeforePlaced -= TetrisRunner.FPS_DELAY;
        }
        else {
            timeBeforePlaced = 1000;
            milliseconds += TetrisRunner.FPS_DELAY;
            if (milliseconds > (window.gameBoard.tetrominoDelay * 1000)) {
                if (y < getMaxY()) y ++;
                milliseconds = 0;
            }
        }
    }

    public void addToBoard() {
        window.musicUtils.playMusic("addToBoard.wav");

        timeBeforePlaced = 1000;
        absoluteTimeBeforePlaced = 3000;

        window.gameBoard.haveHeldThisTurn = false;
        if (getMaxY() >= 0) y = getMaxY();

        for (int row = 0; row < getOrientation().length; row ++) {
            for (int col = 0; col < getOrientation()[0].length; col ++) {
                if (getOrientation()[row][col] == null) continue;
                try {
                    window.gameBoard.Tetrion[row + y][col + x] = getOrientation()[row][col];
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    window.gameBoard.lose();
                }
            }
        }

        window.gameBoard.nextCycle();
    }
//旋轉
    public void rotate() {
        window.musicUtils.playMusic("rotate.wav");

        if (currentOrientation == orientations.length - 1) currentOrientation = 0;
        else currentOrientation ++;

        while (x < getMinX()) x ++;
        while (x > getMaxX()) x --;
        while (y > getMaxY()) y --;
    }
//下移加分
    public void moveDown() {
        if (y < getMaxY()) {
            y ++;
            window.musicUtils.playMusic("move.wav");
            if (window.gameBoard instanceof Tetris1P) {
                ((Tetris1P) window.gameBoard).score ++;
            }
        }
    }

    public void moveUp() {
        y --;
    }

    public void moveLeft() {
        timeBeforePlaced = 1000;
        if (x > getMinX() && isValidAtCoordinates(x - 1, y)) {
            x --;
            window.musicUtils.playMusic("move.wav");
        }
    }

    public void moveRight() {
        timeBeforePlaced = 1000;
        if (x < getMaxX() && isValidAtCoordinates(x + 1, y)) {
            x ++;
            window.musicUtils.playMusic("move.wav");
        }
    }
//輪廓方塊(白邊)
    public void draw(Graphics g, int startingX, int startingY) {
        // Draw outline
        int maxY = getMaxY();
        for (int row = 0; row < getOrientation().length; row ++) {
            for (int col = 0; col < getOrientation()[0].length; col ++) {
                if ((maxY * GameBoard.BLOCK_WIDTH) + (row * GameBoard.BLOCK_WIDTH) < 0) continue;
                if (getOrientation()[row][col] != null) getOrientation()[row][col].drawOutline(g, startingX + (x * GameBoard.BLOCK_WIDTH) + (col * GameBoard.BLOCK_WIDTH), startingY + (maxY * GameBoard.BLOCK_WIDTH) + (row * GameBoard.BLOCK_WIDTH), GameBoard.BLOCK_WIDTH);
            }
        }

        // 上面
        for (int row = 0; row < getOrientation().length; row ++) {
            for (int col = 0; col < getOrientation()[0].length; col ++) {
                if ((y * GameBoard.BLOCK_WIDTH) + (row * GameBoard.BLOCK_WIDTH) < 0) continue;
                if (getOrientation()[row][col] != null) getOrientation()[row][col].draw(g, startingX + (x * GameBoard.BLOCK_WIDTH) + (col * GameBoard.BLOCK_WIDTH), startingY + (y * GameBoard.BLOCK_WIDTH) + (row * GameBoard.BLOCK_WIDTH), GameBoard.BLOCK_WIDTH);
            }
        }
    }

    public Block[][] getOrientation() {
        return orientations[currentOrientation];
    }

    private boolean columnIsEmpty(int column) {
        for (Block[] row : getOrientation()) {
            if (row[column] != null) return false;
        }
        return true;
    }

    private boolean rowIsEmpty(int row) {
        for (Block block : getOrientation()[row]) {
            if (block != null) return false;
        }
        return true;
    }

    private int getMinX() {
        int minX = 0;
        for (int i = 0; i < getOrientation()[0].length; i ++) {
            if (columnIsEmpty(i)) minX --;
            else break;
        }
        return minX;
    }

    private int getMaxX() {
        int maxX = GameBoard.TETRION_WIDTH - getOrientation()[0].length;
        for (int i = getOrientation()[0].length - 1; i > 0; i --) {
            if (columnIsEmpty(i)) maxX ++;
            else break;
        }
        return maxX;
    }

    private int getMaxY() {
        int maxY = GameBoard.TETRION_HEIGHT - getOrientation().length;
        for (int i = getOrientation().length - 1; i > 0; i --) {
            if (rowIsEmpty(i)) maxY ++;
            else break;
        }
        while (true) {
            while (!isValidAtCoordinates(x, maxY)) maxY --;

            int tempMaxY = maxY;
            boolean allClear = true;

            while (tempMaxY > y) {
                if (!isValidAtCoordinates(x, tempMaxY--)) {
                    allClear = false;
                    break;
                }
            }
            if (!allClear) maxY --;
            else break;
        }

        return maxY;
    }

    public void jumpScore() {
        if (window.gameBoard instanceof Tetris1P) {
            ((Tetris1P) window.gameBoard).score += 2 * (getMaxY() - y);
        }
    }

    private boolean isValidAtCoordinates(int x, int y) {
        for (int row = 0; row < getOrientation().length; row ++) {
            for (int col = 0; col < getOrientation()[0].length; col ++) {
                if (getOrientation()[row][col] == null) continue;
                if (row + y < 0) continue;
                if (window.gameBoard.Tetrion[row + y][col + x] != null) return false;
            }
        }
        return true;
    }

    public boolean isValidAtCoordinates() {
        return isValidAtCoordinates(x, y);
    }
//隨機產生方塊
    public static String getRandomTetromino() {
        return TetrisRunner.TETROMINOES[((int) (Math.random() * TetrisRunner.TETROMINOES.length))];
    }

    public String getType() {
        return type;
    }

    public boolean equals(Tetromino other) {
        return other.getType().equals(this.getType());
    }
}
