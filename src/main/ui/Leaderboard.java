package ui;

import model.Game;
import model.Player;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

/*
Create a leaderboard to display all or selected scores
 */
public class Leaderboard extends JPanel {

    private Game game;
    private Map<String, List<Player>> allScores;
    private Map<String, Player> lead;
    private JComboBox filter;
    private Border outline;
    private JPanel display;

    //REQUIRES: Game
    //EFFECTS: Create a leaderboard instance
    public Leaderboard(Game g) {
        game = g;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        lead = game.getLeaderboard();
        outline = BorderFactory.createLineBorder(Color.black);
        allScores = new HashMap<>();
        filter = generateFilter();
        add(filter);
        createDisp();

    }

    //EFFECTS: Create a dropdown menu for completed map sizes
    private JComboBox generateFilter() {
        Set<String> n = lead.keySet();
        JComboBox f = new JComboBox<>();
        f.addItem("none");
        Set<String> mapSizes = new HashSet<>();
        for (String st: n) {
            Player p = lead.get(st);
            mapSizes.addAll(p.getScores().keySet());
        }
        System.out.println(mapSizes);
        for (String st: mapSizes) {
            f.addItem(st);
        }
        generateEntries(mapSizes);
        f.addActionListener(this::filterActionPerformed);
        return f;
    }

    //MODIFIES: This
    //EFFECTS: Create a record what players have won on which map size
    private void generateEntries(Set<String> mapSize) {
        Set<String> names = lead.keySet();
        for (String s: mapSize) {
            List<Player> hasScoreHere = new ArrayList<>();
            for (String k : names) {
                if (lead.get(k).getScores().containsKey(s)) {
                    Player p = lead.get(k);
                    hasScoreHere.add(p);
                }
            }
            allScores.put(s, hasScoreHere);
        }
    }

    //MODIFIES: This
    //EFFECTS: Determine which scores need to be shown
    public void filterActionPerformed(ActionEvent ae) {
        String sel = (String) filter.getSelectedItem();
        remove(display);
        validate();
        if (sel.equals("none")) {
            createDisp();
        } else {
            createDisp(sel);
        }
    }

    //MODIFIES: This
    //EFFECTS: Show all scores
    private void createDisp() {
        JPanel d = new JPanel();
        d.setLayout(new GridLayout(0, 3));
        d.add(createEntry("Map Size"));
        d.add(createEntry("Player Name"));
        d.add(createEntry("Time"));
        for (String ms: allScores.keySet()) {
            for (Player p: allScores.get(ms)) {
                for (Long l : p.getTimesForMap(ms)) {
                    d.add(createEntry(ms));
                    d.add(createEntry(p.getName()));
                    d.add(createEntry(Long.toString(l / 1000000000)));
                }
            }
        }
        add(d);
        display = d;
        validate();
    }

    //REQUIRES: A mapSize
    //MODIFIES: This
    //EFFECTS: Show scores for a given map size
    private void createDisp(String ms) {
        JPanel d = new JPanel();
        d.setLayout(new GridLayout(0, 3));
        d.add(createEntry("Map Size"));
        d.add(createEntry("Player Name"));
        d.add(createEntry("Time"));
        for (Player p: allScores.get(ms)) {
            for (Long l : p.getTimesForMap(ms)) {
                d.add(createEntry(ms));
                d.add(createEntry(p.getName()));
                d.add(createEntry(Long.toString(l / 1000000000)));
            }
        }
        add(d);
        display = d;
        validate();
    }

    //EFFECTS: Create a Boxed JLabel with given title
    private JLabel createEntry(String title) {
        JLabel entry = new JLabel(title);
        entry.setBorder(outline);
        return entry;
    }
}
