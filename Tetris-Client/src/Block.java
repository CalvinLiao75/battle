import java.awt.*;
//各個方塊顏色設定
public class Block {
    public static final Color
            EMPTY = Color.black,
            CYAN = Color.cyan,
            BLUE = new Color(37,115,255),
            ORANGE = Color.orange,
            YELLOW = new Color(255, 255, 0),
            GREEN = new Color(106, 238, 54),
            PURPLE = new Color(218,23,204),
            GRAY = Color.gray,
            RED = Color.red;

    private Color color;

    private Color getAccent(Color c) {
        if (color.equals(EMPTY)) {
            return new Color(50, 50, 50);
        } else if (color.equals(CYAN)) {
            return new Color(56,186,241);
        } else if (color.equals(BLUE)) {
            return new Color(22,68,149);
        } else if (color.equals(ORANGE)) {
            return new Color(174,122,24);
        } else if (color.equals(YELLOW)) {
            return new Color(173, 173, 0);
        } else if (color.equals(GREEN)) {
            return new Color(69, 134, 45);
        } else if (color.equals(PURPLE)) {
            return new Color(158,17,148);
        } else if (color.equals(RED)) {
            return new Color(167,0,0);
        }
        return Color.black;
    }

    public Block(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void draw(Graphics g, int x, int y, int width) {
        g.setColor(getAccent(color));
        g.fillRect(x, y, width, width);
        g.setColor(color);
        g.fillRect(x + 1, y + 1, width - 2, width - 2);
    }

    public void drawOutline(Graphics g, int x, int y, int width) {
        g.setColor(Color.white);
        g.fillRect(x, y, width, width);
        g.setColor(new Color(25, 25, 25));
        g.fillRect(x + 2, y + 2, width - 4, width - 4);
    }
}
