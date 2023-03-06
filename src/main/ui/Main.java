package ui;

import model.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {


    //EFFECTS: Initiate the program, making a Game
    public static void main(String[] args) {
        Game g = new Game();
        initiate(g);
    }

    //EFFECTS: Change any settings or start a new game.
    public static void initiate(Game g) {
        boolean ongoing = true;
        Scanner input = new Scanner(System.in);
        while (ongoing) {
            System.out.println("What would you like to do? start to start a game, dim to change dimensions"
                    + ", bombs to change total number of bombs, and end to quit.");
            String move = input.next();
            if (move.equals("start")) {
                gameStart(g);
            } else if (move.equals("dim")) {
                newBoard(g);
            } else if (move.equals("end")) {
                ongoing = false;
            } else if (move.equals("bombs")) {
                changeBomb(g);
            } else {
                System.out.println("Invalid option, try again.");
            }
        }
        System.out.println("Game closed");
    }

    //MODIFIES: This, Grid
    //EFFECTS: Initiate a new game with a board of size height by width with the given number of bombs.
    private static void gameStart(Game g) {
        int loc;
        System.out.println("Pick Starting Cell: ");
        loc = validCell(g);
        if (loc == -1) {
            System.out.println("Exiting this game...");
            return;
        }
        g.gameInitialization(loc);
        display(false, g);
        int state = game(g);
        if (state == 1) {
            g.endTimer();
            addTime(g);
        }
    }

    //REQUIRES: A starting time
    //MODIFIES: This
    //EFFECTS: Adds time taken to clear to scores
    private static void addTime(Game g) {
        System.out.println("Would you like to add your time? Y or N");
        Scanner inp = new Scanner(System.in);
        String response = inp.nextLine();
        g.addTime(response);
    }

    //REQUIRES: A starting location
    //EFFECTS: Reads what move is being performed and acts accordingly. Update grid in console and check win or loss.
    private static int game(Game g) {
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
                inPlay = makeMove(choice, g);
                display(!inPlay, g);
                if (g.hasWon() && inPlay) {
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
    private static boolean makeMove(String choice, Game g) throws IndexOutOfBoundsException {
        if (!(choice.equals("open")||choice.equals("flag") || choice.equals("mid"))) {
            System.out.println("invalid choice");
            return true;
        }
        System.out.println("enter a location (cell locations are by row num * width + col num): ");
        int loc = validCell(g);
        boolean clickedBomb = g.chooseMove(choice, loc);
        if (!clickedBomb) {
            System.out.println("You clicked a bomb!");
        }
        return clickedBomb;
    }

    //EFFECTS: returns an integer input that is within range
    private static int validCell(Game g) throws IndexOutOfBoundsException {
        int s = getNum(0);
        while (s < 0 || s >= g.getHeight() * g.getWidth()) {
            System.out.println("Invalid cell");
            s = getNum(0);
        }
        return s;
    }

    //REQUIRES: A Grid
    //EFFECTS: Displays the board in console
    private static void display(boolean end, Game g) {
        for (int y = 0; y < g.getHeight(); y++) {
            String line = " ";
            for (int x = 0; x < g.getWidth(); x++) {
                line += g.getChar(end, x, y);
            }
            System.out.println(line);
        }
    }

    //MODIFIES: This
    //EFFECTS: Changes the width and height for new boards
    private static void newBoard(Game g) {
        System.out.println("Enter a new width: ");
        int w = getNum(0);
        if (w < 4) {
            System.out.println("Invalid width");
            return;
        }
        g.changeWidth(w);
        System.out.println("Enter a new height: ");
        int h = getNum(0);
        if (h < 4) {
            System.out.println("Invalid height");
            return;
        }
        g.changeHeight(h);
    }

    //MODIFIES: This
    //EFFECTS: Set new number of bombs
    private static void changeBomb(Game g) {
        System.out.println("How many bombs?");
        try {
            int b = getNum(0);
            if (b < 4 || b > g.getWidth() * g.getHeight() - 10) {
                System.out.println("Invalid bomb number");
                return;
            }
            g.changeBombNum(b);
        } catch (IndexOutOfBoundsException e) {
            return;
        }
    }

    //EFFECTS: Gets an integer from console. Exit after 10 tries of trying to get an integer.
    private static int getNum(int t) throws IndexOutOfBoundsException {
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
