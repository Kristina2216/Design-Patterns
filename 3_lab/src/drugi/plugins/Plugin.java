package drugi.plugins;

import drugi.ClipboardStack;
import drugi.TextEditorModel;
import drugi.UndoManager;

public interface Plugin {
	String getName(); // ime plugina (za izbornicku stavku)
	 String getDescription(); // kratki opis
	 void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack);
}
