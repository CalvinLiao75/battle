import java.awt.*;

public class GraphicsUtils {
    public static void centerString(Graphics g, String s, int XPos, int YPos){
        int stringLen = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
        int start = -stringLen / 2;
        g.drawString(s, start + XPos, YPos);
    }

    public static void multilineString(Graphics g, String text, int x, int y) {
        for (String line : text.split("\n"))
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
    }
}
