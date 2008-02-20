import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.BufferedImage;

public class GameGui extends JFrame{
	HashMap<String,Ship> map;
	Client c;
	BufferedImage backbuffer=new BufferedImage(800,600,BufferedImage.TYPE_INT_RGB );
	public static final long serialVersionUID = 1L;
	int fps=0,tfps=0;
	long lastTime;
	KeyBoardState kb;
	ArrayList<String> msgs=new ArrayList<String>(5);
	public GameGui(Client c){
		super("LAN Space Combat");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        this.c=c;
        kb=new KeyBoardState(this);
	}
	public synchronized void addMsg(String msg){
		msgs.add(msg);
		while(msgs.size()>=6){
			msgs.remove(0);
		}
	}
	public void setMap(HashMap<String,Ship> map)
	{
		this.map=map;
		//System.out.println(map);
		update();
	}
	
	public synchronized void paint(Graphics g)
	{
		//calculate fps
		tfps++;
		if(kb!=null) {
			ArrayList<String> keys=kb.keysDown();
			Iterator<String> kiter=keys.iterator();
			while(kiter.hasNext()){
				String k=kiter.next();
				if(k!=null&&k.equals("Up")) c.moveUp();
				else if(k!=null&&k.equals("Down")) c.moveDown();
				else if(k!=null&&k.equals("Left")) c.moveLeft();
				else if(k!=null&&k.equals("Right")) c.moveRight();
				else if(k!=null&&k.equals("Space")) c.shoot();
			}
		}
//		clear(g);
		Graphics2D bg=(Graphics2D)backbuffer.getGraphics();
		bg.setColor(Color.DARK_GRAY);
		bg.fillRect(0, 0, 800, 600);
		bg.setColor(Color.GREEN);
		Graphics2D g2=(Graphics2D)g;
		if(map==null) return;
		Iterator<String> iter=map.keySet().iterator();
		bg.drawString("Scoreboard",15,45);
		int y=60;
		while(iter.hasNext()){
			String k=iter.next();
			Collidable temp=map.get(k);
			//System.out.println("Drawing"+k+temp);
			//drawing stuff
			bg.setColor(Color.GREEN);
			bg.drawString(k,temp.getX(),temp.getY());
			bg.drawString(k+"\t "+((Ship)temp).getKills(),20,y);
			drawShip(temp,bg);
			y+=15;
			//g2.drawString(temp.toString(),0,500);
		}
		long now=new Date().getTime();
		if((now-lastTime)>1000)//A second has passed
		{
			lastTime=now;
			fps=tfps;
			tfps=0;
		}
		bg.setColor(Color.GREEN);
		bg.drawString(fps+"FPS",750,40);
		//Draw messages, currently for debugging only
		Iterator<String> msgIt=msgs.iterator();
		int my=580;
		while(msgIt.hasNext()){
			bg.drawString(msgIt.next(),5,my);
			my-=15;
		}
		
		g2.drawImage(backbuffer,null,0,0);
	//update();
	}
	public void update(){
		paint(this.getGraphics());
	}
	public void drawShip(Collidable c, Graphics2D g2){
		Polygon p = c.getPoly();
		if(c.getClass().toString().equals("class Ship"))g2.setColor(Color.GREEN);
		if(c.getClass().toString().equals("class Bullet"))g2.setColor(Color.CYAN);
		g2.drawPolygon(p);
		if(c.getClass().toString().equals("class Ship"))g2.setPaint(Color.WHITE);
		g2.fill(p);
		
		if(c.getClass().toString().equals("class Ship")){
			Ship s=(Ship)c;
			g2.fill3DRect(s.getX(),s.getY(),100,5,true);
			if(s.getHealth()<=20) g2.setPaint(Color.RED);
			else if(s.getHealth()<=40) g2.setPaint(Color.ORANGE);
			else if(s.getHealth()<=60) g2.setPaint(Color.YELLOW);
			else if(s.getHealth()<=80) g2.setPaint(Color.CYAN);
			else if(s.getHealth()>80) g2.setPaint(Color.GREEN);
			
			g2.fill3DRect(s.getX(),s.getY(),s.getHealth(),5,true);
			try{
				Iterator<Bullet> bs=s.getBullets();
				while(bs.hasNext()){
					drawShip(bs.next(),g2);
				}
			}
			catch(ConcurrentModificationException e){
			}
		}
	}
	
}