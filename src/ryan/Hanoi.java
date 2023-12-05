package ryan;
import java.io.IOException;
import java.util.Scanner;

import biz.source_code.utils.RawConsoleInput;
import gameEngine.*;

public class Hanoi {

	public static void main(String[] args) throws IOException {
		Engine.loadConfig();
		StackSet.clearScreen();
		// Take user input for the number of blocks
		System.out.println("Welcome to Tower of Hanoi!  Enter the number of blocks (1 - 8):");

		boolean valid = false;
		int blocks = 8;
		while(!valid) {
			try {
				String s = "" + (char) RawConsoleInput.read(true);
				System.out.println(s);
				int tempBlocks = Integer.parseInt(s);
				if(tempBlocks > 0 && tempBlocks < 9) {
					blocks = tempBlocks;
					valid = true;
				} else {
					System.out.println("bad input try again");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		
		// Create a new StackSet to run the game
		StackSet towers = new StackSet(blocks);

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
		while (Engine.EXIT_CHARS.contains(RawConsoleInput.read(true))) {
		}
	}

}
