package drugi.plugins;


import java.util.Iterator;

import javax.swing.JOptionPane;

import drugi.ClipboardStack;
import drugi.TextEditorModel;
import drugi.UndoManager;

public class StatisticsPlugin implements Plugin{

	@Override
	public String getName() {
		  return "StatisticsPlugin";
	}

	@Override
	public String getDescription() {
		return "Counts number of words, rows, columns, etc.";
	}

	@Override
	public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
		        Iterator<String> it = model.allLines();
		        int lineNum = model.rowCount();
		        int wordNum = 0;
		        int charNum = 0;
		        while (it.hasNext()) {
		            String line = it.next();
		            wordNum += line.split("\\s+").length;
		            charNum += line.length();
		        }

		        String message = "Number of lines: " + lineNum+ "\nNumber of words: " + wordNum + "\nNumber of characters: " + charNum;
		        JOptionPane.showMessageDialog(null, message, "StatisticsPlugin",
		                JOptionPane.INFORMATION_MESSAGE);
		    }
}
