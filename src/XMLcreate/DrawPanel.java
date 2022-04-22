package XMLcreate;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import XMLcreate.ObjectPanel.actionEvent;

//������ �г�
public class DrawPanel extends JPanel implements Runnable{
	
	int x1,y1,x2,y2;
	boolean flag = true;
	static int SCREEN_WIDTH;
	static int SCREEN_HEIGHT;
	static boolean resetFlag = false;
	static boolean isSelected = false;
	static boolean isMapPane = false;
	static boolean isObjectPane = false;
	static SuperObject selectedObject;
	static ImageIcon backImg;
	MainPanel mainPanel;
	
	public DrawPanel(int width ,int height,MainPanel mainPanel) {
		
		SCREEN_WIDTH = width;
		SCREEN_HEIGHT = height;
		this.setLayout(null);
		this.setSize(width,height);
		this.setLocation(450 - DrawPanel.SCREEN_WIDTH/2, 300 - DrawPanel.SCREEN_HEIGHT/2);
		this.setBackground(Color.black);
		this.mainPanel =  mainPanel;
		x1= y1= x2 =y2= 0;
		this.addMouseListener(new mouseEvent());
		this.addMouseMotionListener(new mouseEvent());
		
		Thread th = new Thread(this);
		th.start();
	}
	
	//���ٿ��� �ҷ����⸦ �ϴ� ��� ȣ�� - drawPanel�� �ٽ� ����
	public void getTheFile(int width ,int height) {
		
		//reset�Լ��� �������� �ϳ��� ����
		MyEnemy enemy = new MyEnemy();
		MyItem item = new MyItem();
		Player player = new Player();
		MyGoal goal = new MyGoal();
		MyTile tile = new MyTile();
		Rectangle rect = new Rectangle();
			
		enemy.reset();
		item.reset();
		player.reset();
		goal.reset();
		tile.reset();
		rect.reset();

		SCREEN_WIDTH = width;
		SCREEN_HEIGHT = height;
		this.removeAll();
		this.setSize(width,height);
		this.setLocation(450 - DrawPanel.SCREEN_WIDTH/2, 300 - DrawPanel.SCREEN_HEIGHT/2);
		this.setBackground(Color.black);
		this.repaint();
	}
	
	//object���� �� ���ο� �ִ��� üũ. �� ���ο� ���Ե��� ������ false ����
	public boolean objectMapInside(int x, int y, int w, int h) {
		for(int i=0;i<Rectangle.rectangleVector.size(); i++) {
			Polygon poly = Rectangle.rectangleVector.get(i).poly;
			if(poly.contains(x + w/2, y + h/2)) return true;
		}		
		return false;
	}
	
