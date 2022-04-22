package XMLcreate;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Game.XMLReader;

public class ToolBar extends JToolBar{
	JButton openBt = new JButton(new ImageIcon("src/img/open.jpg"));
	JButton saveBt = new JButton(new ImageIcon("src/img/save.jpg"));
	MainPanel mainPanel;
	public ToolBar(MainPanel mainPanel) {
		super("toolBar");
		this.setSize(1200, 50);
		this.setFloatable(false);
		this.setBackground(Color.gray);
		this.mainPanel = mainPanel;
		this.add(new JButton("New"));
		this.addSeparator();
		this.add(openBt);
		this.addSeparator();
		this.add(saveBt);
		
		saveBt.addActionListener(new SaveListener());
		openBt.addActionListener(new OpenListener(mainPanel));
	}
}

//���ٿ��� open �������� Ŭ���ϴ� ���
class OpenListener implements ActionListener {
	private JFileChooser chooser;
	MainPanel mainPanel;
	
	public OpenListener(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
		
		chooser = new JFileChooser("C:\\Users\\kuj\\eclipse-workspace\\Finding the 2nd Earth");
	}
	public void actionPerformed(ActionEvent e) {
		ImageIcon icon;
		int x,y;
		int w,h;
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", "xml");
		chooser.setFileFilter(filter);
		int ret = chooser.showOpenDialog(XMLFrame.drawPanel);
		if (ret != JFileChooser.APPROVE_OPTION) {
			// ������ �����ų� ��ҹ�ư�� �������
			JOptionPane.showMessageDialog(null, "������ �������� �ʾҽ��ϴ�.", "���", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// ����ڰ� "����"��ư�� �������
		if (chooser.getSelectedFile() != null) {
			DrawPanel.isSelected = false;
			String filePath = chooser.getSelectedFile().getPath();
			//������ ���ϸ��� xml�� ����
			XMLReader xml = new XMLReader(filePath);
			Node gamePanelNode =xml.getGamePanelElement();
					
			Node spaceGameNode = xml.getSpaceGameElement();
			Node sizeNode = XMLReader.getNode(spaceGameNode, XMLReader.E_SIZE);
			w = Integer.parseInt(XMLReader.getAttr(sizeNode, "w"));
			h = Integer.parseInt(XMLReader.getAttr(sizeNode, "h"));
			//drawPanel ũ��,��ġ�� �ٲ���	
			XMLFrame.drawPanel.getTheFile(w, h);
						
			//���ȭ��
			Node bgNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BG);
			if(bgNode!=null) {
				icon = new ImageIcon((XMLReader.getAttr(bgNode, "img")));
				String imageDescription = ((ImageIcon)icon).getDescription();
				DrawPanel.backImg = new ImageIcon(imageDescription);
			}
					
			//get ActiveMap Node 
			Node activeMapNode = XMLReader.getNode(gamePanelNode, XMLReader.E_ACTIVEMAP);
			
			//�� �׵θ�
			Node closedAreaNode = XMLReader.getNode(activeMapNode, XMLReader.E_CLOSEDAREA);
			if(closedAreaNode!= null) {
				XMLFrame.blockSize = Integer.parseInt(XMLReader.getAttr(closedAreaNode, "blockSize"));			
				NodeList polyNodeList = closedAreaNode.getChildNodes();
				//
				for(int i=0; i<polyNodeList.getLength(); i++) {
					Node node = polyNodeList.item(i);		
					if(node.getNodeType() != Node.ELEMENT_NODE)
						continue;
					// �ϳ��� rectangle �±�
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
								Rectangle rect = new Rectangle(xpoints[0],ypoints[0], xpoints[2],ypoints[1]);
								XMLFrame.drawPanel.add(rect);
								//�����ϴ� �簢�� ã�� �� ����
								Rectangle.rectangleVector.add(rect);
								break;
							}
						}
						
					}
				}
			}
			//��
			Node enemyNode = XMLReader.getNode(activeMapNode, XMLReader.E_ENEMY);
			if(enemyNode != null) {
				NodeList enemyNodeList = enemyNode.getChildNodes();
				for(int i=0; i<enemyNodeList.getLength(); i++) {
					Node node = enemyNodeList.item(i);		
					if(node.getNodeType() != Node.ELEMENT_NODE)
						continue;
					// found!!, <Obj> tag
					if(node.getNodeName().equals(XMLReader.E_OBJ)) {
						
						icon =  new ImageIcon((XMLReader.getAttr(node, "img")));
						x = Integer.parseInt(XMLReader.getAttr(node, "x"));
						y = Integer.parseInt(XMLReader.getAttr(node, "y"));
						w = Integer.parseInt(XMLReader.getAttr(node, "w"));
						h = Integer.parseInt(XMLReader.getAttr(node, "h"));
						int speed = Integer.parseInt(XMLReader.getAttr(node, "speed"));
						String type= XMLReader.getAttr(node, "type");
						
						MyEnemy enemy = new MyEnemy(x,y,x+w,y+h,speed,type,icon);
						MyEnemy.enemyVector.add(enemy);
						XMLFrame.drawPanel.add(enemy);
					}
				}
			}
				
