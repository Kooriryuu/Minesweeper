package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Game {
    private Grid board;
    private int height;
    private int width;
    private int bombNum;
    private List<Score> scores;
    private long start;
    private long end;

    //MODIFIES: This
    //EFFECTS: Creates a game with no scores, no start time, and no end time.
    public Game() {
        scores = new ArrayList<>();
        start = 0;
        end = 0;
        height = 4;
        width = 4;
        bombNum = 5;
    }

    //REQUIRES: A starting location
    //MODIFIES: This
    //EFFECTS: Begin the game, generating a board and opening the first cell.
    public void gameInitialization(int loc) {
        board = new Grid(height, width, bombNum);
        start = System.nanoTime();
        board.generate(loc);
        standardOpen(loc);
    }

    //MODIFIES: This
    //EFFECTS: End the timer
    public void endTimer() {
        end = System.nanoTime();
    }

    //EFFECTS: Show what string should show at that spot
    public String getChar(boolean e, int x, int y) {
        Cell c = board.getCell(y * width + x);
        if (c.isFlagged()) {
            return "⚐";
        } else if (!c.isOpen() && !e) {
            return "□";
        } else if (c.isBomb()) {
            return "B";
        } else {
            return String.valueOf(c.getNumBombs());
        }
    }

    //REQUIRES: a Grid and a valid position
    //MODIFIES: Cell
    //EFFECTS: Toggles the flag on cell at location
    private void flag(int loc) {
        Cell toFlag = board.getCell(loc);
        toFlag.toggleFlag();
    }

    //REQUIRES: a location and string
    //EFFECTS: Starts the appropriate move or moves on
    public boolean chooseMove(String choice, int loc) {
        switch (choice) {
            case "open":
                return standardOpen(loc);
            case "flag":
                flag(loc);
                return true;
            case "mid":
                return board.middleClick(loc);
        }
        return true;
    }

    //REQUIRES: A Grid and a valid position
    //MODIFIES: Cell
    //EFFECTS: Open cell at location, and surround cells if initial cell has no bombs around it
    private boolean standardOpen(int loc) {
        return !board.openFirst(loc);
    }

    //EFFECTS: Checks if the game is won
    public boolean hasWon() {
        return board.winCheck();
    }

    //EFFECTS: gets the height
    public int getHeight() {
        return height;
    }

    //REQUIRES: An int greater or equal to 4
    //MODIFIES: This
    //EFFECTS: Changes the height
    public void changeHeight(int h) {
        height = h;
    }

    //EFFECTS: Returns the width
    public int getWidth() {
        return width;
    }


    //REQUIRES: An int greater or equal to 4
    //MODIFIES: This
    //EFFECTS: Changes the width
    public void changeWidth(int w) {
        width = w;
    }

    //EFFECTS: returns total number of bombs
    public int getBombNum() {
        return bombNum;
    }

    //REQUIRES: An int greater than 4 and 10 less than the grid size
    //MODIFIES: This
    //EFFECTS: Changes the number of bombs
    public void changeBombNum(int b) {
        bombNum = b;
    }

    //EFFECTS: Get the start time
    public long getStart() {
        return start;
    }

    //EFFECTS: Get the end time
    public long getEnd() {
        return end;
    }

    //EFFECTS: Get the grid
    public Grid getGrid() {
        return board;
    }

    //REQUIRES: A decision string
    //MODIFIES: This
    //EFFECTS: If yes, add a score to the score list
    public void addTime(String s) {
        if (s.toUpperCase().equals("Y")) {
            Score sc;
            sc = new Score(end - start, height, width);
            scores.add(sc);
        }
        start = 0;
        end = 0;
    }

    //EFFECTS: Get the list of scores
    public List<Score> getScores() {
        return scores;
    }
}
