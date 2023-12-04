package src;

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
		final int HEIGHT = 10;
		final int MS_PER_FRAME = 100;
		int ticks = 0;
		int DROP_EVERY_X_TICKS = 5;
		long time = new Date(0).getTime();

		ArrayList<JSONObject> temp = new ArrayList<JSONObject>();
		
		Engine.makeBoard(WIDTH, HEIGHT);
		
		ArrayList<BoardEntity> currentBlock = new ArrayList<>();
		ArrayList<BoardEntity> savedBlock = new ArrayList<>();
		
		makeBlock(0,currentBlock);
		for(int i =0; i <currentBlock.size(); i++) {
			BoardEntity block = currentBlock.get(i);
			Engine.addEntity(block);
		}
		
		System.out.println(INSTRUCTIONS);
		while(shouldContinue) {
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
					//is up input
					for(int i =0; i <currentBlock.size(); i++) {
						BoardEntity block = currentBlock.get(i);
						block.move(0, -1, WIDTH, HEIGHT);
						if(Engine.getCollision(block.getX(),block.getY()+1)) {
							currentBlockCollision = true;
						}
						
					}
				} 
				
				
				
				if(DOWN_CHARS.contains(input)) {
					//is right input
					DROP_EVERY_X_TICKS = 2;
				} else {
					DROP_EVERY_X_TICKS = 5;
				}
				
				if(ticks % DROP_EVERY_X_TICKS == 0) {
					for(int i =0; i <currentBlock.size(); i++) {
						BoardEntity block = currentBlock.get(i);
						block.move(0, 1, WIDTH, HEIGHT);
					}
				}
				
				if(currentBlockCollision) {
					makeBlock(0, currentBlock);
				}
				
				Engine.printBoardFrame();
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
			list.add(new BoardEntity(0,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(1,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(2,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(2,0,Engine.SQUARE_CHAR));
			break;
		case 2:
			//l
			list.add(new BoardEntity(0,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(1,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(2,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(3,0,Engine.SQUARE_CHAR));
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
			list.add(new BoardEntity(0,0,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(1,0,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(1,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(2,1,Engine.SQUARE_CHAR));
			break;
		case 6:
			//backwards z
			list.add(new BoardEntity(0,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(1,1,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(1,0,Engine.SQUARE_CHAR));
			list.add(new BoardEntity(2,0,Engine.SQUARE_CHAR));
			break;
		}
	}
}
