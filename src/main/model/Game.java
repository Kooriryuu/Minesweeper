package model;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

// Represents the over all game and handles moves

public class Game implements Writable {
    private Grid board;
    private int height;
    private int width;
    private int bombNum;
    private Map<String, Player> playerList;
    private long start;
    private long end;
    private String name;
    private long deltaT;
    private EventLog log;

    //MODIFIES: This
    //EFFECTS: Creates a game with no scores, no start time, and no end time.
    public Game() {
        playerList = new HashMap<>();
        start = 0;
        end = 0;
        height = 4;
        width = 4;
        bombNum = 5;
        deltaT = 0;
        board = new Grid(height, width, bombNum);
        name = "";
        log = EventLog.getInstance();
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
        boolean b = !board.openFirst(loc);
        return b;
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
        log.logEvent(new Event("Changed height to " + Integer.toString(h)));
        height = h;
    }

    //EFFECTS: Returns the width
    public int getWidth() {
        return width;
    }

    //EFFECTS: Starts the timer
    public void startTimer() {
        start = System.nanoTime();
    }

    //REQUIRES: An int greater or equal to 4
    //MODIFIES: This
    //EFFECTS: Changes the width
    public void changeWidth(int w) {
        log.logEvent(new Event("Changed width to " + Integer.toString(w)));
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
        log.logEvent(new Event("Changed number of bombs to " + Integer.toString(b)));
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

    //EFFECTS: Set the grid
    public void setGrid(Grid b) {
        board = b;
    }

    //REQUIRES: A username
    //MODIFIES: This
    //EFFECTS: Sets the current player
    public void setPlayer(String name) {
        this.name = name;
        if (!playerList.containsKey(name)) {
            Player p = new Player(name);
            playerList.put(name, p);
        }
    }

    //EFFECTS: Gets the current player
    public String getCurrentPlayer() {
        return this.name;
    }

    //MODIFIES: This
    //EFFECTS: Set the previously elapsed time
    public void setDeltaT(long delT) {
        deltaT = delT;
    }

    //EFFECTS: Get the already elapsed time
    public long getDeltaT() {
        return deltaT;
    }

    //REQUIRES: A decision string
    //MODIFIES: This
    //EFFECTS: If yes, add a score to the score list
    public void addTime(String s) {
        if (s.toUpperCase().equals("Y")) {
            long totalTime = end - start + deltaT;
            Player p;
            log.logEvent(new Event("Added a time for " + name));
            if (playerList.containsKey(name)) {
                p = playerList.get(name);
            } else {
                p = new Player(name);
                playerList.put(name, p);
            }
            p.addScore(height, width, totalTime);
        }
        start = 0;
        deltaT = 0;
        end = 0;
    }

    //EFFECTS: Get the list of scores
    public Map<String, Player> getLeaderboard() {
        return playerList;
    }

    //MODIFIES: This
    //EFFECTS: Load the player scores
    public void setLeaderboard(Map<String, Player> leaderboard) {
        playerList = leaderboard;
    }

    public void showEvents() {
        for (Event e: log) {
            System.out.println(e.toString());
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("board", board.toJson());
        json.put("height", height);
        json.put("width", width);
        json.put("bombNum", bombNum);
        JSONObject a = new JSONObject();
        for (Map.Entry<String, Player> p: playerList.entrySet()) {
            a.put(p.getKey(), p.getValue().toJson());
        }
        json.put("playerList", a);
        json.put("player", name);
        endTimer();
        deltaT = end - start;
        json.put("deltaT", end - start);
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game game = (Game) o;
        return height == game.height && width == game.width && bombNum == game.bombNum && deltaT == game.deltaT
                && board.equals(game.board) && Objects.equals(playerList, game.playerList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, height, width, bombNum, playerList, deltaT);
    }
}
