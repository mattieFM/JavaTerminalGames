package engine;

import java.util.ArrayList;
import java.util.Iterator;

public class Engine {
	/**
	 * a 2d board of chars to display for 2d board based games
	 */
	private static char[][] board;
	
	private static BoardEntities entities;
	
	/**
	 * the char used to indicate that a space is empty with nothing in it
	 */
	public static final char EMPTYCHAR = 0;
	
	public static final char SQUARE_CHAR = '■';
	
	/**
	 * initalzie the board with all empty chars
	 * @param width the height of the board
	 * @param height the width of the board
	 */
	public static void makeBoard(int width, int height) {
		board = new char[height][width];
		entities = new BoardEntities(height, width);
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				board[i][j] = ' ';
			}
		}
	}
	
	public static void rotateGroupWithCenter(ArrayList<BoardEntity> list, BoardEntity center, int degrees, int maxX, int maxY) {
		int biggestXOffset = 0;
		int biggestYOffset = 0;
		
		for(int i =0; i <list.size(); i++) {
			BoardEntity block = list.get(i);
			block.rotateWithCenter(degrees, center.getX(), center.getY());
			
			//find x off
			if(block.getX() > maxX) {
				int xOff = Math.abs(maxX -1 - block.getX());
				if(xOff > Math.abs(biggestXOffset)) {
					biggestXOffset = xOff;
				}
			} else if (block.getX() < 0) {
				int xOff = Math.abs(0 - block.getX());
				if(xOff > Math.abs(biggestXOffset)) {
					biggestXOffset = xOff;
				}
			}
			
			//find y off
			if(block.getY() > maxY) {
				int yOff = Math.abs(maxY -1 - block.getY());
				if(yOff > Math.abs(biggestYOffset)) {
					biggestYOffset = yOff;
				}
			} else if (block.getY() < 0) {
				int yOff = Math.abs(0 - block.getY());
				if(yOff > Math.abs(biggestYOffset)) {
					biggestYOffset = yOff;
				}
			}
		}
		
		for(int i =0; i <list.size(); i++) {
			BoardEntity block = list.get(i);
			
			block.set(block.getX()+biggestXOffset, block.getY()+biggestYOffset);
			
		}
	
	}
	
	/**
	 * add a new entity to the map
	 * @param e the entity to add
	 * @return 
	 */
	public static BoardEntity addEntity(BoardEntity e) {
		entities.addEntity(e);
		return e;
	}
	
	public static boolean getCollision(int x, int y) {
		char[][] entitiesBoard = entities.getBoard();
		if(y >= entitiesBoard.length || x >= entitiesBoard.length) {
			return true;
		}
		return entitiesBoard[x][y] != EMPTYCHAR;
	}
	
	/**
	 * return true if a whole row collides
	 * @param y
	 * @return
	 */
	public boolean getRowCollision(int y) {
		char[][] entitiesBoard = entities.getBoard();
		for (int i = 0; i < entitiesBoard[y].length; i++) {
			if(entitiesBoard[y][i] == EMPTYCHAR) {
				return false;
			}
		}
		return true;
		
	}
	
	/**
	 * 
	 */
	public static void printBoardFrame() {
		String frame = "";
		
		char[][] entitiesBoard = entities.getBoard();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if(entitiesBoard[i][j] != EMPTYCHAR) {
					frame += entitiesBoard[i][j];
				} else {
					frame += board[i][j];
				}
				
			}
			frame += "\n";
		}
		
		displayFrame(frame);
	}
	
	/**
	 * clear the terminal works for unix and windows
	 */
	public static void clearScreen() {  
		try
	    {
	        final String os = System.getProperty("os.name");
	        
	        if (os.contains("Windows"))
	        {
	        	//windows nonsense to call cls
	        	new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	        }
	        else
	        {
	        	//print clear code (should work on most unix systems)
	        	System.out.print("\033[H\033[2J\n");
	        	System.out.flush();
	        	//call clear cmd
	            Runtime.getRuntime().exec("clear");
	        }
	    }
	    catch (final Exception e)
	    {
	    	//System.out.print(e);
	    }
	}  
	
	/**
	 * display a string clearing the screen beforehand
	 * @param frame string to dsiplay
	 */
	public static void displayFrame(String frame) {  
	    clearScreen();
	    System.out.println(frame);
	    
	}  
	
}