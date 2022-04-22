package Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Game.MainPanel.MyLabel;




public class GamePanel extends JPanel implements Runnable {
			
		int x,y,w,h;
		private ImageIcon bgImg;
		Vector<Point> lineVector;
		Vector<Polygon> polyVector = new Vector<Polygon>(100);
		ArrayList <Enemy> enemys = new ArrayList <Enemy>(20);
		ArrayList <Item> itemList = new ArrayList <Item>(20);
		static Character character;
		Map map;
		boolean flag = true;
		boolean getAllItem = false;
		ClosedArea closedArea = null;
		static boolean gameOver;
		Goal goal;
		
		
		public GamePanel(Node gamePanelNode) {
			// 뒤로갈수록 밑에 깔릴것
			setLayout(null);
			setSize(SpaceGame.SCREEN_WIDTH, SpaceGame.SCREEN_HEIGHT);
			
			
			add(new MenuBar(this));
			//get ActiveMap Node 
			Node activeMapNode = XMLReader.getNode(gamePanelNode, XMLReader.E_ACTIVEMAP);
			//맵 테두리
			Node closedAreaNode = XMLReader.getNode(activeMapNode, XMLReader.E_CLOSEDAREA);
			int blockSize = Integer.parseInt(XMLReader.getAttr(closedAreaNode, "blockSize"));			
			NodeList polyNodeList = closedAreaNode.getChildNodes();
			//
			for(int i=0; i<polyNodeList.getLength(); i++) {
				Node node = polyNodeList.item(i);		
				if(node.getNodeType() != Node.ELEMENT_NODE)
					continue;
				// 하나의 rectangle 태그
				if(node.getNodeName().equals(XMLReader.E_RECTANGLE)) {
					int [] xpoints = new int[4];
					int [] ypoints = new int[4];
					int xIndex = 0;
					int yIndex = 0;
					Node rectangleNode = node;
					NodeList rectangleNodeList = rectangleNode.getChildNodes();
					for(int j=0; j<rectangleNodeList.getLength(); j++) {
						Node pointNode = rectangleNodeList.item(j);	
					
						if(pointNode.getNodeType() != Node.ELEMENT_NODE)
							continue;
						// found!!, <Obj> tag
						if(pointNode.getNodeName().equals(XMLReader.E_POINT)) {
							xpoints[xIndex++] = Integer.parseInt(XMLReader.getAttr(pointNode, "x"));
							ypoints[yIndex++] = Integer.parseInt(XMLReader.getAttr(pointNode, "y"));
						}
						if(xIndex == 4 && yIndex == 4) {
							polyVector.add(new Polygon(xpoints, ypoints, 4));
							break;
						}
					}
					
				}
			}
			add(new ClosedArea(polyVector,blockSize));
			
			//적
			Node enemyNode = XMLReader.getNode(activeMapNode, XMLReader.E_ENEMY);
			NodeList enemyNodeList = enemyNode.getChildNodes();
			for(int i=0; i<enemyNodeList.getLength(); i++) {
				Node node = enemyNodeList.item(i);		
				if(node.getNodeType() != Node.ELEMENT_NODE)
					continue;
				// found!!, <Obj> tag
				if(node.getNodeName().equals(XMLReader.E_OBJ)) {
					
					ImageIcon icon =  new ImageIcon((XMLReader.getAttr(node, "img")));
					int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
					int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
					int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
					int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
					int speed = Integer.parseInt(XMLReader.getAttr(node, "speed"));
					String type= XMLReader.getAttr(node, "type");
					
					Enemy enemy = new Enemy(x,y,w,h,speed,type,icon,polyVector);
					enemys.add(enemy);
					add(enemy);
				}

			}
			//아이템
			Node itemNode = XMLReader.getNode(activeMapNode, XMLReader.E_ITEM);
			NodeList itemNodeList = itemNode.getChildNodes();
			for(int i=0; i<itemNodeList.getLength(); i++) {
				Node node = itemNodeList.item(i);		
				if(node.getNodeType() != Node.ELEMENT_NODE)
					continue;
				// found!!, <Obj> tag
				if(node.getNodeName().equals(XMLReader.E_OBJ)) {
					
					ImageIcon icon =  new ImageIcon((XMLReader.getAttr(node, "img")));
					int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
					int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
					int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
					int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
					
					Item item = new Item(x,y,w,h,icon);
					itemList.add(item);
					add(item);
				}

			}					
			//캐릭터
			Node charNode = XMLReader.getNode(activeMapNode, XMLReader.E_CHARACTER);
			x = Integer.parseInt(XMLReader.getAttr(charNode, "x"));
			y = Integer.parseInt(XMLReader.getAttr(charNode, "y"));
			w = Integer.parseInt(XMLReader.getAttr(charNode, "w"));
			h = Integer.parseInt(XMLReader.getAttr(charNode, "h"));
			int speed = Integer.parseInt(XMLReader.getAttr(charNode, "speed"));

			ImageIcon icon =  new ImageIcon((XMLReader.getAttr(charNode, "img")));
			character = new Character(x,y,w,h,speed,icon, polyVector);		
			add(character);
			
			//배경화면
			Node bgNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BG);		
			map = new Map(bgNode);			
			
