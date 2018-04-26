package EVcharge;

public class ChPoint{
	private int ID;
	private int Type;
	private int[] timeSlot;
	
	public ChPoint(int id, int type){
		setID(id);
		setChargingType(type);
		timeSlot = new int[1440];
		for (int i=0;i<1440;i++){
			setTimeSlot(i,0);
		}
	}	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getChargingType() {
		switch (Type){
		case 1: 
			return "Level2";
		case 2:
			return "CHAdeMO";
		case 3:
			return "CCS";
		case 4:	
			return "SuperCharger";
		default :
			return "Error";
		}
	}
	public int getChargingTypeID() {
		return Type;
	}
	public void setChargingType(int chargingType) {
		this.Type = chargingType;
	}
	public int getTimeSlot(int index) {
		return timeSlot[index];
	}
	public void setTimeSlot(int index, int id) {
		this.timeSlot[index] = id;
	}
	public String toString(){
		return getID()+" "+getChargingType();
	}
}
