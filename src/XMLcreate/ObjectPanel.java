package XMLcreate;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

class MyEnemy extends SuperObject {
	Image img;
	int speed;
	ImageIcon icon;
	String type;
	static Vector<MyEnemy> enemyVector = new Vector<MyEnemy>(100);

	public MyEnemy() {
	}

	public MyEnemy(int x1, int y1, int x2, int y2, ImageIcon icon) {
		super(x1, y1, x2, y2);
		this.img = icon.getImage();
		this.icon = icon;
		speed = 3;
		type = "UpDown";
		
	}
	public MyEnemy(int x1, int y1, int x2, int y2,int speed, String type, ImageIcon icon) {
		super(x1, y1, x2, y2);
		this.img = icon.getImage();
		this.icon = icon;
		this.speed = speed;
		this.type = type;		
		
	}

	@Override
	public String XMLtoString() {
		String text = "<Enemy>\n";
		MyEnemy enemy;
		for(int i=0; i< enemyVector.size(); i++) {
			enemy= enemyVector.get(i);
			String imageDescription = ((ImageIcon) enemy.icon).getDescription();
			text += "\t<Obj ";
			text += "img =\"" + imageDescription +"\" x = \"" + enemy.x1 + "\""; 
			text += " y =\"" + enemy.y1 +"\" w = \"" + (enemy.x2 - enemy.x1) + "\""; 
			text += " h =\"" + (enemy.y2 - enemy.y1) +"\" type = \"";
			text +=  enemy.type +  "\"";
			text += " speed =\"" + enemy.speed +"\">";
			text += "</Obj>\n";
		}
		text += "</Enemy>";
		return text;
	}

	@Override
	public void reset() {
		for (int i = 0; i < enemyVector.size(); i++) {
			MyEnemy rect = enemyVector.get(i);
			XMLFrame.drawPanel.remove(rect);
		}
		enemyVector.removeAllElements();

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, x2 - x1, y2 - y1, this);

	}
}

class MyItem extends SuperObject {
	Image img;
	ImageIcon icon;
	static Vector<MyItem> itemVector = new Vector<MyItem>(100);

	public MyItem() {}

	public MyItem(int x1, int y1, int x2, int y2, ImageIcon icon) {
		super(x1, y1, x2, y2);
		this.icon = icon;
		img = icon.getImage();
	}

	@Override
	public void reset() {
		for (int i = 0; i < itemVector.size(); i++) {
			MyItem rect = itemVector.get(i);
			XMLFrame.drawPanel.remove(rect);
		}
		itemVector.removeAllElements();

	}

	@Override
	public String XMLtoString() {
		String text = "<Item>\n";
		MyItem item;
		for(int i=0; i< itemVector.size(); i++) {
			item= itemVector.get(i);
			String imageDescription = ((ImageIcon) item.icon).getDescription();
			text += "\t<Obj ";
			text += "img =\"" + imageDescription +"\" x = \"" + item.x1 + "\""; 
			text += " y =\"" + item.y1 +"\" w = \"" + (item.x2 - item.x1) + "\""; 
			text += " h =\"" + (item.y2 - item.y1) +"\">";
			text += "</Obj>\n";
		}
		text += "</Item>";
		return text;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, x2 - x1, y2 - y1, this);

	}
}
class MyTile extends SuperObject {
	Image img;
	ImageIcon icon;
	static Vector<MyTile> tileVector = new Vector<MyTile>(100);

	public MyTile() {
	}

	public MyTile(int x1, int y1, int x2, int y2, ImageIcon icon) {
		super(x1, y1, x2, y2);
		this.icon = icon;
		img = icon.getImage();
	}

	@Override
	public void reset() {
		for (int i = 0; i < tileVector.size(); i++) {
			MyTile tile = tileVector.get(i);
			XMLFrame.drawPanel.remove(tile);
		}
		tileVector.removeAllElements();

	}

	@Override
	public String XMLtoString() {
		String text = "<Tile>\n";	
		MyTile tile;
		for(int i=0; i< tileVector.size(); i++) {
			tile= tileVector.get(i);
			String imageDescription = ((ImageIcon) tile.icon).getDescription();
			text += "\t<Obj ";
			text += "img =\"" + imageDescription +"\" x = \"" + tile.x1 + "\""; 
			text += " y =\"" + tile.y1 +"\" w = \"" + (tile.x2 - tile.x1) + "\""; 
			text += " h =\"" + (tile.y2 - tile.y1) +"\">";
			text += "</Obj>\n";
		}
		text += "</Tile>";
		return text;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, x2 - x1, y2 - y1, this);

	}
}
class MyGoal extends SuperObject {
	Image img;
	ImageIcon icon;
	static Vector<MyGoal> goalVector = new Vector<MyGoal>(2);

