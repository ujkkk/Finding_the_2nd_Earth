package Game;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;


import org.w3c.dom.Node;

public class SpaceGame extends JFrame implements Runnable{

	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static boolean isGame = false;
	public static boolean isMain = false;
	public static boolean isGameOver = false;
	public static boolean isNextStage = false;
	public static boolean isHome = false;
	public static int stageN = 1;
	public static int deathN =0;
	public static int allStageN =16;
	public static StageFile file = new StageFile();
	GamePanel gamePanel = null;
	static Music music = new Music();
	
	XMLReader xml;
	public SpaceGame() {
		
		super("SPACE GAME");
		SCREEN_WIDTH = 900;
		SCREEN_HEIGHT = 600;
		setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//������ �󿡼� �߾ӿ� ��ġ
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	        Dimension frm = this.getSize();
	       
	        int xpos = (int)(screen.getWidth() / 2 - frm.getWidth() / 2);
	        int ypos = (int)(screen.getHeight() / 2 - frm.getHeight() / 2);
	       
	        this.setLocation(xpos, ypos);
		
		music.play("bg");
		MainPanel mainPanel = new MainPanel();
		mainPanel.isMain= true;
		this.setContentPane(mainPanel);
		
		Thread th = new Thread(this);
		th.start();
		setVisible(true);
	}	
	
	@Override
	public void run() {
		while(true) {
			gameMode();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				return;
			}
		}
		
	}
	public void gameOver() {
		Enemy enemy;
		for(int i=0; i< gamePanel.enemys.size(); i++) {
			//enemy ������ �ߴ�
			enemy = gamePanel.enemys.get(i);
			enemy.flag = false;		
		}
		//ĳ���� ������ �ߴ�
		GamePanel.character.flag = false;
		gamePanel.flag = false;
		
		String xmlFileName = "stage" + SpaceGame.stageN +".xml";
		XMLReader xml = new XMLReader(xmlFileName);
		xml = new XMLReader(xmlFileName);
		Node spaceGameNode = xml.getSpaceGameElement();
		Node sizeNode = XMLReader.getNode(spaceGameNode, XMLReader.E_SIZE);
		String w = XMLReader.getAttr(sizeNode, "w");
		String h = XMLReader.getAttr(sizeNode, "h");
		SCREEN_WIDTH = Integer.parseInt(w);
		SCREEN_HEIGHT = Integer.parseInt(h);
		setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
		gamePanel = new GamePanel(xml.getGamePanelElement());
		if(file.n < stageN) {
			file.wirteFile(stageN+"", StageFile.file);
		}
		this.setContentPane(gamePanel);
	}

	
	public void gameMode() {
		
		if(isGame) {
			
			String xmlFileName = "stage" + SpaceGame.stageN +".xml";
			
			xml = new XMLReader(xmlFileName);
			Node spaceGameNode = xml.getSpaceGameElement();
			Node sizeNode = XMLReader.getNode(spaceGameNode, XMLReader.E_SIZE);
			String w = XMLReader.getAttr(sizeNode, "w");
			String h = XMLReader.getAttr(sizeNode, "h");
			SCREEN_WIDTH = Integer.parseInt(w);
			SCREEN_HEIGHT = Integer.parseInt(h);
			setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
			//������ �󿡼� �߾ӿ� ��ġ
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		        Dimension frm = this.getSize();
		       
		        int xpos = (int)(screen.getWidth() / 2 - frm.getWidth() / 2);
		        int ypos = (int)(screen.getHeight() / 2 - frm.getHeight() / 2);
		       
		        this.setLocation(xpos, ypos);
			
			System.out.println("isGame : ");
			System.out.println(Thread.currentThread());
			isGame = false;
			gamePanel = new GamePanel(xml.getGamePanelElement());
			if(file.n < stageN || file.n == 0) {
				file.wirteFile(stageN+"", StageFile.file);
			}
			this.setContentPane(gamePanel);
		}

		else if(isMain) {
			this.setSize(900,600);
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	        Dimension frm = this.getSize();
	       
	        int xpos = (int)(screen.getWidth() / 2 - frm.getWidth() / 2);
	        int ypos = (int)(screen.getHeight() / 2 - frm.getHeight() / 2);
	       
	        this.setLocation(xpos, ypos);
		
			System.out.println("isMain");
			isMain = false;
			MainPanel mainPanel = new MainPanel();
			mainPanel.isStage = true;
			
			this.setContentPane(mainPanel);
		}
		else if(isGameOver) {
			gameOver();
			isGameOver = false;
		}
		else if(isNextStage) {
			gameOver();
			isNextStage = false;
		}
		else if(isHome) {
			isHome = false;
			Enemy enemy;
			for(int i=0; i< gamePanel.enemys.size(); i++) {
				//enemy ������ �ߴ�
				enemy = gamePanel.enemys.get(i);
				enemy.flag = false;		
			}
			//ĳ���� ������ �ߴ�
			GamePanel.character.flag = false;
			gamePanel.flag = false;
			
			isMain = true;
		}
		
	}
}