			//������
			Node itemNode = XMLReader.getNode(activeMapNode, XMLReader.E_ITEM);
			if(itemNode != null) {
				NodeList itemNodeList = itemNode.getChildNodes();
				for(int i=0; i<itemNodeList.getLength(); i++) {
					Node node = itemNodeList.item(i);		
					if(node.getNodeType() != Node.ELEMENT_NODE)
						continue;
					// found!!, <Obj> tag
					if(node.getNodeName().equals(XMLReader.E_OBJ)) {
						
						icon =  new ImageIcon((XMLReader.getAttr(node, "img")));
						x = Integer.parseInt(XMLReader.getAttr(node, "x"));
						y = Integer.parseInt(XMLReader.getAttr(node, "y"));
						w = Integer.parseInt(XMLReader.getAttr(node, "w"));
						h = Integer.parseInt(XMLReader.getAttr(node, "h"));
						
						MyItem item = new MyItem(x,y,x+w,y+h,icon);
						MyItem.itemVector.add(item);
						XMLFrame.drawPanel.add(item);
					}
				}
			}
			
			//ĳ����
			Node charNode = XMLReader.getNode(activeMapNode, XMLReader.E_CHARACTER);
			if(charNode != null) {
				x = Integer.parseInt(XMLReader.getAttr(charNode, "x"));
				y = Integer.parseInt(XMLReader.getAttr(charNode, "y"));
				w = Integer.parseInt(XMLReader.getAttr(charNode, "w"));
				h = Integer.parseInt(XMLReader.getAttr(charNode, "h"));
				int speed = Integer.parseInt(XMLReader.getAttr(charNode, "speed"));

				icon =  new ImageIcon((XMLReader.getAttr(charNode, "img")));
				Player player = new Player(x,y,x+w,y+h,speed,icon);		
				Player.playerVector.add(player);
				XMLFrame.drawPanel.add(player);
			}
			
			Node goalNode = XMLReader.getNode(activeMapNode, XMLReader.E_GOAL);
			if(goalNode != null) {
				w = Integer.parseInt((XMLReader.getAttr(goalNode, "w")));
				h = Integer.parseInt((XMLReader.getAttr(goalNode, "h")));
				x = Integer.parseInt((XMLReader.getAttr(goalNode, "x")));
				y = Integer.parseInt((XMLReader.getAttr(goalNode, "y")));
				icon =  new ImageIcon((XMLReader.getAttr(goalNode, "img")));
				MyGoal goal = new MyGoal(x,y,x+w,y+h,icon);
				MyGoal.goalVector.add(goal);
				XMLFrame.drawPanel.add(goal);
			}

			//Ÿ��
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
						MyTile tile = new MyTile(x,y,x+w,y+h, icon);
						MyTile.tileVector.add(tile);
						XMLFrame.drawPanel.add(tile);
					}
				}
			
			}
		

		}

	}
}
class SaveListener implements ActionListener {
	private JFileChooser chooser;
	File files[];
	public SaveListener() {
		chooser = new JFileChooser("C:\\Users\\kuj\\eclipse-workspace\\Finding the 2nd Earth");
		File dir = new File("C:\\Users\\kuj\\eclipse-workspace\\Finding the 2nd Earth");
		files = dir.listFiles();
	}

	public void actionPerformed(ActionEvent e) {

		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML", "xml");
		chooser.setFileFilter(filter);

		int ret = chooser.showSaveDialog(XMLFrame.drawPanel);
		if (ret != JFileChooser.APPROVE_OPTION) {
			// ������ �����ų� ��ҹ�ư�� �������
			JOptionPane.showMessageDialog(null, "������ �������� �ʾҽ��ϴ�.", "���", JOptionPane.WARNING_MESSAGE);
			return;
		}
		boolean flag = true;
		// ����ڰ� "����"��ư�� �������
		if (chooser.getSelectedFile() != null) {
			String filePath = chooser.getSelectedFile().getPath();
			String imageDescription = ((ImageIcon)DrawPanel.backImg).getDescription();
			XMLFile file = new XMLFile(filePath);
			//������ �ִ� ���ϸ�� ��ġ�� ��� �����
			for(int i=0; i< files.length; i++) {
				String filePath2 = files[i].toString();
				if(filePath.equals(filePath2)) {
					System.out.println("���ϰ�ħ");
					flag = false;
				}
			}
			String text = "<SpaceGame>\n<Screen>\n<Size w = \"" + DrawPanel.SCREEN_WIDTH +"\"";
			text +=  " h = \"" + DrawPanel.SCREEN_HEIGHT +"\" />\n</Screen>\n";
			text += "<GamePanel>\n<Bg  img = \"" + imageDescription +"\"";
			text +=  " w = \"" + DrawPanel.backImg.getIconWidth() +"\"";
			text +=  " h = \"" + DrawPanel.backImg.getIconHeight() +"\" />\n";
			text +=  "<ActiveMap>\n";
			// ����� Ȥ�� �̾��
			file.wirteFile(text, XMLFile.file,flag);
			//�̾���
			flag = true;
			Rectangle rect = new Rectangle();
			file.wirteFile(rect.XMLtoString(), XMLFile.file,flag);
			MyEnemy enemy = new MyEnemy();
			file.wirteFile(enemy.XMLtoString(), XMLFile.file,flag);
			MyItem item = new MyItem();
			file.wirteFile(item.XMLtoString(), XMLFile.file,flag);
			MyGoal goal = new MyGoal();
			file.wirteFile(goal.XMLtoString(), XMLFile.file,flag);
			Player player = new Player();
			file.wirteFile(player.XMLtoString(), XMLFile.file,flag);
			MyTile tile = new MyTile();
			file.wirteFile(tile.XMLtoString(), XMLFile.file,flag);
			
			text = "</ActiveMap>\n</GamePanel>\n</SpaceGame>";
			file.wirteFile(text, XMLFile.file,flag);
		}

	}
}
