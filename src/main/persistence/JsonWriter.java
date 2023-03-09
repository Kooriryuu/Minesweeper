package persistence;

import model.Game;
import org.json.JSONObject;

import java.io.*;

public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    //EFFECTS: Constructs a writer to write to the destination file
    public JsonWriter(String dest) {
        this.destination = dest;
    }

    //MODIFIES: This
    //EFFECTS: Opens the file, throws error if file isn't found
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    //MODIFIES: This
    //EFFECTS: Closes the writer
    public void close() {
        writer.close();
    }

    //MODIFIES: This
    //EFFECTS: writes JSON representation of game to the file
    public void write(Game g) {
        JSONObject json = g.toJson();
        saveToFile(json.toString(TAB));
    }

    //MODIFIES: This
    //EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
