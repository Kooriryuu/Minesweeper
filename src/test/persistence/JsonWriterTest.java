package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;


public class JsonWriterTest {

    @Test
    public void testWriterInvalidFile() {
        try {
            Game g = new Game();
            JsonWriter writer = new JsonWriter("./data/mydoesnotexist\0.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            //pass
        }
    }

    @Test
    public void testWriterGameNotStarted() {
        try {
            Game g = new Game();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyGame.json");
            writer.open();
            writer.write(g);
            writer.close();
            JsonReader reader = new JsonReader("./data/testWriterEmptyGame.json");
            Game g2 = reader.read();
            assertEquals(g, g2);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testWriterGameInProgressNoScores() {
        try {
            Game g = new Game();
            g.gameInitialization(6);
            g.getGrid().getCell(10).toggleFlag();
            g.endTimer();
            g.setDeltaT(g.getEnd() - g.getStart());
            JsonWriter writer = new JsonWriter("./data/testWriterGameNoScores.json");
            writer.open();
            writer.write(g);
            writer.close();
            JsonReader reader = new JsonReader("./data/testWriterGameNoScores.json");
            Game g2 = reader.read();
            assertEquals(g, g2);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testWriterGameWithScore() {
        try {
            Game g = new Game();
            g.gameInitialization(6);
            g.endTimer();
            g.setDeltaT(g.getEnd() - g.getStart());
            g.addTime("y");
            JsonWriter writer = new JsonWriter("./data/testWriterGameScores.json");
            writer.open();
            writer.write(g);
            writer.close();
            JsonReader reader = new JsonReader("./data/testWriterGameScores.json");
            Game g2 = reader.read();
            assertEquals(g, g2);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testWriterGameWithTwoScores() {
        try {
            Game g = new Game();
            g.gameInitialization(6);
            g.endTimer();
            g.setDeltaT(g.getEnd() - g.getStart());
            g.addTime("y");
            g.gameInitialization(3);
            g.endTimer();
            g.addTime("Y");
            JsonWriter writer = new JsonWriter("./data/testWriterGameScores.json");
            writer.open();
            writer.write(g);
            writer.close();
            JsonReader reader = new JsonReader("./data/testWriterGameScores.json");
            Game g2 = reader.read();
            assertEquals(g, g2);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
    @Test
    public void testWriterGameWithTwoPlayers() {
        try {
            Game g = new Game();
            g.gameInitialization(6);
            g.endTimer();
            g.setDeltaT(g.getEnd() - g.getStart());
            g.addTime("y");
            g.setPlayer("h");
            g.gameInitialization(3);
            g.endTimer();
            g.addTime("y");
            JsonWriter writer = new JsonWriter("./data/testWriterGameScores.json");
            writer.open();
            writer.write(g);
            writer.close();
            JsonReader reader = new JsonReader("./data/testWriterGameScores.json");
            Game g2 = reader.read();
            assertEquals(g, g2);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

}
