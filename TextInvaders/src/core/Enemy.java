package core;



/**
 * @author Ibrahim
 *
 */
public class Enemy extends Collidable {

	/**
	 * 
	 */
	public Enemy() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param dmg
	 * @param shape
	 */
	public Enemy(int x, int y, int width, int height, int dmg, String shape) {
		super(x, y, width, height, dmg, shape);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see Collidable#collide(Collidable)
	 */
	@Override
	public void collide(Collidable other) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see Collidable#move()
	 */
	@Override
	public void move() {
		// TODO Auto-generated method stub

	}

}
