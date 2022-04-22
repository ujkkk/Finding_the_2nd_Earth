package XMLcreate;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import javax.swing.JLabel;

public abstract class SuperObject extends JLabel {
	int x1, x2, y1, y2;

	Point [] point = new Point[4];
	Polygon poly = new Polygon();
	boolean isSelected;
	boolean flag = true;
	public SuperObject() {}
	public SuperObject(int x1, int y1, int x2, int y2){
		if(x2 < x1) {
			int temp = x1;
			this.x1 = x2;
			this.x2 = temp;
		}
		else {
			this.x1= x1;
			this.x2 = x2;
		}
		if(y2 < y1) {
			int temp = y1;
			this.y1 = y2;
			this.y2 = temp;
		}
		else {
			this.y1= y1;
			this.y2= y2;
		}				
		
		this.setBounds(x1, y1, x2-x1 , y2-y1);
		int [] xpoints = {x1,x1,x2,x2};
		int [] ypoints = {y1,y2,y2,y1};
		poly= new Polygon(xpoints, ypoints, 4);

	}
	abstract public void reset();
	abstract public String XMLtoString();
	
	 public void setStartPoint(int x, int y) {
         this.x1 = x;
         this.y1 = y;
         this.poly.xpoints[0] = x;
         this.poly.xpoints[1] = x;
         this.poly.ypoints[0] = y;
         this.poly.ypoints[3] = y;
         this.setBounds(x1-1, y1-1, x2-x1 +2, y2-y1+2);
     }

     public void setEndPoint(int x, int y) {
         this.x2 = (x);
         this.y2 = (y);
         this.poly.xpoints[2] = x;
         this.poly.xpoints[3] = x;
         this.poly.ypoints[1] = y;
         this.poly.ypoints[2] = y;
         this.setBounds(x1-1, y1-1, x2-x1 +2, y2-y1+2);
     }
    
     public void standard() {
    	 if(x1 % XMLFrame.blockSize > XMLFrame.blockSize/2) {
 			x1+=  XMLFrame.blockSize - (x1 % XMLFrame.blockSize);
 		}
 		else {
 			x1 -= (x1 % XMLFrame.blockSize);
 		}
    	 if(y1 % XMLFrame.blockSize > XMLFrame.blockSize/2) {
 			y1 +=  XMLFrame.blockSize - (y1 % XMLFrame.blockSize);
 		}
 		else {
 			y1 -= (y1 % XMLFrame.blockSize);
 		}
    	 
    	 if(x2 % XMLFrame.blockSize > XMLFrame.blockSize/2) {
 			x2 +=  XMLFrame.blockSize - (x2 % XMLFrame.blockSize);
 		}
 		else {
 			x2 -= (x2 % XMLFrame.blockSize);
 		}
    	 if(y2 % XMLFrame.blockSize > XMLFrame.blockSize/2) {
 			y2 +=  XMLFrame.blockSize - (y2 % XMLFrame.blockSize);
 		}
 		else {
 			y2 -= (y2 % XMLFrame.blockSize);
 		}
    	 int xpoints[] = {x1,x1,x2,x2};
    	 int ypoints[] = {y1,y2,y2,y1};
    	 this.poly = new Polygon(xpoints, ypoints, 4);
         this.setBounds(x1, y1, x2-x1, y2-y1);
     }
    
      @Override
      public void paintComponent(Graphics g) {
    	
    	  super.paintComponent(g);
    	  
      }
}
