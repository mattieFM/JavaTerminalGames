import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.io.File;
import biz.source_code.utils.RawConsoleInput; 
import org.json.*;
import java.util.ArrayList;
import engine.*;

public class Tetris {
	public static int score = 0;
	public static void printBoardFrame() {
		String frame = "";
		
		char[][] entitiesBoard = Engine.entities.getBoard(true);
		for (int i = 0; i < Engine.board.length; i++) {
			frame +='|';
			for (int j = 0; j < Engine.board[0].length; j++) {
				if(entitiesBoard[i][j] != Engine.EMPTYCHAR) {
					frame += entitiesBoard[i][j];
				} else {
					frame += Engine.board[i][j];
				}
				
			}
			
			frame +='|';
			if(i ==0) {
				frame +=String.format("Score: %d", score);
			}
			frame += "\n";
		}
		
		Engine.displayFrame(frame);
	}
	
	static ArrayList<ArrayList<BoardEntity>> blocks = new ArrayList<>();
	public static void main(String[] args) throws IOException, InterruptedException {
		final String CONFIG_JSON_STRING = new Scanner(new File("config.json")).useDelimiter("\\Z").next();
		JSONObject config = new JSONObject(CONFIG_JSON_STRING);
		boolean shouldContinue = true;
		
		//get config vars
		final String INSTRUCTIONS = config.getString("instructions");
		final char EXIT_CHAR = config.getString("exitChar").charAt(0);
		final ArrayList<Integer> LEFT_CHARS = mapJSONArrToIntArr(config.getJSONArray("left"));
		final ArrayList<Integer> RIGHT_CHARS = mapJSONArrToIntArr(config.getJSONArray("right"));
		final ArrayList<Integer> UP_CHARS = mapJSONArrToIntArr(config.getJSONArray("up"));
		final ArrayList<Integer> DOWN_CHARS = mapJSONArrToIntArr(config.getJSONArray("down"));
		final int WIDTH =10;
		final int HEIGHT = 25;
		final int MS_PER_FRAME = 50;
		int ticks = 0;
		int DROP_EVERY_X_TICKS = 20;
		long time = new Date(0).getTime();

		ArrayList<JSONObject> temp = new ArrayList<JSONObject>();
		
		Engine.makeBoard(WIDTH, HEIGHT);
		
		ArrayList<BoardEntity> currentBlock = new ArrayList<>();
		ArrayList<BoardEntity> savedBlock = new ArrayList<>();
		
		int randomNum = 0 + (int)(Math.random() * 6);
		makeBlock(randomNum,currentBlock);
		
		System.out.println(INSTRUCTIONS);
		while(shouldContinue) {
			randomNum = 0 + (int)(Math.random() * 6);
			boolean currentBlockCollision = false;
			BoardEntity centerOfBlock = currentBlock.get(0);
			TimeUnit.MILLISECONDS.sleep(MS_PER_FRAME);
			ticks++;
			int input = RawConsoleInput.read(false);
				if(input == EXIT_CHAR) {
					shouldContinue = false;
				} else if(UP_CHARS.contains(input)) {
					//is left input
					Engine.rotateGroupWithCenter(currentBlock, centerOfBlock, 90, WIDTH, HEIGHT);
					
				} else if(RIGHT_CHARS.contains(input)) {
					//is right input
					for(int i =0; i <currentBlock.size(); i++) {
						BoardEntity block = currentBlock.get(i);
						block.move(1, 0, WIDTH, HEIGHT);
					}
				} else if(LEFT_CHARS.contains(input)) {
					//is left input
					for(int i =0; i <currentBlock.size(); i++) {
						BoardEntity block = currentBlock.get(i);
						block.move(-1, 0, WIDTH, HEIGHT);
					}
				} else if (input == 'x') {
					ArrayList<BoardEntity> tempBlock = new ArrayList<>();
					tempBlock.addAll(currentBlock);
					for (int i = 0; i < currentBlock.size(); i++) {
						Engine.entities.entities.remove(Engine.entities.entities.indexOf(currentBlock.get(i)));
					}
					currentBlock.clear();
					if(savedBlock.size() > 0) {
						currentBlock.addAll(savedBlock);
						for (int i = 0; i < currentBlock.size(); i++) {
							Engine.addEntity(currentBlock.get(i));
						}
					} else {
						makeBlock(randomNum,currentBlock);
					}
					savedBlock.clear();
					savedBlock.addAll(tempBlock);
				}
				
				
				if(DOWN_CHARS.contains(input)) {
					//is right input
					DROP_EVERY_X_TICKS = 2;
				} else {
					DROP_EVERY_X_TICKS = 20;
				}
				
				if(ticks % DROP_EVERY_X_TICKS == 0) {
					for(int i =0; i < blocks.size(); i++) {
						ArrayList<BoardEntity> block = blocks.get(i);
						boolean collision = false;
						for (int j = 0; j < block.size(); j++) {
							BoardEntity subBlock = block.get(j);
							if(Engine.getCollision(subBlock.getX(),subBlock.getY()+1, block) && !subBlock.isDead) {
								collision = true;
							}
						}
						if(!collision) {
							for (int j = 0; j < block.size(); j++) {
								BoardEntity subBlock = block.get(j);
								subBlock.move(0, 1, WIDTH, HEIGHT);
							}
							
						} else {
							if(currentBlock.contains(block.get(0)))
								currentBlockCollision = true;
						}
				}
				
				if(currentBlockCollision) {
					randomNum = 0 + (int)(Math.random() * 6);
					makeBlock(randomNum, currentBlock);
					
					int numberOfLinesCleared = 0;
					for(int i = 0; i < HEIGHT; i++) {
						if(Engine.getRowCollision(i)) {
							clearRow(i);
							numberOfLinesCleared++;
						};
					}
					if(numberOfLinesCleared > 0) score += Math.pow(2, numberOfLinesCleared);
				}
				
				}
				
				printBoardFrame();
		}
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
	/**
	 * x 0-6 for all 7 block variients
	 */
	static void makeBlock(int x, ArrayList<BoardEntity> list) {
		// clear list
		list.clear();
		
		switch(x) {
		case 0:
			list.add(new BoardEntity(0,0,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(1,0,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(2,0,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(3,0,Engine.SQUARE_CHAR));
			break;
		case 1:
			//t
			list.add(new BoardEntity(2,0,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(0,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(1,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(2,1,Engine.SQUARE_CHAR));
			
			break;
		case 2:
			//l
			list.add(new BoardEntity(0,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(1,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(2,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(2,0,Engine.SQUARE_CHAR));
			break;
		case 3:
			//backwards l
			list.add(new BoardEntity(0,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(1,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(2,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(0,0,Engine.SQUARE_CHAR));
			break;
		case 4:
			//cube
			list.add(new BoardEntity(0,0,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(0,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(1,0,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(1,1,Engine.SQUARE_CHAR));
			break;
		case 5:
			//z
			list.add(new BoardEntity(1,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(0,0,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(1,0,Engine.SQUARE_CHAR));
			
			list.add(new BoardEntity(2,1,Engine.SQUARE_CHAR));
			break;
		case 6:
			//backwards z
			list.add(new BoardEntity(1,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(0,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(1,0,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(2,0,Engine.SQUARE_CHAR));
			break;
		}
		for(int i =0; i <list.size(); i++) {
			BoardEntity block = list.get(i);
			Engine.addEntity(block);
		}
		ArrayList<BoardEntity> l2 = new ArrayList<>();
		l2.addAll(list);
		blocks.add(l2);
	}
	
	static void clearRow(int y) {
		int s = Engine.entities.entities.size();
		for (int i = 0; i < s; i++) {
			BoardEntity b = Engine.entities.entities.get(i);
			if(b.getY() == y) {
				Engine.entities.entities.remove(b);
				b.setDead(true);
				--s;
				--i;
			}
		}
		Engine.printBoardFrame();
		
	}
}
