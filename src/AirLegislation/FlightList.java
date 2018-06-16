package AirLegislation;

import java.util.ArrayList;

public class FlightList {

	private Avion[] acList; //List of planes
	private  String lastDv; //Timestamp for further query
	 
	public Avion[] getAcList() {
		return acList;
	}
	
	public ArrayList<Avion> getAcArrayList(){
		ArrayList<Avion> rslt = new ArrayList<Avion>();
		for(Avion a : this.acList){
			rslt.add(a);
		}
		return rslt;
	}
	public void setAcList(Avion[] acList) {
		this.acList = acList;
	}
	

	public String getLastDv() {
		return lastDv;
	}

	public void setLastDv(String lastDv) {
		this.lastDv = lastDv;
	}
}
