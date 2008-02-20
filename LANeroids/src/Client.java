import java.nio.*;
import java.io.*;
import java.net.*;
import java.util.*;
public class Client extends Thread{
	String name;
	static int players=0;
	HashMap<String,Ship> data;
	protected Ship myShip;
	Socket socket=null;
	Random r = new Random();
		
	int port=12345;
	String host ="127.0.0.1";
	GameGui gui;
	public Client(String host, boolean guiEnabled) {
		this.host=host;
		players++;
		myShip=new Ship(r.nextInt(700),r.nextInt(500),100);
		if(guiEnabled) gui=new GameGui(this);
		/*try{
			socket = new Socket(host,port);
		}
		catch(Exception e){
			System.err.println(e+"\nServer Socket Error!");
		}
		//////print("Connection made with "+socket);
		//new reader(socket).start();
		//new writer(socket,myShip).start();
		
		//////print("This client has a ship "+myShip);
		new connector(socket, data, name, myShip);*/
		name="Test client"+players;
		this.start();
		
	}
	public Client(String host, boolean guiEnabled, String name) {
		this.host=host;
		players++;
		myShip=new Ship(r.nextInt(700),r.nextInt(500),100);
		if(guiEnabled) gui=new GameGui(this);
		/*try{
			socket = new Socket(host,port);
		}
		catch(Exception e){
			System.err.println(e+"\nServer Socket Error!");
		}
		//////print("Connection made with "+socket);
		//new reader(socket).start();
		//new writer(socket,myShip).start();
		
		//////print("This client has a ship "+myShip);
		new connector(socket, data, name, myShip);*/
		this.name=name;
		this.start();
		
	}
	public synchronized void moveUp(){
		myShip.accelerate();
		//myShip.setY(myShip.getY()-1);
	}
	public synchronized void moveDown(){
		myShip.decelerate();
		//myShip.setY(myShip.getY()+1);
	}
	public synchronized void moveLeft(){
		myShip.left();
	}
	public synchronized void moveRight(){
		myShip.right();
	}
	public synchronized void shoot(){
		myShip.shoot();
	}
	public synchronized void respawn(){
		try
		{
			sleep(500);
		}
		catch(InterruptedException e){}		
		myShip.respawn();
	}
	public static void main(String Args[]){
		if(Args[1]==null)
			new Client(Args[0],true);
		else{
			if(Args[1].equals("Dummy")){
				new Client(Args[0],false);
			}
			else
				new Client(Args[0],true).name=Args[1];
			
		}	
	}
	public String getPlayerName(){
		return name;
	}
	public synchronized void collide(Collidable other){
		myShip.collide(other);
	}
	public void run(){
		try{
			socket = new Socket(host,port);
		}
		catch(Exception e){
			System.err.println(e+"\nServer Socket Error!");
		}
		//////print("Connection made with "+socket);
		new connector(socket, data, name, myShip,gui);
		
		while(true){
			myShip.move();
			//if(data!=null)gui.setMap(data);
			if(data!=null)
			{
				data.put(name,myShip);
				Iterator<String> keys=data.keySet().iterator();
				while(keys.hasNext())
					data.get(keys.next()).move();
			}
			if(gui==null){//this is headless
				if(r.nextInt(5)>2)myShip.left();
				else if(r.nextInt(5)>2)myShip.right();
				if(r.nextInt(20)>18)myShip.shoot();
				if(r.nextInt(10)>5)myShip.accelerate();
			}
			else{
				gui.update();
			}
			try
			{
				sleep(45);
			}
			catch(InterruptedException e){}
		}
	}
	synchronized void print(Object stuff){
    	if(gui!=null)
    		gui.addMsg("Client:"+stuff);
    	else
    		System.out.println("Client:"+stuff);
    }

}

class connector extends Thread{
	//sends HashMap of stuff to clients
	Socket server;
	String name;
	Ship me;
	//Ship old;
	ObjectInputStream in;
	ObjectOutputStream out;
	HashMap<String,Ship> map;
	GameGui gui;
	public connector(Socket theServer, HashMap<String,Ship> theMap, String theName, Ship theShip, GameGui g)
	{
		server=theServer;
		map=theMap;
		name=theName;
		me=theShip;
			try {
      			out = new ObjectOutputStream(server.getOutputStream());
      			in= new ObjectInputStream(server.getInputStream());
     		}
     		catch(Exception e1) {
         		try {
            		server.close();
         		}
         		catch(Exception e) {
           			//////print(e.getMessage());
         		}
         	return;
     	}
     	gui=g;
     	this.start();
     	
	}
	public synchronized void run(){
		try
		{
			out.writeObject(name);
		} 
		catch(IOException e)
		{
			e.printStackTrace();
		}
		while(!server.isClosed()){
			try {
				if(me!=null&&out!=null)out.writeObject(me);//This line is giving off errors
	        	//print("Client "+name+" just wrote its ship which is "+me+" at "+java.text.DateFormat.getTimeInstance(java.text.DateFormat.FULL).format(new java.util.Date()));
	         	out.flush();
	         	
	         	map=(HashMap<String,Ship>)(in.readObject());
	         	Object temp=in.readObject();
	         	if(temp!=null){
	         		Collidable other = (Collidable)temp;
	         		me.collide(other);
	         	}
	         	temp=in.readObject();
	         	if(temp!=null){
	         		Boolean b=(Boolean)temp;
	         		if(b.booleanValue())
	         			me.gotKill();
	         		else
	         			me.respawn();
	         	}
	         	temp=in.readObject();//server messages
	         	if(temp!=null){
	         		String msg=(String)temp;
	         		print(msg);
	         		if(msg.equals("You killed yourself, you fool!")){
	         			me.lostKill();
	         		}
	         	}/**/
	         	temp=in.readObject();//remove bullets
	         	if(temp!=null){
	         		Long bl=(Long)temp;
	         		me.removeBullet(bl);
	         			
	         			//print("Removed bullet created at "+bl);
	         		//else
	         			//print("Removal bullet created at "+bl+" failed!");
	         	}/**/
	         	//print("Client \""+name+"\" just got this map"+map);
	         	map.put(name,me);
	         	if(gui!=null)gui.setMap(map);
	         	out.reset();
	      }
	      catch(Exception e) {
	      	//////print("Something bad happened in client")	;
	      	if(e!=null&&e.getMessage()!=null&&e.getMessage().equals("Connection reset"))
	      	{
	      		print("Connection to server lost");
	      			try{finalize();}catch(Throwable t){}
	      	}
	      	e.printStackTrace();
	      	//try{sleep(1000);}
	      	//catch(InterruptedException e1){}
	      }
		try
			{
				sleep(45);
			}
			catch(InterruptedException e){}
		
		}
	}
  	protected void finalize() throws Throwable{	
         	try{
         		in.close();
         		out.close();
         		server.close();
         	}
         	catch(Exception e){}
    }
    void print(Object stuff){
    	if(gui!=null)
    		gui.addMsg("Client:"+stuff);
    	else 
    		System.out.println("Client:"+stuff);
    }
}