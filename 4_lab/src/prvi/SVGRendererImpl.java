package prvi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import prvigraphicalObjects.Point;

public class SVGRendererImpl implements Renderer {

	private List<String> lines = new ArrayList<String>();
	private String fileName;
	
	public SVGRendererImpl(String fileName) {
		// zapamti fileName; u lines dodaj zaglavlje SVG dokumenta:
		// <svg xmlns=... >
		this.fileName=fileName;
		 lines.add("<svg xmlns=\"http://www.w3.org/2000/svg\"" +
	                " xmlns:xlink=\"http://www.w3.org/1999/xlink\">");

	}

	public void close() throws IOException {
		// u lines još dodaj završni tag SVG dokumenta: </svg>
		// sve retke u listi lines zapiši na disk u datoteku
		lines.add("</svg>");
        Files.write(Paths.get(fileName), lines);

	}
	
	@Override
	public void drawLine(Point s, Point e) {
		lines.add("<line x1=\"" + s.getX() + "\"  y1=\"" + s.getY()
        + "\" x2=\"" + e.getX() + "\" y2=\"" + e.getY()
        + "\" style=\"stroke:#0000ff;\"/>");

	}

	@Override
	public void fillPolygon(Point[] points) {
		// Dodaj u lines redak koji definira popunjeni poligon:
		// <polygon points="..." style="stroke: ...; fill: ...;" />
		String toWrite="<polygon points=\"";
		for (Point p:points) {
			toWrite+=p.getX()+","+p.getY()+" ";
		}
		toWrite=toWrite.substring(0, toWrite.length()-1);
		toWrite+="\" style=\"stroke:#ff0000; fill:#0000ff;\"/>";
		 lines.add(toWrite);
	}

}
