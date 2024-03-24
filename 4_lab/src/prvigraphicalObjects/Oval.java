package prvigraphicalObjects;

import java.util.List;
import java.util.Stack;

import prvi.Renderer;

public class Oval extends AbstractGraphicalObject{

	public Oval() {
		super(new Point[]{ new Point(0, 10), new Point(10,0) });
	}
	
	
	public Oval(Point down, Point right) {
		super(new Point[]{ down, right });
	}
	
	@Override
	public Rectangle getBoundingBox() {
		Point down = getHotPoint(0);
		Point right = getHotPoint(1);
		int x = down.getX() - (right.getX() - down.getX());
		int y = right.getY() - (down.getY() - right.getY());
		int width = 2 * (right.getX() - down.getX());
		int height = 2 * (down.getY() - right.getY());
		
		return new Rectangle(x, y, width, height);
	}

	@Override
	public double selectionDistance(Point mousePoint) {

		if(checkInside(mousePoint))
			return 0;
		
		int a = getHotPoint(1).getX() - getHotPoint(0).getX(); //horizontal axis radius
	    int b = getHotPoint(0).getY() - getHotPoint(1).getY(); //vertical axis radius
	    int p = mousePoint.getX() - getHotPoint(0).getX();
	    int q = mousePoint.getY() - getHotPoint(1).getY();
	    double x = a*b*p/Math.sqrt((double) p*p * b*b + q*q * a*a);
        double y = x * q/p;
        double positive = Math.pow(x - p, 2) + Math.pow(y - q, 2);
        double negative = Math.pow(x + p, 2) + Math.pow(y + q, 2);
        double min=Math.sqrt(Math.min(negative, positive));
        return min;
			
	}

	@Override
	public String getShapeName() {
		return "Oval";
	}

	@Override
	public GraphicalObject duplicate() {
		Oval duplicate=new Oval(getHotPoint(0), getHotPoint(1));
		duplicate.setHotPointSelected(0, isHotPointSelected(0));
        duplicate.setHotPointSelected(1, isHotPointSelected(1));
        duplicate.setSelected(isSelected());
		return duplicate;
	}
	
	private boolean checkInside(Point p ) {
		Point down = getHotPoint(0);
		Point right = getHotPoint(1);
		int h = down.getX();
		int k = right.getY();
		int a = right.getX() - h;
		int b = down.getY() - k;
		double u = ((int)Math.pow((p.getX() - h), 2) / (int)Math.pow(a, 2))
	            + ((int)Math.pow((p.getY() - k), 2) / (int)Math.pow(b, 2));
		return  u<= 1;
	}


	@Override
	public void render(Renderer r) {
		Point down = getHotPoint(0);
        Point right = getHotPoint(1);
        int a = right.getX() - down.getX(); //horizontal axis radius
        int b = down.getY() - right.getY(); //vertical axis radius
        Point[] toFill = new Point[250];
        
        for (int i = 0; i < 250; i++) {
            double angle = (2*Math.PI/250) * i; //angle
            int x = (int)(Math.cos(angle)*a) + down.getX(); // get point for a 
            int y = (int)(Math.sin(angle)*b) + right.getY();
            toFill[i] = new Point(x, y);
        }
		r.fillPolygon(toFill);
		
	}


	@Override
	public String getShapeID() {
		 return "@OVAL";
	}


	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		String[] ints = data.split(" ");
        try {
            stack.push(new Oval(
                    new Point(Integer.parseInt(ints[2].strip()),Integer.parseInt(ints[3].strip())),
                    new Point(Integer.parseInt(ints[0].strip()),Integer.parseInt(ints[1].strip()))));
        } catch (NumberFormatException ex) {
         
        }
	}


	@Override
	public void save(List<String> rows) {
		Point right = getHotPoint(1);
        Point down = getHotPoint(0);
        rows.add(getShapeID() + " " + right.getX() + " " + right.getY() + " " + down.getX() + " " + down.getY());
	}
	

}
