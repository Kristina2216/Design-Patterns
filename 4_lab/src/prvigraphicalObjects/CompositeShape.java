package prvigraphicalObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import prvi.Renderer;

public class CompositeShape extends AbstractGraphicalObject{

	private List<GraphicalObject> objects=new ArrayList<GraphicalObject>();
	boolean selected;
	List<GraphicalObjectListener> listeners=new ArrayList<GraphicalObjectListener>();
	
	public CompositeShape(List<GraphicalObject> objects) {
		super(new Point[0]);
		if(objects!=null) {
			for(GraphicalObject o: objects)
				this.objects.add(o);
		}
		selected=false;
	}
	
	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected=true;
		notifySelectionListeners();
		
	}

	@Override
	public int getNumberOfHotPoints() {
		return 0;
	}

	@Override
	public Point getHotPoint(int index) {
		return null;
	}

	@Override
	public void setHotPoint(int index, Point point) {
	}

	@Override
	public boolean isHotPointSelected(int index) {
		return false;
	}

	@Override
	public void setHotPointSelected(int index, boolean selected) {
		
	}

	@Override
	public double getHotPointDistance(int index, Point mousePoint) {
		return 0;
	}

	@Override
	public void turnOffHotPoints() {
		
	}

	@Override
	public void translate(Point delta) {
		for(GraphicalObject o: objects) {
			o.translate(delta);
		}
		notifyListeners();
		
	}

	@Override
	public Rectangle getBoundingBox() {
		int minHorizontal=Integer.MAX_VALUE;
		int maxHorizontal=0;
		int minVertical=Integer.MAX_VALUE;
		int maxVertical=0;
		for(GraphicalObject o:objects) {
			 Rectangle boundingBox = o.getBoundingBox();
			 int x=boundingBox.getX();
			 int y=boundingBox.getY();
			 int width=boundingBox.getWidth();
			 int height=boundingBox.getHeight();
			 if(x<minHorizontal) 
				 minHorizontal=x;
			 if(y<minVertical)
				 minVertical=y;
			 if(x+width>maxHorizontal)
				 maxHorizontal=x+width;
			 if(y+height>maxVertical)
				 maxVertical=y+height;
		}
		return new Rectangle(minHorizontal, minVertical, maxHorizontal-minHorizontal, maxVertical-minVertical);
	}

	@Override
	public double selectionDistance(Point mousePoint) {
		double min=Integer.MAX_VALUE;
		for(GraphicalObject o: objects){
			if(o.selectionDistance(mousePoint)<min)
				min=o.selectionDistance(mousePoint);
		}
		if(min!=Integer.MAX_VALUE)
			return min;
		return 0;
	}

	@Override
	public void render(Renderer r) {
		for(GraphicalObject o: objects)
			o.render(r);
		
	}
	
	public List<GraphicalObject> getShapes() {
		return objects;
	}

	@Override
	public void addGraphicalObjectListener(GraphicalObjectListener l) {
		listeners.add(l);
		
	}

	@Override
	public void removeGraphicalObjectListener(GraphicalObjectListener l) {
		listeners.remove(l);
		
	}

	@Override
	public String getShapeName() {
		return "Composite Shape";
	}

	@Override
	public GraphicalObject duplicate() {
		List<GraphicalObject> duplicate=new ArrayList<GraphicalObject>();
		for(GraphicalObject o: objects)
			duplicate.add(o.duplicate());
		return new CompositeShape(duplicate);
	}
	
	private void notifyListeners() {
		for(GraphicalObjectListener o:listeners) {
			o.graphicalObjectChanged(this);
		}
	}
	
	private void notifySelectionListeners() {
		for(GraphicalObjectListener o:listeners) {
			o.graphicalObjectSelectionChanged(this);
		}
	}

	@Override
	public String getShapeID() {
		return "@COMP";
	}

	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		try {
            int num = Integer.parseInt(data);
            List<GraphicalObject> objectsSVG = new ArrayList<>();
            for (int i = 0; i < num; i ++) {
                objectsSVG.add(0, stack.pop());
            }
            stack.push(new CompositeShape(objectsSVG));
        } catch (NumberFormatException ex) {
        }

		
	}

	@Override
	public void save(List<String> rows) {
		// TODO Auto-generated method stub
		
	}
	
	

}
