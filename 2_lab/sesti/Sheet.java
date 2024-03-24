package sestiZ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sheet {
	private Cell[][] sheet;
	int x, y;
	
	
	public Sheet(int x, int y) {
		sheet = new Cell[x][y];
		for(int i = 0; i < x; i++){
			for(int z = 0; z < y; z++){
				sheet[i][z] = new Cell(this, (char)i, (char)z);
			}
		}
		this.x = x;
		this.y = y;
	}
	
	
	
	public Cell call(String ref){
		String[] parsed=ref.split("-");
		int x = parsed[0].charAt(0) - 65;
		int y = parsed[1].charAt(0) - 49;
		return sheet[x][y];	
	}
	
	public void set(String ref, String content){
		
		Cell newContent = call(ref);
		newContent.changeExp(content);
		/*List<CellObserver> beforeDeleting= newContent.allObservers();
		for(CellObserver obs : beforeDeleting){
			newContent.removeObserver(obs);
		}*/
		newContent.removeObservers();
		evaluate(newContent);
		return;
	}
	
	public void evaluate(Cell cell){
		String exp = cell.getExp();	
		try{
			Integer.parseInt(exp);
			cell.setValue(Integer.parseInt(exp));
			return;
		}catch(NumberFormatException ex) {
			List<Cell> refs = getRefs(cell);	
			int newValue = 0;
			for(Cell c : refs){
				newValue = newValue + c.getValue();
			}	
			cell.setValue(newValue);
		return;
		}
	}
	
	public List<Cell> getRefs(Cell cell){

		String exp = cell.getExp();
		List<String> refs = Arrays.asList(exp.split("\\+"));
		List<Cell> refCells = new ArrayList<>();
		for(String ref : refs){
			Cell refCell = call(ref);
			cell.addDependency(refCell);
			refCells.add(refCell);
		}
		
		return refCells;
	}
	
	public String toString(){
		String print = "";
		for(int i = 0; i < x; i++){
			for(int j = 0; j < y; j++){
				if(sheet[i][j].getExp()!="0") {
					print += "(" + i + ", " + j + ")=" + sheet[i][j].getExp() + ", " + sheet[i][j].getValue() + " {"; 
					List<CellObserver> obs=sheet[i][j].allObservers();
					for(int z=0;z<obs.size();z++) {
						if(z!=0)
							print+=", ";
						print=print+(Cell)obs.get(z); 
						}
					print+="}\n";
				} 
			}
		}
		return print;
	}
	
	
}
