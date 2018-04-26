package EVcharge;

public class schedule {
	private int chSerial;
	private int startPoint;
	private int totalInterval;
	
	public schedule(int id, int point, int interval){
		setChSerial(id);
		setStartPoint(point);
		setTotalInterval(interval);
	}
	public int getChSerial() {
		return chSerial;
	}
	public void setChSerial(int ID) {
		this.chSerial = ID;
	}
	public int getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(int start) {
		this.startPoint = start;
	}
	public int getTotalInterval() {
		return totalInterval;
	}
	public void setTotalInterval(int totalInterval) {
		this.totalInterval = totalInterval;
	}
}
