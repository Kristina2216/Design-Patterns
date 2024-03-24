package drugi.plugins;

import java.util.Iterator;

import javax.swing.JOptionPane;

import drugi.ClipboardStack;
import drugi.TextEditorModel;
import drugi.UndoManager;

public class CapitalLetterPlugin implements Plugin{
	@Override
	public String getName() {
		  return "CaptialLetterPlugin";
	}

	@Override
	public String getDescription() {
		return "Capitalizes first letter of every word.";
	}

	@Override
	public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
		        Iterator<String> it = model.allLines();
		        String newLines="";
		        while (it.hasNext()) {
		            String line = it.next();
		            String[] words= line.split("\\s+");
		            for(int i=0;i<words.length;i++) {
		            	newLines+=words[i].substring(0, 1).toUpperCase() + words[i].substring(1) +" ";
		            }
		            newLines+="\n";
		        }

		       model.setEditedText(newLines, false);
		    }
	

}
