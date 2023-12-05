package ryan;
import java.io.IOException;
import java.util.Scanner;
import biz.source_code.utils.RawConsoleInput;
public class Minesweeper {

	// Runs the Minesweeper game
	public static void main(String[] args) throws IOException {
		Board.clearScreen();
		// Get user input for the game difficulty
		System.out.println("Welcome to Minesweeper!  Enter a difficulty, 1 (easy), 2 (medium) or 3 (hard)");
		boolean valid = false;
		int difficulty = 1;
		while(!valid) {
			try {
				String s = "" + (char) RawConsoleInput.read(true);
				System.out.println(s);
				int tempBlocks = Integer.parseInt(s);
				if(tempBlocks > 0 && tempBlocks <= 3) {
					difficulty = tempBlocks;
					valid = true;
				} else {
					System.out.println("bad input try again");
				}
			} catch (Exception e) {
				System.out.println("bad input try again");
			}
		}

		// Generate the board size based on the chosen difficulty
		Board board = new Board(difficulty);

		// Print the controls
		System.out.println(
				"Controls:\n - Use the arrow keys to move the cursor around the board\n - Press spacebar to select a space on the board\n - Type F to flag a space\n - Type R to remove a flag\n - Type Q to give up");

		board.printGameBoard();

		// Play the game
		int input = board.playGame();
		// If the user enters a Q character, the game will quit prematurely
		if (input != 81) {
			// Check if the user won
			if (board.checkWin()) {
				System.out.println("You Win!!!");
			} else {
				// If the user lost, print the board showing all mines
				board.loseGame();
			}
		} else {
			System.out.println("Game quit.  Better luck next time!");
		}

		// Stop the program from closing right away so the user can view the game they
		// just won or lost
		System.out.println("Press spacebar to exit.");
		while (RawConsoleInput.read(true) != 32) {
		}
	}
}
