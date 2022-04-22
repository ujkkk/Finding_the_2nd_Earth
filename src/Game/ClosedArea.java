package Game;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.util.Vector;

import javax.swing.JLabel;

public class ClosedArea extends JLabel {

	// �簢���� ������ ����ִ� ����
	Vector<Polygon> polyVector = null;
	Vector<Point> pointVector = new Vector<Point>(100);
	Vector<Point> closedAreaVector = new Vector<Point>(100);
	int blockSize = 0;
	int[][] binaryArray = null;
	int[][] contoureArray = null;
	Point startPoint = new Point();
	static final int UP = 0;
	static final int RIGHT = 1;
	static final int DOWN = 2;
	static final int LEFT = 3;
	int xMin,xMax, yMin, yMax;

	public ClosedArea(Vector<Polygon> polyVector, int blockSize) {
		this.setBounds(0, 0, SpaceGame.SCREEN_WIDTH, SpaceGame.SCREEN_HEIGHT);
		this.polyVector = polyVector;
		this.blockSize = blockSize;
		// ���� 0��1�� ǥ���ϴ� �迭 ����
		createBinaryArray();
		// �ܰ��� vector ����
		createContoureArray();
	}

	@Override
	synchronized public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, 0));
		Point p1;
		Point p2;
		int xp = 0, yp = 0;
		int x1, x2 = 0, y1, y2 = 0;
		for (int i = 0; i < pointVector.size(); i++) {
			p1 = pointVector.get(i);
			x1 = (startPoint.y - blockSize) + (p1.x + xp) * blockSize;
			y1 = (startPoint.x - blockSize) + (p1.y + yp) * blockSize;

			if (i + 1 == pointVector.size()) {
				p2 = pointVector.get(0);
			}

			else {
				p2 = pointVector.get(i + 1);
			}
			x2 = (startPoint.y - blockSize) + (p2.x + xp) * blockSize;
			y2 = (startPoint.x - blockSize) + (p2.y + yp) * blockSize;
			g2.draw(new Line2D.Double(y1, x1, y2, x2));
		}

	}

	// �ܰ��� �迭 ����
	public void createContoureArray() {
		int x = 0, y = 0;
		int preD = 0;
		//��ŸƮ ã��
		while (binaryArray[1][y] != 1) {
			y++;
		}

		Point start = new Point(1, y);
		Point prePoint = new Point();
		System.out.println("start: (1, " + y);
		Point p = new Point(0, 0);
		int direction = DOWN;
		
		p = start;
		do {
			// 1�� ������ �簢�� ���θ� �ǹ�, p�� �������� ȸ��
			if (binaryArray[p.x][p.y] == 1) {
				// �ٷ� ���� �߰��� ���� ����� ���� �������� ������ �� Ž�� ���� �߻�
				if(preD == direction ) {					
					System.out.println("�����ģ ����Ʈ (" + p.x + ", " + p.y + ") , d : " + direction + "preD: "+ preD);
					// �ܰ��� �迭�� �߰��� �ٷ����� ������ ���ư���
					p = prePoint;
					// �� ���ǹ��� �ɸ��� �ʵ��� preD�� ���� �������� �ٲ���
					preD = -1;
					prePoint = p;
				}
				
				// 1�� �����ϸ� point������ �ܰ��� ���Ϳ� �����ϰ� �������� ȸ��
				else {
					//�ܰ��� �迭�� 1 ���� - ����Ʈ ��
					contoureArray[p.x][p.y] = 1;
					// ���� �׸��� ���Ϳ� ����
					pointVector.add(p);
					System.out.println("current Point (" + p.x + ", " + p.y + ") , d : " + direction);
					// �������� ȸ��
					preD = direction;
					prePoint = p;
					direction = (direction - 1) % 4;
					if (direction < 0)
						direction += 4;
					
				}				
			} 
			//0�� ������ p�� ���������� ȸ��
			else {
				direction = (direction + 1) % 4;
				if (direction < 0)
					direction += 4;
			}
			//direction�� ���� p�� �·Ḧ �ű�
			switch (direction) {
			case UP:
				p = new Point(p.x, p.y - 1);
				break;
			case RIGHT:
				p = new Point(p.x + 1, p.y);
				break;
			case DOWN:
				p = new Point(p.x, p.y + 1);
				break;
			case LEFT:
				p = new Point(p.x - 1, p.y);
				break;
			}
		//d ~ while break ���� - ��ŸƮ �������� ���ƿԴٸ� break....
		} while (!p.equals(start));

		// �ܼ� Ȯ�ο�
		for (int i = 0; i < contoureArray.length; i++) {
			for (int j = 0; j < contoureArray[i].length; j++) {
				System.out.print(contoureArray[i][j] + " ");

			}
			System.out.println();
		}
		repaint();


	}	

	// polyVector�� ������ 0��1�� ������ ���� �迭 ����
	public void createBinaryArray() {
		Polygon poly = polyVector.get(0);
		xMin = poly.xpoints[0]; xMax = poly.xpoints[3];
		yMin = poly.ypoints[0]; yMax = poly.ypoints[1];

		for (int i = 0; i < polyVector.size(); i++) {
			poly = polyVector.get(i);
			// �簢���� ���� �ִ� x
			int x1 = poly.xpoints[0];
			// �簢���� �Ʒ��� �ִ� x
			int x2 = poly.xpoints[3];
			// �簢���� ���� �ִ� y
			int y1 = poly.ypoints[0];
			// �簢���� �Ʒ��� �ִ� y
			int y2 = poly.ypoints[1];

			// min, max ã��
			if (x1 < xMin)
				xMin = x1;
			if (x2 > xMax)
				xMax = x2;
			if (y1 < yMin)
				yMin = y1;
			if (y2 > yMax)
				yMax = y2;
		}
		startPoint = new Point(xMin, yMin);
		System.out.println("xMin (" + xMin + ", " + yMin + ")");
		int column = (xMax - xMin) / blockSize;
		int rows = (yMax - yMin) / blockSize;

		binaryArray = new int[rows + 4][column + 4];
		contoureArray = new int[rows +4][column + 4];
		// binaryArray 0���� �ʱ�ȭ
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < column; j++) {
				binaryArray[i][j] = 0;
				contoureArray[i][j] = 0;
			}
		}
		// binaryArray�� 1�� 0 ���� �Է�
		for (int p = 0; p < polyVector.size(); p++) {
			poly = polyVector.get(p);
			int x1 = (poly.xpoints[0] - (xMin)) / blockSize + 1;
			// �簢���� �Ʒ��� �ִ� x
			int x2 = (poly.xpoints[3] - (xMin)) / blockSize + 1;
			// �簢���� ���� �ִ� y
			int y1 = (poly.ypoints[0] - (yMin)) / blockSize + 1;
			// �簢���� �Ʒ��� �ִ� y
			int y2 = (poly.ypoints[1] - (yMin)) / blockSize + 1;
			for (int j = y1; j <= y2; j++) {
				for (int i = x1; i <= x2; i++) {
					binaryArray[j][i] = 1;
				}
			}
		}

		// print
		for (int i = 0; i < rows + 4; i++) {
			for (int j = 0; j < column + 4; j++) {
				System.out.print(binaryArray[i][j] + " ");

			}
			System.out.println();
		}
		System.out.println("============================================");
		
	}

}