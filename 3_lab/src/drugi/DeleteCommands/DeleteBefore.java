package drugi.DeleteCommands;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import drugi.EditAction;
import drugi.Location;
import drugi.TextEditorModel;
import drugi.UndoManager;

public class DeleteBefore implements AbstractCommand, EditAction{
	private TextEditorModel model;
	private List<String> textBefore=new ArrayList<String>();
	private Location cursorBefore;

	
	public DeleteBefore(TextEditorModel m) {
		this.model=m;
		Iterator<String> it=model.allLines();
		while(it.hasNext())
			textBefore.add(it.next());
		cursorBefore=model.getCursorLocation();
	}
	
	@Override
	public void makeChanges() {
		String ret="";
		Location current=model.getCursorLocation();
		if(model.isSelected()) {
			DeleteRange dr=new DeleteRange(model);
			dr.makeChanges();
			return;
			}
		if(current.getX()==0 && current.getY()==0)
			return;
		if(current.getY()==0) {
			current.setX(current.getX()-1);
			Iterator<String> it=model.allLines();
			int counter=0;
			while(it.hasNext()) {
				if(counter==current.getX()) {
					String l=it.next();
					ret+=l.substring(0, l.length()-1)+"\n";
					model.getCursorLocation().setY(l.length()-1);
					counter++;
					continue;
				}
				ret+=it.next()+"\n";
				counter++;
			}
			
		}else {
			Iterator<String> it=model.allLines();
			int counter=0;
			while(it.hasNext()) {
				if(counter==current.getX()) {
					String l=it.next();
					ret+=l.substring(0, current.getY()-1)+l.substring(current.getY())+"\n";
					model.getCursorLocation().setY(current.getY()-1);
					counter++;
					continue;
				}
				ret+=it.next()+"\n";
				counter++;
			}
		
		}
		model.setEditedText(ret, false);
		UndoManager.getInstance().push(this);
	}
	
	@Override
	public void execute_do() {
		makeChanges();
		
	}

	@Override
	public void execute_undo() {
		Iterator<String> it=textBefore.iterator();
		String old="";
		while(it.hasNext())
			old+=it.next()+"\n";
		model.setEditedText(old, false);
		model.setCursorLocation(cursorBefore);
		
	}
	

}
