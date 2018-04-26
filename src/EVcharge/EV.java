package EVcharge;

public class EV{
	private int ID;
	private int Type;
	private int stHour,stMinute;
	private int endHour,endMinute;
	private int distance;
	private int[] chargingTime = new int[5];
	private int chargingPoint;
	public final int[][] Rate = {{0,  0,  0,  0,  0},
			   {0, 22, 73,  0,  0},
			   {0, 24,  0,130,  0},
			   {0, 25, 85,  0,160}};
	
	public EV(int id,int type,int startH,int startM, int stopH, int stopM,int dis){
		setUserID(id);
		setEVtype(type);
		setStartHour(startH);
		setStartMinute(startM);
		setStopHour(stopH);
		setStopMinute(stopM);
		setDistance(dis);
		for (int i=1;i<5;i++){
			chargingTime[i]=(int) (Rate[Type][i]!=0? Math.ceil(this.getDistance()*60.0 / Rate[Type][i]):0);
			if (chargingTime[i]>getTimeWindow()) chargingTime[i]=0;
		}
		setChargingPoint(0);
	}
	
	public int getUserID() {
		return ID;
	}
	public void setUserID(int userID) {
		this.ID = userID;
	}
	public String getEVtype() {
		switch (Type){
			case 1: 
				return "Nissan";
			case 2:
				return "Chev";
			case 3:
				return "Tesla";
			default :
				return "Error";
		}
	}
	public void setEVtype(int evType) {
		Type = evType;
	}
	public int getStartHour() {
		return stHour;
	}
	public void setStartHour(int startHour) {
		this.stHour = startHour;
	}
	public int getStartMinute() {
		return stMinute;
	}
	public void setStartMinute(int startMinute) {
		this.stMinute = startMinute;
	}
	public int getStopHour() {
		return endHour;
	}
	public void setStopHour(int stopHour) {
		this.endHour = stopHour;
	}
	public int getStopMinute() {
		return endMinute;
	}
	public void setStopMinute(int stopMinute) {
		this.endMinute = stopMinute;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getTimeWindow() {
		return (endHour-stHour)*60+(endMinute-stMinute);
	}
	public int getChargingPoint() {
		return chargingPoint;
	}
	public void setChargingPoint(int chargingPoint) {
		this.chargingPoint = chargingPoint;
	}	
	public int getChargingTime(int index) {
		return chargingTime[index];
	}
	public String getChargingTimes(){
		String result="";
		for (int i=1;i<5;i++){
			result+=" "+chargingTime[i];
		}
		return result;
	}
	public String toString(){
		return getUserID()+" "+getEVtype()+" "+getStartHour()+":"+getStartMinute()+" "+
				getStopHour()+":"+getStopMinute()+" "+getDistance()+getChargingTimes();
	}
}
