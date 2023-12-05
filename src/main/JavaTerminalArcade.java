package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import biz.source_code.utils.RawConsoleInput;
import gameEngine.*;
import mattie.Tetris;

/**
 * the main class to boot into the menu and all other games
 * @author Avalee & Mattie 
 *
 */
public class JavaTerminalArcade {

	/**
	 * the main method
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		Engine.clearScreen();
		new Menu().run(args);

	}

}

/**
 * a menu for the game selection
 * @author Ava & Mattie
 *
 */
class Menu {
	private static Dictionary<GAMES, String> DisplayNames = new Hashtable<>();
	private static ArrayList<GAMES> Games = new ArrayList<>();

	static enum GAMES {
		MINESWEEPER, TETRIS, TOWER, SNAKE, EXIT
	}
	
	public Menu(){
		//setup games enum
		DisplayNames.put(GAMES.MINESWEEPER, "Mine Sweaper");
		DisplayNames.put(GAMES.TETRIS, "Tetris");
		DisplayNames.put(GAMES.TOWER, "Tower Of Hanoi");
		DisplayNames.put(GAMES.SNAKE, "Snake");
		DisplayNames.put(GAMES.EXIT, "Exit");
		
		Enumeration<GAMES> k = DisplayNames.keys();
		while(k.hasMoreElements()) {
			GAMES s = (k.nextElement());
			Games.add(s);
		}
	}
	
	public void run(String[] args) throws IOException, InterruptedException {	
		// Make the menu board
		int height = Games.size()*7;
		int width = 20;
		Engine.makeBoard(width, height);
		Engine.printBoardFrame();
		
		// Create an array to hold all the entities that make up the selection box
		ArrayList<BoardEntity> selectionBox = new ArrayList<BoardEntity>();

		// Create the outline for the selection box
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 18; j++) {
				BoardEntity addedCharacter = new BoardEntity(j, i*4,'-');
				Engine.addEntity(addedCharacter);
				selectionBox.add(addedCharacter);
			}
		}
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 5; j++) {
				BoardEntity addedCharacter = new BoardEntity(i*18, j,'|');
				Engine.addEntity(addedCharacter);
				selectionBox.add(addedCharacter);
			}
		}
		
		// Write the names of all the games on the screen	
		int yOffset = 2;
		for (int i = 0; i < Games.size(); i++) {
			String gameName = DisplayNames.get(Games.get(i));
			for (int j = 0; j < gameName.length(); j++) {
				Engine.addEntity(new BoardEntity(2+j, yOffset, gameName.charAt(j)));
			}
			yOffset+=5;
		}
		
		Engine.printBoardFrame();
		System.out.print("\nPress 'e' to make a selection: ");
		Engine.printBoardFrame();
		
		// Run the menu until the user makes a selection
		boolean madeASelection = false;
		int inputIndex = 0;
		
		while (madeASelection == false) {
			// Wait until input is read in
			int input = RawConsoleInput.read(true);
			
			// Check to see if the direction changed
			if(Engine.UP_CHARS.contains(input)) {
				//up char
				inputIndex--;
				if(inputIndex < 0) inputIndex = 0;
				for (int i = 0; i < selectionBox.size(); i++) {
					selectionBox.get(i).move(0, -5, width, height);
				}
			} else if(Engine.DOWN_CHARS.contains(input)) {
				//down char
				inputIndex++;
				if(inputIndex > Games.size()-1) inputIndex = 0;
				for (int i = 0; i < selectionBox.size(); i++) {
					selectionBox.get(i).move(0, 5, width, height);
				}
			} else if(Engine.EXIT_CHARS.contains(input)) {
				//exit char
				madeASelection = true;
				inputIndex = Games.indexOf(GAMES.EXIT);
			} else if(Engine.ACCEPT_CHARS.contains(input)) {
				madeASelection = true;
			}
			
			Engine.printBoardFrame();
		}
		//they made a selection now
		GAMES game = Games.get(inputIndex);
		System.out.printf("Playing: %s", DisplayNames.get(game));
		boolean loop = true;
		switch (game) {
		case MINESWEEPER: {
			ryan.Minesweeper.main(args);
			break;
		}
		case SNAKE: {
			ava.Snake.main(args);
			break;
				}
		case TETRIS: {
			mattie.Tetris.main(args);
			break;
		}
		case TOWER: {
			ryan.Hanoi.main(args);
			break;
		}
		case EXIT: {
			loop = false;
			break;
		}
				default:
			throw new IllegalArgumentException("Unexpected value: " + game);
		}
		if(loop) run(args);
	}
	
}



