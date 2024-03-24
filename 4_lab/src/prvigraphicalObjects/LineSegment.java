package prvigraphicalObjects;

import java.util.List;
import java.util.Stack;

import prvi.GeometryUtil;
import prvi.Renderer;

public class LineSegment extends AbstractGraphicalObject{
	
	
	public LineSegment() {
		super(new Point[]{ new Point(0, 0), new Point(10, 0) });
	}
	
	
	public LineSegment(Point start, Point end) {
		super(new Point[]{start, end});
		
	}
	
	@Override
	public Rectangle getBoundingBox() {
		Point point1 = getHotPoint(0);
		Point point2 = getHotPoint(1);
		int x;
		int width;
		if(point1.getX() < point2.getX()) {
			x = point1.getX();
			width= point2.getX()-point1.getX();
		}else {
			x=point2.getX();
			width= point1.getX()-point2.getX();
		}
		int y;
		int height;
		if(point1.getY() < point2.getY()) {
			y = point1.getY();
			height=point2.getY()-point1.getY();
		}else {
			y=point2.getY();
			height=point1.getY()-point2.getY();
		}
		return new Rectangle(x, y, width, height);
	}

	@Override
	public double selectionDistance(Point mousePoint) {
		return GeometryUtil.distanceFromLineSegment(getHotPoint(0), getHotPoint(1), mousePoint);
	}

	@Override
	public String getShapeName() {
		return "Linija";
	}

	@Override
	public GraphicalObject duplicate() {
		LineSegment duplicate=new LineSegment(new Point(getHotPoint(0).getX(),getHotPoint(0).getY()), new Point(getHotPoint(1).getX(),getHotPoint(1).getY()));
		duplicate.setHotPointSelected(0, isHotPointSelected(0));
        duplicate.setHotPointSelected(1, isHotPointSelected(1));
		duplicate.setSelected(this.isSelected());
		return duplicate;
	}


	@Override
	public void render(Renderer r) {
		r.drawLine(this.getHotPoint(0), this.getHotPoint(1));
		
	}


	@Override
	public String getShapeID() {
		return "@LINE";
	}


	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		String[] ints = data.split(" ");
        try {
            stack.push(new LineSegment(new Point(Integer.parseInt(ints[0].strip()),Integer.parseInt(ints[1].strip())),
                    					new Point(Integer.parseInt(ints[2].strip()),Integer.parseInt(ints[3].strip()))));
        } catch (NumberFormatException ex) {
         
        }
		
	}


	@Override
	public void save(List<String> rows) {
		Point p1 = getHotPoint(0);
        Point p2 = getHotPoint(1);
        rows.add(getShapeID() + " " + p1.getX() + " " + p1.getY() + " " + p2.getX() + " " + p2.getY());
		
	}
	

}
