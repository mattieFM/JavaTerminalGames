package src;

import java.io.IOException;
import java.util.ArrayList;

import biz.source_code.utils.RawConsoleInput;
import engine.BoardEntity;
import engine.Engine;

public class Menu {

	public static void main(String[] args) throws IOException {
		// Make the menu board
		Engine.makeBoard(20, 20);
		
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
		String game1Name = "Tower of Hanoi";
		for (int i = 0; i < game1Name.length(); i++) {
			Engine.addEntity(new BoardEntity(2+i, 2, game1Name.charAt(i)));
		}
		String game2Name = "Tetris";
		for (int i = 0; i < game2Name.length(); i++) {
			Engine.addEntity(new BoardEntity(2+i, 7, game2Name.charAt(i)));
		}
		String game3Name = "Minesweeper";
		for (int i = 0; i < game3Name.length(); i++) {
			Engine.addEntity(new BoardEntity(2+i, 12, game3Name.charAt(i)));
		}
		String game4Name = "Snake";
		for (int i = 0; i < game4Name.length(); i++) {
			Engine.addEntity(new BoardEntity(2+i, 17, game4Name.charAt(i)));
		}
		
		System.out.print("Press 'e' to make a selection: ");
		
		// Run the menu until the user makes a selection
		boolean madeASelection = false;
		
		while (madeASelection == false) {
			// Wait until input is read in
			char input = (char) RawConsoleInput.read(true);
			
			// Check to see if the direction changed
			switch (input) {
			case 'w': {
				for (int i = 0; i < selectionBox.size(); i++) {
					selectionBox.get(i).move(0, -5, 20, 20);
				}
				break;
			}
			case 's': {
				for (int i = 0; i < selectionBox.size(); i++) {
					selectionBox.get(i).move(0, 5, 20, 20);
				}
				break;
			}
			case 'e': {
				madeASelection = true;
				break;
			}
			default:
				//throw new IllegalArgumentException("Unexpected value: " + input);
			}
			
			Engine.printBoardFrame();
		}
	}
}
