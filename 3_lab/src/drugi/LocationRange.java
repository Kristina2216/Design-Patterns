package drugi;

public class LocationRange {
		private Location min;
		private Location max;
		
		public LocationRange(Location min, Location max) {
			this.min=min;
			this.max=max;
		}
		
		public boolean checkLocation(Location a) {
			if(a.getX()<min.getX() || a.getX()>max.getX() || a.getY()>max.getY() || a.getY()<min.getY())
				return false;
			return true;
		}
		
		public Location getMin() {
			return min;
		}
		
		public Location getMax() {
			return max;
		}
		
		@Override
		public String toString() {
			return "("+min.getX()+", "+min.getY()+") - ("+max.getX()+", "+max.getY()+")";
		}
}
