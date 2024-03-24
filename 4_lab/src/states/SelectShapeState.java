package states;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import prvi.DocumentModel;
import prvi.Renderer;
import prvigraphicalObjects.CompositeShape;
import prvigraphicalObjects.GraphicalObject;
import prvigraphicalObjects.Point;
import prvigraphicalObjects.Rectangle;

public class SelectShapeState implements State{
	private DocumentModel model;
	
	public SelectShapeState(DocumentModel m) {
		model=m;
	}
	
	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		 GraphicalObject object = model.findSelectedGraphicalObject(mousePoint);
	     if (!ctrlDown) {
	    	 List<GraphicalObject> selected = model.getSelectedObjects();
	    	 if (selected.size() == 1 && selected.get(0) == object) {
	    		 int index = model.findSelectedHotPoint(object, mousePoint);
	             if (index != -1) 
	            	 object.setHotPointSelected(index, true);
	             else
	                return;
	         } else {
	            for(GraphicalObject obj:model.list())
	            	obj.setSelected(false);
	            if (object != null) {
	                object.setSelected(true);
	            }
	            else
	                return;
	        }
	     } else {
	        if (object == null) return;
	        object.setSelected(true);	        	
	     }
	}

	@Override
	public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		 List<GraphicalObject> selected = model.getSelectedObjects();
	        if (selected.size() == 1) {
	            GraphicalObject o = selected.get(0);
	            o.turnOffHotPoints();
	        }

	}

	@Override
	public void mouseDragged(Point mousePoint) {
		List<GraphicalObject> selected = model.getSelectedObjects();
        if (selected.size() != 1) 
        	return;
	    GraphicalObject object = selected.get(0);
	    for (int i = 0; i < object.getNumberOfHotPoints(); i ++) {
	       if (object.isHotPointSelected(i)) {
	           object.setHotPoint(i, mousePoint);
	           return;
	       }
	  }
		
	}

	@Override
	public void keyPressed(int keyCode) {
		List<GraphicalObject> selected = model.getSelectedObjects();
		GraphicalObject object;
		switch(keyCode) {
			case KeyEvent.VK_DOWN:
				for(GraphicalObject o:selected)
					o.translate(new Point(0,1));
				break;
			case KeyEvent.VK_UP:
				for(GraphicalObject o:selected)
					o.translate(new Point(0,-1));
				break;
			case KeyEvent.VK_RIGHT:
				for(GraphicalObject o:selected)
					o.translate(new Point(1,0));
				break;
			case KeyEvent.VK_LEFT:
				for(GraphicalObject o:selected)
					o.translate(new Point(-1,0));
				break;
			case KeyEvent.VK_PLUS:
				if(selected.size()!=1)
					return;
				object=selected.get(0);
				model.increaseZ(object);
				break;
			case KeyEvent.VK_MINUS:
				if(selected.size()!=1)
					return;
				object=selected.get(0);
				model.decreaseZ(object);
				break;
			case KeyEvent.VK_G:
				if(selected.size()<=1)
					return;
				makeCompositeObject();
				break;
			case KeyEvent.VK_U:
				if(selected.size()!=1)
					return;
				if(!model.getSelectedObjects().get(0).getShapeName().equals("Composite Shape"))
					return;
				destroyCompositeObject((CompositeShape)model.getSelectedObjects().get(0));
		}
	}

	@Override
	public void afterDraw(Renderer r, GraphicalObject go) {
		if (go.isSelected()) {
            Rectangle rectangle = go.getBoundingBox();
            int width = rectangle.getWidth();
            int height = rectangle.getHeight();
            r.drawLine(new Point(rectangle.getX(), rectangle.getY()), new Point(rectangle.getX() + width, rectangle.getY()));
            r.drawLine(new Point(rectangle.getX() + width, rectangle.getY()), new Point(rectangle.getX() + width, rectangle.getY()+height));
            r.drawLine(new Point(rectangle.getX() + width, rectangle.getY()+height), new Point(rectangle.getX(), rectangle.getY()+height));
            r.drawLine(new Point(rectangle.getX(), rectangle.getY()+height), new Point(rectangle.getX(), rectangle.getY()));
            if (model.getSelectedObjects().size() == 1) {
                for (int i = 0; i < go.getNumberOfHotPoints(); i ++) {
                    Point hotPoint = go.getHotPoint(i);
                    int x = hotPoint.getX();
                    int y = hotPoint.getY();
                    r.drawLine(new Point(x - 2, y - 2),new Point(x + 2, y - 2));
                    r.drawLine(new Point(x + 2, y - 2), new Point(x + 2, y + 2));
                    r.drawLine(new Point(x + 2, y + 2), new Point(x - 2, y + 2));
                    r.drawLine(new Point(x - 2, y + 2), new Point(x - 2, y - 2));
                    
                }
            }
        }

		
	}
	
	public void makeCompositeObject() {
		List<GraphicalObject> objects=new ArrayList<GraphicalObject>();
		for(GraphicalObject o:model.getSelectedObjects()) {
			objects.add(o);
		}
		for(GraphicalObject o:objects)
			model.removeGraphicalObject(o);
		CompositeShape cs = new CompositeShape(objects);
		cs.setSelected(true);
		model.addGraphicalObject(cs);
	}
	
	public void destroyCompositeObject(CompositeShape cs) {
		List<GraphicalObject> objects=new ArrayList<GraphicalObject>(cs.getShapes());
		for(GraphicalObject o:objects) {
			o.setSelected(true);
			model.addGraphicalObject(o);
		}
		model.removeGraphicalObject(cs);
		
	}

	@Override
	public void afterDraw(Renderer r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLeaving() {
		for (GraphicalObject o: model.list()) {
			o.turnOffHotPoints();
			o.setSelected(false);
		}
		
	}
	

}
