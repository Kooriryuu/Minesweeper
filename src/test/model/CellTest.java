package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {
    private Cell c1, c2, c3, c4;

    @BeforeEach
    public void setUp() {
        c1 = new Cell();
        c2 = new Cell();
        c3 = new Cell();
        c4 = new Cell();
    }

    @Test
    public void testCellConstructor() {
        assertFalse(c1.isFlagged());
        assertFalse(c1.isBomb());
        assertFalse(c1.isOpen());
        assertFalse(c2.isFlagged());
        assertFalse(c2.isBomb());
        assertFalse(c2.isOpen());
    }

    @Test
    public void testMakeBomb() {
        c1.makeBomb();
        assertTrue(c1.isBomb());
        assertFalse(c2.isBomb());
    }

    @Test
    public void testSetSurroundings() {
        List<Cell> around = new ArrayList<>();
        around.add(c2);
        around.add(c3);
        c1.setSurrounding(around);
        assertEquals(2, c1.getSurrounding().size());
        assertEquals(c2, c1.getSurrounding().get(0));
        assertEquals(c3, c1.getSurrounding().get(1));
    }

    @Test
    public void testFindOneNearbyBomb() {
        List<Cell> around = new ArrayList<>();
        around.add(c2);
        around.add(c3);
        c1.setSurrounding(around);
        c2.makeBomb();
        c1.findNearbyBombs();
        assertEquals(1, c1.getNumBombs());
    }

    @Test
    public void testFindNoNearbyBomb() {
        List<Cell> around = new ArrayList<>();
        around.add(c2);
        around.add(c3);
        c1.setSurrounding(around);
        c1.findNearbyBombs();
        assertEquals(0, c1.getNumBombs());
    }

    @Test
    public void testFindMultipleNearbyBombs() {
        List<Cell> around = new ArrayList<>();
        around.add(c2);
        around.add(c3);
        around.add(c4);
        c1.setSurrounding(around);
        for (Cell c: around) {
            c.makeBomb();
        }
        c1.findNearbyBombs();
        assertEquals(3, c1.getNumBombs());
    }

    @Test
    public void testOpenCell() {
        c1.openCell();
        assertTrue(c1.isOpen());
    }

    @Test
    public void testAlreadyOpenCell() {
        c1.openCell();
        c1.openCell();
        assertTrue(c1.isOpen());
    }

    @Test
    public void testToggleFlagOnce() {
        c1.toggleFlag();
        assertTrue(c1.isFlagged());
    }

    @Test
    public void testToggleFlagTwice() {
        c1.toggleFlag();
        assertTrue(c1.isFlagged());
        c1.toggleFlag();
        assertFalse(c1.isFlagged());
    }

}