	public MyGoal() {
	}

	public MyGoal(int x1, int y1, int x2, int y2, ImageIcon icon) {
		super(x1, y1, x2, y2);
		img = icon.getImage();
		this.icon = icon;
	}

	@Override
	public void reset() {
		for (int i = 0; i < goalVector.size(); i++) {
			MyGoal rect = goalVector.get(i);
			XMLFrame.drawPanel.remove(rect);
		}
		goalVector.removeAllElements();

	}

	@Override
	public String XMLtoString() {
		String text = "<Goal ";
		MyGoal goal;
		for(int i=0; i< goalVector.size(); i++) {
			goal= goalVector.get(i);
			String imageDescription = ((ImageIcon) goal.icon).getDescription();
			text += "img =\"" + imageDescription +"\" x = \"" + goal.x1 + "\""; 
			text += " y =\"" + goal.y1 +"\" w = \"" + (goal.x2 - goal.x1) + "\""; 
			text += " h =\"" + (goal.y2 - goal.y1) +"\"";
		}		
		text += "/>";
		return text;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, x2 - x1, y2 - y1, this);

	}
}

class Player extends SuperObject {
	Image img;
	ImageIcon icon;
	int speed;
	static Vector<Player> playerVector = new Vector<Player>(2);

	public Player() {
	}

	public Player(int x1, int y1, int x2, int y2, ImageIcon icon) {
		super(x1, y1, x2, y2);
		img = icon.getImage();
		this.icon = icon;
		speed = 5;
	}
	public Player(int x1, int y1, int x2, int y2,int speed, ImageIcon icon) {
		super(x1, y1, x2, y2);
		this.img = icon.getImage();
		this.icon = icon;
		this.speed = speed;				
	}
	@Override
	public void reset() {
		for (int i = 0; i < playerVector.size(); i++) {
			Player rect = playerVector.get(i);
			XMLFrame.drawPanel.remove(rect);
		}
		playerVector.removeAllElements();

	}

	@Override
	public String XMLtoString() {
		String text = "<Character ";
		Player player;
		for(int i=0; i< playerVector.size(); i++) {
			player= playerVector.get(i);
			String imageDescription = ((ImageIcon) player.icon).getDescription();
			text += "img =\"" + imageDescription +"\" x = \"" + player.x1 + "\""; 
			text += " y =\"" + player.y1 +"\" w = \"" + (player.x2 - player.x1) + "\""; 
			text += " h =\"" + (player.y2 - player.y1) +"\""; 
			text += " speed =\"" + player.speed +"\"";
		}
		text += "/>";
		return text;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, x2 - x1, y2 - y1, this);

	}
}

class MyLabel extends JLabel {
	ImageIcon icon = new ImageIcon();

	int x, y;
	int w, h;

	public MyLabel() {
	};

	public MyLabel(int x, int y, int w, int h, ImageIcon icon) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.setBounds(x, y, w, h);
		this.icon = icon;

		this.setOpaque(true);
		this.setBackground(Color.BLACK);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image img;

		img = this.icon.getImage();
		g.drawImage(img, 0, 0, w, h, null);
	}
}

class MyButton extends JButton {
	ImageIcon icon;

	int x, y;
	int w, h;

	public MyButton() {
	}

	public MyButton(int x, int y, int w, int h, ImageIcon icon) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.setBounds(x, y, w, h);
		this.icon = icon;

		this.setOpaque(true);
		this.setBackground(Color.white);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setLocation(x, y);
		Image img = icon.getImage();
		g.drawImage(img, 0, 0, w, h, 0, 0, img.getWidth(null), img.getHeight(null), null);

	}
}

public class ObjectPanel extends JPanel implements Runnable {
	
	static JComboBox<String> objectCombo;
	static JComboBox<String> speedCombo;
	static JComboBox<String> typeCombo;
	static MyButton selectedButton = new MyButton();

	MyLabel imgLabel = new MyLabel();
	JButton resetBt;
	JScrollPane scrollPane;
	ButtonPanel buttonPanel;
	
