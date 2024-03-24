package prvigraphicalObjects;

import java.util.ArrayList;
import java.util.List;

import prvi.GeometryUtil;

public abstract class AbstractGraphicalObject implements GraphicalObject{
	private Point[] hotPoints;
	boolean[] hotPointSelected;
	boolean selected;
	List<GraphicalObjectListener> listeners=new ArrayList<GraphicalObjectListener>();
	
	public AbstractGraphicalObject(Point[] points) {
		super();
		selected=false;
		hotPoints=new Point[points.length];
		for(int i=0;i<points.length;i++)
			hotPoints[i]=points[i];
		hotPointSelected = new boolean[hotPoints.length];
	}
	
	@Override
	public Point getHotPoint(int index) {
		return hotPoints[index];
	}
	
	@Override
	public void setHotPoint(int index, Point p) {
		hotPoints[index]=p;
		notifyListeners();
	}
	
	public void turnOffHotPoints() {
		int i=0;
		for(Boolean p:hotPointSelected) {
			hotPointSelected[i]=false;
			i++;
		}	
		notifySelectionListeners();
	}
	
	@Override
	public int getNumberOfHotPoints() {
		return hotPoints.length;
	}
	@Override
	public double getHotPointDistance(int index, Point p) {
		return GeometryUtil.distanceFromPoint(hotPoints[index], p);
	}
	
	@Override
	public boolean isHotPointSelected(int index) {
		return hotPointSelected[index];
	}
	
	@Override
	public void setHotPointSelected(int index, boolean toSet) {
		hotPointSelected[index]=toSet;
		notifySelectionListeners();
	}
	
	@Override
	public boolean isSelected() {
		return selected;
	}
	
	@Override 
	public void setSelected(boolean toSet) {
		selected=toSet;
		notifySelectionListeners();
	}
	
	@Override
	public void translate(Point p) {
		for(int i=0;i<hotPoints.length;i++) 
			hotPoints[i]=hotPoints[i].translate(p);
		notifyListeners();
	}
	
	@Override
	public void addGraphicalObjectListener(GraphicalObjectListener o) {
		listeners.add(o);
	}
	
	@Override
	public void removeGraphicalObjectListener(GraphicalObjectListener o) {
		listeners.remove(o);
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
	
}
