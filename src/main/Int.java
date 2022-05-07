package main;

import java.lang.reflect.InvocationTargetException;

public class Int implements Runnable {
	public Game g;
	public boolean exit = false;

	public Int(Game g) {
		super();
		this.g = g;
	}

	public void stop(){
		exit = true;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		exit=false;
		while(!exit) {
			try {
				
				g.spostaConIntelligenza();
			

			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException | InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
	}



}
