package com.coditation;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConnectFour {

    // we define characters for players (R for Red, Y for Yellow)
    private static final char[] PLAYERS = {'1', '2'};
    // colors for the players
    private static final char[] COLORS = {'r','y'};
    // number of pieces
    private int pieces;
    // dimensions for our board
    private final int width, height;
    // grid for the board
    private final char[][] grid;
    // we store last move made by a player
    private int lastCol = -1, lastTop = -1;

    public ConnectFour(int w, int h, int p) {
        width = w;
        height = h;
        pieces = p;
        grid = new char[h][];

        // init the grid will blank cell
        for (int i = 0; i < h; i++) {
            Arrays.fill(grid[i] = new char[w], '-');
        }
    }

    // we use Streams to make a more concise method
    // for representing the board
    public String toString() {
        return IntStream.range(0,  width).
                mapToObj(Integer::toString).
                collect(Collectors.joining()) +
                "\n" +
                Arrays.stream(grid).
                        map(String::new).
                        collect(Collectors.joining("\n"));
    }

    // get string representation of the row containing
    // the last play of the user
    public String horizontal() {
        return new String(grid[lastTop]);
    }

    // get string representation fo the col containing
    // the last play of the user
    public String vertical() {
        StringBuilder sb = new StringBuilder(height);

        for (int h = 0; h < height; h++) {
            sb.append(grid[h][lastCol]);
        }

        return sb.toString();
    }

    // get string representation of the "/" diagonal
    // containing the last play of the user
    public String slashDiagonal() {
        StringBuilder sb = new StringBuilder(height);

        for (int h = 0; h < height; h++) {
            int w = lastCol + lastTop - h;

            if (0 <= w && w < width) {
                sb.append(grid[h][w]);
            }
        }

        return sb.toString();
    }

    // get string representation of the "\"
    // diagonal containing the last play of the user
    public String backslashDiagonal() {
        StringBuilder sb = new StringBuilder(height);

        for (int h = 0; h < height; h++) {
            int w = lastCol - lastTop + h;

            if (0 <= w && w < width) {
                sb.append(grid[h][w]);
            }
        }

        return sb.toString();
    }

    // static method checking if a substring is in str
    public static boolean contains(String str, String substring) {
        return str.indexOf(substring) >= 0;
    }

    // now, we create a method checking if last play is a winning play
    public boolean isWinningPlay() {
        if (lastCol == -1) {
            System.err.println("No move has been made yet");
            return false;
        }

        // winning streak with the last play symbol
        String streak = "";
        char sym = grid[lastTop][lastCol];
        for (int i = 0; i < pieces; i++) {
            streak += sym;
        }

//        String streak1 = String.format("%c%c%c%c", sym, sym, sym, sym);

        // check if streak is in row, col,
        // diagonal or backslash diagonal
        return contains(horizontal(), streak) ||
                contains(vertical(), streak) ||
                contains(slashDiagonal(), streak) ||
                contains(backslashDiagonal(), streak);
    }

    // prompts the user for a column, repeating until a valid choice is made
    public void chooseAndDrop(char symbol, Scanner input, int player) {
        do {
            System.out.print("\nPlayer " + (player+1) + ", what column do you want to put your piece? ");
            int col = input.nextInt();

            // check if column is ok
            if (!(0 <= col && col < width)) {
                System.out.println("Column must be between 0 and " + (width - 1));
                continue;
            }

            // now we can place the symbol to the first
            // available row in the asked column
            for (int h = height - 1; h >= 0; h--) {
                if (grid[h][col] == '-') {
                    grid[lastTop = h][lastCol = col] = symbol;
                    return;
                }
            }

            // if column is full ==> we need to ask for a new input
            System.out.println("Column " + col + " is full.");
        } while (true);
    }

    public static void main(String[] args) {
        // we assemble all the pieces of the puzzle for
        // building our Connect Four Game
        try (Scanner input = new Scanner(System.in)) {
            // we define some variables for our game like
            // dimensions and nb max of moves
            int height = 6; int width = 7; int pieces = 0; int moves = height * width;

            //accepting inputs
            System.out.print("Enter the values saperated by spaces -r -c -p: ");
            width = input.nextInt();
            height = input.nextInt();
            pieces = input.nextInt();

            //condition for pieces
            while (pieces <= 0) {
                if (pieces == 0) {
                    System.out.println("You cannot have 0 pieces to connect.");
                }
                else if (pieces < 0) {
                    System.out.println("You cannot have a negative pieces to connect:");
                }
                System.out.print("\nPlease Enter a positive, non-zero integer for the number of pieces to connect: ");
                pieces = input.nextInt();
            }

            // we create the ConnectFour instance
            ConnectFour board = new ConnectFour(width, height, pieces);

            // accepting the player one choice for the color
            System.out.print("\nPlayer one, do you want red or yellow (r or y): ");
            String color = input.next();

            // checking the entered choice is correct or not
            while (! (color.equals("r") || color.equals("y"))) {
                    System.out.print("Please, enter valid color: ");
                    color = input.next();
            }

            // we explain users how to enter their choices
            System.out.println("Use 0-" + (width - 1) + " to choose a column");
            // we display initial board
            System.out.println(board);

            if (color.equals("y")) {
                COLORS[0] = 'y';
                COLORS[1] = 'r';
            }

            // we iterate until max nb moves be reached
            // simple trick to change player turn at each iteration
            for (int player = 0; moves-- > 0; player = 1 - player) {
                // symbol for current player
                char symbol = COLORS[player];

                // we ask user to choose a column
                board.chooseAndDrop(symbol, input, player);

                // we display the board
                System.out.println(board);

                // we need to check if a player won. If not,
                // we continue, otherwise, we display a message
                if (board.isWinningPlay()) {
                    System.out.println("\nPlayer " + (player+1) + " wins!");
                    System.out.println("\nDo you want to play again (0-no, 1-yes)? ");
                    if(input.nextInt() == 0) {
                        return;
                    }
                    else {
                        System.out.println("\nLets play again,");
                        player = 1;
                        board = new ConnectFour(width, height, pieces);
                    }
                }
            }

            System.out.println("Game over. No winner. Try again!");
        }
    }

}