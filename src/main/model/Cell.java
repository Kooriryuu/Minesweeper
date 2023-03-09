package model;

import org.json.JSONObject;
import persistence.Writable;
import java.util.List;
import java.util.Objects;

//Represents a minesweeper cell

public class Cell implements Writable {

    private int numNearbyBombs;
    private boolean flagged;
    private List<Cell> aroundThis;
    private boolean open;
    private boolean isBomb;

    //MODIFIES: This
    //EFFECT: Create a cell
    public Cell() {
        isBomb = false;
        open = false;
        flagged = false;
    }

    //MODIFIES: This
    //EFFECT: Turn cell into a bomb
    public void makeBomb() {
        this.isBomb = true;
    }

    //REQUIRES: A non-empty lift of cells
    //MODIFIES: This
    //EFFECTS: Sets list of cells around this cell.
    public void setSurrounding(List<Cell> around) {
        this.aroundThis = around;
    }

    //MODIFIES: This
    //EFFECT: Sets the number of bombs around the cell
    public void findNearbyBombs() {
        int bomb = 0;
        for (Cell c: aroundThis) {
            if (c.isBomb()) {
                bomb++;
            }
        }
        this.numNearbyBombs = bomb;
    }

    //MODIFIES: This
    //EFFECT: Opens the cell
    public void openCell() {
        this.open = true;
    }

    //MODIFIES: This
    //EFFECT: flags or unflags the cell if the cell isn't open
    public void toggleFlag() {
        if (!open) {
            this.flagged = !(this.flagged);
        }
    }

    //EFFECT: checks if cell is flagged.
    public boolean isFlagged() {
        return this.flagged;
    }

    //EFFECT: checks if cell is a bomb.
    public boolean isBomb() {
        return this.isBomb;
    }

    //EFFECT: check if cell has been clicked.
    public boolean isOpen() {
        return this.open;
    }

    //EFFECT: get the number of bombs around the cell.
    public int getNumBombs() {
        return this.numNearbyBombs;
    }

    //EFFECT: get the cells around this cell.
    public List<Cell> getSurrounding() {
        return this.aroundThis;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("flagged", flagged);
        json.put("open", open);
        json.put("isBomb", isBomb);
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cell cell = (Cell) o;
        return numNearbyBombs == cell.numNearbyBombs && isFlagged() == cell.isFlagged()
                && isOpen() == cell.isOpen() && isBomb() == cell.isBomb();
    }

    @Override
    public int hashCode() {
        return Objects.hash(numNearbyBombs, isFlagged(), isOpen(), isBomb());
    }
}
