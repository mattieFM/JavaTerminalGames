package gameEngine;

/**
 * a class representing a 2d board based entity
 * @author mmful
 *
 */
public class BoardEntity {
	/**
	 * the x cord of the entity
	 */
	private int x;
	/**
	 * the y cord
	 */
	private int y;
	/**
	 * the char to display
	 */
	private char c;
	
	/**
	 * the last x this entity was at
	 */
	private int lastX = 0;
	
	/**
	 * the last y this entity was at
	 */
	private int lastY = 0;
	
	/**
	 * whether this entity has changed since last rendered
	 */
	private boolean hasChanged;
	
	/**
	 * whether the entity is dead or not
	 */
	public boolean isDead;
	
	
	/**
	 * create a new board entity
	 * @param x the x pos
	 * @param y the y pos
	 * @param c the char of the entity (that is what the entity looks like)
	 */
	public BoardEntity(int x,int y,char c){
		this.x=x;
		this.y=y;
		this.c=c;
		this.hasChanged = true;
	}
	
	/**
	 * move the char
	 * @param x relative x to move
	 * @param y relative y to move
	 */
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
		this.hasChanged = true;
	}
	
	/**
	 * move the char releative to x and y passed staying within bounds
	 * @param x relative x to move
	 * @param y relative y to move
	 */
	public void move(int x, int y, int maxX, int maxY) {
		if(!isDead) {
			lastX = this.x;
			lastY = this.y;
			this.x= Math.max(0, Math.min(maxX-1, this.x + x));
			this.y= Math.max(0, Math.min(maxY-1, this.y + y));
			hasChanged = true;
		}
	}
	
	public void rotateWithCenter(int degrees, int centerX, int centerY) {
		double theta = Math.toRadians(degrees);
		double x1 = getX() - centerX;
		double y1 = getY() - centerY;

		double x2 = x1 * Math.cos(theta) - y1 * Math.sin(theta);
		double y2 = x1 * Math.sin(theta) + y1 * Math.cos(theta);

		lastX = this.x;
		lastY = this.y;
		
		this.x= (int) x2 + centerX;
		this.y= (int) y2 + centerY;
	}
	
	/**
	 * get the x cord of this entity
	 * @return the x cord
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * get the y cord of this entity
	 * @return the y cord
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * get the display char
	 * @return the char
	 */
	public char getChar() {
		return c;
	}
	
	/**
	 * whether this entity has changed since last rendered or not
	 * @return
	 */
	public boolean hasChanged() {
		return this.hasChanged;
	}
	
	/**
	 * the last x pos this entity was at
	 * @return
	 */
	public int getLastX() {
		return lastX;
	}
	
	/**
	 * the last y pos this entity was at
	 * @return
	 */
	public int getLastY() {
		return lastY;
	}
	
	/**
	 * same as get char but updates the hasChanged var to assert that this is in its correct pos
	 * @return
	 */
	char render() {
		hasChanged = false;
		return getChar();
	}

	public void setDead(boolean b) {
		this.isDead = b;
		
	}
}