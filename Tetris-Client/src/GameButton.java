public class GameButton {
    private int x, y, width, height;
    private String gameState;
    private boolean isBeingHighlighted = false;
    private TetrisWindow window;

    public GameButton(TetrisWindow window, int x, int y, int width, int height, String gameState) {
        this.window = window;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.gameState = gameState;

        window.buttons.addButton(this);
    }

    public void highlightIfMouseInRange(int mouseX, int mouseY) {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            if (!isBeingHighlighted) window.musicUtils.playSoundEffect("move.wav");
            isBeingHighlighted = true;
        }
        else isBeingHighlighted = false;
    }

    public boolean isHighlighted() {
        return isBeingHighlighted;
    }

    public void removeHighlight() {
        isBeingHighlighted = false;
    }

    public void click() {
        window.musicUtils.playSoundEffect("addToBoard.wav");
    }

    public String getGameState() {
        return gameState;
    }
}
