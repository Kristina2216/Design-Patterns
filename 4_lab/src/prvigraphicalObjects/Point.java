package prvigraphicalObjects;

public class Point {

		private int x;
		private int y;
		
		public Point(int x, int y) {
			this.x=x;
			this.y=y;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}

		public Point translate(Point dp) {
			int newx=x+dp.getX();
			int newy=y+dp.getY();
			return new Point(newx, newy);
		}
		
		public Point difference(Point p) {
			int xDistance=this.x-p.getX();
			int yDistance=this.y-p.getY();
			return new Point(xDistance, yDistance);
	}
		
		@Override
		public String toString() {
			return "("+ String.valueOf(x)+", "+String.valueOf(y)+")";
		}

}
