package prvi;

import java.awt.Color;
import java.awt.Graphics2D;

import prvigraphicalObjects.Point;

public class G2DRendererImpl implements Renderer{
	private Graphics2D g2d;
	
	public G2DRendererImpl(Graphics2D g2d) {
		this.g2d = g2d;
	}

	@Override
	public void drawLine(Point s, Point e) {
		g2d.setColor(Color.BLUE);
		g2d.drawLine(s.getX(), s.getY(), e.getX(), e.getY());
	}

	@Override
	public void fillPolygon(Point[] points) {
		int[] xcord = new int[points.length];
		int[] ycord = new int[points.length];
		int index=0;
		for (Point p:points) {
			xcord[index] = p.getX();
			ycord[index] = p.getY();
			index++;
		}
		
		g2d.setColor(Color.RED);
		g2d.drawPolygon(xcord, ycord, points.length);
		g2d.setColor(Color.BLUE);
		g2d.fillPolygon(xcord, ycord, points.length);
	}
}
