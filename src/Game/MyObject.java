package Game;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MyObject extends JLabel{
	
	Image img;
	int x, y, w, h;
	public MyObject() {}
	
	public MyObject(int x, int y, int w, int h, ImageIcon icon) {
		this.x= x;
		this.y = y;
		this.w = w;
		this.h=  h;
		img = icon.getImage();
		this.setBounds(x,y,w,h);
	}
	public void paintComponent(Graphics g) {
		setLocation(x,y);
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
