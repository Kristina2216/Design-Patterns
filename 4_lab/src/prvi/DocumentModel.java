package prvi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import prvigraphicalObjects.GraphicalObject;
import prvigraphicalObjects.GraphicalObjectListener;
import prvigraphicalObjects.Point;

public class DocumentModel {
	public final static double SELECTION_PROXIMITY = 10;

	// Kolekcija svih grafi�kih objekata:
	private List<GraphicalObject> objects = new ArrayList<GraphicalObject>();
	// Read-Only proxy oko kolekcije grafi�kih objekata:
	private List<GraphicalObject> roObjects = Collections.unmodifiableList(objects);
	// Kolekcija prijavljenih promatra�a:
	private List<DocumentModelListener> listeners = new ArrayList<DocumentModelListener>();
	// Kolekcija selektiranih objekata:
	private List<GraphicalObject> selectedObjects = new ArrayList<GraphicalObject>();
	// Read-Only proxy oko kolekcije selektiranih objekata:
	private List<GraphicalObject> roSelectedObjects = Collections.unmodifiableList(selectedObjects);

	// Promatra� koji �e biti registriran nad svim objektima crte�a...
	private final GraphicalObjectListener goListener = new GraphicalObjectListener() {
		@Override
		public void graphicalObjectSelectionChanged(GraphicalObject go) {
			int index = selectedObjects.indexOf(go);
			if(go.isSelected()) {
				if(index == -1)
					selectedObjects.add(go);
				else return;
			} else {
				if(index != -1) 
					selectedObjects.remove(index);
				else return;
			}
			notifyListeners();
		}

		@Override
		public void graphicalObjectChanged(GraphicalObject go) {
			notifyListeners();
		}
	};
	
	public DocumentModel() {}

	// Brisanje svih objekata iz modela (pazite da se sve potrebno odregistrira)
	// i potom obavijeste svi promatra�i modela
	public void clear() {
		for (GraphicalObject o : objects) {
			o.removeGraphicalObjectListener(goListener);
		}
		objects.clear();
		selectedObjects.clear();
		roObjects=Collections.unmodifiableList(objects);
		roSelectedObjects = Collections.unmodifiableList(selectedObjects);
		notifyListeners();
	}

	// Dodavanje objekta u dokument (pazite je li ve� selektiran; registrirajte model kao promatra�a)
	public void addGraphicalObject(GraphicalObject obj) {
		obj.addGraphicalObjectListener(goListener);
		objects.add(obj);
		roObjects = Collections.unmodifiableList(objects);
		if(obj.isSelected()) {
			selectedObjects.add(obj);
			roSelectedObjects=Collections.unmodifiableList(selectedObjects);
		}
		notifyListeners();
	}
	
	// Uklanjanje objekta iz dokumenta (pazite je li ve� selektiran; odregistrirajte model kao promatra�a)
	public void removeGraphicalObject(GraphicalObject obj) {
		obj.setSelected(false);
		obj.removeGraphicalObjectListener(goListener);
		objects.remove(obj);
		roObjects = Collections.unmodifiableList(objects);
		selectedObjects.remove(obj);
		roSelectedObjects=Collections.unmodifiableList(selectedObjects);
		notifyListeners();
	}

	// Vrati nepromjenjivu listu postoje�ih objekata (izmjene smiju i�i samo kroz metode modela)
	public List<GraphicalObject> list() {
		return roObjects;
	}

	// Prijava...
	public void addDocumentModelListener(DocumentModelListener l) {
		listeners.add(l);
	}
	
	// Odjava...
	public void removeDocumentModelListener(DocumentModelListener l) {
		listeners.remove(l);
	}

	// Obavje�tavanje...
	public void notifyListeners() {
		for (DocumentModelListener documentModelListener : listeners) {
			documentModelListener.documentChanged();
		}
	}
	
	// Vrati nepromjenjivu listu selektiranih objekata
	public List<GraphicalObject> getSelectedObjects() {
		return roSelectedObjects;
	}

	// Pomakni predani objekt u listi objekata na jedno mjesto kasnije...
	// Time �e se on iscrtati kasnije (pa �e time mo�da ve�i dio biti vidljiv)
	public void increaseZ(GraphicalObject go) {
		int index = objects.indexOf(go);
		if(index != -1 && index!=objects.size()-1 ) {
			GraphicalObject temp=objects.get(index + 1);
			objects.set(index + 1, go);
			objects.set(index, temp);
		}
		roObjects = Collections.unmodifiableList(objects);
		notifyListeners();
	}
	
	// Pomakni predani objekt u listi objekata na jedno mjesto ranije...
	public void decreaseZ(GraphicalObject go) {
		int index = objects.indexOf(go);
		if(index > 0 ) {
			GraphicalObject temp=objects.get(index - 1);
			objects.set(index - 1, go);
			objects.set(index, temp);
		}
		roObjects = Collections.unmodifiableList(objects);
		notifyListeners();
	}
	
	// Prona�i postoji li u modelu neki objekt koji klik na to�ku koja je
	// predana kao argument selektira i vrati ga ili vrati null. To�ka selektira
	// objekt kojemu je najbli�a uz uvjet da ta udaljenost nije ve�a od
	// SELECTION_PROXIMITY. Status selektiranosti objekta ova metoda NE dira.
	public GraphicalObject findSelectedGraphicalObject(Point mousePoint) {
		double min=100;
		GraphicalObject closest=null;
		for (GraphicalObject go : objects) {
			double distance = go.selectionDistance(mousePoint);
			if(distance<min) {
				min=distance;
				closest=go;
			}
		}
		if(min<10)
			return closest;
		else return null;
	}

	// Prona�i da li u predanom objektu predana to�ka mi�a selektira neki hot-point.
	// To�ka mi�a selektira onaj hot-point objekta kojemu je najbli�a uz uvjet da ta
	// udaljenost nije ve�a od SELECTION_PROXIMITY. Vra�a se indeks hot-pointa 
	// kojeg bi predana to�ka selektirala ili -1 ako takve nema. Status selekcije 
	// se pri tome NE dira.
	public int findSelectedHotPoint(GraphicalObject object, Point mousePoint) {
		double min=100;
		int returnIndex=0;
		for (int i = 0; i < object.getNumberOfHotPoints(); i++) {
			double distance=object.getHotPointDistance(i, mousePoint);
			if(distance <= 10 && distance<min) {
				min=distance;
				returnIndex=i;
			}
		}
		if (min<10) return returnIndex;
		return -1;
	}
}
