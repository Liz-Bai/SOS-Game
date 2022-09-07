

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.io.File;
import java.io.IOException;



@SuppressWarnings("serial")
public class SOSGUI extends JFrame {

    public static final int CELL_SIZE = 50;
    public static final int GRID_WIDTH = 1;
    public static final int GRID_WIDHT_HALF = GRID_WIDTH / 2;

    public static final int CELL_PADDING = CELL_SIZE / 6;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
    public static final int SYMBOL_STROKE_WIDTH = 8;

    private GameBoardCanvas gameBoardCanvas;
    private JLabel gameStatusBar;

    private SOSGame game;

    private boolean flag;

    JFrame jf;
    JRadioButton blueButton1;
    JRadioButton blueButton2;
    JRadioButton redButton1;
    JRadioButton redButton2;

    public SOSGUI() {
        this(new SOSGame(8));
        try {
            File file = new File("record.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        jf = this;
    }

    public SOSGUI(SOSGame game) {
        this.game = game;
        setContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setTitle("SOS");
        setVisible(true);
        jf = this;
        flag = false;
    }

    private void setContentPane() {
        gameBoardCanvas = new GameBoardCanvas();
        gameBoardCanvas
                .setPreferredSize(new Dimension(CELL_SIZE * game.getTotalRows(), CELL_SIZE * game.getTotalColumns()));
        gameStatusBar = new JLabel("  ");
        gameStatusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
        gameStatusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(gameBoardCanvas, BorderLayout.CENTER);
        p.add(gameStatusBar, BorderLayout.SOUTH);
        contentPane.add(p, BorderLayout.CENTER);

        JPanel p1 = new JPanel();
        JLabel sosLabel = new JLabel("SOS");
        JRadioButton sosButton1 = new JRadioButton("Simple game", true);
        JRadioButton sosButton2 = new JRadioButton("General game");
        JLabel boardLabel = new JLabel("Board size");
        JTextArea text = new JTextArea("");
        text.setPreferredSize(new Dimension(20, 20));
        JButton confirm = new JButton("ok");
        confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jf.dispose();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        SOSGUI gui = new SOSGUI(new SOSGame(Integer.valueOf(text.getText())));
                        new TestThread(gui).start();
                    }
                });

            }
        });
        ButtonGroup sosGroup = new ButtonGroup();
        sosGroup.add(sosButton1);
        sosGroup.add(sosButton2);
        sosButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.setCurrentGameType(SOSGame.GameType.Simple);
            }
        });
        sosButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.setCurrentGameType(SOSGame.GameType.General);
            }
        });
        p1.add(sosLabel);
        p1.add(sosButton1);
        p1.add(sosButton2);
        p1.add(boardLabel);
        p1.add(text);
        p1.add(confirm);
        contentPane.add(p1, BorderLayout.NORTH);

        JPanel p2 = new JPanel();
        JRadioButton blueHuman = new JRadioButton("Blue player", true);
        JRadioButton blueComputer = new JRadioButton("Computer");
        blueComputer.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                game.isBuleComputer = blueComputer.isSelected();
            }
        });
        blueButton1 = new JRadioButton("S", true);
        blueButton2 = new JRadioButton("O");
        ButtonGroup bluePlayerGroup = new ButtonGroup();
        bluePlayerGroup.add(blueButton1);
        bluePlayerGroup.add(blueButton2);
        ButtonGroup blueGroup = new ButtonGroup();
        blueGroup.add(blueHuman);
        blueGroup.add(blueComputer);
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
        p2.add(blueHuman);
        p2.add(blueButton1);
        p2.add(blueButton2);
        p2.add(blueComputer);

        contentPane.add(p2, BorderLayout.WEST);
        p2.setPreferredSize(new Dimension(100, 400));

        JPanel p3 = new JPanel();
        JRadioButton redHuman = new JRadioButton("Red player", true);
        JRadioButton redComputer = new JRadioButton("Computer");
        redComputer.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                game.isRedComputer = redComputer.isSelected();
            }
        });
        redButton1 = new JRadioButton("S", true);
        redButton2 = new JRadioButton("O");
        ButtonGroup redPlayerGroup = new ButtonGroup();
        redPlayerGroup.add(redButton1);
        redPlayerGroup.add(redButton2);
        ButtonGroup redGroup = new ButtonGroup();
        redGroup.add(redHuman);
        redGroup.add(redComputer);
        p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));
        p3.add(redHuman);
        p3.add(redButton1);
        p3.add(redButton2);
        p3.add(redComputer);
        JButton newGameButton = new JButton("new Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.dispose();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        SOSGUI gui = new SOSGUI(new SOSGame(game.TOTALROWS));
                        new TestThread(gui).start();
                    }
                });
            }

        });
        p3.add(newGameButton);
        JButton recordButton = new JButton("   Record   ");
        recordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Process process = Runtime.getRuntime().exec("cmd.exe /c notepad.exe record.txt");
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

        });
        p3.add(recordButton);

        contentPane.add(p3, BorderLayout.EAST);
        p3.setPreferredSize(new Dimension(100, 400));

    }

    class GameBoardCanvas extends JPanel {

        GameBoardCanvas() {
            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (game.getGameState() == SOSGame.GameState.PLAYING) {
                        int rowSelected = e.getY() / CELL_SIZE;
                        int colSelected = e.getX() / CELL_SIZE;
                        char turn = game.getTurn();
                        int type;
                        if (turn == 'B')
                            type = blueButton1.isSelected() ? 0 : 1;
                        else
                            type = redButton1.isSelected() ? 0 : 1;
                        game.makeMove(rowSelected, colSelected, type);
                    } else {
                        game.resetGame();
                    }
                    repaint();
                }
            });
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);
            drawGridLines(g);
            drawBoard(g);
            drawLines(g);
            printStatusBar();
        }

        private void drawGridLines(Graphics g) {
            g.setColor(Color.LIGHT_GRAY);
            for (int row = 1; row < game.getTotalRows(); ++row) {
                g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDHT_HALF, CELL_SIZE * game.getTotalRows() - 1, GRID_WIDTH,
                        GRID_WIDTH, GRID_WIDTH);
            }
            for (int col = 1; col < game.getTotalColumns(); ++col) {
                g.fillRoundRect(CELL_SIZE * col - GRID_WIDHT_HALF, 0, GRID_WIDTH,
                        CELL_SIZE * game.getTotalColumns() - 1, GRID_WIDTH, GRID_WIDTH);
            }
        }

        private void drawBoard(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int row = 0; row < game.getTotalRows(); ++row) {
                for (int col = 0; col < game.getTotalColumns(); ++col) {
                    int x1 = col * CELL_SIZE + CELL_PADDING;
                    int y1 = row * CELL_SIZE + CELL_PADDING;
                    if (game.getCell(row, col) == SOSGame.Cell.S) {
//						int x2 = (col + 1) * CELL_SIZE - CELL_PADDING;
//						int y2 = (row + 1) * CELL_SIZE - CELL_PADDING;
                        g2d.drawArc(x1 + CELL_SIZE / 5, y1, CELL_SIZE / 2 - CELL_PADDING, CELL_SIZE / 2 - CELL_PADDING,
                                60, 210);
                        g2d.drawArc(x1 + CELL_SIZE / 5, y1 + CELL_SIZE / 2 - CELL_PADDING, CELL_SIZE / 2 - CELL_PADDING,
                                CELL_SIZE / 2 - CELL_PADDING, 240, 210);
                    } else if (game.getCell(row, col) == SOSGame.Cell.O) {
                        g2d.drawOval(x1 + CELL_SIZE / 10, y1, (int) (SYMBOL_SIZE * 0.8), SYMBOL_SIZE);
                    }
                }
            }
        }

        private void drawLines(Graphics g) {
            ArrayList<ArrayList<Integer>> info = game.getSosInfo();
            Graphics2D g2d = (Graphics2D) g;
            if (info == null)
                return;
            for (ArrayList<Integer> it : info) {
                if (it.size() > 1) {
                    if (it.get(0) == 0)
                        g2d.setColor(Color.BLUE);
                    else
                        g2d.setColor(Color.RED);
                    for (int i = 1; i < it.size(); i += 4) {
                        int x1 = it.get(i + 1) * CELL_SIZE + CELL_SIZE / 2;
                        int y1 = it.get(i) * CELL_SIZE + CELL_SIZE / 2;
                        int x2 = it.get(i + 3) * CELL_SIZE + CELL_SIZE / 2;
                        int y2 = it.get(i + 2) * CELL_SIZE + CELL_SIZE / 2;
                        g2d.drawLine(x1, y1, x2, y2);
                    }
                }

            }
        }

        private void printStatusBar() {
            if (game.getGameState() == SOSGame.GameState.PLAYING) {
                gameStatusBar.setForeground(Color.BLACK);
                if (game.getTurn() == 'B') {
                    gameStatusBar.setText("Blue's Turn");
                } else {
                    gameStatusBar.setText("Red's Turn");
                }
            } else if (game.getGameState() == SOSGame.GameState.DRAW) {
                gameStatusBar.setForeground(Color.RED);
                gameStatusBar.setText("It's a Draw! Click to play again.");
                if (!flag) {
                    game.writeLine("It's a Draw!\n\n");
                    flag = true;
                }
            } else if (game.getGameState() == SOSGame.GameState.BLUE_WON) {
                gameStatusBar.setForeground(Color.RED);
                gameStatusBar.setText("'Blue' Won! Click to play again.");
                if (!flag) {
                    game.writeLine("'Blue' Won!\n\n");
                    flag = true;
                }
            } else if (game.getGameState() == SOSGame.GameState.RED_WON) {
                gameStatusBar.setForeground(Color.RED);
                gameStatusBar.setText("'Red' Won! Click to play again.");
                if (!flag) {
                    game.writeLine("'Red' Won!\n\n");
                    flag = true;
                }
            }
        }

    }

    public static class TestThread extends Thread {
        private SOSGUI gui;

        public TestThread(SOSGUI gui) {
            this.gui = gui;
        }

        public void run() {
            while (true) {
                try {
                    if (gui.game.currentGameState != SOSGame.GameState.PLAYING || gui.game.getNumberOfEmptyCells() == 0)
                        break;
                    sleep(10);
                    if (gui.game.isBuleComputer && gui.game.getTurn() == 'B') {
                        gui.game.makeRandomMove();
                        gui.repaint();
                    }
                    if (gui.game.isRedComputer && gui.game.getTurn() == 'R') {
                        gui.game.makeRandomMove();
                        gui.repaint();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SOSGUI gui = new SOSGUI();
                new TestThread(gui).start();
            }
        });
    }
}
