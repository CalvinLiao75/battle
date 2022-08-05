import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//遊戲首頁介面
public class TetrisWindow extends JPanel implements ActionListener {
    // JFrame
    JFrame frame;

   
    public String gameState = "menu";
    public GameBoard gameBoard;
    public MusicUtils musicUtils = new MusicUtils();
    private int howToPlayPage = 1;

    // 按鈕
    public GameButtons buttons = new GameButtons(this);
    private GameButton
            menu1PButton = new GameButton(this, 420, 290, 360, 100, "menu") {
                @Override
                public void click() {
                    super.click();
                    if (gameBoard == null) gameState = "1p";
                }
            },
            menu2PButton = new GameButton(this, 420, 400, 360, 100, "menu") {
                @Override
                public void click() {
                    super.click();
                    if (gameBoard == null) gameState = "2p";
                }
            },
            menuHowToPlayButton = new GameButton(this, 420, 510, 360, 100, "menu") {
                @Override
                public void click() {
                    super.click();
                    gameState = "how_to_play";
                    howToPlayPage = 1;
                }
            },
            unpauseButton = new GameButton(this, 420, 290, 360, 100, "paused") {
                @Override
                public void click() {
                    super.click();
                    gameState = "1p";
                }
            },
            backButton = new GameButton(this, 10, 10, 50, 50, "how_to_play") {
                @Override
                public void click() {
                    super.click();
                    gameState = "menu";
                }
            },
            nextButton = new GameButton(this, TetrisRunner.WIDTH - 80, (TetrisRunner.HEIGHT / 2) - 25, 50, 50, "how_to_play") {
                @Override
                public void click() {
                    super.click();
                    gameState = "how_to_play";
                    howToPlayPage = howToPlayPage == 1 ? 2 : 1;
                }
            };

    Timer timer = new Timer(TetrisRunner.FPS_DELAY, this);

    public TetrisWindow(JFrame frame) {
        this.frame = frame;

        timer.start();

        addKeyListener(new TetrisKeyListener(this));
        addFocusListener(new TetrisFocusListener(this));

        TetrisMouseListener tml = new TetrisMouseListener(this);
        addMouseMotionListener(tml);
        addMouseListener(tml);

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        // Music
       musicUtils.playMusic("tetris.wav");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameState.equals("menu")) {
            gameBoard = null;
        }
        if (gameState.equals("1p")) {
            if (gameBoard == null) gameBoard = new Tetris1P(this);
            else gameBoard.tick();
        } else if (gameState.equals("2p")) {
            if (gameBoard == null) gameBoard = new Tetris2P(this);
            else gameBoard.tick();
        }

        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameState.equals("menu")) {
            paintMenu(g);
        }
        else if (gameState.equals("how_to_play")) {
            paintHowToPlay(g);
        }
        else if (gameState.equals("2p") || gameState.equals("1p")) {
            if (gameBoard != null) gameBoard.paintGame(g);
        }
        else if (gameState.equals("paused")) {
            paintPaused(g);
        }
    }

    private void paintMenu(Graphics g) {
        // Background
        g.setColor(Color.black);
        g.fillRect(0, 0, TetrisRunner.WIDTH, TetrisRunner.HEIGHT);

        // Menu
        g.setColor(new Color(25,25,25));
        g.fillRect(380, 80, 400, TetrisRunner.HEIGHT - 80);

        g.setColor(new Color(25,25,25));
        g.fillRect(400, 100, 400, TetrisRunner.HEIGHT - 100);

        // Logo
        g.setFont(new Font("Sans Serif", Font.BOLD, 100));
        g.setColor(new Color(255, 0, 0));
        g.drawString("T", 420, 200);
        g.setColor(new Color(255, 214, 43));
        g.drawString("E", 485, 200);
        g.setColor(Color.cyan);
        g.drawString("T", 550, 200);
        g.setColor(new Color(106, 238, 54));
        g.drawString("R", 615, 200);
        g.setColor(new Color(161,17,150));
        g.drawString("I", 680, 200);
        g.setColor(Color.orange);
        g.drawString("S", 715, 200);

    
        g.setFont(new Font("Sans Serif", Font.PLAIN, 32));
        g.setColor(Color.white);
        g.drawString("", 432, 260);

        // 按鈕
            // 1P
        if (menu1PButton.isHighlighted()) {
            g.setColor(new Color(37,223,37));
        } else {
            g.setColor(new Color(27,142,27));
        }

        g.fillRect(420, 290, 360, 100);

        g.setFont(new Font("Sans Serif", Font.PLAIN, 40));
        g.setColor(Color.white);
        GraphicsUtils.centerString(g, "單人遊戲", TetrisRunner.WIDTH / 2, 355);

            // 2P
        if (menu2PButton.isHighlighted()) {
            g.setColor(new Color(37,223,37));
        } else {
            g.setColor(new Color(27,142,27));
        }

        g.fillRect(420, 400, 360, 100);

        g.setFont(new Font("Sans Serif", Font.PLAIN, 40));
        g.setColor(Color.white);
        GraphicsUtils.centerString(g, "雙人對戰", TetrisRunner.WIDTH / 2, 465);

            //玩法
        if (menuHowToPlayButton.isHighlighted()) {
            g.setColor(new Color(37,223,37));
        } else {
            g.setColor(new Color(27,142,27));
        }

        g.fillRect(420, 510, 360, 100);

        g.setFont(new Font("Sans Serif", Font.PLAIN, 40));
        g.setColor(Color.white);
        GraphicsUtils.centerString(g, "遊戲說明", TetrisRunner.WIDTH / 2, 575);

        
    }

    private void paintPaused(Graphics g) {
        // 背景
        g.setColor(Color.white);
        g.fillRect(0, 0, TetrisRunner.WIDTH, TetrisRunner.HEIGHT);

        // Menu
        g.setColor(new Color(50,50,50));
        g.fillRect(380, 80, 400, TetrisRunner.HEIGHT - 160);

        g.setColor(new Color(25,25,25));
        g.fillRect(400, 100, 400, TetrisRunner.HEIGHT - 160);

        
        g.setFont(new Font("Sans Serif", Font.BOLD, 90));
        g.setColor(Color.white);
        g.drawString("暫停", 420, 200);

        // 按鈕
        if (unpauseButton.isHighlighted()) {
            g.setColor(new Color(37,223,37));
        } else {
            g.setColor(new Color(27,142,27));
        }

        g.fillRect(420, 290, 360, 100);

        g.setFont(new Font("Sans Serif", Font.PLAIN, 40));
        g.setColor(Color.white);
        g.drawString("繼續", 525, 355);
    }
