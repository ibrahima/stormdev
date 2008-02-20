import java.awt.*;
import java.io.Serializable;
import java.awt.geom.Area;

public abstract class Collidable implements Serializable{
	protected int x, y, width, height;
	protected double speed;
	protected int angle=90;
	protected int dmg;
	protected Polygon bounds;
	public Collidable(){
		x=0;
		y=0;
		width=1;
		height=1;
	}
	public Collidable(int x, int y, int width, int height, int dmg){
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.dmg=dmg;
	}
	public int getDmg(){
		return dmg;
	}
	public Rectangle getRect(){
		return new Rectangle(x,y,width,height);
	}
	public Polygon getPoly(){
		return bounds;
	}
	public boolean checkCollision(Collidable other){
		Area a1= new Area(getPoly());
		Area a2=new Area(other.getPoly());
		a1.intersect(a2);
		//System.out.println("Message from checkCollision:"+a1);
		return !a1.isEmpty();
	}
	public abstract void move();
	public abstract void collide(Collidable other);
	public int getX(){return x;}
	public int getY(){return y;}
}