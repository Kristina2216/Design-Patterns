package drugi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.Iterator;

import javax.swing.JComponent;

public class WhiteArea extends JComponent{
		
		private static final long serialVersionUID = 1L;
		TextEditorModel model;
		
		public WhiteArea(TextEditorModel m) {
			model=m;
		}
	    
	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        requestFocusInWindow();
	        paintWindow(g);
	    }
	    
	    private void paintWindow(Graphics g) {
	    	if(model.hasCursor())
	    		paintCursor(g);
	    	paintText(g);
	    	paintSelected(g);
	    }
	    
	    
	    private void paintCursor(Graphics g) {
	    	Graphics2D g2d= (Graphics2D) g;
	    	FontMetrics fontMetrics = g2d.getFontMetrics();
	    	Location cursor=model.getCursorLocation();
	        String row = "";
	        if(!model.isEmpty()) {
	             row = model.getLine(cursor.getX(), cursor.getY());
	        }
	        int rowHeight = fontMetrics.getHeight();
	        int x1 = fontMetrics.stringWidth(row) + 7;
	        int y1 = (cursor.getX() + 1) * rowHeight + 3 + 3;
	        int y2 = y1 - rowHeight;
	        g2d.drawLine(x1, y1, x1, y2);
	    }
	    
	    private void paintText(Graphics g) {
	    	Graphics2D g2d= (Graphics2D) g;
	    	int maxWidth=0;
	    	FontMetrics fontMetrics = g2d.getFontMetrics();
	    	g.setFont(Font.decode(Font.MONOSPACED + " " +10 ));
	    	int height=3+fontMetrics.getHeight();
	    	Iterator<String> it=model.allLines();
	    	while (it.hasNext()) {
	    		String line=it.next();
	    		g2d.drawString(line, 7, height);
	    		height+=fontMetrics.getHeight();
	    		if((int)fontMetrics.getStringBounds(line, g2d).getWidth()>maxWidth) {
	    			maxWidth=(int)fontMetrics.getStringBounds(line, g2d).getWidth();
	    		}
	    	}
	    	Dimension d=new Dimension(maxWidth+14, height);
	    	setPreferredSize(d);
	    	
	    }
	    
	    private void paintSelected(Graphics g) {
	    	if(!model.isSelected()) return;
	    	g.setFont(Font.decode(Font.MONOSPACED + " " + 10));
	    	Location first=model.getSelectedRange().getMin();
	    	AttributedString as = new AttributedString(model.getSelectedText());
	    	String[] lines=model.getSelectedText().split("\n");
	    	if(lines.length==1) {
		    	as.addAttribute(TextAttribute.BACKGROUND, Color.BLUE);
				as.addAttribute(TextAttribute.FONT, Font.decode(Font.MONOSPACED + " " + 10));
				g.drawString(as.getIterator(), 7+first.getY() * g.getFontMetrics().charWidth(' '), 10*(first.getX()+3));
	    	}
	    	else{
	    		int i=first.getX();
	    		for(String s:lines){
	    			as = new AttributedString(s);
	    			as.addAttribute(TextAttribute.BACKGROUND, Color.BLUE);
					as.addAttribute(TextAttribute.FONT, Font.decode(Font.MONOSPACED + " " + 10));
					g.drawString(as.getIterator(), 7+first.getY() * g.getFontMetrics().charWidth(' '), 10*i+3);
					i++;
	    		}
	    	}
	    	
	    }
	    
}
