package states;

import java.util.ArrayList;
import java.util.List;

import prvi.DocumentModel;
import prvi.GUI.Canvas;
import prvi.Renderer;
import prvigraphicalObjects.GraphicalObject;
import prvigraphicalObjects.Point;

public class EraserState implements State{

	 private DocumentModel model;
	 private Canvas canvas;
	 private List<Point> points = new ArrayList<Point>();

	 public EraserState(DocumentModel m, Canvas c) {
		 model=m;
		 canvas=c;
	 }
	 
	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        for (Point p : points) {
            GraphicalObject object = model.findSelectedGraphicalObject(p);
            if (object != null)
            	model.removeGraphicalObject(object);
        }
        canvas.repaint();
        points.clear();


		
	}

	@Override
	public void mouseDragged(Point mousePoint) {
		points.add(mousePoint);
		
	}

	@Override
	public void keyPressed(int keyCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterDraw(Renderer r, GraphicalObject go) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterDraw(Renderer r) {
			

		
	}

	@Override
	public void onLeaving() {
		// TODO Auto-generated method stub
		
	}
	

}
