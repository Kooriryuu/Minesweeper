package ui;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
Represents the game board
 */
public class MinesweeperBoard extends JPanel implements ActionListener {
    private int gameHeight;
    private int gameWidth;
    private static final int SIZE = 40;
    private static final int BORDER = 3;
    private boolean first;
    private boolean cont;
    private Game game;
    private String move;

    //EFFECTS: Creates a MinesweeperBoard
    public MinesweeperBoard(Game g, boolean load) {
        gameHeight = g.getHeight();
        gameWidth = g.getWidth();
        this.game = g;
        first = !load;
        move = "open";
        cont = true;
        setLayout(new GridLayout(gameHeight, gameWidth, BORDER, BORDER));
        for (int col = 0; col < gameHeight; col++) {
            for (int row = 0; row < gameWidth; row++) {
                makeCell(col * gameWidth + row);
            }
        }
        if (load) {
            update();
        }
    }

    //REQUIRES: An index
    //MODIFIES: This
    //EFFECTS: Create a single cell
    private void makeCell(Integer ind) {
        BoardCell c = new BoardCell(ind);
        c.setMaximumSize(new Dimension(SIZE, SIZE));
        c.setMinimumSize(new Dimension(SIZE,SIZE));
        c.addActionListener(this::actionPerformed);
        c.setMinimumSize(new Dimension(20,20));
        c.setPreferredSize(new Dimension(100, 100));
        JPanel s = new JPanel();
        s.add(c);
        add(s);
    }

    //MODIFIES: BoardCell
    //EFFECTS: Show the appropriate label on the cell
    private void update() {
        for (Component s: this.getComponents()) {
            if (s instanceof JPanel) {
                for (Component c : ((JPanel) s).getComponents()) {
                    if (c instanceof BoardCell) {
                        BoardCell bc = (BoardCell) c;
                        String d = game.getChar(!cont, bc.getLoc() % gameWidth, bc.getLoc() / gameWidth);
                        if (d.equals("□")) {
                            bc.setLabel(d);
                        } else {
                            bc.setLabel("");
                        }
                    }
                }
            }
        }
    }

    //MODIFIES: This
    //EFFECTS: perform the selected action, and determine if the game is won
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object o = ae.getSource();
        BoardCell obj = (BoardCell) o;
        if (first && move.equals("open")) {
            game.gameInitialization(obj.getLoc());
            first = false;
        } else if (cont) {
            cont = game.chooseMove(move, obj.getLoc());
        }
        if (game.hasWon() && cont) {
            cont = false;
            game.endTimer();
            winMove();
        }
        update();
    }


    //MODIFIES: This
    //EFFECTS: Change the current command
    public void changeCommand(String n) {
        move = n;
    }

    //EFFECTS: Return of the game is still going
    public boolean getOngoing() {
        return cont;
    }

    //EFFECTS: Return whether the game has started yet
    public boolean getStarted() {
        return first;
    }

    //MODIFIES: Game
    //EFFECTS: Can save the score to the leaderboards
    private void winMove() {
        int choice = JOptionPane.showConfirmDialog(null, new JPanel(), "Would you like to save your score?",
                JOptionPane.OK_CANCEL_OPTION);
        if (choice == JOptionPane.OK_OPTION) {
            JPanel s = new JPanel();
            JTextField newName = new JTextField(9);
            s.add(new JLabel("Enter a name: "));
            s.add(newName);
            int result = JOptionPane.showConfirmDialog(null, s,
                    "Add Score", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                System.out.println(newName.getText());
                game.setPlayer(newName.getText());
                game.addTime("Y");
            }
        }
    }
}
