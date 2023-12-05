package gameEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Engine {
	/**
	 * a 2d board of chars to display for 2d board based games
	 */
	public static char[][] board;
	
	public static BoardEntities entities;
	
	/**
	 * the char used to indicate that a space is empty with nothing in it
	 */
	public static final char EMPTYCHAR = 0;
	
	public static final char SQUARE_CHAR = '■';
	
	//config stuff
	public static String CONFIG_JSON_STRING;
	public static JSONObject config;
	public static String TETRIS_INSTRUCTIONS;
	/**
	 * all chars for exiting
	 */
	public static ArrayList<Integer> EXIT_CHARS;
	/**
	 * all chars for left input
	 */
	public static ArrayList<Integer> LEFT_CHARS;
	/**
	 * all chars for right input
	 */
	public static ArrayList<Integer> RIGHT_CHARS;
	/**
	 * all chars for up input
	 */
	public static ArrayList<Integer> UP_CHARS;
	/**
	 * all chars for down input
	 */
	public static ArrayList<Integer> DOWN_CHARS;
	/**
	 * main accept button of the game
	 */
	public static ArrayList<Integer> ACCEPT_CHARS;
	/**
	 * secondary accept buttons of the game
	 */
	public static ArrayList<Integer> ALT_ACCEPT_CHARS;
	
	/**
	 * load all config from config file
	 * @throws FileNotFoundException
	 */
	public static void loadConfig() throws FileNotFoundException{
		try {
			CONFIG_JSON_STRING = new Scanner(new File("config.json")).useDelimiter("\\Z").next();
		} catch (Exception e) {
			try {
				CONFIG_JSON_STRING = new Scanner(new File("../config.json")).useDelimiter("\\Z").next();
			} catch (Exception e2) {
				CONFIG_JSON_STRING = new Scanner(new File("../../config.json")).useDelimiter("\\Z").next();
			}
		}
		
		config = new JSONObject(CONFIG_JSON_STRING);
		TETRIS_INSTRUCTIONS = config.getString("tetrisInstructions");
		EXIT_CHARS = mapJSONArrToIntArr(config.getJSONArray("exit"));
		LEFT_CHARS = mapJSONArrToIntArr(config.getJSONArray("left"));
		RIGHT_CHARS = mapJSONArrToIntArr(config.getJSONArray("right"));
		UP_CHARS = mapJSONArrToIntArr(config.getJSONArray("up"));
		DOWN_CHARS = mapJSONArrToIntArr(config.getJSONArray("down"));
		ACCEPT_CHARS = mapJSONArrToIntArr(config.getJSONArray("accept"));
		ALT_ACCEPT_CHARS = mapJSONArrToIntArr(config.getJSONArray("altAccept"));
	}
	
	
	/**
	 * initalzie the board with all empty chars
	 * @param width the height of the board
	 * @param height the width of the board
	 * @throws FileNotFoundException 
	 */
	public static void makeBoard(int width, int height) throws FileNotFoundException {
		loadConfig();
		board = new char[height][width];
		entities = new BoardEntities(height, width);
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				board[j][i] = ' ';
			}
		}
	}
	
	public static boolean getCollisionAlt(int x, int y) {
		char[][] entitiesBoard = entities.getBoard(true);
		if(y >= entitiesBoard.length || x >= entitiesBoard.length) {
			return true;
		}
		return entitiesBoard[x][y] != EMPTYCHAR;
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
	 */
	public static BoardEntity addEntity(BoardEntity e) {
		entities.addEntity(e);
		return e;
	}
	
	public static boolean getCollision(int x, int y,  ArrayList<BoardEntity> list) {
		char[][] entitiesBoard = entities.getBoard(true);
		if(y >= entitiesBoard.length) {
			return true;
		}
		char c = entitiesBoard[y][x];
		for (int i = 0; i < list.size(); i++) {
			BoardEntity b = list.get(i);
			if(x == b.getX() && y == b.getY()) {
				return false;
			}
		}
		return c != EMPTYCHAR;
	}
	
	/**
	 * return true if a whole row collides
	 * @param y
	 * @return
	 */
	public static boolean getRowCollision(int y) {
		char[][] entitiesBoard = entities.getBoard(true);
		for (int i = 0; i < entitiesBoard[y].length; i++) {
			if(entitiesBoard[y][i] == EMPTYCHAR) {
				return false;
			}
		}
		return true;
		
	}
	
	
	

	/**
	 * print a frame of the board handling all updates to entities
	 * @param rightLines everything to display to the right of the board
	 * @param leftLines everything to display to the left of the board
	 * @param top prints at the top of the board
	 * @param bot prints at the btm of the board
	 */
	public static void printBoardFrame(String[] rightLines, String[] leftLines, String top, String bot) {
		String frame = new String();
		frame = frame.concat(top);
		
		char[][] entitiesBoard = entities.getBoard(true);
		for (int i = 0; i < board.length; i++) {
			//add left lines
			if(leftLines.length >= i+1) {
				frame = frame.concat(leftLines[i]);
			}
			for (int j = 0; j < board[0].length; j++) {
				if(entitiesBoard[i][j] != EMPTYCHAR) {
					frame += entitiesBoard[i][j];
				} else {
					frame += board[i][j];
				}
				
			}
			//add right lines
			if(rightLines.length >= i+1) {
				frame = frame.concat(rightLines[i]);
			}
			frame += "\n";
		}
		
		frame = frame.concat(bot);
		displayFrame(frame);
	}
	/**
	 * @overload
	 */
	public static void printBoardFrame() {
		String[] up = {new String("\0")};
		String[] btm = {new String("\0")};
		printBoardFrame(up, btm,new String("\0"),new String("\0"));
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
	        	System.out.println("\u001b[2J");
	        	//call clear cmd
	        	//new ProcessBuilder("/bin/sh", "-c", "cls").inheritIO().start().waitFor();
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
	
	static ArrayList<Integer> mapJSONArrToIntArr(JSONArray arr){
		Iterator<Object> it = arr.iterator();
		ArrayList<Integer> out = new ArrayList<>();
		while(it.hasNext()) {
			Object e = it.next();
			out.add((int)e);
			System.out.println(e);
		}
		
		
		return out;
	}
	
}