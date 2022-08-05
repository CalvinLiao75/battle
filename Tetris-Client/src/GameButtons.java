import java.util.ArrayList;

public class GameButtons {
    public ArrayList<GameButton> buttons = new ArrayList<GameButton>();
    private TetrisWindow window;

    public GameButtons(TetrisWindow window) {
        this.window = window;
    }

    public void addButton(GameButton b) {
        if (!buttons.contains(b)) buttons.add(b);
    }

    public void checkAllButtons(int mouseX, int mouseY) {
        for (GameButton b : buttons) {
            if (b.getGameState().equals(window.gameState)) b.highlightIfMouseInRange(mouseX, mouseY);
        }
    }

    public void clickAllButtons() {
        for (GameButton b : buttons) {
            if (b.getGameState().equals(window.gameState) && b.isHighlighted()) {
                b.click();
                b.removeHighlight();
            }
        }
    }
}
