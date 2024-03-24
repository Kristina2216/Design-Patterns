package sestiZ;

import java.util.ArrayList;
import java.util.List;

public class Cell implements CellObserver{
	private String exp;
	private int value;
	private List<CellObserver> observers;
	private Sheet sheet;
	private String cellTag;
	
	public Cell(String exp,List<CellObserver> observers) {
		this.exp=exp;
		for(CellObserver o:observers) {
			addObserver(o);
		}
		observers=new ArrayList<CellObserver>();
	}
	
	
	public Cell(Sheet sheet, char x, char y) {
		exp="0";
		this.sheet = sheet;
		x += 65;
		y += 49;
		cellTag = x + "-" + y;
		observers=new ArrayList<CellObserver>();
	}
	
	
	public void notifyObservers() {
		for(CellObserver o:observers) {
			o.update(this);
		}
	}
	
	public void setValue(int value) {
		this.value=value;
		notifyObservers();
	}
	
	public int getValue() {
		return value;
	}
	
	public String getExp() {
		return exp;
	}
	
	public void changeExp(String exp) {
		this.exp=exp;
	}
	
	public List<CellObserver> allObservers(){
		return this.observers;
	}
	public void addObserver(CellObserver observer) {
		if(observers.contains(observer))
			return;
		observers.add(observer);
	}
	public void removeObserver(CellObserver observer) {
		observers.remove(observer);
	}
	public void addDependency(Cell other) {
		for (CellObserver o: other.allObservers()) {
			if(checkTag((Cell)o)) {
				throw new RuntimeException("Kružna ovisnost!");
			}
			addObserver(o);
		}
		addObserver((CellObserver)other);
	}
	public void removeObservers() {
		observers.clear();
	}

	@Override
	public void update(Cell cell) {
		sheet.evaluate(this);
		
	}
	
	@Override
	public String toString() {
		return cellTag;
	}
	
	public boolean checkTag(Cell other) {
		return this.cellTag.equals(other.cellTag);
	}

public static void main(String[] args) {
	
			Sheet s = new Sheet(5,5);
			
			s.set("A-1", "A-3");
			s.set("A-2", "5");
			s.set("A-3", "A-1");
			System.out.println(s);
			
			s.set("A-1","4");
			s.set("A-4","A-1+A-3");
			System.out.println(s);
			
			s.set("A-1", "A-3");
			System.out.println(s);
		
}
}
