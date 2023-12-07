package ryan;
import java.io.FileNotFoundException;
import java.io.IOException;

import biz.source_code.utils.RawConsoleInput;
import gameEngine.*;

// Represents the Minesweeper board
public class Board {
	private final int rows;
	private final int columns;
	private final int difficulty;
	private int mines;
	private char[][] board;
	private int[][] numBoard;
	private int[][] mineCoords;
	private char[][] gameBoard;
	private int movesMade;
	public final int xMax;
	public final int yMax;
	private int cursorX;
	private int cursorY;
	private final char square;

	// Take the difficulty level as an argument
	public Board(int level) throws FileNotFoundException {
		Engine.loadConfig();
		this.difficulty = level;
		mines = 10;
		// Based on the difficulty level, generate a different size board with a
		// different number of mines
		switch (difficulty) {
		case 1:
			rows = columns = 9;
			break;
		case 2:
			rows = columns = 16;
			mines = 40;
			break;
		case 3:
			rows = 16;
			mines = 99;
			columns = 30;
			break;
		default:
			rows = columns = 9;
			break;
		}

		board = new char[rows][columns];
		numBoard = new int[rows][columns];
		gameBoard = new char[rows][columns];
		mineCoords = new int[mines][2];
		movesMade = 0;
		xMax = columns - 1;
		yMax = rows - 1;

		square = Engine.SQUARE_CHAR;

		// Blank out the game board
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				gameBoard[i][j] = square;
			}
		}

	}

	// Blank out the 2D arrays used to store the mine locations and board, and
	// generate a new map
	private void generateMap() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				board[i][j] = 'O';
				numBoard[i][j] = 0;
				gameBoard[i][j] = square;
			}
		}
		addMines(mines);
		// Set up numBoard to store the number of mines adjacent to each location on the
		// map
		checkTiles();

	}

	// Print the matrix that shows the location of all the mines. Used for debugging
	public String toString() {
		String result = "";
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				result += board[i][j];
				// Add a space between each character, except the last one on each line
				if (j != columns - 1) {
					result += " ";
				}
			}
			result += "\n";
		}
		return result;
	}

	// Add mines to the board. The number added is determined by the argument passed
	private void addMines(int mines) {
		while (mines != 0) {
			// Get a random location that doesn't have a mine at it
			int[] location = getRandomEmptyLocation();
			// Store the location
			mineCoords[mines - 1] = location;
			// Set the board location to an X, representing a mine
			board[location[0]][location[1]] = 'X';
			mines--;
		}
	}

	// Return a random location on the board that currently does not have a mine
	private int[] getRandomEmptyLocation() {
		int[] coords = new int[2];

		// Generate random coordinates until a set is found that does not have a mine
		do {
			coords[0] = (int) (Math.random() * rows);
			coords[1] = (int) (Math.random() * columns);
		} while (getLocation(coords) == 'X');

		return coords;
	}

	// Return a location on the board
	private char getLocation(int[] location) {
		return board[location[0]][location[1]];
	}

	// Set up the numBoard matrix, so each tile knows how many mines are adjacent
	private void checkTiles() {
		// Iterate through each mine's location
		for (int i = 0; i < mineCoords.length; i++) {
			int r = mineCoords[i][0];
			int c = mineCoords[i][1];

			// For each mine's location, add 1 to the numBoard locations in a 3x3 grid of
			// the mine, not including the tile where the mine itself is
			for (int a = -1; a <= 1; a++) {
				for (int b = -1; b <= 1; b++) {
					// Check that the location specified is actually on the board, to prevent
					// ArrayIndexOutOfBounds exceptions. This approach to the problem seems to be
					// more efficient and concise than writing separate algorithms for edges and
					// corners
					if ((a != 0 || b != 0) && r + a >= 0 && r + a < rows && c + b >= 0 && c + b < columns) {
						numBoard[r + a][c + b]++;
					}
				}
			}
		}

	}

	// Print the numBoard. Used mostly for debugging, and not used by the game
	public void printNumBoard() {
		String result = "";
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				result += numBoard[i][j];
				if (j != columns - 1) {
					result += " ";
				}
			}
			result += "\n";
		}
		System.out.println(result);
	}

	// Print the gameBoard. This is the print method actually used by the game
	public void printGameBoard() {

		String result = "";
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				// Make the output string show a '@' character where the cursor is
				if (i == cursorY && j == cursorX) {
					result += "~";
				} else {
					result += gameBoard[i][j];
				}

				if (j != columns - 1) {
					result += " ";
				}
			}
			result += "\n";
		}

		System.out.println(result);
	}

	// Called when the user selects a location
	private int selectTile(int r, int c) {
		// Makes sure nothing happens if this tile is already revealed
		if (gameBoard[r][c] == square) {
			// Return -1 if a mine is selected
			if (board[r][c] == 'X') {
				return -1;
			}
			// Return 0 if a tile with an adjacent mine is selected
			else if (numBoard[r][c] > 0) {
				gameBoard[r][c] = (char) (numBoard[r][c] + 48);
				return 0;
			}
			// If an empty tile is selected, call the showTile function on the 8 adjacent
			// tiles, and return 1
			else {
				gameBoard[r][c] = ' ';
				showTile(r - 1, c - 1);
				showTile(r, c - 1);
				showTile(r + 1, c - 1);
				showTile(r - 1, c);
				showTile(r + 1, c);
				showTile(r - 1, c + 1);
				showTile(r, c + 1);
				showTile(r + 1, c + 1);
				return 1;
			}
		}
		return 2;
	}

	// Somewhat complex recursive function that reveals tiles on the game board
	private void showTile(int r, int c) {
		// Check that the location is actually on the board to prevent errors, that the
		// location has not already been revealed, and that the location does not have a
		// mine at it
		if (r >= 0 && c >= 0 && r < rows && c < columns && gameBoard[r][c] == square && board[r][c] != 'X') {
			// If the tile has an adjacent mine, show only this tile
			if (numBoard[r][c] > 0) {
				gameBoard[r][c] = (char) (numBoard[r][c] + 48);

			}
			// Otherwise, recursively call showTile() on the adjacent 8 tiles
			else {
				gameBoard[r][c] = ' ';
				showTile(r - 1, c - 1);
				showTile(r, c - 1);
				showTile(r + 1, c - 1);
				showTile(r - 1, c);
				showTile(r + 1, c);
				showTile(r - 1, c + 1);
				showTile(r, c + 1);
				showTile(r + 1, c + 1);
			}

		}
	}

	// Called when the user loses the game
	public void loseGame() {
		System.out.println("You Lose!");

		// Set each gameBoard location with a mine to be an X, revealing every mine on
		// the board
		for (int i = 0; i < mineCoords.length; i++) {
			if (gameBoard[mineCoords[i][0]][mineCoords[i][1]] != 'F') {
				gameBoard[mineCoords[i][0]][mineCoords[i][1]] = 'X';
			}
		}
		// Print the board
		printGameBoard();
	}

	// Called on the user's first move to ensure the first move selects a blank tile
	private void firstMove(int r, int c) {
		// Generate the map until selectTile returns 1, meaning the game always starts
		// by
		// revealing some section of the map
		do {
			generateMap();

		} while (selectTile(r, c) != 1);
	}

	// Called when the user makes a move
	private boolean makeMove(int r, int c) {
		boolean safe = true;
		// Makes sure the first move does not select a mine
		if (movesMade == 0) {
			firstMove(r, c);
		} else {
			// If the user clicked a mine, set safe to false, causing the player to lose
			if (selectTile(r, c) == -1) {
				safe = false;
			}
		}
		movesMade++;
		return safe;
	}

	// Place a flag on the board at the user's selected location
	private void placeFlag(int r, int c) {
		// Make sure the selected tile is hidden
		if (gameBoard[r][c] == square) {
			gameBoard[r][c] = 'F';
		}
	}

	// Remove a flag from the board at the user's selected location
	private void removeFlag(int r, int c) {
		// Make sure the selected tile is a flag
		if (gameBoard[r][c] == 'F') {
			gameBoard[r][c] = square;
		}
	}

	// Check if the user has won the game
	public boolean checkWin() {
		// Return true if all the mines are flagged
		int minesFlagged = 0;
		for (int i = 0; i < mineCoords.length; i++) {
			int r = mineCoords[i][0];
			int c = mineCoords[i][1];
			if (gameBoard[r][c] == 'F') {
				minesFlagged++;
			}
		}

		return minesFlagged == mines;
	}

	// Handle user keyboard input. The value passed as a parameter is the value of
	// the key input
	private boolean userInput(int value) {
		boolean safe = true;
		boolean printBoard = true;
		
		if(Engine.LEFT_CHARS.contains(value)) {
			//left char
			if (cursorX > 0) {
				cursorX--;
			}
		} else if(Engine.RIGHT_CHARS.contains(value)) {
			//right char
			if (cursorX < xMax) {
				cursorX++;
			}
		} else if(Engine.UP_CHARS.contains(value)) {
			//up char
			if (cursorY > 0) {
				cursorY--;
			}
		} else if(Engine.DOWN_CHARS.contains(value)) {
			//down char
			if (cursorY < yMax) {
				cursorY++;
			}
		} else if(Engine.EXIT_CHARS.contains(value)) {
			//exit char
			safe = false;
			printBoard = false;
			// For all other characters, do not print the game board, since there is no
			// board update to be done
		} else {
			switch (value) {
			// F
			case 70:
				placeFlag(cursorY, cursorX);
				break;
			// R
			case 82:
				removeFlag(cursorY, cursorX);
				break;
			// Space
			case 32:
				safe = makeMove(cursorY, cursorX);
				break;
			default:
				printBoard = false;
			}
		}
		
		// If a board update is needed, print the game board
		if (safe && printBoard) {
			clearScreen();
			printGameBoard();
		}
		return safe;
	}

	// Wrapper function for the actual play of the game
	public int playGame() throws IOException {
		// The value of the input is stored so it can be returned. It needs to be
		// returned so the user can press 'Q' to quit
		int input = 0;
		while (!checkWin() && userInput(input = RawConsoleInput.read(true))) {
		}
		return input;
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
