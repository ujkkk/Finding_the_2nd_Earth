package Game;
import java.awt.Image;
import java.awt.Polygon;
import java.util.Vector;
import javax.swing.ImageIcon;

public class Enemy extends MyObject implements Runnable{
	
	int speed;
	String type;
	boolean crush = false;
	boolean flag = true;
	boolean direction = true;
	Vector<Polygon> polyVector;
	Polygon myPoly = new Polygon();
	
	public Enemy (int x, int y, int w, int h, int speed, String type, ImageIcon icon, Vector<Polygon> polyVector) {
		
		super(x,y,w,h,icon);
		this.speed = speed;
		this.type = type;
		this.polyVector = polyVector;
		findMyArea();
		Thread th = new Thread(this);
		th.start();
	
	}
	public void findMyArea() {		
		for(int i = 0; i< polyVector.size(); i++) {
			Polygon poly = polyVector.get(i);
			if(poly.contains(x , y ,w,h))
				myPoly = poly;
		}
	}
	@Override
	public void run() {
		
		while(flag) {				
			process();
			crushCheck();
			repaint();
			try {
				Thread.sleep(20);
							
			} catch (InterruptedException e) {
				return;
			}
		}
	}
	
	public void process() {
		switch (type) {
		//좌우만 움직이는 적
		case "LeftRight":
			if(crush) {
				//벽과 부딪히면 방향 바꿔주기
				crush = false;
				if(direction) 
					direction = false;
				else 
					direction = true;
			}
			if(direction) 
				this.x+= speed;
			else 
				this.x -= speed;
			break;
		//상하 움직이는 적
		case "UpDown":
			if(crush) {
				//벽과 부딪히면 방향 바꿔주기
				crush = false;
				if(direction) 
					direction = false;
				else
					direction = true;
			}
			if(direction)
				this.y+= speed;
			else 
				this.y-= speed;
			break;
		}
	}
	
	public void crushCheck() {
		if(!myPoly.contains(x, y,w,h))
			crush = true;		
	}
	

}