	String[] objects = { "Enemy", "Item", "Chracter", "Goal","Tile" };
	String[] speeds = { "1", "2", "3", "4", "5", "6", "7" };
	String[] types = { "UpDown", "LeftRight" };
	String[] enemyImgs = { "enemy.png", "ghost.png" };
	String[] itemImgs = { "item.png", "key.png", "key2.png", "coin.png", "star.png", "star2.png" };
	String[] chImgs = { "rect2.png", "rect.png", "rect4.png", "mario.png", "p.png" };
	String[] goalImgs = { "goal.jpg" };
	String[] tileImgs = {"black.png", "white.png", "blue.png", "gray.png", "block.png"};

	
	public ObjectPanel() {
		//���ǵ带 ���ϴ� combo
		speedCombo = new JComboBox<String>(speeds);
		speedCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = Integer.parseInt((String) speedCombo.getSelectedItem());
				if (DrawPanel.selectedObject != null) {
					if (DrawPanel.selectedObject.getClass().getName() == "XMLcreate.MyEnemy") {
						//���õ� object�� �����ͼ� ���ǵ� ����
						MyEnemy enemy = (MyEnemy) DrawPanel.selectedObject;
						enemy.speed =  n;
					} else if (DrawPanel.selectedObject.getClass().getName() == "XMLcreate.Player") {
						Player player = (Player) DrawPanel.selectedObject;
						player.speed = n;
					}

				}
			}
		});
		typeCombo = new JComboBox<String>(types);
		//�� type ����
		typeCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String type = ((String) typeCombo.getSelectedItem());
				if (DrawPanel.selectedObject != null) {
					if (DrawPanel.selectedObject.getClass().getName() == "XMLcreate.MyEnemy") {
						MyEnemy enemy = (MyEnemy) DrawPanel.selectedObject;
						enemy.type =  type;
					}
				}
			}
		});
		this.setLayout(null);
		// enemy, item, char, goal, tile ...�� �����ϴ� combo
		objectCombo = new JComboBox<String>(objects);
		objectCombo.setBounds(20, 20, 200, 30);
		objectCombo.addActionListener(new ActionListener() {

			//�̹����� �����ϴ� ��ư �г�
			ButtonPanel enemyButtonPanel = new ButtonPanel(enemyImgs);
			ButtonPanel itmeButtonPanel = new ButtonPanel(itemImgs);
			ButtonPanel chButtonPanel = new ButtonPanel(chImgs);
			ButtonPanel goalButtonPanel = new ButtonPanel(goalImgs);
			ButtonPanel tileButtonPanel = new ButtonPanel(tileImgs);

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(objectCombo.getSelectedIndex());
				// enemy
				if (objectCombo.getSelectedIndex() == 0) {
					scrollPane.setViewportView(enemyButtonPanel);
				}
				// item
				else if (objectCombo.getSelectedIndex() == 1) {

					scrollPane.setViewportView(itmeButtonPanel);

				}
				// ĳ���� - ���ǵ�
				else if (objectCombo.getSelectedIndex() == 2) {
					scrollPane.setViewportView(chButtonPanel);
				}
				// ��
				else if (objectCombo.getSelectedIndex() == 3) {
					scrollPane.setViewportView(goalButtonPanel);
				}
				else if (objectCombo.getSelectedIndex() == 4) {
					scrollPane.setViewportView(tileButtonPanel);
				}
				XMLFrame.objectPanel.repaint();
			}

		});
		this.add(objectCombo);

		resetBt = new JButton("reset");
		resetBt.setBounds(10, 160, 65, 20);
		resetBt.addMouseListener(new MouseAdapter() { // ���콺 �̺�Ʈ

			@Override
			public void mouseClicked(MouseEvent e) {
				if (objectCombo.getSelectedIndex() == 0) {
					MyEnemy enemy = new MyEnemy();
					enemy.reset();
				} else if (objectCombo.getSelectedIndex() == 1) {
					MyItem item = new MyItem();
					item.reset();
				} else if (objectCombo.getSelectedIndex() == 2) {
					Player player = new Player();
					player.reset();

				} else if (objectCombo.getSelectedIndex() == 3) {
					MyGoal goal = new MyGoal();
					goal.reset();
				} else if (objectCombo.getSelectedIndex() == 4) {
					MyTile tile = new MyTile();
					tile.reset();
				}
				
				
			}
		}); // ���콺 ������ end..
		this.add(resetBt);

		Border blackline = BorderFactory.createLineBorder(Color.black);
		imgLabel = new MyLabel(85, 80, 100, 100, new ImageIcon("src/img/select_off.png"));
		imgLabel.setBorder(blackline);
		this.add(imgLabel);

		//��ư �г��� ��ũ�� ������ ����
		scrollPane = new JScrollPane(new ButtonPanel(enemyImgs));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 200, 220, 100);
		this.add(scrollPane);

		// login �ܰ���
		JLabel label = new JLabel("");
		label.setBorder(new TitledBorder(new LineBorder(Color.black, 1), "�Ӽ�"));
		// label.setOpaque(true); // ��� ���� �����ϰ�
		// label.setBackground(new Color(255,255, 255, 200));
		label.setSize(220, 200);
		label.setLocation(10, 310);
		add(label);

		Thread th = new Thread(this);
		th.start();

	}
	public void changeCombo() {
		//���ǵ� �޺��ڽ��� ������ ������ ��ü�� ���ǵ�� ����
		if(DrawPanel.selectedObject != null) {
			if (DrawPanel.selectedObject.getClass().getName() == "XMLcreate.MyEnemy") {
				MyEnemy enemy = (MyEnemy) DrawPanel.selectedObject;
				int n = enemy.speed;
				String strn = n+"";
				ObjectPanel.speedCombo.setSelectedItem(strn);
				
				String type = enemy.type;
				ObjectPanel.typeCombo.setSelectedItem(type);
			} else if (DrawPanel.selectedObject.getClass().getName() == "XMLcreate.Player") {
				Player player = (Player) DrawPanel.selectedObject;
				int n = player.speed;
				String strn = n+"";
				ObjectPanel.speedCombo.setSelectedItem(strn);
			}
			
		}
	}
	@Override
	public void run() {
		while (true) {
			repaint();
			changeCombo();
			try {
				Thread.sleep((100));
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Font font = new Font("", Font.BOLD, 15);
		g.setFont(font);
		// 23.5
		if (DrawPanel.selectedObject != null) {
			SuperObject object = DrawPanel.selectedObject;

			g.drawString("x :", 51, 350);
			g.drawString(String.format("%4d", object.x1), 75, 350);
			g.drawString("y :", 150, 350);
			g.drawString(String.format("%4d", object.y1), 180, 350);

			g.drawString("width :", 20, 380);
			g.drawString(String.format("%4d", object.x2 - object.x1), 75, 380);
			g.drawString("height :", 120, 380);
			g.drawString(String.format("%4d", object.y2 - object.y1), 180, 380);

			Component[] components = this.getComponents();
			switch (object.getClass().getName()) {

			case "XMLcreate.MyEnemy":
				g.drawString("speed :", 20, 420);
				g.drawString("type :", 35, 460);
				// Ÿ��, ���ǵ� �޺� ����
				int i;
				for (i = 0; i < components.length; i++)
					if (components[i].equals(speedCombo))
						break;
				if (i == components.length) {
					speedCombo.setBounds(90, 405, 120, 25);
					this.add(speedCombo);
				}

				for (i = 0; i < components.length; i++)
					if (components[i].equals(typeCombo))
						break;
				if (i == components.length) {
					typeCombo.setBounds(90, 445, 120, 25);
					this.add(typeCombo);
				}

				break;
			case "XMLcreate.MyItem":
				components = this.getComponents();
				// Ÿ��, ���ǵ� �޺� ����
				for (i = 0; i < components.length; i++) {
					if (components[i].equals(speedCombo))
						this.remove(speedCombo);

					else if (components[i].equals(typeCombo))
						this.remove(typeCombo);
				}
				break;
			case "XMLcreate.Tile":
				components = this.getComponents();
				// Ÿ��, ���ǵ� �޺� ����
				for (i = 0; i < components.length; i++) {
					if (components[i].equals(speedCombo))
						this.remove(speedCombo);

					else if (components[i].equals(typeCombo))
						this.remove(typeCombo);
				}
				break;
			case "XMLcreate.Player":
				g.drawString("speed :", 20, 420);
				g.drawString("speed :", 20, 420);
				//���ǵ� �޺� ���̱�
				for (i = 0; i < components.length; i++)
					if (components[i].equals(speedCombo))
						break;
				if (i == components.length) {
					speedCombo.setBounds(90, 405, 120, 25);
					this.add(speedCombo);
				}
				// Ÿ�� �޺� ����
				for (i = 0; i < components.length; i++) {
					if (components[i].equals(typeCombo))
						this.remove(typeCombo);
				}
				if(Player.playerVector.size() >=1) {
					g.setColor(Color.BLUE);
					g.drawString("��� �����Ͽ����ϴ�. (1/1)", 25, 460);
					g.setColor(Color.black);
				}
					
				break;
			case "XMLcreate.MyGoal":
				// Ÿ��, ���ǵ� �޺� ����
				for (i = 0; i < components.length; i++) {
					if (components[i].equals(speedCombo))
						this.remove(speedCombo);

					else if (components[i].equals(typeCombo))
						this.remove(typeCombo);
				}if(MyGoal.goalVector.size() >=1) {
					g.setColor(Color.BLUE);
					g.drawString("��� �����Ͽ����ϴ�. (1/1)", 25, 420);
					g.setColor(Color.black);
				}
				break;

			}
		}
	}

	class ButtonPanel extends JPanel {
		int count = 1;
		Vector<JButton> buttons = new Vector<JButton>(100);

		MyButton button;
		MyButton plusButton;
		int buttonSize = 50;

		public ButtonPanel(String[] imgs) {
			this.setLayout(null);
			// �������� ��ư

			button = new MyButton(0, 0, buttonSize, buttonSize, new ImageIcon("src/img/select_off.png"));
			button.setOpaque(true);
			button.setBackground(Color.gray);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					MyButton b = (MyButton) e.getSource();
					selectedButton = b;
					// imgLabel.setIcon(b.icon);
					imgLabel.icon = b.icon;
					imgLabel.repaint();

					actionEvent.cursor = new Cursor(Cursor.DEFAULT_CURSOR);
					// XMLFrame.drawPanel.setCursor(cursor);
					ObjectPanel.selectedButton = null;
				}
			});
			this.add(button);
			for (int i = 1; i < imgs.length + 1; i++) {
				count++;
				ImageIcon icon = new ImageIcon("src/img/" + imgs[(i - 1)]);
				button = new MyButton(buttonSize * ((i % 4)), buttonSize * (i / 4), buttonSize, buttonSize, icon);
				button.addActionListener(new actionEvent());
				this.add(button);
			}

			// �÷��� ��ư
			plusButton = new MyButton(buttonSize * (count % 4), buttonSize * (count / 4), buttonSize, buttonSize,
					new ImageIcon("src/img/plus.png"));
			plusButton.addActionListener(new OpenActionListener(this));
			plusButton.setOpaque(true);
			plusButton.setBackground(Color.gray);
			this.add(plusButton);
		}
	}

	//�÷��� ��ư�� ������ ������ �̹��������� ��ư ����
	class OpenActionListener implements ActionListener {
		private JFileChooser chooser;
		ButtonPanel btpanel;

		public OpenActionListener(ButtonPanel btpanel) {
			chooser = new JFileChooser();
			this.btpanel = btpanel;
		}

		public void actionPerformed(ActionEvent e) {

			FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png", "gif");
			chooser.setFileFilter(filter);

			int ret = chooser.showOpenDialog(XMLFrame.drawPanel);
			if (ret != JFileChooser.APPROVE_OPTION) {
				// ������ �����ų� ��ҹ�ư�� �������
				JOptionPane.showMessageDialog(null, "������ �������� �ʾҽ��ϴ�.", "���", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// ����ڰ� "����"��ư�� �������
			if (chooser.getSelectedFile() != null) {
				String filePath = chooser.getSelectedFile().getPath();
				ImageIcon icon = new ImageIcon(filePath);
				// ���ο� ��ư ����
				MyButton button = new MyButton(btpanel.buttonSize * ((btpanel.count) % 4),
						btpanel.buttonSize * ((btpanel.count) / 4), btpanel.buttonSize, btpanel.buttonSize,
						new ImageIcon(filePath));
				button.addActionListener(new actionEvent());
				btpanel.add(button);
				btpanel.count++;
				btpanel.plusButton.x = btpanel.buttonSize * ((btpanel.count) % 4);
				btpanel.plusButton.y = btpanel.buttonSize * ((btpanel.count) / 4);

				btpanel.plusButton.repaint();
			}

		}
	}

	class actionEvent implements ActionListener {
		static Cursor cursor;

		@Override
		public void actionPerformed(ActionEvent e) {
			MyButton b = (MyButton) e.getSource();
			selectedButton = b;
			imgLabel.icon = b.icon;
			imgLabel.repaint();
			
			// �̹��� ��� �˾Ƴ���
			String imageDescription = ((ImageIcon) b.icon).getDescription();

			
			/* Ŀ��������� */
			Toolkit tk = Toolkit.getDefaultToolkit();
			Image cursorimage = tk.getImage(imageDescription);// Ŀ���� ����� �̹���
			Point point = new Point(0, 0);
			// ���ο� custom Ŀ��(image cursor, Point hotSpot, String name)
			cursor = tk.createCustomCursor(cursorimage, point, "haha");
			XMLFrame.drawPanel.setCursor(cursor);

		}
	}
}
