package core;

import java.awt.*;
import java.awt.geom.Area;

public abstract class Collidable{
	protected int x, y, width, height;
	protected double speed;
	protected int angle=90;
	protected int dmg;
	protected Polygon bounds;
	protected String shape;
	protected Font smallFont, bigFont;
	public Collidable(){
		x=0;
		y=0;
		width=1;
		height=1;
		shape="/\\";
	}
	public Collidable(int x, int y, int width, int height, int dmg, String shape){
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.dmg=dmg;
		this.shape=shape;
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
	public void draw(Graphics2D g2){
		if(smallFont==null){
			smallFont=g2.getFont();
			bigFont=smallFont.deriveFont((float)18);
		}
		g2.setFont(bigFont);
		g2.setPaint(Color.WHITE);
		g2.drawString(shape,x,y);
		g2.setFont(smallFont);
	}
}