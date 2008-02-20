import java.io.Serializable;
import java.awt.Polygon;
import java.awt.geom.*;
import java.awt.Shape;
import java.util.*;

public class Bullet extends Collidable{
	public static final long serialVersionUID = 1L;
	final int speed=25;
	final int angle;
	int turnsLeft=35;
	final long bulletID;
	public Bullet(int x, int y, int angle, int dmg){
		super(x,y,1,6,dmg);
		this.angle=angle;
		bulletID=new Date().getTime();//each bullet should have its own unique ID so that you can remove them easily.
	}
	public long getID(){
		return bulletID;
	}
	public int getTL(){
		return turnsLeft;
	}
	public void move(){
		if(y<0) y=600;
		if(y>600) y=0;
		if(x<0) x=800;
		if(x>800) x=0;
		y-=speed*Math.sin(Math.toRadians(angle));
		x+=speed*Math.cos(Math.toRadians(angle));
		turnsLeft--;
	}
	public void collide(Collidable other){
		System.out.println("A bullet hit"+other);
//		this=null;
	}
	public Polygon getPoly(){
		Polygon p=new Polygon();
		p.addPoint(x,y);
		p.addPoint(x+1,y);
		p.addPoint(x+1,y+6);
		p.addPoint(x,y+6);
        Point2D.Double c = getPolygonCenter(p);
        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(-(angle-90))/2, c.x, c.y);
        Shape l = at.createTransformedShape(p);
		PathIterator iter=l.getPathIterator(at);
		float[] pts= new float[6];
		p.reset();
		while(!iter.isDone()){
	      	int type = iter.currentSegment(pts);
	      	switch(type){
	        case PathIterator.SEG_MOVETO :
	          //System.out.println("SEG_MOVETO");
	          p.addPoint((int)pts[0],(int)pts[1]);
	          break;
	        case PathIterator.SEG_LINETO :
	          //System.out.println("SEG_LINETO");
	          p.addPoint((int)pts[0],(int)pts[1]);
	          break;
	      	}
	      	iter.next();
    	}
    	return p;
    	
	}
    private Point2D.Double getPolygonCenter(Polygon poly)
    {
        // R + r = height
        Rectangle2D r2 = poly.getBounds2D();
        double cx = r2.getX() + r2.getWidth()/2;
        double cy = r2.getY() + r2.getHeight()/2;
        int sides = poly.xpoints.length;
        double side = Point2D.distance(poly.xpoints[0], poly.ypoints[0],
                                       poly.xpoints[1], poly.ypoints[1]);
        double R = side / (2 * Math.sin(Math.PI/sides));
        double r = R * Math.cos(Math.PI/sides);
        double dy = (R - r)/2;
        return new Point2D.Double(cx, cy + dy);
    }	
}