package XMLcreate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.w3c.dom.Node;

import XMLcreate.ObjectPanel.ButtonPanel;
import XMLcreate.ObjectPanel.OpenActionListener;
import XMLcreate.ObjectPanel.actionEvent;

//처음 사이즈를 정하는 dialog
class MyDialog extends JDialog{
	
	JButton okButton = new JButton("OK");
	JTextField tf1 = new JTextField(10);
	JTextField tf2 = new JTextField(10);
	public MyDialog(JFrame frame) {
		super(frame, "크기 입력 창");
		setSize(200, 170);
		//윈도우 상에서 중앙에 배치
				Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			        Dimension frm = this.getSize();
			       
			        int xpos = (int)(screen.getWidth() / 2 - frm.getWidth() / 2);
			        int ypos = (int)(screen.getHeight() / 2 - frm.getHeight() / 2);
			       
			        this.setLocation(xpos, ypos);
		setLayout(new FlowLayout());
		add(okButton);
		
		JLabel label = new JLabel("    <MAX 900X600>   ");
		label.setFont(new Font("", Font.BOLD, 17));
		
		JLabel label1  = new JLabel("Width :");
		JLabel label2  = new JLabel("Height :");
		add(label);
		add(label1);
		add(tf1);
		add(label2);
		add(tf2);
		add(okButton);
		
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int width = 0, height = 0;
				setVisible(false);
				//아무것도 안적으면 width = 900
				if(tf1.getText().equals("")) {
					width = 900;
				}
				else if(!tf1.getText().equals("")) {
					 if (Integer.parseInt(tf1.getText()) >= 900)
						 width = 900;
					 else 
							width =Integer.parseInt(tf1.getText());
				}
							 
				
				if(tf2.getText().equals("")) {
					height  = 600;
				}
				else if(!tf2.getText().equals("")) {

					 if (Integer.parseInt(tf2.getText()) >= 600)
						 height = 600;
					 else 
							height =Integer.parseInt(tf2.getText());
				}				
				
				System.out.println(width+ ", " + height);
				new XMLFrame(width, height);
			}

		});
	}
}



public class XMLFrame extends JFrame{
	
	static DrawPanel drawPanel;
	static ObjectPanel objectPanel = new ObjectPanel();
	static int blockSize = 40;
	static ScreenBoradPanel screenBoradPanel;
	
	JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);	
	MainPanel mainPanel;
	TabbedPane tabbedPane;
	MyDialog dialog;
	
	public XMLFrame(int drawPanelWidth, int drawPanelHeight) {
		super("XML Authoring");
		this.setSize(1200, 650);
				
		//윈도우 상에서 중앙에 배치
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	        Dimension frm = this.getSize();
	        
	        int xpos = (int)(screen.getWidth() / 2 - frm.getWidth() / 2);
	        int ypos = (int)(screen.getHeight() / 2 - frm.getHeight() / 2);
	       
	        this.setLocation(xpos, ypos);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		// 메인패널의 사이즈는 고정. drawPanel만 사이즈 변경됨.drawPanelWidth는 drawPanel의 사이즈를 의미함.
		mainPanel = new MainPanel(drawPanelWidth,drawPanelHeight);
		//spltPane의 왼쪽에 mainPanel 붙임
		jsp.setLeftComponent(mainPanel);
		jsp.setDividerLocation(900);
		//spltPane의 오른쪽에 tabbedPane 붙임
		tabbedPane=  new TabbedPane();
		tabbedPane.addChangeListener(new ChangeListener() {
	        public void stateChanged(ChangeEvent e) {
	           if(tabbedPane.getSelectedIndex() == 0) {
	        	   
	           }
	           else if(tabbedPane.getSelectedIndex() == 1) {
	        	   DrawPanel.isMapPane = true;
	        	   DrawPanel.isObjectPane = false;	        	  
	           }
	           else if(tabbedPane.getSelectedIndex() == 2) {
	        	   DrawPanel.isMapPane = false;
	        	   DrawPanel.isObjectPane = true;
	           }           
	        }
	    });
		jsp.setRightComponent(tabbedPane);
		
		//frame에 toobar를 붙임
		c.add(new ToolBar(mainPanel), BorderLayout.NORTH);
		//frame에 split pane을 붙임
		c.add(jsp, BorderLayout.CENTER);
		this.setResizable(false);	
		setVisible(true);
		
		
	}

}
class MainPanel extends JPanel{
	
	// 메인패널은 drawPanel을 사이즈에 맞게 가운데이 배치하는 역할
	public MainPanel(int drawPanelWidth, int drawPanelHeight) {
		this.setLayout(null);
		this.setSize(600, 600);
		XMLFrame.drawPanel = new DrawPanel(drawPanelWidth, drawPanelHeight,this);
		this.add(XMLFrame.drawPanel);
	}
}
//왼쪽 패널
class TabbedPane extends JTabbedPane{
	
	JScrollPane scrollPane;
	public TabbedPane(){
		//블록사이즈와 배경화면을 정하는 패널
		this.addTab("init", new ScreenBoradPanel());
		// 맵을 그리는 패널
		this.addTab("Map", new MapPanel());
		scrollPane = new JScrollPane (new ObjectPanel());
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// object를 그리는 패널
		this.addTab("Object", scrollPane);
		
		
	}
}