	//object���� ��ġ���� üũ
	public boolean objectPositionCrush(int x, int y, int w, int h) {
		for(int i=0;i<MyEnemy.enemyVector.size(); i++) {
			Polygon poly = MyEnemy.enemyVector.get(i).poly;
			if(poly.contains(x + w/2, y + h/2)) return false;
		}
		for(int i=0;i<MyItem.itemVector.size(); i++) {
			Polygon poly = MyItem.itemVector.get(i).poly;
			if(poly.contains(x + w/2, y + h/2)) return false;
		}
		for(int i=0;i<Player.playerVector.size(); i++) {
			Polygon poly = Player.playerVector.get(i).poly;
			if(poly.contains(x + w/2, y + h/2)) return false;
		}
		for(int i=0;i<MyGoal.goalVector.size(); i++) {
			Polygon poly = MyGoal.goalVector.get(i).poly;
			if(poly.contains(x + w/2, y + h/2)) return false;
		}
		return true;
	}
	//�׷��� �簢�� ��ǥ ������� - ��
	public void createRectangle() {
		x1 -= x1 % XMLFrame.blockSize;
		y1 -= y1 % XMLFrame.blockSize;
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
		if(x1 == x2 || y1 == y2 ) return;

		//���ο� �簢�� �����
		Rectangle rect = new Rectangle(x1,y1, x2, y2);
		//�ٸ� �簢���� ��ġ���� üũ
		if(!rect.insideRectangle(Rectangle.rectangleVector)) return;
		this.add(rect);
		//�����ϴ� �簢�� ã�� �� ����
		Rectangle.rectangleVector.add(rect);
	}
	@Override
	public void run() {
		while(flag) {			
			repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	//�簢���� �׸��� ���� - ������������ ������
	 public void drawPerfectRect(Graphics g) {
         int px = Math.min(x1,x2);
         int py = Math.min(y1,y2);
         int pw=Math.abs(x1-x2);
         int ph=Math.abs(y1-y2);
         g.drawRect(px, py, pw, ph);
     }

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(backImg != null)
			g.drawImage(backImg.getImage(), 0, 0,SCREEN_WIDTH,SCREEN_HEIGHT, 0,0,backImg.getImage().getWidth(selectedObject),backImg.getImage().getHeight(selectedObject), this);
		if(XMLFrame.blockSize != 0 )
			guideLinePaint(g); // ����
		g.setColor(new Color(255, 255, 255,0));
		if(isSelected) {
			g.setColor(Color.white);
			//����� �����ϴ� �� �� �������� �׸���
			g.fillOval(selectedObject.x1 - 5, selectedObject.y1 -5, 10, 10);
			//g.fillOval(x1 - 5, y2 -5, 10, 10);
			g.fillOval(selectedObject.x2 - 5, selectedObject.y2 -5, 10, 10);
			//g.fillOval(x2 - 5, y1 -5, 10, 10);
		}
        g.setColor(Color.RED);
        drawPerfectRect(g);

	}

	//������ �׸��� �Լ�
	public void guideLinePaint(Graphics g) {
		g.setColor(new Color(255, 255, 255, 100));
		int w = 0;
		int h =0;
		while(h < this.getHeight()) {
			drawDashedLine(g, 0, h,this.getWidth(), h);
			h += XMLFrame.blockSize;
		}
		while(w < this.getWidth()) {
			drawDashedLine(g, w, 0,w, this.getHeight());
			w += XMLFrame.blockSize;
		}
	}

	public void drawDashedLine(Graphics g, int x1, int y1, int x2, int y2){

      Graphics2D g2d = (Graphics2D) g.create();
      Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
      g2d.setStroke(dashed);
      g2d.drawLine(x1, y1, x2, y2);

      g2d.dispose();
	}
	 public void setStartPoint(int x, int y) {
         this.x1 = x;
         this.y1 = y;
     }

     public void setEndPoint(int x, int y) {
         this.x2 = (x);
         this.y2 = (y);
     }
     
     
	class mouseEvent extends MouseAdapter{
		
		static Point prePoint = new Point();
		Point curPoint = new Point();

		@Override
		public void mousePressed(MouseEvent e) {
			Cursor cursor = XMLFrame.drawPanel.getCursor();
			Point p = e.getPoint();
			if(p.x == 0 || p.y == 0) return;
			
			//���ҿ��� isMapPane�� ���� ���� ��
			if(isMapPane) {
				//Ŀ���� ���� ����̴�.
				if(cursor.getType() == 1) {
					setEndPoint(e.getX(), e.getY());
					setStartPoint(e.getX(), e.getY());
					repaint();
				}			
			}		
			
			//���ҿ��� isObjectPane�� ���� ���� ��
			else if(isObjectPane) {
				// ���õ� ��ư�� ���� ��
				if(ObjectPanel.selectedButton != null) {
					//��ü ��ư�� ������ �� drawPane�� ��ü �߰�
					//���콺Ŀ�� ����� ���õ� ��ư�� �̹����� ���� ���� ����
					//Enemy���� 
					if(ObjectPanel.objectCombo.getSelectedIndex() == 0 && XMLFrame.drawPanel.getCursor().equals(actionEvent.cursor)) {
						ImageIcon icon = (ImageIcon) ObjectPanel.selectedButton.icon;
						MyEnemy enemy= new MyEnemy(e.getX(), e.getY(),e.getX() + XMLFrame.blockSize, e.getY() +XMLFrame.blockSize,icon);
						//ĭ ����� ���߱�
						enemy.standard();
						//���� �׵θ� ���ο� �ְ� �� ��ġ�� �׷��� ������Ʈ�� ���� �� ���� ����
						if(objectMapInside(enemy.x1, enemy.y1, enemy.x2- enemy.x1, enemy.y2 -enemy.y1) 
								&& objectPositionCrush(enemy.x1, enemy.y1, enemy.x2- enemy.x1, enemy.y2 -enemy.y1)) {
							selectedObject = (SuperObject)enemy;
							isSelected = true;
							MyEnemy.enemyVector.add(enemy);
							XMLFrame.drawPanel.add(enemy);
						}					
					}
					//������ ����
					else if(ObjectPanel.objectCombo.getSelectedIndex() == 1 && XMLFrame.drawPanel.getCursor().equals(actionEvent.cursor)) {
						ImageIcon icon = (ImageIcon) ObjectPanel.selectedButton.icon;
						MyItem item= new MyItem(e.getX(), e.getY(),e.getX() + XMLFrame.blockSize, e.getY() +XMLFrame.blockSize,icon);
						item.standard();
						if(objectMapInside(item.x1, item.y1, item.x2- item.x1, item.y2 -item.y1)
								&&objectPositionCrush(item.x1, item.y1, item.x2- item.x1, item.y2 -item.y1)) {
							selectedObject = (SuperObject)item;
							isSelected = true;
							MyItem.itemVector.add(item);
							XMLFrame.drawPanel.add(item);
						}
				
					}
					//ĳ����
					else if(ObjectPanel.objectCombo.getSelectedIndex() == 2 && XMLFrame.drawPanel.getCursor().equals(actionEvent.cursor)) {
						ImageIcon icon = (ImageIcon) ObjectPanel.selectedButton.icon;
						Player player= new Player(e.getX(), e.getY(),e.getX() + XMLFrame.blockSize, e.getY() +XMLFrame.blockSize,icon);
						player.standard();
						if(objectMapInside(player.x1, player.y1, player.x2- player.x1, player.y2 -player.y1)
								&& objectPositionCrush(player.x1, player.y1, player.x2- player.x1, player.y2 -player.y1)) {
							selectedObject = (SuperObject)player;
							isSelected = true;
							//�ϳ��� ��������
							if(Player.playerVector.size() <1) {
								Player.playerVector.add(player);
								XMLFrame.drawPanel.add(player);
							}
							
						}
						
					}
					//Goal
					else if(ObjectPanel.objectCombo.getSelectedIndex() == 3 && XMLFrame.drawPanel.getCursor().equals(actionEvent.cursor)) {
						ImageIcon icon = (ImageIcon) ObjectPanel.selectedButton.icon;
						MyGoal goal= new MyGoal(e.getX(), e.getY(),e.getX() + XMLFrame.blockSize, e.getY() +XMLFrame.blockSize,icon);
						goal.standard();	
						if(objectMapInside(goal.x1, goal.y1, goal.x2- goal.x1, goal.y2 -goal.y1)
								&& objectPositionCrush(goal.x1, goal.y1, goal.x2- goal.x1, goal.y2 -goal.y1)) {
							selectedObject = (SuperObject)goal;
							isSelected = true;
							if(MyGoal.goalVector.size() <1) {
								MyGoal.goalVector.add(goal);
								XMLFrame.drawPanel.add(goal);
							}
							
						}
						
					}
					//Ÿ��
					else if(ObjectPanel.objectCombo.getSelectedIndex() == 4 && XMLFrame.drawPanel.getCursor().equals(actionEvent.cursor)) {
						ImageIcon icon = (ImageIcon) ObjectPanel.selectedButton.icon;
						MyTile tile= new MyTile(e.getX(), e.getY(),e.getX() + XMLFrame.blockSize, e.getY() +XMLFrame.blockSize,icon);
						tile.standard();
						if(objectMapInside(tile.x1, tile.y1, tile.x2- tile.x1, tile.y2 -tile.y1)) {
							MyTile.tileVector.add(tile);
							XMLFrame.drawPanel.add(tile);
						}
				
					}
					
				}			
			}
			
			//���õ� ��ü�� ������ drawPanel�� �׷��� ��ü�� ������ ���õ�
			if(!DrawPanel.isSelected) {
				//�簢��
				for(int i=0; i< Rectangle.rectangleVector.size(); i++) {
					Rectangle rect = Rectangle.rectangleVector.get(i);
					if(rect.poly.contains(e.getX(), e.getY())) {	
						selectedObject = (SuperObject)rect;
						isSelected = true;														
					}

				}			
				// enemy ����
				for(int i=0; i< MyEnemy.enemyVector.size(); i++) {				
					MyEnemy enemy = MyEnemy.enemyVector.get(i);
					if(enemy.poly.contains(e.getX(), e.getY())) {
						selectedObject = (SuperObject)enemy;
						isSelected = true;								
					}
				}
				//item
				for(int i=0; i< MyItem.itemVector.size(); i++) {					
					MyItem item = MyItem.itemVector.get(i);
					if(item.poly.contains(e.getX(), e.getY())) {
						selectedObject = (SuperObject)item;
						isSelected = true;					
					}
				}
				//player
				for(int i=0; i<Player.playerVector.size(); i++) {					
					Player player = Player.playerVector.get(i);
					if(player.poly.contains(e.getX(), e.getY())) {
						selectedObject = (SuperObject)player;
						isSelected = true;					
					}
				}
				//goal
				for(int i=0; i< MyGoal.goalVector.size(); i++) {				
					MyGoal goal = MyGoal.goalVector.get(i);
					if(goal.poly.contains(e.getX(), e.getY())) {
						selectedObject = (SuperObject)goal;
						isSelected = true;						
					}
				}
				//Ÿ��
				for(int i=0; i< MyTile.tileVector.size(); i++) {				
					MyTile tile = MyTile.tileVector.get(i);
					if(tile.poly.contains(e.getX(), e.getY())) {
						selectedObject = (SuperObject)tile;
						isSelected = true;						
					}
				}
			}
			//���õ� ��ü�� �ִ� ���
			else {
				if(cursor.getType()== 13) {	
					prePoint = e.getPoint();					
				}
				//���õ� ������ �ƴ� �ٸ� ������ �����ϸ� isSelected = false
				if(!(selectedObject.x1-10 <= e.getX() && selectedObject.x2 +10 >= e.getX()
						&& selectedObject.y1 -10 <= e.getY() && selectedObject.y2+10 >= e.getY())) {
					isSelected = false;
					selectedObject.isSelected = false;
				}
			}
		}
		/*
		 * �巡�� �̺�Ʈ
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			Cursor cursor = XMLFrame.drawPanel.getCursor();		
			Point p = e.getPoint();
			if(p.x == 0 || p.y == 0) return;
			if(isMapPane) {			
				//Ŀ���� ���ڸ���̸� �׸��� ����
				 if(cursor.getType() == 1) {
					setEndPoint(e.getX(), e.getY());
					repaint();
				}		
			}
			
			//���õ� ��ü�� �ִٸ�
			if(isSelected) {
				//������ ������ �ø���
				if(cursor.getType()== 6) {
					selectedObject.setStartPoint(e.getX(), e.getY());
					
				}
				// �����ʹ� ������ �ø���
				else if(cursor.getType()== 5) {
					selectedObject.setEndPoint(e.getX(), e.getY());
				}
				// �����̱�
				else if(cursor.getType()== 13) {
					if(prePoint.x != 0) {
						selectedObject.setStartPoint(selectedObject.x1 - (prePoint.x - e.getX()), selectedObject.y1 - (prePoint.y - e.getY()));
						selectedObject.setEndPoint(selectedObject.x2 - (prePoint.x - e.getX()), selectedObject.y2 - (prePoint.y - e.getY()));
						prePoint = e.getPoint();
					}
					
				}

			}
	    }
		
		// ���콺 ��ư�� �����ٰ� ������ ��
		@Override
		public void mouseReleased(MouseEvent e) {
			Point p = e.getPoint();
			if(p.x == 0 || p.y == 0) return;
			Cursor cursor = XMLFrame.drawPanel.getCursor();	
			if(isMapPane) {
				//Ŀ���� ���� ���
				if(cursor.getType() == 1) {
					 setEndPoint(e.getX(), e.getY());
					 //���� �̷�� �簢�� �����
					 createRectangle();
					 setEndPoint(x1, y1);
		             repaint();
				}				
			}
			//���õ� ��ü�� �ø��ų� �����̰� ���� �� ���콺�� ���� ���
			if(cursor.getType() == 5 || cursor.getType() == 6 || cursor.getType() == 13) {
				if(isSelected) {
					//ĭ ������ ���߱�
					selectedObject.standard();				
				}
					
			}
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			Point p = e.getPoint();
			
			if(isMapPane) {
				//���õ� ��ü�� ���� ��				
				if(isSelected) {
					if(selectedObject.x1 <= 0 ) {
						selectedObject.x1=1;					
					}
					if(selectedObject.y1 <= 0)
						selectedObject.y1=1;
					//Ȯ��
					if(p.x % selectedObject.x1 <=10 && p.y % selectedObject.y1 <= 10 ) {
						XMLFrame.drawPanel.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));				
					}
					//Ȯ��
					else if(p.x % selectedObject.x2 <=10 &&p.y % selectedObject.y2 <= 10 ) {
						XMLFrame.drawPanel.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));//5
					}
					//�̵��ϱ�
					else if(selectedObject.poly.contains(e.getPoint())) {
						XMLFrame.drawPanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
					}
					
					else {
						XMLFrame.drawPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));//13
						
					}
				}
				
				else if(!isSelected) {
					//Ŀ�� ���ڷ� �����
					if(p.x% XMLFrame.blockSize <=6 && p.y% XMLFrame.blockSize <= 6) 			
						XMLFrame.drawPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
					else 
						XMLFrame.drawPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					
				}				
			}
			else if(isObjectPane) {
				
				if(isSelected) {
					if(selectedObject.x1 <= 0 ) {
						selectedObject.x1=1;					
					}
					if(selectedObject.y1 <= 0)
						selectedObject.y1=1;

					//Ȯ��
					if(p.x % selectedObject.x1 <=10 && p.y % selectedObject.y1 <= 10 ) {
						XMLFrame.drawPanel.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));				
					}
					//Ȯ��
					else if(p.x% selectedObject.x2 <=10 && p.y % selectedObject.y2 <= 10 ) {
						XMLFrame.drawPanel.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));//5
					}
					//�̵��ϱ�
					else if(selectedObject.poly.contains(e.getPoint())) {
						XMLFrame.drawPanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
					}
					
					else {
						XMLFrame.drawPanel.setCursor(actionEvent.cursor);//13
						
					}
				}
			}
		}
		
	
	}
}