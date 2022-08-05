import org.json.*;
import javax.swing.*;
import java.awt.*;
import java.security.InvalidParameterException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Tetris2P extends GameBoard {
    private String id;
    private String roomKey;

    private Block[][] opponentTetrion = new Block[TETRION_HEIGHT][TETRION_WIDTH];
    private int opponentLinesBroken;
    private int grayLines;
    private int oldGrayLines;
    private String loser;

    private InternetUtilities internetUtilities;

    private GameButton
        menuButton2P = new GameButton(window, 420, 290, 360, 100, "2p") {
            @Override
            public void click() {
                super.click();
                if (gameState.equals("game_over")) window.gameState = "menu";
            }
        },
        waitingMenuButton2P = new GameButton(window, 420, 290, 360, 100, "2p") {
            @Override
            public void click() {
                super.click();
                if (gameState.equals("waiting_for_player")) {
                    disconnect();
                    window.gameState = "menu";
                }
            }
        };

    private boolean hasStartedUpdating = false;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private Runnable update = new Runnable() {
        public void run() {
            giveData();
            updateOpponent();
        }
    };

    public Tetris2P(TetrisWindow window) {
        super(window);
        gameState = "waiting_for_player";

        internetUtilities = new InternetUtilities(super.window);

        int choice = JOptionPane.showOptionDialog(null, "建立房間或加入房間", "請選擇", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"建立房間", "加入房間"}, "加入房間");
        if (choice == 0) {
            // Create room
            String output = JOptionPane.showInputDialog("請設房間密碼");
            if (output == null) {
                window.gameState = "選單";
                return;
            }
            while (output.isEmpty()) {
                output = JOptionPane.showInputDialog("Please enter something in the input to continue.");
                if (output == null) {
                    window.gameState = "選單";
                    return;
                }
            }

            createRoom(output);
        }
        else if (choice == 1) {
            // Join room
            String output = JOptionPane.showInputDialog("What is the room key?");
            if (output == null) {
                window.gameState = "menu";
                return;
            }
            while (output.isEmpty()) {
                output = JOptionPane.showInputDialog("Please enter something in the input to continue.");
                if (output == null) {
                    window.gameState = "menu";
                    return;
                }
            }

            joinRoom(output);
        }
        else {
            window.gameState = "menu";
        }
    }

    public void tick() {
        if (gameState.equals("waiting_for_player")) {
            updateOpponent();
        } else if (gameState.equals("game")) {
            super.tick();
            updateGrayLines();

            if (!hasStartedUpdating) {
                executor.scheduleAtFixedRate(update, 0, 250, TimeUnit.MILLISECONDS);
                hasStartedUpdating = true;
            }
        } else if (gameState.equals("game_over")) {
            while (loser == null) updateOpponent();
            if (hasStartedUpdating) {
                executor.shutdown();
                hasStartedUpdating = false;
            }
            disconnect();
        }
    }

    private void updateGrayLines() {
        grayLines = opponentLinesBroken - linesBroken;

        if (grayLines < oldGrayLines && oldGrayLines > 0) removeGrayLines(oldGrayLines - grayLines);
        if (grayLines >= 0 && oldGrayLines < grayLines) {
            if (oldGrayLines < 0) addGrayLines(grayLines);
            else addGrayLines(grayLines - oldGrayLines);
        }

        oldGrayLines = grayLines;
    }

    private void removeGrayLines(int lines) {
        for (int line = 0; line < lines; line ++) {
            for (int i = TETRION_HEIGHT - 1; i > 0; i--) {
                Tetrion[i] = Tetrion[i - 1];
            }
            Tetrion[0] = new Block[TETRION_WIDTH];
        }
    }

    private void addGrayLines(int lines) {
        for (int line = 0; line < lines; line ++) {
            if (!isTopRowEmpty()) {
                lose();
                return;
            }
            for (int i = 0; i < TETRION_HEIGHT - 1; i++) {
                Tetrion[i] = Tetrion[i + 1];
            }
            Tetrion[TETRION_HEIGHT - 1] = new Block[TETRION_WIDTH];
            for (int i = 0; i < TETRION_WIDTH; i ++) {
                Tetrion[TETRION_HEIGHT - 1][i] = new Block(Block.GRAY);
            }
        }
    }

    private boolean isTopRowEmpty() {
        for (int i = 0; i < Tetrion[0].length; i ++) {
            if (Tetrion[0][i] != null) return false;
        }
        return true;
    }

    private String tetrionToString(Block[][] tetrion) {
        String json = "";
        for (int row = 0; row < tetrion.length; row ++) {
            for (int col = 0; col < tetrion[row].length; col ++) {
                String color;
                if (tetrion[row][col] == null) color = "";
                else color = Integer.toString(tetrion[row][col].getColor().getRGB());
                json += color;
                if (col < tetrion[row].length - 1) json += ",";
            }
            if (row < tetrion.length - 1) json += ";";
        }
        return json;
    }

    private Block[][] stringToTetrion(String json) {
        Block[][] tetrion = new Block[20][10];
        String[] rows = json.split(";");

        for (int row = 0; row < rows.length; row ++) {
            String[] colors = rows[row].split(",");
            for (int col = 0; col < colors.length; col ++) {
                Color color;
                if (colors[col].isEmpty()) color = Block.EMPTY;
                else color = new Color(Integer.parseInt(colors[col]));
                tetrion[row][col] = new Block(color);
            }
        }
        return tetrion;
    }

    private void createRoom(String key) {
        HashMap<String, String> data = new HashMap<>();

        data.put("roomKey", key);

        String output = internetUtilities.postRequest("/create/", data);
        if (output.equals("room is already taken")) {
            internetUtilities.error("That room name is already taken. Please try again.");
            window.gameState = "menu";
        } else {
            id = output;
            roomKey = key;
        }
    }

    private void joinRoom(String key) {
        HashMap<String, String> data = new HashMap<>();

        data.put("roomKey", key);

        String output = internetUtilities.postRequest("/join/", data);
        if (output.equals("room does not exist")) {
            internetUtilities.error("That room does not exist. Please try again.");
            window.gameState = "menu";
        } else if (output.equals("room full")) {
            internetUtilities.error("Room full!");
            window.gameState = "menu";
        } else {
            id = output;
            super.gameState = "game";
            roomKey = key;
        }
    }

    public void lose() {
        updateOpponent();

        HashMap<String, String> data = new HashMap<>();

        data.put("roomKey", roomKey);
        data.put("id", id);

        String output = internetUtilities.postRequest("/lose/", data);
        if (output.equals("room does not exist")) {
            throw new InvalidParameterException("Room does not exist!");
        }

        gameState = "game_over";
    }   

    public void disconnect() {
        HashMap<String, String> data = new HashMap<>();

        data.put("roomKey", roomKey);
        data.put("id", id);

        String output = internetUtilities.postRequest("/disconnect/", data);
    }

    public void paintGame(Graphics g) {
        if (gameState.equals("waiting_for_player")) {
            paintWaiting(g);
            return;
        }
        if (gameState.equals("game_over")) {
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
        int startingX = 220;
        int startingY = (TetrisRunner.HEIGHT) - (TETRION_HEIGHT * BLOCK_WIDTH) - 30;

        g.setColor(Color.black);
        g.fillRect(startingX - 10, startingY - 10, (TETRION_WIDTH * BLOCK_WIDTH) + 20, (TETRION_HEIGHT * BLOCK_WIDTH) + 20);

        for (int row = 0; row < Tetrion.length; row++) {
            for (int col = 0; col < Tetrion[0].length; col++) {
                if (Tetrion[row][col] == null)
                    new Block(Block.EMPTY).draw(g, startingX + (BLOCK_WIDTH * col), startingY + (BLOCK_WIDTH * row), BLOCK_WIDTH);
                else
                    Tetrion[row][col].draw(g, startingX + (BLOCK_WIDTH * col), startingY + (BLOCK_WIDTH * row), BLOCK_WIDTH);
            }
        }

        // Opponent Tetrion
        // Draw the Tetrion
        int opponentStartingX = (TETRION_WIDTH * BLOCK_WIDTH) + 420;
        int opponentStartingY = (TetrisRunner.HEIGHT) - (TETRION_HEIGHT * BLOCK_WIDTH) - 30;

        g.setColor(Color.black);
        g.fillRect(opponentStartingX - 10, opponentStartingY - 10, (TETRION_WIDTH * BLOCK_WIDTH) + 20, (TETRION_HEIGHT * BLOCK_WIDTH) + 20);

        for (int row = 0; row < opponentTetrion.length; row++) {
            for (int col = 0; col < opponentTetrion[0].length; col++) {
                if (opponentTetrion[row][col] == null)
                    new Block(Block.EMPTY).draw(g, opponentStartingX + (BLOCK_WIDTH * col), opponentStartingY + (BLOCK_WIDTH * row), BLOCK_WIDTH);
                else
                    opponentTetrion[row][col].draw(g, opponentStartingX + (BLOCK_WIDTH * col), opponentStartingY + (BLOCK_WIDTH * row), BLOCK_WIDTH);
            }
        }

        currentHand.draw(g, startingX, startingY);

        // Score
        g.setColor(Color.black);
        g.fillRect(startingX - 200, startingY + ((TETRION_HEIGHT / 2) * BLOCK_WIDTH), 150, 50);

        g.setFont(new Font("Sans Serif", Font.BOLD, 20));
        g.drawString("LINES", startingX - 200, startingY + ((TETRION_HEIGHT / 2) * BLOCK_WIDTH) - 15);

        g.setFont(new Font("Sans Serif", Font.BOLD, 30));
        g.setColor(Color.white);

        NumberFormat scoreFormat = NumberFormat.getNumberInstance(Locale.US);
        g.drawString(scoreFormat.format(linesBroken), startingX - 190, startingY + ((TETRION_HEIGHT / 2) * BLOCK_WIDTH) + 35);

        // Hold
        g.setColor(Color.black);
        g.fillRect(startingX - 200, startingY + 15, 150, 150);

        g.setFont(new Font("Sans Serif", Font.BOLD, 20));
        g.drawString("HOLD", startingX - 200, startingY);

        if (hold != null) {
            int width = hold.orientations[0].length;
            int offset = ((4 - width) * 35) / 2;
            for (int row = 0; row < width; row++) {
                for (int col = 0; col < width; col++) {
                    if (hold.orientations[0][row][col] == null) continue;
                    hold.orientations[0][row][col].draw(g, startingX - 195 + (col * 35) + offset, startingY + 20 + (row * 35) + offset, 35);
                }
            }
        }

        // Queue
        g.setColor(Color.black);
        g.fillRect(startingX + (TETRION_WIDTH * BLOCK_WIDTH) + 20, startingY + 15, 160, 450);

        g.setFont(new Font("Sans Serif", Font.BOLD, 20));
        g.drawString("NEXT UP", startingX + (TETRION_WIDTH * BLOCK_WIDTH) + 20, startingY);

        for (int i = 0; i < queue.length; i++) {
            Tetromino t = queue[i];
            int width = t.getOrientation().length;
            int offset = ((4 - width) * 35) / 2;
            for (int row = 0; row < width; row++) {
                for (int col = 0; col < width; col++) {
                    if (t.getOrientation()[row][col] == null) continue;
                    t.getOrientation()[row][col].draw(g, startingX + (TETRION_WIDTH * BLOCK_WIDTH) + 30 + (col * 35) + offset, startingY + 20 + (150 * i) + (row * 35) + offset, 35);
                }
            }
        }
    }

    private void paintGameOver(Graphics g) {
        // Background
        g.setColor(Color.white);
        g.fillRect(0, 0, TetrisRunner.WIDTH, TetrisRunner.HEIGHT);

        // Menu
        g.setColor(new Color(50, 50, 50));
        g.fillRect(380, 80, 400, TetrisRunner.HEIGHT - 160);

        g.setColor(new Color(25, 25, 25));
        g.fillRect(400, 100, 400, TetrisRunner.HEIGHT - 160);

        // Game Over
        g.setFont(new Font("Sans Serif", Font.BOLD, 50));
        g.setColor(Color.red);
        GraphicsUtils.centerString(g, "GAME OVER", TetrisRunner.WIDTH / 2, 55);

        // Points
        g.setFont(new Font("Sans Serif", Font.BOLD, 50));
        g.setColor(Color.white);

        g.setFont(new Font("Sans Serif", Font.BOLD, 40));
        if (loser != null) {
            g.setColor((loser.equals(id)) ? Color.red : Color.green);
            GraphicsUtils.centerString(g, (loser.equals(id)) ? "YOU LOSE" : "YOU WIN", TetrisRunner.WIDTH / 2, 150);
        }

        // Buttons
        if (menuButton2P.isHighlighted()) {
            g.setColor(new Color(37, 223, 37));
        } else {
            g.setColor(new Color(27, 142, 27));
        }

        g.fillRect(420, 290, 360, 100);

        g.setFont(new Font("Sans Serif", Font.PLAIN, 40));
        g.setColor(Color.white);
        GraphicsUtils.centerString(g, "MENU", TetrisRunner.WIDTH / 2, 355);
    }

    private void paintWaiting(Graphics g) {
        // Background
        g.setColor(Color.white);
        g.fillRect(0, 0, TetrisRunner.WIDTH, TetrisRunner.HEIGHT);

        // Menu
        g.setColor(new Color(50, 50, 50));
        g.fillRect(380, 80, 400, TetrisRunner.HEIGHT - 160);

        g.setColor(new Color(25, 25, 25));
        g.fillRect(400, 100, 400, TetrisRunner.HEIGHT - 160);

        // Waiting for player
        g.setFont(new Font("Sans Serif", Font.BOLD, 50));
        g.setColor(Color.red);
        GraphicsUtils.centerString(g, "WAITING FOR PLAYER", TetrisRunner.WIDTH / 2, 55);

        // Buttons
        if (waitingMenuButton2P.isHighlighted()) {
            g.setColor(new Color(37, 223, 37));
        } else {
            g.setColor(new Color(27, 142, 27));
        }

        g.fillRect(420, 290, 360, 100);

        g.setFont(new Font("Sans Serif", Font.PLAIN, 40));
        g.setColor(Color.white);
        GraphicsUtils.centerString(g, "MENU", TetrisRunner.WIDTH / 2, 355);
    }

    private void giveData() {
        HashMap<String, String> data = new HashMap<>();

        data.put("roomKey", roomKey);
        data.put("id", id);
        data.put("tetrion", tetrionToString(Tetrion));
        data.put("lines", linesBroken + "");

        String output = internetUtilities.postRequest("/give/", data);
        if (output.equals("invalid room")) {
            throw new InvalidParameterException("Invalid room!");
        } else if (output.equals("invalid id")) {
            throw new InvalidParameterException("Invalid ID!");
        }
    }

    private void updateOpponent() {
        HashMap<String, String> data = new HashMap<>();

        data.put("roomKey", roomKey);
        data.put("id", id);

        String output = internetUtilities.postRequest("/info/", data);
        if (output.equals("room does not exist")) {
            throw new InvalidParameterException("Invalid room!");
        } else if (output.equals("you are not in this game")) {
            throw new InvalidParameterException("You are not in this game!");
        } else {
            try {
                JSONObject obj = new JSONObject(output);

                opponentLinesBroken = obj.getInt("lines");
                if (obj.get("gamestate") != JSONObject.NULL) gameState = obj.getString("gamestate");
                if (obj.get("loser") != JSONObject.NULL) loser = obj.getString("loser");
                if (obj.get("tetrion") != JSONObject.NULL) {
                    opponentTetrion = stringToTetrion(obj.getString("tetrion"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}