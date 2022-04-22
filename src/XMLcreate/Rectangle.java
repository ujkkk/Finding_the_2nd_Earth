package XMLcreate;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

//맵을 구성하는 사각형 클래스
public class Rectangle extends SuperObject{
	
	Color color;
	static Vector<Rectangle> rectangleVector= new Vector<Rectangle>(50);
	public Rectangle() {}
	public Rectangle(int x1, int y1, int x2, int y2){
		super(x1,y1,x2,y2);
	
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;					
		g2.setColor(MapPanel.selectedColor);
		g2.setStroke(new BasicStroke(3,BasicStroke.CAP_ROUND,0));
		g2.drawRect(1, 1, x2-x1-2, y2-y1-2);
			 		
	}
	
	@Override
	public void reset() {
		for(int i=0; i< rectangleVector.size(); i++) {
			Rectangle rect = rectangleVector.get(i);
			XMLFrame.drawPanel.remove(rect);
		}
		rectangleVector.removeAllElements();
	}
	
	@Override
	public String XMLtoString() {
		String text = "<ClosedArea blockSize = \"" + XMLFrame.blockSize + "\">\n";
		Rectangle rect;
		for(int i=0; i< rectangleVector.size(); i++) {
			rect= rectangleVector.get(i);
			text += "<Rectangle>\n";
			text += "\t<Point x =\"" + rect.x1 +"\" y = \"" + rect.y1 + "\"/>\n"; 
			text += "\t<Point x =\"" + rect.x1 +"\" y = \"" + rect.y2 + "\"/>\n"; 
			text += "\t<Point x =\"" + rect.x2 +"\" y = \"" + rect.y2 + "\"/>\n"; 
			text += "\t<Point x =\"" + rect.x2 +"\" y = \"" + rect.y1 + "\"/>\n"; 
			text += "\n</Rectangle>\n";
		}
		text += "</ClosedArea>";
		return text;
	}
	
	//이미 그려진 사각형 영역에 포함되어 있는지 체크
	//영역이 아예 겹치면 안됨
		public boolean insideRectangle(Vector<Rectangle> rectangleVector) {
			Point p1,p2,p3,p4;
			p1 = new Point(this.x1, this.y1);
			p2 = new Point(this.x2, this.y1);
			p3 = new Point(this.x2, this.y2);
			p4 = new Point(this.x1, this.y2);
			Vector<Rectangle> rect = new Vector<Rectangle>(1);
			rect.add(this);
			
			//한 점이라도 내부에 있다면 false리턴
			for(int i=0; i< rectangleVector.size(); i++) {
				Rectangle getRect = rectangleVector.get(i);
				if(p1.x >getRect.x1 && p1.x < getRect.x2 && p1.y > getRect.y1 && p1.y < getRect.y2) return false;
				if(p2.x >getRect.x1 && p2.x < getRect.x2 && p2.y > getRect.y1 && p2.y < getRect.y2) return false;
				if(p3.x >getRect.x1 && p3.x < getRect.x2 && p3.y > getRect.y1 && p3.y < getRect.y2) return false;
				if(p4.x >getRect.x1 && p4.x < getRect.x2 && p4.y > getRect.y1 && p4.y < getRect.y2) return false;
				
				//영역을 감쌀 때
				if(y1 <=getRect.y1 && y2 >= getRect.y2) {
					if(x1 < getRect.x1 && x2 > getRect.x1) return false;
					if(x1 < getRect.x2 && x2 > getRect.x2) return false;
					
				}
				if(x1 <=getRect.x1 && x2 >= getRect.x2) {
					if(y1 < getRect.y1 && y2 > getRect.y1) return false;
					if(y1 < getRect.y2 && y2 > getRect.y2) return false;
					
				}
				//기존에 있던 함수를 감싸는지 체크
				//if(!getRect.insideRectangle(rect)) return false;
			}
			return true;
		}
		
	
}