package model;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

//represents a player and their associated scores

public class Player implements Writable {

    private String username;
    private Map<String, List<Long>> scores;

    //REQUIRES: A string
    //MODIFIES: This
    //EFFECTS: Creates a player with the given string
    public Player(String name) {
        username = name;
        scores = new HashMap<>();
    }


    //MODIFIES: This
    //EFFECTS: Adds a score to the player
    public void addScore(int h, int w, long time) {
        String k = String.valueOf(h) + "x" + String.valueOf(w);
        if (scores.containsKey(k)) {
            scores.get(k).add(time);
        } else {
            List<Long> temp = new ArrayList<>();
            temp.add(time);
            scores.put(k, temp);
        }
    }

    //EFFECTS: Gets all scores of player
    public Map<String, List<Long>> getScores() {
        return scores;
    }

    //EFFECTS: Gets username of player
    public String getName() {
        return this.username;
    }

    //EFFECTS: Gets the corresponding times to a grid size
    public List<Long> getTimesForMap(String k) {
        if (scores.containsKey(k)) {
            return scores.get(k);
        }
        return new ArrayList<Long>();
    }

    //MODIFIES: This
    //EFFECTS: Set the player score
    public void setScores(Map<String, List<Long>> sc) {
        scores = sc;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("username", username);
        JSONArray a = new JSONArray();
        for (Map.Entry<String, List<Long>> e: scores.entrySet()) {
            JSONObject s = new JSONObject();
            s.put("dim", e.getKey());
            JSONArray s2 = new JSONArray();
            for (long l: e.getValue()) {
                s2.put(l);
            }
            s.put("times",s2);
            a.put(s);
        }
        json.put("scores", a);
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
        Player player = (Player) o;
        return Objects.equals(username, player.username) && Objects.equals(scores, player.scores);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, scores);
    }
}
