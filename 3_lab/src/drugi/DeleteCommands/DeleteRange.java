package drugi.DeleteCommands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import drugi.EditAction;
import drugi.Location;
import drugi.TextEditorModel;
import drugi.UndoManager;
import drugi.LocationRange;

public class DeleteRange implements AbstractCommand, EditAction{
	private TextEditorModel model;
	private List<String> textBefore=new ArrayList<String>();
	private Location cursorBefore;

	
	public DeleteRange(TextEditorModel m) {
		this.model=m;
		Iterator<String> it=model.allLines();
		while(it.hasNext())
			textBefore.add(it.next());
		cursorBefore=model.getCursorLocation();
	}
	
	@Override
	public void makeChanges() {
		String ret="";
		if(!model.isSelected())
			return;
		LocationRange lr = model.getSelectedRange();
		int counter=0;
		Iterator<String> it=model.allLines();
		while(it.hasNext()) {
			String line=it.next();
			if(counter==lr.getMin().getX()) {
				ret+=line.substring(0,lr.getMin().getY());
				int sameRow=counter;
				while(it.hasNext() && counter!=lr.getMax().getX()) {
					line=it.next();
					counter++;
				}
				if (counter!=sameRow) 
					ret+="\n";
				ret+=line.substring(lr.getMax().getY())+"\n";
				counter++;
				continue;
			}
			ret+=line+"\n";
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
