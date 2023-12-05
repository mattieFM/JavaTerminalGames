package ryan;
import java.io.IOException;
import java.util.Stack;
import biz.source_code.utils.RawConsoleInput;

import gameEngine.*;


public class StackSet {
	Stack<Integer> startStack;
	Stack<Integer> midStack;
	Stack<Integer> endStack;
	Stack<Integer> selectedStack;
	Stack<Integer> targetStack;
	private int difficulty;
	private int movesMade;
	private int location;

	public StackSet(int size) {
		startStack = new Stack<Integer>();
		midStack = new Stack<Integer>();
		endStack = new Stack<Integer>();
		if (size > 0 && size < 9) {
			difficulty = size;
		} else {
			difficulty = 3;
		}
		for (int b = difficulty; b > 0; b--) {
			startStack.push(b);
		}
		location = 1;
	}

	// Select a stack to move a block from
	public void selectStack(int num) {
		// Set the selectedStack pointer to the chosen stack
		switch (num) {
		case 1:
			selectedStack = startStack;
			break;
		case 2:
			selectedStack = midStack;
			break;
		case 3:
			selectedStack = endStack;
			break;
		default:
			selectedStack = null;
			break;
		}

		// Check that the selected stack is not null
		if (selectedStack != null) {
			// Prevent selecting an empty stack
			if (selectedStack.size() == 0) {
				selectedStack = null;
			}
		}
	}

	// Move the top block of one stack to another stack
	public void moveToStack(int num) {

		// Select the target stack
		switch (num) {
		case 1:
			targetStack = startStack;
			break;
		case 2:
			targetStack = midStack;
			break;
		case 3:
			targetStack = endStack;
			break;
		default:
			targetStack = selectedStack;
		}

		// Check that the targetStack is different from the selectedStack
		if (targetStack != selectedStack) {
			// If the target stack is empty or the top element of it is greater than the
			// selected stack, move the block
			if (targetStack.size() == 0 || targetStack.peek() > selectedStack.peek()) {
				targetStack.push(selectedStack.pop());
				movesMade++;

			}
		}
		// Reset the values of selectedStack and targetStack
		selectedStack = null;
		targetStack = null;

	}

	// Check if a stack has been selected
	public boolean stackIsSelected() {
		return selectedStack != null;
	}

	// Check if the player has won
	public boolean checkWin() {
		// Check if all the blocks are on the endStack
		return endStack.size() == difficulty;
	}

	// Print the stacks, showing the cursor location and selected stack
	public void printBoard() {
		String result = "";
		// Add the stacks to the result string
		for (int i = difficulty - 1; i >= 0; i--) {
			result += getElement(startStack, i) + "\t" + getElement(midStack, i) + "\t" + getElement(endStack, i)
					+ "\n";
		}

		// Put lines under each stack location so the player can see them
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < difficulty; j++) {
				result += "-";
			}
			// Put a tab between the lines
			if (i != 2) {
				result += "\t";
			} else {
				result += "\n";
			}
		}

		// Put a line of '%' under the selected stack
		if (selectedStack != null) {
			if (selectedStack == midStack) {
				result += "\t";
			} else if (selectedStack == endStack) {
				result += "\t\t";
			}
			for (int j = 0; j < difficulty; j++) {
				result += "%";
			}
			result += "\n";
		}

		// Put a '^' symbol where the cursor is
		switch (location) {
		case 1:
			result += "^";
			break;
		case 2:
			result += "\t^";
			break;
		case 3:
			result += "\t\t^";
			break;
		}

		// Print the board
		System.out.println(result);
	}

	// Get the string value of a block in a stack
	private String getElement(Stack<Integer> stack, int num) {
		int size = stack.size();
		String value = "";

		// If there are blocks at this location on the stack
		if (num < size) {
			int val = stack.get(num);
			// Put the appropriate number of block characters into the output.
			for (int i = 0; i < val; i++) {
				value += Engine.SQUARE_CHAR;
			}
		}

		return value;
	}

	// Called when the player wins the game
	public void winGame() {
		System.out.println("You Win!!!");
		System.out.println("You completed the game in " + movesMade + " moves!");
	}

	// Runs the game
	public void playGame() throws IOException {
		boolean quit = false;
		// While the player has not quit, and has not won
		while (!quit && !checkWin()) {
			// Get the user's raw keyboard input
			int input = RawConsoleInput.read(true);
			boolean print = true;
			if(Engine.LEFT_CHARS.contains(input)) {
				// Left arrow
				if (location > 1) {
					location--;
				}
			} else if(Engine.RIGHT_CHARS.contains(input)) {
				// Right arrow
				if (location < 3) {
					location++;
				}
			} else if(Engine.ACCEPT_CHARS.contains(input)) {
				//accept button
				
				// If the player has not selected a stack, select one
				if (!stackIsSelected()) {
					selectStack(location);
				}
				// Otherwise, move the block
				else {
					moveToStack(location);
				}
			} else if(Engine.EXIT_CHARS.contains(input)) {
				//quit button
				quit = true;
			} else {
				print = false;
			}
			
			// If a valid character is input, clear the screen and print the board
			if (print) {
				clearScreen();
				printBoard();
			}
		}
	}

	// Clears the screen for a Windows a Linux terminal to make gameplay smoother
	public static void clearScreen() {
		try {
			// Get the OS
			final String os = System.getProperty("os.name");
			// If Windows, run "cls"
			if (os.contains("Windows")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			}
			// Otherwise, run "clear"
			else {
				System.out.flush();
				Runtime.getRuntime().exec("clear");
			}
		} catch (final Exception e) {

		}
	}
}
