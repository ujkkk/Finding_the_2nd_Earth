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

//ó�� ����� ���ϴ� dialog
class MyDialog extends JDialog{
	
	JButton okButton = new JButton("OK");
	JTextField tf1 = new JTextField(10);
	JTextField tf2 = new JTextField(10);
	public MyDialog(JFrame frame) {
		super(frame, "ũ�� �Է� â");
		setSize(200, 170);
		//������ �󿡼� �߾ӿ� ��ġ
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
				//�ƹ��͵� �������� width = 900
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
				
		//������ �󿡼� �߾ӿ� ��ġ
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	        Dimension frm = this.getSize();
	        
	        int xpos = (int)(screen.getWidth() / 2 - frm.getWidth() / 2);
	        int ypos = (int)(screen.getHeight() / 2 - frm.getHeight() / 2);
	       
	        this.setLocation(xpos, ypos);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		// �����г��� ������� ����. drawPanel�� ������ �����.drawPanelWidth�� drawPanel�� ����� �ǹ���.
		mainPanel = new MainPanel(drawPanelWidth,drawPanelHeight);
		//spltPane�� ���ʿ� mainPanel ����
		jsp.setLeftComponent(mainPanel);
		jsp.setDividerLocation(900);
		//spltPane�� �����ʿ� tabbedPane ����
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
		
		//frame�� toobar�� ����
		c.add(new ToolBar(mainPanel), BorderLayout.NORTH);
		//frame�� split pane�� ����
		c.add(jsp, BorderLayout.CENTER);
		this.setResizable(false);	
		setVisible(true);
		
		
	}

}
class MainPanel extends JPanel{
	
	// �����г��� drawPanel�� ����� �°� ����� ��ġ�ϴ� ����
	public MainPanel(int drawPanelWidth, int drawPanelHeight) {
		this.setLayout(null);
		this.setSize(600, 600);
		XMLFrame.drawPanel = new DrawPanel(drawPanelWidth, drawPanelHeight,this);
		this.add(XMLFrame.drawPanel);
	}
}
//���� �г�
class TabbedPane extends JTabbedPane{
	
	JScrollPane scrollPane;
	public TabbedPane(){
		//��ϻ������ ���ȭ���� ���ϴ� �г�
		this.addTab("init", new ScreenBoradPanel());
		// ���� �׸��� �г�
		this.addTab("Map", new MapPanel());
		scrollPane = new JScrollPane (new ObjectPanel());
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// object�� �׸��� �г�
		this.addTab("Object", scrollPane);
		
		
	}
}

//JTabbedPane - 2, ���� �׸��� �г�
class MapPanel extends JPanel implements ChangeListener{
	JColorChooser colorChooser;
	JButton resetBt;
	Rectangle rect = new Rectangle();
	static Color selectedColor = Color.white;
	public MapPanel() {
		//������� chooser
		colorChooser = new JColorChooser();
		colorChooser.getSelectionModel().addChangeListener(this);
		colorChooser.setBorder(BorderFactory.createTitledBorder(null, "������"));
	
		this.add(colorChooser);
		
		resetBt = new JButton("reset");
		resetBt.addMouseListener(new MouseAdapter() { // ���콺 �̺�Ʈ
			
		@Override
		public void mouseClicked(MouseEvent e) { 
			rect.reset();
		}
	}); // ���콺 ������ end..
		add(resetBt);
		
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		selectedColor = colorChooser.getColor(); //���� ���ñ��� ���� �÷��� ��ȯ
		System.out.println(selectedColor);
	}

}
//JTabbedPane - 1, init�г�
class ScreenBoradPanel extends JPanel{
	public static final int SCREEN_WIDTH = 270;
	//����
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
		
		//30 �����
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
		sizeCombo.addActionListener(new ActionListener() { //�׼��̺�Ʈ. �ɼ��� �����ϰų� �ٲٸ� ����
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb =(JComboBox<String>)e.getSource(); // �޺��ڽ��� ĳ����
				String level = sizes[cb.getSelectedIndex()];//���õ� value �� �����ͼ� level �� ����
				// �������� �ܾ �����Ǵ� �ӵ��� �ٲ�
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
		
		// ���ȭ�� �ܰ���
				JLabel label = new JLabel("");
				label.setBorder(new TitledBorder(new LineBorder(Color.black, 1), "���ȭ��"));

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
	
	// init �гο��� ���ȭ�� �����ϴ� panel
	class ButtonPanel extends JPanel {
		int count = 0;
		Vector<JButton> buttons = new Vector<JButton>(100);
		MyButton button;
		MyButton plusButton;
		int buttonSize = 50;

		public ButtonPanel(String[] imgs) {
			this.setLayout(null);
			setOpaque(true); // ��� ���� �����ϰ�
			setBackground(Color.white);
			for (int i = 0; i < imgs.length ; i++) {
				count++;
				ImageIcon icon = new ImageIcon("src/img/" + imgs[(i)]);
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

	//�÷��� ��ư�� ������ �̹����� ���Ͽ��� ã�� �߰��ϴ� �̺�Ʈ������
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
			
			// �̹��� ��� �˾Ƴ���
			String imageDescription = ((ImageIcon) b.icon).getDescription();
			DrawPanel.backImg = new ImageIcon(imageDescription);
		}
	}
}


