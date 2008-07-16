package core;




import java.util.*;

public class TextInvaders extends Thread{
	GUI gui;	
    public TextInvaders(){
    	gui = new GUI();
    	this.start();
    }
    public void run(){
    	while(true){
    		gui.update();
    	}
    }
}