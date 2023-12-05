package gameEngine;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Represents all entities on a board in terms of displayment
 * @author mmful
 *
 */
public class BoardEntities {
	/**
	 * the max x cord
	 */
	private int boardWidth;
	/**
	 * the max y cord
	 */
	private int boardHeight;
	
	/**
	 * a incomplete arr of entities on the board
	 */
	private char[][] entitiesBoard;
	
	/**
	 * all entities on this board
	 */
	public LinkedList<BoardEntity> entities;
	
	
	/**
	 * create a new board entities obj 
	 * @param height the height of the board
	 * @param width the width of the board this is assosiated with
	 */
	public BoardEntities(int height, int width) {
		boardWidth = width;
		boardHeight = height;
		entitiesBoard = new char[height][width];
		entities = new LinkedList<BoardEntity>();
		
		//fill board with empty
		wipeBoard();
	}
	/**
	 * fill the board completely with empty chars
	 */
	private void wipeBoard() {
		for (int i = 0; i < boardWidth; i++) {
			for (int j = 0; j < boardHeight; j++) {
				entitiesBoard[j][i] = Engine.EMPTYCHAR;
			}
		}
	}
	
	/**
	 * update the board
	 */
	public void updateBoard(boolean force) {
		
		if(force) {
			wipeBoard();
		}
		
		//for all that have moved erase
		for (int i = 0; i < entities.size(); i++) {
			BoardEntity e = entities.get(i);
			if(e.hasChanged()) {
				entitiesBoard[e.getLastY()][e.getLastX()] = Engine.EMPTYCHAR;
			}
		}
		//update all the ones that need to be displayed
		for (int i = 0; i < entities.size(); i++) {
			BoardEntity e = entities.get(i);
			if(e.hasChanged() || force) {
				entitiesBoard[e.getY()][e.getX()] = e.render();
			}
		}
	}
	
	/**
	 * add a new entity to the board
	 * @param e the entity to add
	 */
	public void addEntity(BoardEntity e) {
		entities.add(e);
	}
	
	/**
	 * get the board of entities
	 * @return
	 */
	public char[][] getBoard(boolean force) {
		
		updateBoard(force);
		return entitiesBoard;
	}
	
	
}
