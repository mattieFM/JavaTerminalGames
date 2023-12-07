/* This program will simulate the game Snake that runs in the terminal */

package src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import biz.source_code.utils.RawConsoleInput;
import engine.BoardEntity;
import engine.Engine;

public class Snake {

	public static void main(String[] args) throws IOException, InterruptedException {
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
		while(checkCollision(coordinates, length) == true) {
			
			// Delay time
			TimeUnit.MILLISECONDS.sleep(500);
			char input = (char) RawConsoleInput.read(false);
			
			// Check to see if the direction changed
			switch (input) {
			case 'w': {
				direction = 'n';
				break;
			}
			case 's': {
				direction = 's';
				break;
			}
			case 'a': {
				direction = 'w';
				break;
			}
			case 'd': {
				direction = 'e';
				break;
			}
			default:
				//throw new IllegalArgumentException("Unexpected value: " + input);
			}
			
			// Move in the correct direction
			switch (direction) {
			case 'n': {
				snake.move(0, -1, 18, 18);
				Coordinate addCoor = new Coordinate();
				addCoor.xCoor = snake.getX();
				addCoor.yCoor = snake.getY();
				coordinates.add(addCoor);
			break;
			}
			case 's': {
				snake.move(0, 1, 18, 18);
				Coordinate addCoor = new Coordinate();
				addCoor.xCoor = snake.getX();
				addCoor.yCoor = snake.getY();
				coordinates.add(addCoor);
				break;
			}
			case 'w': {
				snake.move(-1, 0, 18, 18);
				Coordinate addCoor = new Coordinate();
				addCoor.xCoor = snake.getX();
				addCoor.yCoor = snake.getY();
				coordinates.add(addCoor);
				break;
			}
			case 'e': {
				snake.move(1, 0, 18, 18);
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
		
		System.out.print("Game Over");
	}
	
	static public boolean checkCollision(ArrayList<Coordinate> coordinates, int length) {
		boolean doesNotHit = true;
		Coordinate headCoordinate = coordinates.get(coordinates.size()-1);
		if (headCoordinate.xCoor == 17 || headCoordinate.yCoor == 17) {
			doesNotHit = false;
		}
		if (headCoordinate.xCoor == 0 || headCoordinate.yCoor == 0) {
			doesNotHit = false;
		}
		if (length > 1) {
			for (int i = 0; i < length; i++) {
				Coordinate currentCoordinate = coordinates.get(coordinates.size()-2-i);
				if (currentCoordinate.xCoor == coordinates.get(coordinates.size()-1).xCoor && 
						currentCoordinate.yCoor == coordinates.get(coordinates.size()-1).yCoor) {
					doesNotHit = false;
				}
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
		Engine.addEntity(new BoardEntity(randomXCoor, randomYCoor,'o'));
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