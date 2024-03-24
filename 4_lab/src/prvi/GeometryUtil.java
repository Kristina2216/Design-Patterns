package prvi;

import prvigraphicalObjects.Point;

public class GeometryUtil {

	public static double distanceFromPoint(Point point1, Point point2) {
		return Math. sqrt((point2.getX()-point1.getX())*(point2.getX()-point1.getX()) + (point2.getY()-point1.getY())*(point2.getY()-point1.getY()));
	}
	
	public static double distanceFromLineSegment(Point s, Point e, Point p) {
		float px=e.getX()-s.getX();
        float py=e.getY()-s.getY();
        float temp=(px*px)+(py*py);
        float u=((p.getX() - s.getX()) * px + (p.getY() - s.getY()) * py) / (temp);
        if(u>1){
            u=1;
        }
        else if(u<0){
            u=0;
        }
        float x = s.getX() + u * px;
        float y = s.getY() + u * py;

        float dx = x - p.getX();
        float dy = y - p.getY();
        double dist = Math.sqrt(dx*dx + dy*dy);
        return dist;
	}
	
}
