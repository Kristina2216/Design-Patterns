package drugi;

import javax.swing.JLabel;

public class StatusBar extends JLabel implements CursorObserver {
	  private TextEditorModel model;

	    public StatusBar(TextEditorModel m) {
	        model = m;
	        model.addCursorObserver(this);
	        setHorizontalAlignment(RIGHT);
	        updateStatus();
	    }

	    @Override
	    public void updateCursorLocation(Location location) {
	        updateStatus();
	    }

	    private void updateStatus() {
	        Location cursor = model.getCursorLocation();
	        if(cursor==null) {
	        	cursor=new Location(0,0);
	        }
	        setText("Ln: " + (cursor.getX()+1)
	                + "     Col: " + (cursor.getY()+1)
	                + "      lines: " + model.rowCount() + "  ");
	    }

}
