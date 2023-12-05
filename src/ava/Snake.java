/* This program will simulate the game Snake that runs in the terminal */

package ava;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import gameEngine.*;

import biz.source_code.utils.RawConsoleInput;

public class Snake {
	static final int MOVE_EVERY_X_FRAMES = 5;
	static BoardEntity APPLE;
	public static void main(String[] args) throws IOException, InterruptedException {
		int frames = 0;
		Engine.loadConfig();
		// Make the game board and entity list
		Engine.makeBoard(18, 18);
		ArrayList<BoardEntity> snakeParts = new ArrayList<BoardEntity>();
		
		// Create the boarder and snake
		BoardEntity snake = new BoardEntity(1,1, 's');
		snakeParts.add(Engine.addEntity(snake));
		Coordinate spawnCoor = new Coordinate();
		spawnCoor.xCoor = 1;
		spawnCoor.yCoor = 1;
		
		// Create the boarders for the board
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 17; j++) {
				Engine.addEntity(new BoardEntity(j, i*17,'-'));
			}
		}
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 17; j++) {
				Engine.addEntity(new BoardEntity(i*17, j,'|'));
			}
		}
		
		// Define variables for the snake
		char direction = 'e';
		int length = 1;
		ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
		coordinates.add(spawnCoor);
		Coordinate apple = new Coordinate();
		spawnApple(apple, coordinates, length);
		
		// Check if there is a collision. If not, continue moving in the specified direction	
		boolean notDead = checkCollision(coordinates);
		while(notDead) {
			notDead = checkCollision(coordinates);;
			// Delay time
			TimeUnit.MILLISECONDS.sleep(100);
			frames++;
			int input = RawConsoleInput.read(false);
			
			if(Engine.LEFT_CHARS.contains(input)) {
				//left char
				direction = 'w';
			} else if(Engine.RIGHT_CHARS.contains(input)) {
				//right char
				direction = 'e';
			} else if(Engine.UP_CHARS.contains(input)) {
				//up char
				direction = 'n';
			} else if(Engine.DOWN_CHARS.contains(input)) {
				//down char
				direction = 's';
			} else if(Engine.EXIT_CHARS.contains(input)) {
				//exit char
				notDead = false;
			}

			if(frames % MOVE_EVERY_X_FRAMES ==0) {
				frames = 0;
				// Move in the correct direction
				switch (direction) {
				case 'n': {
					snake.move(0, -1, 17, 17);
					Coordinate addCoor = new Coordinate();
					addCoor.xCoor = snake.getX();
					addCoor.yCoor = snake.getY();
					coordinates.add(addCoor);
				break;
				}
				case 's': {
					snake.move(0, 1, 17, 17);
					Coordinate addCoor = new Coordinate();
					addCoor.xCoor = snake.getX();
					addCoor.yCoor = snake.getY();
					coordinates.add(addCoor);
					break;
				}
				case 'w': {
					snake.move(-1, 0, 17, 17);
					Coordinate addCoor = new Coordinate();
					addCoor.xCoor = snake.getX();
					addCoor.yCoor = snake.getY();
					coordinates.add(addCoor);
					break;
				}
				case 'e': {
					snake.move(1, 0, 17, 17);
					Coordinate addCoor = new Coordinate();
					addCoor.xCoor = snake.getX();
					addCoor.yCoor = snake.getY();
					coordinates.add(addCoor);
					break;
				}
				}
				
				// Move the rest of the snake parts
				if (snakeParts.size() > 1) {
					for (int i = 0; i < snakeParts.size(); i++) {
						snakeParts.get(i).set(coordinates.get(coordinates.size()-1-i).xCoor,
								coordinates.get(coordinates.size()-1-i).yCoor);
					}
				}
				
				// Check to see if there's a collision with the apple. If so, create a new one and add length
				boolean hitsAnApple = checkAppleCollision(coordinates, apple, length);
				if (hitsAnApple == true) {
					BoardEntity snakePart = new BoardEntity((int)(apple.xCoor),
							(int)(apple.yCoor),'s');
					Engine.addEntity(snakePart);
					length++;
					snakeParts.add(snakePart);
					spawnApple(apple, coordinates, length);
					hitsAnApple = false;
				}
				
				Engine.printBoardFrame();
			}
			
		}
		
		System.out.print("Game Over");
	}
	
	static public boolean checkCollision(ArrayList<Coordinate> coordinates) {
		boolean doesNotHit = true;
		for (int i = 0; i < coordinates.size(); i++) {
			Coordinate currentCoordinate = coordinates.get(i);
			if (currentCoordinate.xCoor >= 16 || currentCoordinate.yCoor >= 16) {
				doesNotHit = false;
			}
			if (currentCoordinate.xCoor == 0 || currentCoordinate.yCoor == 0) {
				doesNotHit = false;
			}
		}
		return doesNotHit;
	}
	
	public static void spawnApple(Coordinate apple, ArrayList<Coordinate> coordinates, int length) {
		boolean valid = false;
		int randomXCoor = (int)(Math.random()*(17-1)+1);
		int randomYCoor = (int)(Math.random()*(17-1)+1);
		while (valid == false) {
			randomXCoor = (int)(Math.random()*(17-1)+1);
			randomYCoor = (int)(Math.random()*(17-1)+1);
			for (int i = 0; i < length; i++) {
				Coordinate currentBlock = coordinates.get(i);
				if (currentBlock.xCoor != randomXCoor || currentBlock.yCoor != randomYCoor) {
					valid = true;
				}
				else
				{
					valid = false;
				}
			}
		}
		apple.xCoor = randomXCoor;
		apple.yCoor = randomYCoor;
		if(APPLE == null) {
			APPLE = Engine.addEntity(new BoardEntity(randomXCoor, randomYCoor,'o'));
		} else {
			APPLE.set(randomXCoor, randomYCoor);
		}
			
		
	}
	
	public static boolean checkAppleCollision(ArrayList<Coordinate> coordinates, Coordinate apple, int length) {
		boolean hitsApple = false;
		for (int i = 0; i < length; i++) {
			Coordinate currentCoordinate = coordinates.get(coordinates.size()-1-i);
			if (currentCoordinate.xCoor == apple.xCoor && currentCoordinate.yCoor == apple.yCoor) {
				hitsApple = true;
			}
		}
		return hitsApple;
	}
}

class Coordinate {
	int xCoor = 0;
	int yCoor = 1;
}