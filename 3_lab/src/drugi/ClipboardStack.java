package drugi;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ClipboardStack {
	 Stack<String> texts=new Stack<String>();
	 List<ClipboardObserver> observers=new ArrayList<ClipboardObserver>();
	 
	 public String peek(){
		 return texts.peek();
	 }
	 
	 
	public void push(String text){
			texts.push(text);
		}
		
	public String pop(){
		return texts.pop();
	}
	
	public boolean isEmpty(){
		return texts.isEmpty();
	}
	
	public void addClipboardObserver(ClipboardObserver o){
		observers.add(o);
	}
	
	public void removeClipboardObserver(ClipboardObserver o){
		observers.remove(o);
	}
}
