package com.coditation;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    // characters for players red and yellow
    private static final char[] players = {'r', 'y'};
    // grid for board
    private  final char[][] board;
    // rows and columns for board
    private final int width, height;
    //last move made by player
    private int lastCol = -1, lastTop = -1;

    public Main(int width, int height) {
        this.width = width;
        this.height = height;
        board = new char[width][];

        // filling the blank spaces with '- '
        for (int i = 0; i < width; i++) {
            Arrays.fill(board[i] = new char[height], '-');
        }
    }

    // Stream is used to make more concise method for representing the board
    public String toString() {
        return IntStream.range(0, height).mapToObj(Integer::toString).collect(Collectors.joining()) + "\n" +
                Arrays.stream(board).map(String::new).collect(Collectors.joining("\n"));
    }

    //get String representation of the row containing the last play of the user
    public String horizontal() {
        return new String(board[lastTop]);
    }

    //get String representation of the col containing the last play of the user
    public String vertical() {
        StringBuilder sb = new StringBuilder(height);

        for (int c = 0; c < height; c++) {
            sb.append(board[c][lastCol]);
        }
        return sb.toString();
    }

    // get string representation of the "/" diagonal containing the last play of the user
    public String slashDiagonal() {
        StringBuilder sb = new StringBuilder(height);
        for (int c = 0; c < height; c++) {
            int r = lastCol + lastTop - c;

            if(0 <= r && r < width) {
                sb.append(board[c][r]);
            }
        }
        return sb.toString();
    }

    // get string representation of the "\" diagonal containing the last play of the user
    public String backSlashDiagonal() {
        StringBuilder sb = new StringBuilder(height);

        for (int c = 0; c < height; c++) {
            int r = lastCol - lastTop + c;

            if(0 < c && c < height) {
                sb.append(board[c][r]);
            }
        }
        return sb.toString();
    }

    // static method checking if substring in a str
    public static boolean contains(String str, String subString) {
        return str.indexOf(subString) >= 0;
    }

    // now, we create a method checking if last play is winning play
    public boolean isWinningPlay() {
        if (lastCol == -1) {
            System.err.println("No move has been made yet");
            return  false;
        }

        char sym = board[lastTop][lastCol];
        // winning streak with last play symbol
        String streak = String.format("%c%c%c%c",sym, sym, sym, sym);

        //check if streak is in row, col, diagonal or backslash diagonal
        return contains(horizontal(), streak) || contains(vertical(), streak) ||
                contains(slashDiagonal(), streak) || contains(backSlashDiagonal(), streak);
    }

    // prompt a user for a column, repeating until valid choice is made
    public void chooseAndDrop(char symbol, Scanner input) {
        do {
            System.out.println("\nPlayer "+ symbol + "turn: ");
            int col = input.nextInt();

            //check if colmn is ok
            if (! (0 <= col  && col < height)) {
                System.out.println("Column must be between 0 and " + (height - 1));
                continue;
            }

            //now, we can place the symbol to the first available row in the asked column
            for(int h = height-1; h >= 0; h--) {
                if (board[h][col] == '.') {
                    board[lastTop = h][lastCol = col] = symbol;
                    return;
                }
            }

            // if column is full ==> we need to ask for new input
            System.out.println("Column " + col + " is full.");
        } while (true);
    }

    public static void main(String[] args) {

        // we assemble all the pieces of puzzles for building our Connect four game
        try {
            Scanner input = new Scanner(System.in);
            // we define some variables for our game like dimensions and nb  max of moves
            int height = 6, width = 8, moves = height * width;

            //we connect the connectFour instance
            Main main = new Main(width, height);

            // we explain users how to enter their choices
            System.out.println("Use 0-" + (width-1) + " to choose a column");

            // we display initial board
            System.out.println(main);

            // we iterate until max nb moves be reached
            for (int player = 0; moves-- > 0; player = 1 - player) {
                // simple trick to change player turn at each iteration
                // symbol for current player
                char symbol = players[player];

                // we ask user to choose column
                main.chooseAndDrop(symbol, input);

                // we display the board
                System.out.println(main);

                // we need to check if a player won. If not, we continue, otherwise, we display a message
                if (main.isWinningPlay()) {
                    System.out.println("\nPlayer "+ symbol + " wins!");
                    return;
                }
            }
            System.out.println("Gave over, No winner. Try Again!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
