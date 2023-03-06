package model;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class ScoreTest {

    Score sc1, sc2, sc3, sc4;

    @BeforeEach
    public void setup() {
        sc1 = new Score(2345, 4,5);
        sc2 = new Score(2345, 4,5);
        sc3 = new Score(1234, 4, 5);
        sc4 = new Score (2345, 6,1);
    }

    @Test
    public void testConstructor() {
        assertEquals(4, sc1.getHeight());
        assertEquals(5, sc1.getWidth());
        assertEquals(2345, sc1.getTime());
    }

    @Test
    public void testEqualsSameObject() {
        assertEquals(sc1, sc1);
    }

    @Test
    public void testEqualsDiffTypes() {
        assertNotEquals(sc1, new Cell());
    }

    @Test
    public void testEqualsSameTypeDiffFields() {
        assertFalse(sc1.equals(sc3));
    }

    @Test
    public void testEqualsAllDiffFields() {
        assertNotEquals(sc3, sc4);
    }

    @Test
    public void testEqualsDiffObjects() {
        assertEquals(sc1, sc2);
    }





}
