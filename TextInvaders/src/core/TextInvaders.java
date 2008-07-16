package core;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;


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