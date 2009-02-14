import java.net.*;
import java.nio.*;
import java.io.*;
import java.util.*;

public class Server extends Thread{
	HashMap<String,Ship> players = new HashMap<String,Ship>();
	HashMap<String,Collidable> colls = new HashMap<String,Collidable>();
	HashMap<String,Boolean> kills = new HashMap<String,Boolean>();
	protected HashMap<String,Long> toRemove = new HashMap<String,Long>();	
	protected HashMap<String,String> messages = new HashMap<String,String>();	
	static int numPlayers;
	ServerSocket me;
	
	void print(Object stuff){
    	System.out.println("Server:"+stuff);
    }
	public Server(int port){
		try{
			me=new ServerSocket(port);
		}
		catch(IOException e){
			System.err.println("Server Socket Error!\n"+e);
		}
		print("Game server is listening to port "+port);
		this.start();
	}

	public static void main(String Args[]){

		new Server(12345);
	}
	protected synchronized void collDetect(){
		Iterator<String> iter=players.keySet().iterator();
		String temp, temp2;
		
		while(iter.hasNext())
		{
			temp=iter.next();
			Iterator<String> iter2=players.keySet().iterator();
			boolean flag=false;
			
			while(iter2.hasNext())
			{
				temp2=iter2.next();
					Iterator<Bullet> bullets=players.get(temp).getBullets();
					while(bullets.hasNext()){
						Bullet tempb=bullets.next();
						if(players.get(temp2).checkCollision(tempb))
						{
							colls.put(temp2,tempb);
							players.get(temp2).collide(tempb);
							toRemove.put(temp,tempb.getID());
							if(players.get(temp2).getHealth()==0&&!temp2.equals(temp))
							{
								print(temp+" killed "+temp2);
								kills.put(temp,new Boolean(true));
								messages.put(temp,"You got a kill!");
								kills.put(temp2,new Boolean(false));
								messages.put(temp2,"You got killed!");
							}
							if(players.get(temp2).getHealth()==0&&temp2.equals(temp)){
								messages.put(temp,"You killed yourself, you fool!");
							}
						}
					}
				
	
				/**/
				if(!flag){
					if(temp.equals(temp2))
						flag=true;
						//print(flag);
				}/**/
				//print("Checking for collisions between" +players.get(temp)+" and "+players.get(temp2)+flag);
				if(!temp.equals(temp2)){

					if(players.get(temp).checkCollision(players.get(temp2)))
					{
						colls.put(temp,players.get(temp2));
						players.get(temp2).collide(players.get(temp));
						players.get(temp).collide(players.get(temp2));
						colls.put(temp2,players.get(temp));
						//players.get(temp2).collide(players.get(temp));
						//players.get(temp).collide(players.get(temp2));
						
					}
				}
			}
		}
	}
	public void run(){
		while(true) {
			try{collDetect();}catch(Exception e){
			}
			try {
        		//////print("Waiting for connections.");
        		Socket client = me.accept();
        		//print("Accepted a connection from: "+ client.getInetAddress());
        		connection c = new connection(client, players, colls,kills,this);
        		numPlayers++;
       		} 
       		catch(Exception e) {e.printStackTrace();}
     	}
	}
}

class connection extends Thread{
	//sends HashMap of stuff to clients, gets client's updated positions
	Socket client;
	ObjectInputStream in;
	ObjectOutputStream out;
	HashMap<String,Ship> map;
	HashMap<String,Collidable> colls;
	HashMap<String,Boolean> kills;
	HashMap<String,Integer> toRemove;
	
	Server myServer;
	String name="";
	Ship player;
	Object temp;
	void print(Object stuff){
    	System.out.println("Server:"+stuff);
    }
	public connection(Socket theClient, HashMap<String,Ship> theMap,HashMap<String,Collidable> colls,
	HashMap<String,Boolean> kills, Server myServer){
		client=theClient;
		map=theMap;
		this.colls=colls;
		this.kills=kills;
		this.myServer=myServer;
		try {
      		in= new ObjectInputStream(client.getInputStream());
      		out = new ObjectOutputStream(client.getOutputStream());
     	}
     	catch(Exception e1) {
     		
     		e1.printStackTrace();
        	try {
           		client.close();
        	}
        	catch(Exception e) {
           		e.printStackTrace();
         	}
         //return;
     	}
		try
		{
			temp=in.readObject();
			name = (String)temp;
			print(name+"("+client.getInetAddress()+") has joined the game.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
     	this.start();
	}
	public synchronized void run(){		
		while(!client.isClosed()){
			try {
				temp=in.readObject();//This line also gives off errors at home but not school...
				player = (Ship)temp;
				
				//if(map!=null&&!map.isEmpty()&&map.get(name)!=null)player.setHealth(map.get(name).getHealth());
				//print("Player "+name+" is "+player.toString());
				
				map.put(name,player);
				////print(map);
	         	out.writeObject(map);
	         	out.reset();
	         	myServer.collDetect();
	         	if(colls.containsKey(name)){
	         		out.writeObject(colls.get(name));
	         		colls.remove(name);
	         	}
	         	else{
	         		out.writeObject(null);
	         	}
	         	if(kills.containsKey(name)){
	         		out.writeObject(kills.get(name));
	         		kills.remove(name);
	         	}
	         	else{
	         		out.writeObject(new Boolean(false));
	         	}
	         	if(myServer.messages.containsKey(name)){
	         		out.writeObject(myServer.messages.get(name));
	         		myServer.messages.remove(name);
	         	}/**/
	         	else{
	         		out.writeObject(null);
	         	}
	         	if(myServer.toRemove.containsKey(name)){
	         		out.writeObject(myServer.toRemove.get(name));
	         		myServer.toRemove.remove(name);
	         	}/**/
	         	else{
	         		out.writeObject(null);
	         	}
	         	out.flush();

	      }
	      catch(Exception e) {
	      	if(e!=null&&e.getMessage()!=null&&e.getMessage().equals("Connection reset"))
	      		try{finalize();}catch(Throwable t){}
			else
	      		e.printStackTrace();
	      }
		}
	}
	protected void finalize() throws Throwable{
		map.remove(name);
		print(name+"("+client.getInetAddress()+") has left the game.");
		try{in.close();
	    out.close();
		client.close(); }
		catch(IOException e){
			e.printStackTrace();
		}
	}
}