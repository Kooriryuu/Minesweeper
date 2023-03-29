package ui;

import javax.swing.*;

public class BoardCell extends JButton {
    private int loc;

    public BoardCell(int i) {
        super();
        loc = i;
    }

    public int getLoc() {
        return loc;
    }
}
