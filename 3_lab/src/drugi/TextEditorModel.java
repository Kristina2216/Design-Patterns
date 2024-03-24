package drugi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TextEditorModel {
	private List<String> lines=new ArrayList<String>();
	private LocationRange selectionRange;
	private Location cursorLocation;
	private List<CursorObserver> cursorObservers = new ArrayList<>();
	private List<TextObserver> textObservers = new ArrayList<>();
	public LocationRange range;
	
	public TextEditorModel(String text) {
		String[] array=text.split("\n");
		int maxY=0;
		for(int i=0;i<array.length;i++) {
			lines.add(array[i]);
			if(array[i].length()>maxY)
				maxY=array[i].length();
		}
		range=new LocationRange(new Location(0,0), new Location(lines.size()-1,maxY-1));
	}
	
	public void addCursorObserver(CursorObserver o) {
		cursorObservers.add(o);
	}
	
	public void removeCursorObserver(CursorObserver o) {
		cursorObservers.remove(o);
	}
	
	public boolean hasCursor() {
		return cursorLocation!=null;
	}
	
	public void initCursor(){
		cursorLocation=new Location(0,0);
	}
	
	public Location moveCursorLeft() {
		Location test=new Location(cursorLocation.getX(), cursorLocation.getY()-1);
		if(!range.checkLocation(test))
			return cursorLocation;
		cursorLocation.setY(cursorLocation.getY()-1);
		updateCursorObservers();
		return cursorLocation;
	}
	
	public Location moveCursorRight() {
		Location test=new Location(cursorLocation.getX(), cursorLocation.getY()+1);
		if(!range.checkLocation(test))
			return cursorLocation;
		cursorLocation.setY(cursorLocation.getY()+1);
		updateCursorObservers();
		return cursorLocation;
	}
	
	public Location moveCursorUp() {
		Location test=new Location(cursorLocation.getX()-1, cursorLocation.getY());
		if(!range.checkLocation(test))
			return cursorLocation;
		cursorLocation.setX(cursorLocation.getX()-1);
		updateCursorObservers();
		return cursorLocation;
	}
	
	public Location moveCursorDown() {
		Location test=new Location(cursorLocation.getX()+1, cursorLocation.getY());
		if(!range.checkLocation(test))
			return cursorLocation;
		cursorLocation.setX(cursorLocation.getX()+1);
		updateCursorObservers();
		return cursorLocation;
	}
	
	public void setCursorLocation(Location l) {
		cursorLocation=l;
		updateCursorObservers();
	}
	
	public void updateCursorObservers() {
		for(CursorObserver o:cursorObservers)
			o.updateCursorLocation(cursorLocation);
	}
	
	public void updateTextObservers() {
		for(TextObserver o:textObservers)
			o.updateText();
	}
	
	public Location getCursorLocation() {
		return cursorLocation;
	}
	
	
	public boolean isEmpty() {
		return this.lines.size()==0;
	}
	
	public String getLine(int x, int y) {
		String line=lines.get(x);
		if (y>=line.length())
			return line;
		return line.substring(0,y);
	}
	
	public Iterator<String> allLines(){
		return lines.iterator();
	}
	public Iterator<String> linesRange(int index1, int index2) {
		return lines.subList(index1 - 1, index2 - 1).iterator();
	}
	
	public void addTextObserver(TextObserver o) {
		this.textObservers.add(o);
	}
	public void removeTextObserver(TextObserver o) {
		this.textObservers.remove(o);
	}
	
	public LocationRange getSelectedRange() {
		return selectionRange;
	}
	
	public void setSelectedRangeBegin(Location b) {
		if(selectionRange==null) {
			selectionRange=new LocationRange(new Location(b.getX(), b.getY()), new Location(b.getX(), b.getY()));
		}
		selectionRange.getMin().setX(b.getX());
		selectionRange.getMin().setY(b.getY());
	}
	
	public void setSelectedRangeEnd(Location b) {
		selectionRange.getMax().setX(b.getX());
		selectionRange.getMax().setY(b.getY());
	}
	
	public boolean isSelected() {
		return selectionRange!=null;
	}
	
	public void setEditedText(String newText, boolean stringInsert) {
		lines.clear();
		String[] array=newText.split("\n");
		int maxY=0;
		for(int i=0;i<array.length;i++) {
			lines.add(array[i]);
			if(array[i].length()>maxY)
				maxY=array[i].length();
		}
		range=new LocationRange(new Location(0,0), new Location(lines.size()-1,maxY-1));
		selectionRange=null;
		if(!stringInsert)
			updateTextObservers();
	}
	
	public void insert(char c, boolean string) {
		Iterator<String> it=lines.iterator();
		int counter=0;
		String ret="";
		while(it.hasNext()) {
			String line=it.next();
			if(counter==cursorLocation.getX()) {
				ret+=line.substring(0,cursorLocation.getY());
				ret+=c+"";
				ret+=line.substring(cursorLocation.getY())+"\n";
				counter++;
				continue;
			}
			ret+=line+"\n";
			counter++;
		}
		cursorLocation.reallocate(0,1);
		setEditedText(ret, string);
		
	}
	
	public void insert(String s) {
		for (int i = 0; i < s.length(); i++){
		    char c = s.charAt(i); 
		    insert(c, true);
		}
		updateTextObservers();
	}
	
	public String getSelectedText() {
		String ret="";
		if(!isSelected())
			return null;
		int counter=0;
		Iterator<String> it=lines.iterator();
		while(it.hasNext()) {
			String line=it.next();
			if(counter==selectionRange.getMin().getX()) {
				if(selectionRange.getMin().getX()==selectionRange.getMax().getX()) {
					ret+=line.substring(selectionRange.getMin().getY(),selectionRange.getMax().getY());
					return ret;
					}
				ret=ret+line.substring(selectionRange.getMin().getY());
				while(it.hasNext() && counter!=selectionRange.getMax().getX()) {
					line=it.next();
					ret+="\n"+line;
					counter++;
				}
				ret+="\n"+line.substring(0, selectionRange.getMax().getY())+"\n";
				counter++;
				return ret;
			}
			counter++;
		}
		return ret;
	}
	
	public void setSelectedRange(LocationRange l) {
		selectionRange=l;
		updateTextObservers();
	}
	public int rowCount() {
		return lines.size();
	}
}