//JTabbedPane - 2, 맵을 그리는 패널
class MapPanel extends JPanel implements ChangeListener{
	JColorChooser colorChooser;
	JButton resetBt;
	Rectangle rect = new Rectangle();
	static Color selectedColor = Color.white;
	public MapPanel() {
		//색상고르는 chooser
		colorChooser = new JColorChooser();
		colorChooser.getSelectionModel().addChangeListener(this);
		colorChooser.setBorder(BorderFactory.createTitledBorder(null, "색상선택"));
	
		this.add(colorChooser);
		
		resetBt = new JButton("reset");
		resetBt.addMouseListener(new MouseAdapter() { // 마우스 이벤트
			
		@Override
		public void mouseClicked(MouseEvent e) { 
			rect.reset();
		}
	}); // 마우스 리스너 end..
		add(resetBt);
		
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		selectedColor = colorChooser.getColor(); //색상 선택기의 현재 컬러값 반환
		System.out.println(selectedColor);
	}

}
//JTabbedPane - 1, init패널
class ScreenBoradPanel extends JPanel{
	public static final int SCREEN_WIDTH = 270;
	//상하
	 JLabel lblSize= new JLabel("block size:");
	 JTextField txtId = new JTextField();
	 JPanel panel1 = new JPanel();
	 JLabel imgLabel;
	 JButton button;
	 JComboBox<String> sizeCombo;
	 ButtonPanel buttonPanel;
	 String [] imgs = {"space.jpg", "back2.jpg","black.png", "blue.png", "white.png"};
	 MyButton selectedButton = new MyButton();

	private String [] sizes = {"30", "35", "40","45", "50","55","70","80"};
	public ScreenBoradPanel() {
		this.setBackground(Color.white);		
		Font font = new Font("OCR A", Font.BOLD, 15);
		
		//30 어디갔지
		this.setLayout(null);
		ImageIcon img = new ImageIcon("src/img/space.jpg");
		lblSize.setFont(font);
		lblSize.setBounds(20, 50,150 ,50 );
		
		button = new JButton("OK");
		button.setFont(font);
		button.setBounds(100,45, 100, 70);
		
		sizeCombo = new JComboBox<String>(sizes);
		sizeCombo.setLocation(100, 60);
		sizeCombo.setSize(60,30);
		sizeCombo.addActionListener(new ActionListener() { //액션이벤트. 옵션을 선택하거나 바꾸면 실행
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb =(JComboBox<String>)e.getSource(); // 콤보박스로 캐스팅
				String level = sizes[cb.getSelectedIndex()];//선택된 value 를 가져와서 level 에 대입
				// 레벨마다 단어가 생성되는 속도를 바꿈
				if(level.equals("30"))
					XMLFrame.blockSize = 30;
				else if(level.equals("35"))
					XMLFrame.blockSize = 35;
				else if(level.equals("40"))
					XMLFrame.blockSize = 40;
				else if(level.equals("45"))
					XMLFrame.blockSize = 45;
				else if(level.equals("50"))
					XMLFrame.blockSize = 50;
				else if(level.equals("55"))
					XMLFrame.blockSize = 55;
				else if(level.equals("70"))
					XMLFrame.blockSize = 70;
				else if(level.equals("80"))
					XMLFrame.blockSize = 80;
			}
		});
		
		// 배경화면 외곽선
				JLabel label = new JLabel("");
				label.setBorder(new TitledBorder(new LineBorder(Color.black, 1), "배경화면"));

				label.setSize(220, 150);
				label.setLocation(10, 150);
				add(label);
		buttonPanel = new ButtonPanel(imgs);
		buttonPanel.setBounds(20, 180, 210, 100);
		add(buttonPanel);
		
		add(lblSize);
		add(sizeCombo);
		//add(imgLabel);		
	}
	
	// init 패널에서 배경화면 선택하는 panel
	class ButtonPanel extends JPanel {
		int count = 0;
		Vector<JButton> buttons = new Vector<JButton>(100);
		MyButton button;
		MyButton plusButton;
		int buttonSize = 50;

		public ButtonPanel(String[] imgs) {
			this.setLayout(null);
			setOpaque(true); // 배경 지정 가능하게
			setBackground(Color.white);
			for (int i = 0; i < imgs.length ; i++) {
				count++;
				ImageIcon icon = new ImageIcon("src/img/" + imgs[(i)]);
				button = new MyButton(buttonSize * ((i % 4)), buttonSize * (i / 4), buttonSize, buttonSize, icon);
				button.addActionListener(new actionEvent());
				this.add(button);
			}

			// 플러스 버튼
			plusButton = new MyButton(buttonSize * (count % 4), buttonSize * (count / 4), buttonSize, buttonSize,
					new ImageIcon("src/img/plus.png"));
			plusButton.addActionListener(new OpenActionListener(this));
			plusButton.setOpaque(true);
			plusButton.setBackground(Color.gray);
			this.add(plusButton);
		}
	}

	//플러스 버튼를 누르면 이미지를 파일에서 찾아 추가하는 이벤트리스너
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
				// 강제로 닫혔거나 취소버튼을 누른경우
				JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다.", "경고", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// 사용자가 "열기"버튼을 누른경우
			if (chooser.getSelectedFile() != null) {
				String filePath = chooser.getSelectedFile().getPath();
				ImageIcon icon = new ImageIcon(filePath);
				// 새로운 버튼 생성
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
			
			// 이미지 경로 알아내기
			String imageDescription = ((ImageIcon) b.icon).getDescription();
			DrawPanel.backImg = new ImageIcon(imageDescription);
		}
	}
}


