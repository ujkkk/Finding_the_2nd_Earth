package Game;
import java.awt.Polygon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.ImageIcon;


public class Character extends MyObject implements Runnable {


	boolean flag = true;
	boolean stop;
	int speed;
	static boolean crush = false;
	//static Polygon polygon = new Polygon();

	Vector<Polygon> polyVector;
	Polygon myPoly = new Polygon();


	public static boolean up, down, right, left;

	public Character(int x, int y, int w, int h, int speed, ImageIcon icon, Vector<Polygon> polyVector) {

		super(x,y,w,h,icon);
		this.speed = speed;
		this.polyVector = polyVector;

		findMyArea(x,y ,w ,h);
		//point = new Point(x, y);
		Thread th = new Thread(this);
		th.start();
		init();
		//img = icon.getImage();
		this.addKeyListener(new KeyListener());
		this.requestFocus();
		this.setFocusable(true);
	}

	public void init() {
		up = false;
		down = false;
		left = false;
		right = false;
	}
	// 자기 영역의 polygon 찾기
	public boolean findMyArea(int x,int y, int w, int h) {
		for (int i = 0; i < polyVector.size(); i++) {
			Polygon poly = polyVector.get(i);
			if (poly.contains(x, y, w, h)) {
				if(myPoly != poly) {
					this.myPoly = poly;
					return true;
				}
				
			}
		}
		return false;
	}

	public void process() {
		if (!myPoly.contains(x, y, w, h)) {
			if (up && findMyArea(x+w/2, y-speed, 1, 1));
			else if (down && findMyArea(x+w/2, y+h+h/2, 1, 1));
			else if (right && findMyArea(x+w+w/2, y+h/2, 1, 1));
			else if (left && findMyArea(x-speed, y+h/2, 1, 1));				
			else {
				crush = true;
				crushCheck();
			}
				
		}
		else {
			crush = false;
		}
	}

	//벽과의 충돌 체크
	public void crushCheck() {
		if (crush) {
			if (this.x - w/5< myPoly.xpoints[0] && this.x- w/5 < myPoly.xpoints[1] && this.x- w/5< myPoly.xpoints[2]
					&& this.x- w/5 < myPoly.xpoints[3])
				setLeft(false);
			if (this.x + w +5 > myPoly.xpoints[0] && this.x + w+5 > myPoly.xpoints[1] && this.x + w+5 > myPoly.xpoints[2]
					&& this.x + w+5 >= myPoly.xpoints[3])
				setRight(false);
			if (this.y < myPoly.ypoints[0] && this.y < myPoly.ypoints[1] && this.y < myPoly.ypoints[2]
					&& this.y <= myPoly.ypoints[3])
				setUp(false);
			if (this.y + h > myPoly.ypoints[0] && this.y + h > myPoly.ypoints[1] && this.y + h > myPoly.ypoints[2]
					&& this.y + h >= myPoly.ypoints[3])
				setDown(false);
		}
	}

	@Override
	public void run() {
		while (flag) {
			this.requestFocus();
			this.setFocusable(true);
			process();
			keyProcess();
			repaint();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	public void keyProcess() {

		if (up && y - speed > 0) {
			y -= speed;
		}
		if (down && x - speed > 0) {
			y += speed;
		}
		if (left && x - speed > 0) {
			x -= speed;
		}
		if (right && x - speed > 0) {
			x += speed;
		}
	}

	public static void setUp(boolean up) {
		Character.up = up;
	}

	public static void setDown(boolean down) {
		Character.down = down;
	}

	public static void setLeft(boolean left) {
		Character.left = left;
	}

	public static void setRight(boolean right) {
		Character.right = right;
	}

}

class KeyListener extends KeyAdapter {
	@Override
	public void keyPressed(KeyEvent e) { // 키를 눌렀을 때

		switch (e.getKeyCode()) {
		case KeyEvent.VK_S:
			Character.setDown(true);
			break;
		case KeyEvent.VK_W:
			Character.setUp(true);
			break;
		case KeyEvent.VK_A:
			Character.setLeft(true);
			break;
		case KeyEvent.VK_D:
			Character.setRight(true);
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) { // 키를 뗴었을 때

		switch (e.getKeyCode()) {
		case KeyEvent.VK_S:
			Character.setDown(false);
			break;
		case KeyEvent.VK_W:
			Character.setUp(false);
			break;
		case KeyEvent.VK_A:
			Character.setLeft(false);
			break;
		case KeyEvent.VK_D:
			Character.setRight(false);
			break;

		}
	}
}
