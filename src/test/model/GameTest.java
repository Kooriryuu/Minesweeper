package model;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class GameTest {

    Game g;

    @BeforeEach
    public void setUp() {
        g = new Game();

    }

    @Test
    public void testGameConstructor() {
        assertEquals(4, g.getHeight());
        assertEquals(4, g.getWidth());
        assertEquals(5, g.getBombNum());
        assertEquals(0, g.getStart());
        assertEquals(0, g.getEnd());
        assertEquals(16, g.getGrid().getBoard().size());
    }

    @Test
    public void testChangeDifferentHeight() {
        g.changeHeight(9);
        assertEquals(9, g.getHeight());
    }

    @Test
    public void testChangeSameHeight() {
        g.changeHeight(4);
        assertEquals(4, g.getHeight());
    }

    @Test
    public void testChangeHeightByOne() {
        g.changeHeight(5);
        assertEquals(5, g.getHeight());
    }

    @Test
    public void testChangeDifferentWidth() {
        g.changeWidth(9);
        assertEquals(9, g.getWidth());
    }

    @Test
    public void testChangeSameWidth() {
        g.changeWidth(4);
        assertEquals(4, g.getWidth());
    }

    @Test
    public void testChangeWidthByOne() {
        g.changeWidth(5);
        assertEquals(5, g.getWidth());
    }

    @Test
    public void testChangeDifferentBombNum() {
        g.changeBombNum(9);
        assertEquals(9, g.getBombNum());
    }

    @Test
    public void testChangeSameBombNum() {
        g.changeBombNum(4);
        assertEquals(4, g.getBombNum());
    }

    @Test
    public void testChangeBombNumByOne() {
        g.changeBombNum(5);
        assertEquals(5, g.getBombNum());
    }

    @Test
    public void testGameInitialization() {
        g.gameInitialization(7);
        assertNotEquals(null, g.getGrid());
        assertNotEquals(0,  g.getStart());
        assertEquals(0, g.getEnd());
    }

    @Test
    public void testChooseOpenBomb() {
        int l = notInstantWinGrid(true,false);
        Grid board = g.getGrid();
        boolean fin = g.chooseMove("open", l);
        assertFalse(fin);
        assertTrue(board.getCell(l).isOpen());
    }

    @Test
    public void testChooseOpenCell() {
        int l = notInstantWinGrid(false, false);
        Grid board = g.getGrid();
        boolean fin = g.chooseMove("open", l);
        assertTrue(fin);
        assertTrue(board.getCell(l).isOpen());
    }

    @Test
    public void testChooseFlag() {
        int l = notInstantWinGrid(false, false);
        Grid board = g.getGrid();
        boolean fin = g.chooseMove("flag", l);
        assertTrue(fin);
        assertFalse(board.getCell(l).isOpen());
        assertTrue(board.getCell(l).isFlagged());
    }

    @Test
    public void testChooseMid() {
        int l = notInstantWinGrid(false, false);
        boolean fin = g.chooseMove("mid", l);
        assertTrue(fin);
    }

    @Test
    public void testChooseInvalid() {
        g.gameInitialization(8);
        boolean fin = g.chooseMove("p", 8);
        assertTrue(fin);
    }

    @Test
    public void testEndTimer() {
        g.endTimer();
        assertNotEquals(0, g.getEnd());
    }

    private int findBombOrNormalCell(boolean state, boolean open) {
        Grid b = g.getGrid();
        for (int i =0; i < g.getWidth() * g.getHeight(); i++) {
            Cell c = b.getCell(i);
            if (c.isBomb() && state) {
                return i;
            } else if (!state && !c.isBomb() && !c.isOpen() && !open) {
                return i;
            } else if (!state && !c.isBomb() && c.isOpen() && open) {
                return i;
            }
        }
        return -1;
    }

    private int notInstantWinGrid(boolean state, boolean open) {
        int l = -1;
        g.gameInitialization(9);
        while (l == -1) {
            g.gameInitialization(9);
            l = findBombOrNormalCell(state, open);
        }
        return l;
    }

    @Test
    public void testGetFlag() {
        int l = notInstantWinGrid(false, false);
        Grid board = g.getGrid();
        board.getCell(l).toggleFlag();
        String result = g.getChar(false, l%4, l/4);
        assertEquals("⚐", result);
    }

    @Test
    public void testGetSpace() {
        int l = notInstantWinGrid(false, false);
        Grid board = g.getGrid();
        String result = g.getChar(false, l%4, l/4);
        assertEquals("□", result);
    }

    @Test
    public void testGetBomb() {
        int l = notInstantWinGrid(true, false);
        Grid board = g.getGrid();
        String result = g.getChar(true, l%4, l/4);
        assertEquals("B", result);
    }

    @Test
    public void testGetNum() {
        int l = notInstantWinGrid(false, true);
        Grid board = g.getGrid();
        int ex = board.getCell(l).getNumBombs();
        String result = g.getChar(true, l%4, l/4);
        assertEquals(String.valueOf(ex), result);
    }

    @Test
    public void testHasWon() {
        g.gameInitialization(6);
        Grid board = g.getGrid();
        for (Cell c: board.getBoard()) {
            if(!c.isBomb()) {
                c.openCell();
            }
        }
        boolean res = g.hasWon();
        assertTrue(res);
    }

    @Test
    public void testHasNotWon() {
        g.gameInitialization(0);
        boolean res = g.hasWon();
        assertFalse(res);
    }

    @Test
    public void testSetName() {
        g.setPlayer("hello");
        assertEquals("hello",g.getCurrentPlayer());
    }

    @Test
    public void testNoAddTime() {
        g.gameInitialization(9);
        g.addTime("no");
        assertEquals(0, g.getLeaderboard().size());
        assertEquals(0, g.getEnd());
        assertEquals(0, g.getStart());
    }

    @Test
    public void testAddTime() {
        g.setPlayer("p");
        g.gameInitialization(10);
        long begin = g.getStart();
        g.endTimer();
        long end = g.getEnd();
        g.addTime("y");
        Map<String, Player> scoreList = g.getLeaderboard();
        String player = g.getCurrentPlayer();
        String m = String.valueOf(g.getHeight()) + "x" + String.valueOf(g.getWidth());
        assertEquals(1, scoreList.size());
        assertEquals(0, g.getEnd());
        assertEquals(0, g.getStart());
        assertEquals(end - begin, scoreList.get(player).getTimesForMap(m).get(0));
        assertEquals(1, scoreList.get(player).getTimesForMap(m).size());
    }

    @Test
    public void testAddTimeTwice() {
        g.setPlayer("p");
        g.gameInitialization(10);
        g.endTimer();
        g.addTime("y");
        g.gameInitialization(10);
        long begin = g.getStart();
        g.endTimer();
        long end = g.getEnd();
        g.addTime("Y");
        Map<String, Player> scoreList = g.getLeaderboard();
        String player = g.getCurrentPlayer();
        String m = String.valueOf(g.getHeight()) + "x" + String.valueOf(g.getWidth());
        assertEquals(1, scoreList.size());
        assertEquals(0, g.getEnd());
        assertEquals(0, g.getStart());
        assertEquals(end - begin, scoreList.get(player).getTimesForMap(m).get(1));
        assertEquals(2, scoreList.get(player).getTimesForMap(m).size());
    }

    @Test
    public void testGetDeltaT() {
        assertEquals(0, g.getDeltaT());
    }

    @Test
    public void testHashCodeSame() {
        Game g2 = new Game();
        assertTrue(g.equals(g2) && g2.equals(g));
        assertTrue(g.hashCode() == g2.hashCode());
    }

    @Test
    public void testHashCodeDiff() {
        Integer i = 1;
        assertNotEquals(g, i);
        assertFalse(i.hashCode() == g.hashCode());
    }

    @Test
    public void testEqualsSameObj() {
        assertEquals(g, g);
    }

    @Test
    public void testEqualsDiffObj() {
        assertFalse(g.equals(1));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(g.equals(null));
    }

    @Test
    public void testEqualsHeightFail(){
        Game g2 = new Game();
        g2.changeHeight(8);
        assertFalse(g.equals(g2));
    }

    @Test
    public void testEqualsWidthFail(){
        Game g2 = new Game();
        g2.changeWidth(8);
        assertFalse(g.equals(g2));
    }

    @Test
    public void testEqualsBombNumFail(){
        Game g2 = new Game();
        g2.changeBombNum(8);
        assertFalse(g.equals(g2));
    }

    @Test
    public void testEqualsBoardFail(){
        Game g2 = new Game();
        g2.gameInitialization(8);
        assertFalse(g.equals(g2));
    }

    @Test
    public void testEqualsDeltaFail(){
        Game g2 = new Game();
        g2.setDeltaT(8);
        assertFalse(g.equals(g2));
    }


    @Test
    public void testEqulsPlayerListFail(){
        Game g2 = new Game();
        g2.setPlayer("h");
        g2.addTime("Y");
        assertFalse(g.equals(g2));
    }
}
