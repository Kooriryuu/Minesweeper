package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;


public class JavaReaderTest {

    @Test
    public void testFileNotExist() {
        JsonReader reader = new JsonReader("./data/oops.json");
        try {
            Game g = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            //pass
        }
    }

    @Test
    public void readEmptyFile() {
        JsonReader reader = new JsonReader("./data/testEmptyGame.json");
        try {
            Game g = new Game();
            Game g2 = reader.read();
            assertEquals(g, g2);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
