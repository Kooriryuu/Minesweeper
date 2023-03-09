package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private Player p1, p2;

    @BeforeEach
    public void setUp() {
        p1 = new Player("a");
        p2 = new Player("b");
    }

    @Test
    public void testConstructor() {
        assertEquals("a", p1.getName());
        assertEquals(0, p1.getScores().size());
        assertEquals("b", p2.getName());
        assertEquals(0, p2.getScores().size());
    }

    @Test
    public void testAddScoreNoScores() {
        p1.addScore(5,6,1000);
        assertEquals(1,p1.getTimesForMap("5x6").size());
        assertEquals(1000, p1.getTimesForMap("5x6").get(0));
    }

    @Test
    public void testAddScoreSameMapSize() {
        p1.addScore(5,6,1000);
        p1.addScore(5, 6, 2000);
        assertEquals(2,p1.getTimesForMap("5x6").size());
        assertEquals(2000, p1.getTimesForMap("5x6").get(1));
    }

    @Test
    public void testAddScoreDiffMapSize() {
        p1.addScore(5,6,1000);
        p1.addScore(4,5, 209);
        assertEquals(1,p1.getTimesForMap("4x5").size());
        assertEquals(209, p1.getTimesForMap("4x5").get(0));
    }

    @Test
    public void testGetScoreNoMap() {
        assertEquals(new ArrayList<Long>(), p1.getTimesForMap("H"));
    }

    @Test
    public void testHashCode() {
        Player p3 = new Player("a");
        assertTrue(p1.hashCode() == p3.hashCode());
    }

    @Test
    public void testHashCodeFail() {
        Integer i = 1;
        assertFalse(p1.hashCode() == i.hashCode());
    }

    @Test
    public void testEqualsSame() {
        assertEquals(p1, p1);
    }

    @Test
    public void testEqualsDiffType() {
        assertFalse(p1.equals(1));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(p1.equals(null));
    }

    @Test
    public void testEqualsNameFail() {
        assertFalse(p1.equals(p2));
    }

    @Test
    public void testEqualsScoreFail() {
        Player p3 = new Player("a");
        p3.addScore(3,3,10);
        assertFalse(p1.equals(p3));
    }
}
