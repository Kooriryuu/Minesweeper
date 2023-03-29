package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.Game;
import persistence.JsonReader;
import persistence.JsonWriter;


/*
The Main window of the game application. Represents the home panel.
 */
public class MainMenu extends JFrame implements ActionListener {

    private Game current;
    private Container background;
    private CardLayout layout;
    private MinesweeperBoard board;
    private JScrollPane scroll;
    private Timer time;
    private JLabel timeText;

    //EFFECTS: Create a MainMenu
    public MainMenu() {
        super();
        current = new Game();
        getLeaderboard();
        background = getContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        layout = new CardLayout();
        background.setLayout(layout);
        background.add(menuSetUp());
        setBounds(400, 200,500,500);
        setVisible(true);
    }

    //EFFECTS: Creates the main menu window
    private JPanel menuSetUp() {
        JPanel center = new JPanel();
        List<String> labels = Arrays.asList("Play", "Load", "Leaderboard", "Settings", "Quit");
        for (String l: labels) {
            JButton b = new JButton(l);
            center.add(b);
            b.addActionListener(this::actionPerformed);
            b.setActionCommand(l);
        }
        center.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.setAlignmentY(Component.CENTER_ALIGNMENT);
        return center;
    }


    //EFFECTS: Performs the selected command
    @Override
    public void actionPerformed(ActionEvent ae) {
        String option = ae.getActionCommand();
        boolean loaded = false;
        switch (option) {
            case "Load":
                updateBoard();
                loaded = true;
            case "Play":
                makeGameWindow(loaded);
                break;
            case "Settings":
                openSettings();
                break;
            case "Leaderboard":
                makeLeaderboard();
                break;
            case "Quit":
                //saveScores();
                //System.out.println(current.getLeaderboard());
                System.exit(0);
                break;
        }
        System.out.println(option);
    }

    //MODIFIES: MinesweeperBoard
    //EFFECTS; Performs the selected command
    public void menuActionPerformed(ActionEvent ae) {
        String sel = ae.getActionCommand();
        if (sel.equals("change")) {
            JComboBox cb = (JComboBox) ae.getSource();
            String selected = (String) cb.getSelectedItem();
            if (selected.equals("Chording")) {
                board.changeCommand("mid");
            } else if (selected.equals("Flag Cell")) {
                board.changeCommand("flag");
            } else {
                board.changeCommand("open");
            }
        } else if (sel.equals("save")) {
            if (board.getOngoing()) {
                save(current);
            }
        } else if (sel.equals("return")) {
            backtrack(scroll);
            saveScores();
        }
    }

    //MODIFIES: JLabel
    //EFFECTS: Updates the timer under certain conditions
    public void timerAction(ActionEvent ae) {
        long cur = System.nanoTime();
        if (current.getStart() != 0) {
            Long t = (current.getDeltaT() + (cur - current.getStart())) / 1000000000;
            timeText.setText(Long.toString(t));
            timeText.validate();
        }
        if (!board.getOngoing()) {
            time.stop();
        }
    }

    //MODIFIES: This
    //EFFECTS: Return to the main menu
    private void backtrack(Component c) {
        layout.first(background);
        background.remove(c);
        background.repaint();
        background.revalidate();
    }

