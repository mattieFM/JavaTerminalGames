import java.io.IOException;
import java.util.Scanner;

public class Hanoi {

	public static void main(String[] args) throws IOException {
		StackSet.clearScreen();
		// Take user input for the number of blocks
		Scanner userInput = new Scanner(System.in);
		System.out.println("Welcome to Tower of Hanoi!  Enter the number of blocks (1 - 8):");

		int difficulty = 3;

		// Handle any input mismatch or other exceptions
		try {
			difficulty = userInput.nextInt();
		} catch (final Exception e) {
		}

		// Create a new StackSet to run the game
		StackSet towers = new StackSet(difficulty);

		// Print the controls and premise of the game
		System.out.println(
				"Controls:\n - Use the left and right arrow keys to move the cursor\n - Press spacebar to select a stack\n - Press spacebar again to move the top block of the stack\n - Blocks can only sit on top of blocks bigger than them\n - The goal is to get all the blocks to the rightmost stack - Press Q to give up\n");

		// Run the game
		towers.printBoard();
		towers.playGame();

		// Check if the player won the game or if they quit
		if (towers.checkWin()) {
			towers.winGame();
		} else {
			System.out.println("Game quit.  Better luck next time!");
		}

		// Keep the game open until the user exits. This ensures the user can view the
		// results of the game
		System.out.println("Press spacebar to exit.");
		while (RawConsoleInput.read(true) != 32) {
		}

		userInput.close();
	}

}