//玩法裡面
    private void paintHowToPlay(Graphics g) {
        
        g.setColor(Color.white);
        g.fillRect(0, 0, TetrisRunner.WIDTH, TetrisRunner.HEIGHT);

       
        g.setFont(new Font("Sans Serif", Font.BOLD, 50));
        g.setColor(Color.black);
        GraphicsUtils.centerString(g, "HOW TO PLAY " + howToPlayPage + "P", TetrisRunner.WIDTH / 2, 55);

        
        g.setFont(new Font(" Serif", Font.BOLD, 28));
        if (howToPlayPage == 1) GraphicsUtils.multilineString(g, "1.按下左和右箭鍵移動。\n按下上鍵進行旋轉。\n按下鍵向下移動一個空格，或者按空格鍵將方塊一直向下移動。\n一整行充滿了方塊時，它將消失並且您將獲得積分。\n試著擊敗您的高分！\n一次消除的線越多，獲得的積分就越多！ \n例如，如果一次消除一行，則會比一次消除四行的得分要少。\n。每消除10條線，您的難度就會增加1！\n隨著難度的提高，您獲得更多的積分。\n您可以通過按C鍵來保存一個方塊供以後使用。再次按C，將其\n替換為您現在的方塊。您只能每轉一圈保留一次。\n要暫停，請按ESCAPE鍵或按左上方的暫停按鈕。", 70, 100);
        else if (howToPlayPage == 2) GraphicsUtils.multilineString(g, "1.與1P相同的所有規則均適用。但是，沒有分數或級別，\n隨著時間的推移，遊戲不會提高速度。\n一旦兩個人使用同一個房間鑰匙進入一個房間，比賽將\n自動開始。無論哪個競爭對手中消除的行數最少，\n 你的的底部將出現臨時的灰色墊高方塊。\n第一個失敗的人（通過\n視窗頂部無法再放置任何方塊）失去了戰鬥，房間將自動關閉。", 70, 100);

           
        if (backButton.isHighlighted()) {
            g.setColor(new Color(50,50,50));
        } else {
            g.setColor(Color.black);
        }

        g.fillRect(10, 10, 50, 50);

        g.setFont(new Font("Sans Serif", Font.BOLD, 30));
        g.setColor(Color.white);
        g.drawString("◀", 21, 45);

        
        if (nextButton.isHighlighted()) {
            g.setColor(new Color(50,50,50));
        } else {
            g.setColor(Color.black);
        }

        g.fillRect(TetrisRunner.WIDTH - 80, (TetrisRunner.HEIGHT / 2) - 25, 50, 50);

        g.setFont(new Font("Sans Serif", Font.BOLD, 30));
        g.setColor(Color.white);
        g.drawString((howToPlayPage == 1 ? 2 : 1) + "P", TetrisRunner.WIDTH - 75, (TetrisRunner.HEIGHT / 2) + 10);
    }
}
