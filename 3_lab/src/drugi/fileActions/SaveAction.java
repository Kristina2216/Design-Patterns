package drugi.fileActions;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.swing.AbstractAction;

import drugi.TextEditorModel;

public class SaveAction extends AbstractAction{
		TextEditorModel m;
		
		public SaveAction(TextEditorModel m) {
			this.m=m;
		}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	
	}

}
