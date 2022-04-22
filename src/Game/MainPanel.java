package Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class MainPanel extends JPanel implements Runnable{
	
	Image mapImage;	
	PlayButton playLabel;
	private int back1X = 0;
	private int back2X= 1000;
	boolean flag = true;
	boolean isStage = false;
	boolean isMain = false;
	
	public MainPanel() {
		
		setLayout(null);
		setSize(900, 600);
		mapImage =  new ImageIcon("src/img/space.jpg").getImage();
		
		Thread th = new Thread(this);
		th.start();
		
	}
	@Override
	public void run() {
		while(flag) {
			gameMode();
			repaint();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
	public void gameMode() {
		if(isMain) {
			playLabel = new PlayButton(this);
			add(playLabel);	
			isMain = false;
		}
		else if(isStage) {
			isStage = false;
			stageSeclect();
		}
		
	}
	class MyLabel extends JLabel {
		ImageIcon icon = new ImageIcon();

		int x, y;
		int w, h;
		int num;
		Color color1 = new Color(237, 180, 254,150);
		Color color2 = new Color(43, 30, 81,150);
		Color color;
		public MyLabel() {
		};
		
		public MyLabel(int x, int y, int w, int h, int num) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.num = num;
			this.setBounds(x, y, w, h);
			this.color = color1;
			this.setOpaque(true);
			this.setBackground(Color.BLACK);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			g.setColor(new Color(255, 255, 255,170));
			g.fillRoundRect(0, 0, w, h, 20, 20);
			g.setColor(this.color1);
			g.fillRoundRect(0, 0, w, h, 20, 20);
			g.setColor(new Color(255,255,255));
			g.setFont(new Font("1훈점보맘보 Bold", Font.CENTER_BASELINE, 25));
			if(color == color2) {
				g.setColor(new Color(50, 70, 90,170));
				g.fillRoundRect(0, 0, w, h, 20, 20);
			}
			
			
			g.drawString(num+"", w/2 -10, h/2+7);			
		}
	}
	public void stageSeclect() {
		MyLabel [] selectBlocks = new MyLabel[40];
		int num;
		
		for(int j=0; j<= SpaceGame.allStageN/5; j++) {
			for(int i=0; i< 5; i++) {
				num = i+ j*5+1;
				if(num > SpaceGame.allStageN) break;
				selectBlocks[num] = new MyLabel(150 + i*130, 150 +j*100,60, 60,num);
				selectBlocks[num].setOpaque(true);
				this.add(selectBlocks[num]);

				selectBlocks[num].addMouseListener(new stageMouseEvent(selectBlocks[num]));
				this.repaint();
			}
		}	
		SpaceGame.file.readFile();
		int limitN = SpaceGame.file.n;

		for(int i = limitN+1 ; i< selectBlocks.length;i++) {
			if(i ==1 ) continue;
			MyLabel myLabel= selectBlocks[i];
			if(myLabel != null)
				myLabel.color = myLabel.color2;
		}
		
	}
	class stageMouseEvent extends  MouseAdapter{
		MyLabel myLabel;
		public stageMouseEvent(MyLabel myLabel) {
			this.myLabel = myLabel;
		}
		@Override
		public void mouseClicked(MouseEvent e) { 
		// 로딩 스크린 실행
			if(myLabel.color == myLabel.color1) {
								
				SpaceGame.music.play("get");
				SpaceGame.stageN = myLabel.num;
				SpaceGame.isGame = true;
				flag = false;
			}			
		}
	}
	@Override
	public void paintComponent(Graphics g) {	
		super.paintComponent(g);
		//배경
		g.drawImage(mapImage, back1X, 0, this);
		g.drawImage(mapImage, back2X, 0, this);
		back1X--;
		back2X--;
		if (back1X <= -(mapImage.getWidth(null))) {
			back1X = mapImage.getWidth(null);
		}
		if (back2X <= -(mapImage.getWidth(null))) {
			back2X = mapImage.getWidth(null);
		}
	}
		//g.drawImage(img,0, 0, this);
	
}

class PlayButton extends JLabel implements Runnable{
	
	ImageIcon playImage =  new ImageIcon("src/img/play (1).png");
	int x,y,w,h;
	boolean flag = true;
	boolean sizeUp = true;
	MainPanel mainPanel;
	public PlayButton(MainPanel mainPanel) {
		
		this.mainPanel = mainPanel; 
		w = playImage.getIconWidth();
		h = playImage.getIconHeight();
		x = SpaceGame.SCREEN_WIDTH/2 - w/2; 
		y = SpaceGame.SCREEN_HEIGHT/2 - h/2;
		
		setBounds(x, y, w, h);
		this.addMouseListener(new ClickListener(this));
		
		
		Thread th = new Thread(this);
		th.start();
	}
	//플레이 버튼 클릭 리스너
	class ClickListener extends MouseAdapter{
		PlayButton playButton;
		public ClickListener(PlayButton playButton) {
			this.playButton = playButton;
		}
		@Override
		  public void mousePressed(MouseEvent e) {
			SpaceGame.music.play("get");
			mainPanel.remove(playButton);
			playButton.flag = false;
			mainPanel.isStage = true;
		  }
		 }

	
	@Override 
	public void run() {
		while(flag) {
			sizeEvent();
			repaint();
			try {
				Thread.sleep(90);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
	
	public void sizeEvent() {
		if(x == 370) {
			sizeUp = false;
		}
		if(x == 380) {
			sizeUp = true;
		}
		
		if(sizeUp) {
			x--; y--; w++;h++;
		}
		else {
			x++;y++; w--; h--;
		}
		
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBounds(x, y, w, h);
		g.drawImage(playImage.getImage(), 0,0, w, h, 0,0, playImage.getIconWidth(),playImage.getIconHeight(),null);
	//	g.drawImage(playImage.getImage(), 0, 0,w,h,null);
	}
}