			// 골인
			Node goalNode = XMLReader.getNode(activeMapNode, XMLReader.E_GOAL);
			
			w = Integer.parseInt((XMLReader.getAttr(goalNode, "w")));
			h = Integer.parseInt((XMLReader.getAttr(goalNode, "h")));
			x = Integer.parseInt((XMLReader.getAttr(goalNode, "x")));
			y = Integer.parseInt((XMLReader.getAttr(goalNode, "y")));
			icon =  new ImageIcon((XMLReader.getAttr(goalNode, "img")));
			goal = new Goal(x,y,w,h,icon);
			add(goal);
			
			//타일
			Node blockNode = XMLReader.getNode(activeMapNode, XMLReader.E_TILE);
			if(blockNode != null) {
				NodeList blokcNodeList = blockNode.getChildNodes();
				for(int i=0; i<blokcNodeList.getLength(); i++) {
					Node node = blokcNodeList.item(i);
					if(node.getNodeType() != Node.ELEMENT_NODE)
						continue;
					// found!!, <Obj> tag
					if(node.getNodeName().equals(XMLReader.E_OBJ)) {
						x = Integer.parseInt(XMLReader.getAttr(node, "x"));
						y = Integer.parseInt(XMLReader.getAttr(node, "y"));
						w = Integer.parseInt(XMLReader.getAttr(node, "w"));
						h = Integer.parseInt(XMLReader.getAttr(node, "h"));
						
						icon = new ImageIcon(XMLReader.getAttr(node, "img"));
						Tile tile = new Tile(x,y,w,h, icon);
						add(tile);
					}
				}
			
			}
		
			
			Thread th = new Thread(this);
			th.start();	
		}
		
		//적과 캐릭터가 부딪히면 게임오버
		public void crushEnemy() {
			Enemy enemy;	
			for(int i=0; i< enemys.size(); i++) {
				enemy = enemys.get(i);				
				int [] xpoints = {enemy.x, enemy.x, enemy.x+enemy.w, enemy.x+enemy.w};
				int [] ypoints = {enemy.y, enemy.y +enemy.h, enemy.y +enemy.h, enemy.y};
				Polygon poly = new Polygon(xpoints, ypoints, 4);
				if(poly.contains(character.x, character.y,10,10)) {
					SpaceGame.music.play("die");
					System.out.println("1");
					SpaceGame.isGameOver = true;
					SpaceGame.deathN++;
					this.flag = false;	
					break;
				}

				else if(poly.contains(character.x+ character.w/2, character.y + character.h/2 ,10,10)) {
					SpaceGame.music.play("die");
					System.out.println("2");
					SpaceGame.isGameOver = true;
					SpaceGame.deathN++;
					this.flag = false;		
					break;
				}
				//조정필요
				else if(poly.contains(character.x+ character.w - 5, character.y+ character.h/2,5,5)) {
					SpaceGame.music.play("die");
					System.out.println("3");
					SpaceGame.isGameOver = true;
					SpaceGame.deathN++;
					this.flag = false;	
					break;
				}
					
			}
		}
		//아이템을 모두 먹어 스테이지 클리어 조건체크
		public void getItem() {
			if(itemList.size() == 0) 
				getAllItem = true;
			for(int i=0; i< itemList.size(); i++) {
				Item item = itemList.get(i);
				int [] xpoints = {item.getX(), item.getX(), item.getX()+item.getWidth(), item.getX()+item.getWidth()};
				int [] ypoints = {item.getY(), item.getY() +item.getHeight(), item.getY() +item.getHeight(),  item.getY()};
				Polygon poly = new Polygon(xpoints, ypoints, 4);
				if(poly.contains(character.x + character.w/2, character.y + character.h/2)|| poly.contains(character.x + character.w, character.y)) {
					SpaceGame.music.play("get");
					item.setBounds(0, 0, 0, 0);
					item.repaint();
					itemList.remove(item);
				}
			}
		}
		public void nextStage() {
			if(getAllItem) {
				int [] xpoints = {goal.x, goal.x, goal.x+goal.w, goal.x+goal.w};
				int [] ypoints = {goal.y, goal.y +goal.h, goal.y +goal.h, goal.y};
				Polygon poly = new Polygon(xpoints, ypoints, 4);
				if(poly.contains(character.x, character.y)|| poly.contains(character.x + character.w, character.y)) {
					SpaceGame.stageN++;
					SpaceGame.music.play("get");
					this.flag = false;	
					System.out.println(SpaceGame.stageN);
					SpaceGame.isNextStage = true;
				}
			}
		}
		public void process() {
			//골인 여부 체크
			nextStage();
			//아이템 먹었는지 체크
			getItem();
			//적과 캐릭터가 부딪혔는지 체크
			crushEnemy();
		}
		@Override
		public void run() {
			while (flag) {		
				process();
				repaint();
				try {		
					
					Thread.sleep(10);						
				}catch (InterruptedException e) {
						return;
				}
			}
		}

		@Override
		//GamePanel paintComponent
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(map.img.getImage(),0, 0,SpaceGame.SCREEN_WIDTH, SpaceGame.SCREEN_HEIGHT, 0, 0,map.w ,map.h,this);
			
		}
	
}

