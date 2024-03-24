package drugi;

import java.util.Stack;

public class UndoManager {
	Stack<EditAction> undoStack=new Stack<EditAction>();
	Stack<EditAction> redoStack=new Stack<EditAction>();
	 private static UndoManager instance = new UndoManager();
	
	
	public void undo() {
		if(undoStack.isEmpty())
			return;
		EditAction action=undoStack.pop();
		action.execute_undo();
		redoStack.push(action);
	}
	
	public void push(EditAction c) {
		redoStack.removeAllElements();
		undoStack.push(c);
	}
	public static UndoManager getInstance() {
        return instance;
    }
	
	public void repeat() {
		if(!redoStack.isEmpty()) {
			EditAction a=redoStack.pop();
			a.execute_do();
			undoStack.push(a);
		}
	}
}
