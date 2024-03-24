package drugi.fileActions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.AbstractAction;
//import javax.swing.JFileChooser;

import drugi.TextEditorModel;



public class OpenAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	TextEditorModel m;
	
	public OpenAction(TextEditorModel m) {
		this.m=m;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		/*JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Open file");
		if(fc.showOpenDialog(this)!=JFileChooser.APPROVE_OPTION) {
			return;
		}
		File fileName = fc.getSelectedFile();
		Path filePath = fileName.toPath();
		loadDocument(filePath);
	}
	public void loadDocument(Path path) {
		if(path==null) throw new NullPointerException("Path cannot be null!");
		if(!Files.isReadable(path)) {
			throw new RuntimeException("Incorrect path!");
		}
		
		byte[] okteti;
		try {
			okteti = Files.readAllBytes(path);
		} catch(Exception ex) {
			throw new RuntimeException("File not readable!");
		}
		String tekst = new String(okteti, StandardCharsets.UTF_8);
		model.setEditedText(tekst, false);
		return;*/
	}


}