class Map{
	ImageIcon img;
	int w, h;
	
	public Map(Node bgNode) {		
		System.out.println("map");
		this.img = new ImageIcon((XMLReader.getAttr(bgNode, "img")));
		this.w = Integer.parseInt((XMLReader.getAttr(bgNode, "w")));
		this.h = Integer.parseInt((XMLReader.getAttr(bgNode, "h")));
	}	

}

//color로 할 수 있게
class Tile extends JLabel{
	
	Image img;
//  Color color;
	
	public Tile(int x, int y, int w, int h, ImageIcon icon) {
		this.setBounds(x,y,w,h);
		img = icon.getImage();
	}
	public void paintComponent(Graphics g) {
		
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	
	}
}

class Item extends MyObject{
	
	public Item(int x, int y, int w, int h, ImageIcon icon) {	
		super(x,y,w,h,icon);
	}
}
class Goal extends MyObject{
	public Goal (int x, int y, int w, int h, ImageIcon icon) {
		super(x,y,w,h,icon);
	}	
}

class MenuBar extends JLabel{
	JLabel home;
	Image img;
	GamePanel gamePanel;
	public MenuBar(GamePanel gamePanel){
		this.gamePanel = gamePanel;
		ImageIcon icon = new ImageIcon("src/img/home.png");
		img = icon.getImage();
		this.setBounds(0, 0, SpaceGame.SCREEN_WIDTH, 50);
		home = new JLabel();
		home.setBounds(SpaceGame.SCREEN_WIDTH - 60, 0, SpaceGame.SCREEN_WIDTH - 60+ 40, 40);
		gamePanel.add(home);
		home.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { //
			
				SpaceGame.music.play("get");
				gamePanel.flag = false;
				SpaceGame.isHome =true;
				
			}
		});
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.black);
		g.fillRect(0, 0,  SpaceGame.SCREEN_WIDTH, 40);
		g.setColor(Color.white);
		g.drawLine(0, 40, SpaceGame.SCREEN_WIDTH, 40);
	
		Font font = new Font("OCR A", Font.BOLD, 23);
		g.setFont(font);
		g.drawString("STAGE  ",30 ,30);
		g.drawString(String.format("%02d",SpaceGame.stageN),120,30);
		g.drawString("DEATH  ",SpaceGame.SCREEN_WIDTH/2 + 50 ,30);
		g.drawString(String.format("%02d",SpaceGame.deathN),SpaceGame.SCREEN_WIDTH/2 + 150,30);

		g.drawImage(img, SpaceGame.SCREEN_WIDTH - 60, 0, SpaceGame.SCREEN_WIDTH -60+ 40, 40, 0, 0, img.getWidth(home), img.getHeight(home), null);
	}
}



