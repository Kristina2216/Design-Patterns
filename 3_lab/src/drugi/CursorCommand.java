package drugi;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class CursorCommand extends AbstractAction{
	private static final long serialVersionUID = 1L;
	public long SerialVersionUID;
	public String key;
	public String description;
	
	public CursorCommand
	(String key, String description) {
		this.key=key;
		this.description=description;
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}
	


}
