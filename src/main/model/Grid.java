package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid {
    private List<Cell> board;
    private int height;
    private int width;
    private int totalBombs;
    private List<Integer> bombLoc;

    //MODIFIES: This
    //EFFECTS: Create a grid of cells with dimensions w x h.
    public Grid(int h, int w, int bombNum) {
        height = h;
        width = w;
        totalBombs = bombNum;
        board = new ArrayList<>();
        for (int x = 0; x < height * width; x++) {
            Cell c = new Cell();
            board.add(c);
        }
        setAround();
    }

    //MODIFIES: Cell
    //EFFECTS: Gives each cell their surrounding cells.
    private void setAround() {
        for (int x = 0; x < width * height; x++) {
            List<Integer> around = findAround(x);
            List<Cell> aroundCells = new ArrayList<>();
            for (int i: around) {
                aroundCells.add(board.get(i));
            }
            board.get(x).setSurrounding(aroundCells);
        }
    }

    //REQUIRES: A valid position
    //EFFECTS: returns positions around a cell.
    private List<Integer> findAround(int cen) {
        List<Integer> initial = new ArrayList<>();
        initial.add(cen - 1);
        initial.add(cen + 1);
        initial.add(cen - width - 1);
        initial.add(cen - width);
        initial.add(cen - width + 1);
        initial.add(cen + width - 1);
        initial.add(cen + width);
        initial.add(cen + width + 1);
        List<Integer> fin = new ArrayList<>();
        for (int pos: initial) {
            boolean atEndCols = (cen % width == 0 && pos % width == width - 1)
                    || (pos % width == 0 && cen % width == width - 1);
            if (!((pos < 0) || (pos >= height * width) || atEndCols)) {
                fin.add(pos);
            }
        }
        return fin;
    }

    //REQUIRES: a valid position
    //MODIFIES: This, Cell
    //EFFECTS: Places bombs around the grid, at least 1 away from first cell. Updates each cell w num bombs around it
    public void generate(int first) {
        bombLoc = new ArrayList<>();
        Random r = new Random();
        if (totalBombs > width * height - 10) {
            totalBombs = width * height - 10;
        }
        List<Integer> aroundFirst = findAround(first);
        while (bombLoc.size() < totalBombs) {
            int spot = r.nextInt(width * height);
            if ((spot != first) && (!bombLoc.contains(spot)) && (!aroundFirst.contains(spot))) {
                bombLoc.add(spot);
                board.get(spot).makeBomb();
            }
        }
        for (Cell c: board) {
            c.findNearbyBombs();
        }
    }

    //REQUIRES: A valid location
    //MODIFIES: Cells
    //EFFECTS: if num flagged == bombs in surrounding, then open all surrounding cells.
    //         If num flagged + closed cells = surrounding bombs, then flag all unopened surrounding cells
    //         Indicates whether a bomb has been opened in the process
    public boolean middleClick(int loc) {
        int aroundFlag = 0;
        int closedCell = 0;
        Cell c = getCell(loc);
        if (!c.isFlagged() && c.isOpen()) {
            List<Cell> around = c.getSurrounding();
            for (Cell a : around) {
                if (a.isFlagged()) {
                    aroundFlag++;
                } else if (!a.isOpen()) {
                    closedCell++;
                }
            }
            if (aroundFlag == c.getNumBombs()) {
                return !openSurrounding(c.getSurrounding());
            } else if (c.getNumBombs() == aroundFlag + closedCell) {
                flagAround(around);
            }
        }
        return true;
    }

    //REQUIRES: a nonempty list of cells
    //MODIFIES: Cell
    //EFFECTS: flags all unopened cells that aren't flagged
    private void flagAround(List<Cell> around) {
        for (Cell a: around) {
            if (!a.isOpen() && !a.isFlagged()) {
                a.toggleFlag();
            }
        }
    }

    //REQUIRES: A valid location
    //MODIFIES: Cell
    //EFFECTS: Opens cell at location.
    public boolean openFirst(int loc) {
        Cell c = getCell(loc);
        return openCells(c);
    }

    //MODIFIES: Cell
    //EFFECTS: Opens the given cell. Opens cells around if there are no nearby bombs. Says if a bomb has been opened
    private boolean openCells(Cell c) {
        if (!c.isOpen() && !c.isFlagged()) {
            c.openCell();
            if (c.isBomb()) {
                return true;
            } else if (c.getNumBombs() == 0) {
                openSurrounding(c.getSurrounding());
            }
        }
        return false;
    }

    //EFFECTS: Opens all the cells in the list. Indicates if a bomb has been opened in the process
    public boolean openSurrounding(List<Cell> surround) {
        boolean foundBomb = false;
        for (Cell c: surround) {
            boolean isB = openCells(c);
            if (isB) {
                foundBomb = true;
            }
        }
        return foundBomb;
    }

    //EFFECTS: Determines if the player has won or not
    public boolean winCheck() {
        int unopenedCells = 0;
        for (Cell c: board) {
            if (!c.isOpen()) {
                unopenedCells++;
            }
        }
        return (unopenedCells == totalBombs);
    }

    //EFFECTS: returns height of grid
    public int getHeight() {
        return this.height;
    }

    //EFFECTS: returns the width of the grid
    public int getWidth() {
        return this.width;
    }


    //EFFECTS: get the board
    public List<Cell> getBoard() {
        return board;
    }

    //EFFECTS: get the total number of bombs
    public int getTotalBombs() {
        return totalBombs;
    }

    //REQUIRES: A valid location
    //EFFECTS: returns the cell at given location
    public Cell getCell(int loc) {
        return board.get(loc);
    }
}
