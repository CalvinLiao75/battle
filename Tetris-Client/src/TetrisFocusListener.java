import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TetrisFocusListener implements FocusListener {
    TetrisWindow window;

    public TetrisFocusListener(TetrisWindow window) {
        this.window = window;
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (window.gameState.equals("1p")) window.gameState = "paused";
    }

    @Override
    public void focusGained(FocusEvent e) {

    }
}
