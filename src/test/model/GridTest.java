package model;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class GridTest {
    private Grid board1, board2, board3;


    @BeforeEach
    public void setUp() {
        board1 = new Grid(4, 4, 4);
        board2 = new Grid(6, 5, 20);
        board2.generate(20);
    }

    @Test
    public void testConstructor() {
        assertEquals(16, board1.getBoard().size());
        assertEquals(4, board1.getHeight());
        assertEquals(4, board1.getWidth());
        assertEquals(30, board2.getBoard().size());
        assertEquals(6, board2.getHeight());
        assertEquals(5, board2.getWidth());
    }

    @Test
    public void testSetAroundTopCorners() {
        List<Cell> topLeft1 = new ArrayList<>();
        topLeft1.add(board1.getCell(1));
        topLeft1.add(board1.getCell(4));
        topLeft1.add(board1.getCell(5));
        assertEquals(topLeft1, board1.getCell(0).getSurrounding());
        List<Cell> topRight1 = new ArrayList<>();
        topRight1.add(board1.getCell(2));
        topRight1.add(board1.getCell(6));
        topRight1.add(board1.getCell(7));
        assertEquals(topRight1, board1.getCell(3).getSurrounding());
        List<Cell> topLeft2 = new ArrayList<>();
        topLeft2.add(board2.getCell(1));
        topLeft2.add(board2.getCell(5));
        topLeft2.add(board2.getCell(6));
        assertEquals(topLeft2, board2.getCell(0).getSurrounding());
        List<Cell> topRight2 = new ArrayList<>();
        topRight2.add(board2.getCell(3));
        topRight2.add(board2.getCell(8));
        topRight2.add(board2.getCell(9));
        assertEquals(topRight2, board2.getCell(4).getSurrounding());
    }

    @Test
    public void testSetAroundBottomCorners() {
        List<Cell> botLeft1 = new ArrayList<>();
        botLeft1.add(board1.getCell(13));
        botLeft1.add(board1.getCell(8));
        botLeft1.add(board1.getCell(9));
        assertEquals(botLeft1, board1.getCell(12).getSurrounding());
        List<Cell> botRight1 = new ArrayList<>();
        botRight1.add(board1.getCell(14));
        botRight1.add(board1.getCell(10));
        botRight1.add(board1.getCell(11));
        assertEquals(botRight1, board1.getCell(15).getSurrounding());
        List<Cell> botLeft2 = new ArrayList<>();
        botLeft2.add(board2.getCell(26));
        botLeft2.add(board2.getCell(20));
        botLeft2.add(board2.getCell(21));
        assertEquals(botLeft2, board2.getCell(25).getSurrounding());
        List<Cell> botRight2 = new ArrayList<>();
        botRight2.add(board2.getCell(28));
        botRight2.add(board2.getCell(23));
        botRight2.add(board2.getCell(24));
        assertEquals(botRight2, board2.getCell(29).getSurrounding());
    }

    @Test
    public void testGenerate() {
        board1.generate(8);
        List<Integer> placed = new ArrayList<>();
        for (int i = 0; i < board1.getBoard().size(); i++) {
            if (board1.getCell(i).isBomb()) {
                placed.add(i);
            }
        }
        assertEquals(4, placed.size());
        assertEquals(4, board1.getTotalBombs());
    }

    @Test
    public void testGenerateTooManyBombs() {
        Grid board3 = new Grid(4, 4, 10);
        board3.generate(10);
        List<Integer> placed = new ArrayList<>();
        for (int i = 0; i < board3.getBoard().size(); i++) {
            if (board3.getCell(i).isBomb()) {
                placed.add(i);
            }
        }
        assertEquals(6, placed.size());
        assertEquals(6, board3.getTotalBombs());
    }

    @Test
    public void testOpenCellWithNoBombsAroundIt() {
        board1.getCell(5).makeBomb();
        board1.getCell(11).makeBomb();
        for (Cell c: board1.getBoard()) {
            c.findNearbyBombs();
        }
        assertFalse(board1.openFirst(12));
        assertTrue(board1.getCell(12).isOpen());
        assertTrue(board1.getCell(8).isOpen());
        assertTrue(board1.getCell(9).isOpen());
        assertTrue(board1.getCell(10).isOpen());
        assertTrue(board1.getCell(13).isOpen());
        assertTrue(board1.getCell(14).isOpen());
        assertFalse(board1.getCell(4).isOpen());
        assertFalse(board1.getCell(5).isOpen());
        assertFalse(board1.getCell(6).isOpen());
        assertFalse(board1.getCell(7).isOpen());
        assertFalse(board1.getCell(11).isOpen());
        assertFalse(board1.getCell(15).isOpen());
    }

    @Test
    public void testOpenCellWithBombsNearby() {
        board1.getCell(5).makeBomb();
        board1.getCell(10).makeBomb();
        for (Cell c: board1.getBoard()) {
            c.findNearbyBombs();
        }
        assertFalse(board1.openFirst(12));
        assertTrue(board1.getCell(12).isOpen());
        assertTrue(board1.getCell(8).isOpen());
        assertTrue(board1.getCell(9).isOpen());
        assertFalse(board1.getCell(10).isOpen());
        assertTrue(board1.getCell(13).isOpen());
        assertFalse(board1.getCell(14).isOpen());
        assertFalse(board1.getCell(4).isOpen());
        assertFalse(board1.getCell(5).isOpen());
        assertFalse(board1.getCell(6).isOpen());
        assertFalse(board1.getCell(7).isOpen());
        assertFalse(board1.getCell(11).isOpen());
        assertFalse(board1.getCell(15).isOpen());
    }

    @Test
    public void testOpenCellWithBomb() {
        board1.getCell(4).makeBomb();
        for (Cell c: board1.getBoard()) {
            c.findNearbyBombs();
        }
        assertTrue(board1.openFirst(4));

    }


    @Test
    public void testMiddleClickOnClosedCell() {
        for (int i = 0; i < 8; i++) {
            board1.getCell(i).makeBomb();
        }
        assertTrue(board1.middleClick(9));
        for (int i = 0; i < 16; i++) {
            Cell c = board1.getCell(i);
            assertFalse(c.isOpen());
        }
    }

    @Test
    public void testMiddleClickOnFlaggedCell() {
        for (int i = 0; i < 8; i++) {
            board1.getCell(i).makeBomb();
        }
        board1.getCell(9).toggleFlag();
        assertTrue(board1.middleClick(9));
        for (int i = 0; i < 16; i++) {
            Cell c = board1.getCell(i);
            assertFalse(c.isOpen());
        }
    }

    @Test
    public void testMiddleClickDoesNothing() {
        board1.getCell(5).makeBomb();
        board1.getCell(7).makeBomb();
        for (Cell c: board1.getBoard()) {
            c.findNearbyBombs();
        }
        board1.openFirst(13);
        assertTrue(board1.middleClick(10));
        assertTrue(board1.getCell(13).isOpen());
        assertTrue(board1.getCell(14).isOpen());
        assertTrue(board1.getCell(9).isOpen());
        assertTrue(board1.getCell(11).isOpen());
        assertFalse(board1.getCell(5).isOpen());
        assertFalse(board1.getCell(6).isOpen());
        assertFalse(board1.getCell(7).isOpen());
    }

    @Test
    public void testMiddleClickFlagsCells() {
        board1.getCell(5).makeBomb();
        board1.getCell(7).makeBomb();
        for (Cell c: board1.getBoard()) {
            c.findNearbyBombs();
        }
        board1.openFirst(13);
        board1.openFirst(6);
        assertTrue(board1.middleClick(10));
        assertTrue(board1.getCell(13).isOpen());
        assertTrue(board1.getCell(14).isOpen());
        assertTrue(board1.getCell(9).isOpen());
        assertTrue(board1.getCell(11).isOpen());
        assertFalse(board1.getCell(5).isOpen());
        assertTrue(board1.getCell(6).isOpen());
        assertTrue(board1.getCell(15).isOpen());
        assertFalse(board1.getCell(7).isOpen());
        assertTrue(board1.getCell(5).isFlagged());
        assertTrue(board1.getCell(7).isFlagged());
    }

    @Test
    public void testMiddleClickFlagsWithOneFlagged() {
        board1.getCell(5).makeBomb();
        board1.getCell(7).makeBomb();
        for (Cell c: board1.getBoard()) {
            c.findNearbyBombs();
        }
        board1.getCell(7).toggleFlag();
        board1.openFirst(13);
        board1.openFirst(6);
        assertTrue(board1.middleClick(10));
        assertTrue(board1.getCell(13).isOpen());
        assertTrue(board1.getCell(14).isOpen());
        assertTrue(board1.getCell(9).isOpen());
        assertTrue(board1.getCell(11).isOpen());
        assertFalse(board1.getCell(5).isOpen());
        assertTrue(board1.getCell(6).isOpen());
        assertTrue(board1.getCell(15).isOpen());
        assertFalse(board1.getCell(7).isOpen());
        assertTrue(board1.getCell(5).isFlagged());
        assertTrue(board1.getCell(7).isFlagged());
    }

    @Test
    public void testMiddleClickOpensCells() {
        board1.getCell(5).makeBomb();
        board1.getCell(7).makeBomb();
        for (Cell c: board1.getBoard()) {
            c.findNearbyBombs();
        }
        board1.openFirst(10);
        board1.getCell(5).toggleFlag();
        board1.getCell(7).toggleFlag();
        assertTrue(board1.middleClick(10));
        assertTrue(board1.getCell(13).isOpen());
        assertTrue(board1.getCell(14).isOpen());
        assertTrue(board1.getCell(9).isOpen());
        assertTrue(board1.getCell(11).isOpen());
        assertFalse(board1.getCell(5).isOpen());
        assertTrue(board1.getCell(6).isOpen());
        assertFalse(board1.getCell(7).isOpen());
        assertTrue(board1.getCell(5).isFlagged());
        assertTrue(board1.getCell(7).isFlagged());
    }

    @Test
    public void testMiddleClickOpensBomb() {
        board1.getCell(5).makeBomb();
        board1.getCell(7).makeBomb();
        for (Cell c: board1.getBoard()) {
            c.findNearbyBombs();
        }
        board1.openFirst(13);
        board1.getCell(6).toggleFlag();
        assertFalse(board1.middleClick(11));
        assertTrue(board1.getCell(14).isOpen());
        assertTrue(board1.getCell(11).isOpen());
        assertTrue(board1.getCell(15).isOpen());
        assertFalse(board1.getCell(6).isOpen());
        assertTrue(board1.getCell(7).isOpen());
    }

    @Test
    public void testWin() {
        board1.generate(9);
        for (Cell c: board1.getBoard()) {
            if (!c.isBomb()) {
                c.openCell();
            }
        }
        assertTrue(board1.winCheck());
    }

    @Test
    public void testNotWin() {
        board1.generate(8);
        assertFalse(board1.winCheck());
    }

    @Test
    public void testHashCode() {
        board3 = new Grid(4, 4, 4);
        assertTrue(board1.hashCode() == board3.hashCode());
    }

    @Test
    public void testHashCodeFail() {
        Integer i = 1;
        assertFalse(board1.hashCode() == i.hashCode());
    }

    @Test
    public void testEqualsSame() {
        assertEquals(board1, board1);
    }

    @Test
    public void testEqualsNull() {
        assertFalse(board1.equals(null));
    }

    @Test
    public void testEqualsDiffClass() {
        Integer i = 1;
        assertFalse(board1.equals(i));
    }

    @Test
    public void testEqualsBoardFail() {
        board3 = new Grid(4, 4, 4);
        board3.generate(2);
        assertFalse(board1.equals(board3));
    }

    @Test
    public void testEqualsHeightFail() {
        board3 = new Grid(5, 4, 4);
        assertFalse(board1.equals(board3));
    }

    @Test
    public void testEqualsSameDiffObj() {
        board3 = new Grid(4, 4, 4);
        assertEquals(board1, board3);
    }

    @Test
    public void testEqualsWidthFail() {
        board3 = new Grid(4, 10, 4);
        assertFalse(board1.equals(board3));
    }

    @Test
    public void testEqualsTotalbombFail() {
        board3 = new Grid(4, 4, 9);
        assertFalse(board1.equals(board3));
    }

}
