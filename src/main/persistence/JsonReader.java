package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Read data from a json file to build the previous game state.
 * Template provided at
 */

public class JsonReader {
    private String source;

    //EFFECTS: Construct a reader to be able to read from a file
    public JsonReader(String origin) {
        source = origin;
    }

    public Game read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseGame(jsonObject);
    }

    //EFFECTS: read source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    private Game parseGame(JSONObject jsonObject) {
        Game g = new Game();
        g.setGrid(parseBoard(jsonObject.getJSONObject("board")));
        g.changeWidth(jsonObject.getInt("width"));
        g.changeHeight(jsonObject.getInt("height"));
        g.changeBombNum(jsonObject.getInt("bombNum"));
        g.setDeltaT(jsonObject.getLong("deltaT"));
        g.setPlayer(jsonObject.getString("player"));
        Map<String, Player> pl = new HashMap<>();
        JSONObject playList = jsonObject.getJSONObject("playerList");
        Iterator keys = playList.keys();
        while (keys.hasNext()) {
            String name = (String) keys.next();
            Player p = parsePlayer(playList.getJSONObject(name));
            pl.put(name, p);
        }
        g.setLeaderboard(pl);
        return g;
    }

    private Player parsePlayer(JSONObject jsonObject) {
        Player p = new Player(jsonObject.getString("username"));
        Map<String, List<Long>> score = new HashMap<>();
        for (Object o: jsonObject.getJSONArray("scores")) {
            JSONObject obj = (JSONObject) o;
            List<Long> s = new ArrayList<>();
            JSONArray t = obj.getJSONArray("times");
            for (int i = 0; i < t.length(); i++) {
                Long time = t.getLong(i);
                s.add(time);
            }
            score.put(obj.getString("dim"), s);
        }
        p.setScores(score);
        return p;
    }

    private Grid parseBoard(JSONObject jsonObject) {
        int h = jsonObject.getInt("height");
        int w = jsonObject.getInt("width");
        int bombNum = jsonObject.getInt("totalBombs");
        Grid previous = new Grid(h, w, bombNum);
        List<Cell> board = new ArrayList<>();
        JSONArray oldBoard = jsonObject.getJSONArray("board");
        for (Object o: oldBoard) {
            JSONObject cell = (JSONObject) o;
            board.add(parseCell(cell));
        }
        previous.setBoard(board);
        for (Cell c: board) {
            c.findNearbyBombs();
        }
        return previous;
    }

    private Cell parseCell(JSONObject jsonObject) {
        Cell c = new Cell();
        if (jsonObject.getBoolean("isBomb")) {
            c.makeBomb();
        }
        if (jsonObject.getBoolean("flagged")) {
            c.toggleFlag();
        }
        if (jsonObject.getBoolean("open")) {
            c.openCell();
        }
        return c;
    }

}
