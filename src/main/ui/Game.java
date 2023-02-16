package ui;

import model.Cell;
import model.Grid;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Game {
    private Grid board;
    private int height = 4;
    private int width = 4;
    private int bombNum = 4;
    private List<Long> scores;

    //EFFECTS: Initiate the program, making a Game
    public static void main(String[] args) {
        new Game();
    }

    //EFFECTS: Change any settings or start a new game.
    public Game() {
        boolean ongoing = true;
        scores = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        while (ongoing) {
            System.out.println("What would you like to do? start to start a game, dim to change dimensions"
                    + ", bombs to change total number of bombs, and end to quit.");
            String move = input.next();
            if (move.equals("start")) {
                gameStart();
            } else if (move.equals("dim")) {
                newBoard();
            } else if (move.equals("end")) {
                break;
            } else if (move.equals("bombs")) {
                changeBomb();
            } else {
                System.out.println("Invalid option, try again.");
            }
        }
        System.out.println("Game closed");
    }

    //MODIFIES: This, Grid
    //EFFECTS: Initiate a new game with a board of size height by width with the given number of bombs.
    private void gameStart() {
        long start = System.nanoTime();
        board = new Grid(height, width, bombNum);
        int loc;
        System.out.println("Pick Starting Cell: ");
        loc = validCell();
        if (loc == -1) {
            System.out.println("Exiting this game...");
            return;
        }
        board.generate(loc);
        openNormally(loc);
        display(false);
        int state = game(loc);
        long end = System.nanoTime();
        if (state == 1) {
            addTime(start, end);
        }
    }

    //REQUIRES: A starting time
    //MODIFIES: This
    //EFFECTS: Adds time taken to clear to scores
    private void addTime(long start, long end) {
        System.out.println("Would you like to add your time? Y or N");
        Scanner inp = new Scanner(System.in);
        String response = inp.nextLine();
        if (response.equals("Y")) {
            scores.add(end - start);
        }
    }

    //REQUIRES: A starting location
    //EFFECTS: Reads what move is being performed and acts accordingly. Update grid in console and check win or loss.
    private int game(int loc) {
        Scanner inp = new Scanner(System.in);
        boolean inPlay = true;
        while (inPlay) {
            System.out.println("mid for middle click, open to open a cell, flag to flag a cell, quit to exit");
            String choice = inp.nextLine();
            if (choice.equals("quit")) {
                System.out.println("Exiting this game...");
                return -1;
            }
            try {
                inPlay = makeMove(choice);
                display(!inPlay);
                if (board.winCheck() && inPlay) {
                    System.out.println("You win!");
                    return 1;
                } else if (!inPlay) {
                    System.out.println("You lose!");
                }
            } catch (IndexOutOfBoundsException e) {
                return -1;
            }
        }
        return -1;
    }

    //EFFECTS: Initiate the appropriate move at the appropriate cell
    private boolean makeMove(String choice) throws IndexOutOfBoundsException {
        System.out.println("enter a location (cell locations are by row num * width + col num): ");
        int loc = validCell();
        switch (choice) {
            case "open":
                return openNormally(loc);
            case "flag":
                flag(loc);
                return true;
            case "mid":
                return board.middleClick(loc);
            default:
                System.out.println("invalid choice");
                return true;
        }
    }

    //EFFECTS: returns an integer input that is within range
    private int validCell() throws IndexOutOfBoundsException {
        int s = getNum(0);
        while (s < 0 || s >= height * width) {
            System.out.println("Invalid cell");
            s = getNum(0);
        }
        return s;
    }

    //REQUIRES: A Grid
    //EFFECTS: Displays the board in console
    private void display(boolean end) {
        for (int y = 0; y < height; y++) {
            String line = " ";
            for (int x = 0; x < width; x++) {
                Cell c = board.getCell(y * width + x);
                if (c.isFlagged()) {
                    line += "⚐";
                } else if (!c.isOpen() && !end) {
                    line += "□";
                } else if (c.isBomb()) {
                    line += "B";
                } else {
                    line += String.valueOf(c.getNumBombs());
                }
            }
            System.out.println(line);
        }
    }

    //REQUIRES: a Grid and a valid position
    //MODIFIES: Cell
    //EFFECTS: Toggles the flag on cell at location
    private void flag(int loc) {
        Cell toFlag = board.getCell(loc);
        toFlag.toggleFlag();
    }

    //REQUIRES: A Grid and a valid position
    //MODIFIES: Cell
    //EFFECTS: Open cell at location, and surround cells if initial cell has no bombs around it
    private boolean openNormally(int loc) {
        boolean boom = board.openFirst(loc);
        if (boom) {
            System.out.println("You clicked a bomb!");
            return false;
        }
        return true;
    }

    //MODIFIES: This
    //EFFECTS: Changes the width and height for new boards
    private void newBoard() {
        System.out.println("Enter a new width: ");
        int w = getNum(0);
        if (w < 4) {
            System.out.println("Invalid width");
            return;
        }
        width = w;
        System.out.println("Enter a new height: ");
        int h = getNum(0);
        if (h < 4) {
            System.out.println("Invalid height");
            return;
        }
        height = h;
    }

    //MODIFIES: This
    //EFFECTS: Set new number of bombs
    private void changeBomb() {
        System.out.println("How many bombs?");
        try {
            int b = getNum(0);
            if (b < 4 || b > width * height - 10) {
                System.out.println("Invalid bomb number");
                return;
            }
            bombNum = b;
        } catch (IndexOutOfBoundsException e) {
            return;
        }
    }

    //EFFECTS: Gets an integer from console. Exit after 10 tries of trying to get an integer.
    private int getNum(int t) throws IndexOutOfBoundsException {
        Scanner input = new Scanner(System.in);
        if (t > 10) {
            System.out.println("Too many tries, proceeding to quit");
            throw new IndexOutOfBoundsException();
        }
        try {
            int n = input.nextInt();
            return n;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input");
            return getNum(t + 1);
        }
    }
}
