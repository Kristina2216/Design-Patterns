package drugi.DeleteCommands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import drugi.EditAction;
import drugi.Location;
import drugi.TextEditorModel;
import drugi.UndoManager;

public class DeleteAfter implements AbstractCommand, EditAction{
		private TextEditorModel model;
		private List<String> textBefore=new ArrayList<String>();
		private Location cursorBefore;

	
	public DeleteAfter(TextEditorModel m) {
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
		Iterator<String> it=model.allLines();
		int counter=0;
		while(it.hasNext()) {
			if(counter==current.getX()) {
				String l=it.next();
				ret+=l.substring(0, current.getY());
				if(l.length()==current.getY()) {
					if(!it.hasNext())
						return;
					counter++;
					ret+="\n"+it.next().substring(1)+"\n";
					model.getCursorLocation().setX(current.getX()+1);
					model.getCursorLocation().setY(0);
					counter++;
					continue;
				}else {
					ret+=l.substring(current.getY()+1)+"\n";
					counter++;
					continue;
				}
			}
			ret+=it.next()+"\n";
			counter++;
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