    //MODIFIES: This
    //EFFECTS: Creates a leaderboard display
    private void makeLeaderboard() {
        Leaderboard l = new Leaderboard(current);
        System.out.println(current.getLeaderboard());
        JPanel lb = new JPanel();
        JButton ret = new JButton("Back");
        lb.add(l);
        lb.add(ret);
        background.add(lb);
        layout.next(background);
        ret.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backtrack(lb);
            }
        });

    }

    //MODIFIES: JsonFile
    //EFFECTS: Saves scores to file
    private void saveScores() {
        JsonReader reader = new JsonReader("./data/standardGameSave.json");
        try {
            Game g = reader.read();
            current.setGrid(g.getGrid());
            save(current);
        } catch (IOException e) {
            System.out.println("file doesn't exist");
        }
    }

    //MODIFIES: JsonFile
    //EFFECTS: Saves game to file
    private static void save(Game g) {
        JsonWriter writer = new JsonWriter("./data/standardGameSave.json");
        try {
            writer.open();
            writer.write(g);
            writer.close();
            System.out.println("saved!");
        } catch (IOException e) {
            System.out.println("unable to save file");
        }
    }

    //MODIFIES: this
    //EFFECTS: Creates the game panel
    private void makeGameWindow(boolean loaded) {
        board = new MinesweeperBoard(current, loaded);
        scroll = new JScrollPane(board, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        JScrollBar v = scroll.getVerticalScrollBar();
        JScrollBar h = scroll.getHorizontalScrollBar();
        InputMap keysVert = v.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        InputMap keysHor = h.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        keysVert.put(KeyStroke.getKeyStroke("S"), "positiveUnitIncrement");
        keysVert.put(KeyStroke.getKeyStroke("W"), "negativeUnitIncrement");
        keysHor.put(KeyStroke.getKeyStroke("D"), "positiveUnitIncrement");
        keysHor.put(KeyStroke.getKeyStroke("A"), "negativeUnitIncrement");
        background.add(scroll);
        scroll.setColumnHeaderView(makeGameOptions());
        layout.next(background);
    }

    //MODIFIES: This
    //EFFECTS: makes the header for the game.
    private JPanel makeGameOptions() {
        JPanel side = new JPanel();
        JButton ret = new JButton("Return");
        ret.addActionListener(this::menuActionPerformed);
        ret.setActionCommand("return");
        String[] opt = {"Open Cell", "Flag Cell", "Chording"};
        JComboBox moves = new JComboBox(opt);
        moves.addActionListener(this::menuActionPerformed);
        moves.setActionCommand("change");
        JButton save = new JButton("Save");
        save.addActionListener(this::menuActionPerformed);
        save.setActionCommand("save");
        time = new Timer(100, this::timerAction);
        time.setRepeats(true);
        time.start();
        timeText = new JLabel("0");
        side.add(ret);
        side.add(moves);
        side.add(save);
        side.add(new JLabel("Time taken: "));
        side.add(timeText);
        side.setAlignmentX(Component.CENTER_ALIGNMENT);
        return side;
    }

    //REQUIRES: a non empty file
    //MODIFIES: This
    //EFFECTS: loads the saved board into the current game
    private void updateBoard() {
        JsonReader reader = new JsonReader("./data/standardGameSave.json");
        try {
            Game g = reader.read();
            g.setLeaderboard(current.getLeaderboard());
            current = g;
        } catch (IOException e) {
            System.out.println("file doesn't exist");
        }

    }

    //REQUIRES: a non-empty file
    //MODIFIES: This
    //EFFECTS: retrieves saved scores
    private void getLeaderboard() {
        JsonReader reader = new JsonReader("./data/standardGameSave.json");
        try {
            Game g = reader.read();
            current.setLeaderboard(g.getLeaderboard());
        } catch (IOException e) {
            System.out.println("file doesn't exist");
        }
    }

    //EFFECTS: Opens a popup window to change settings
    private void openSettings() {
        JPanel s = new JPanel();
        JTextField forHeight = new JTextField(9);
        JTextField forWidth = new JTextField(9);
        JTextField forBombs = new JTextField(9);
        s.add(new JLabel("Width: "));
        s.add(forWidth);
        s.add(new JLabel("Height: "));
        s.add(forHeight);
        s.add(new JLabel("Bombs: "));
        s.add(forBombs);
        int result = JOptionPane.showConfirmDialog(null, s,
                "Update Settings", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            verifyResponse(forWidth.getText(), forHeight.getText(),
                    forBombs.getText());
        }
    }

    //MODIFIES: Game
    //EFFECTS: Change settings for game
    private void verifyResponse(String w, String h, String b) {
        try {
            int wid = withinBounds(w);
            current.changeWidth(wid);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(new JPanel(), "Invalid Width");
        }
        try {
            int hei = withinBounds(h);
            current.changeHeight(hei);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(new JPanel(), "Invalid Height");
        }
        try {
            int bn = Integer.parseInt(b);
            if (bn > 4 && bn < current.getHeight() * current.getWidth() - 10) {
                current.changeBombNum(bn);
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(new JPanel(), "Invalid Bomb Amount");
        }
    }

    //EFFECTS: Verify that the setting change is valid
    private int withinBounds(String n) throws NumberFormatException {
        int num = Integer.parseInt(n);
        if (num >= 4) {
            return num;
        }
        throw new NumberFormatException();
    }
}
