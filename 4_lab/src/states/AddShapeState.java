package states;



import prvi.DocumentModel;
import prvi.Renderer;
import prvigraphicalObjects.GraphicalObject;
import prvigraphicalObjects.Point;

public class AddShapeState implements State {
	
	private GraphicalObject prototype;
	private DocumentModel model;
	
	public AddShapeState(DocumentModel model, GraphicalObject prototype) {
		this.model=model;
		this.prototype=prototype;
	}

	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		GraphicalObject duplicate=prototype.duplicate();
		duplicate.translate(mousePoint);
	    model.addGraphicalObject(duplicate);
		
	}

	@Override
	public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(Point mousePoint) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLeaving() {
		// TODO Auto-generated method stub
		
	}

	
}