package AirLegislation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.javafx.collections.MappingChange.Map;

public class Avion {
	
	public String Type;
	public int Id;
	public String Mdl;
	public float Long;
	public float Lat;
	public float Alt;
	public float Spd;
	public float Trak;
	public String Man;
	public String FSeen;
	public String Icao;
	public String From;
	public String To;
	public boolean Mil;
	public String Cou; //country
	public int Year;
	
	
	public Avion(){
		
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Id:" + Integer.toString(this.Id) + "\n");
		sb.append("From:" + this.From.split(",")[0] + "\n");
		sb.append("To: "+ this.To.split(",")[0] + "\n");
		if(Mil){
			sb.append("Avion militaire \n");
		}
		else{
			sb.append("Avion non militaire \n");
		}
		sb.append("Speed: " + this.Spd + "\n");
		sb.append("Altitude: " + this.Alt + "\n");
		sb.append("ICAO:" + this.Icao + "\n");
		sb.append("Modèle:" + this.Mdl + "\n");
		sb.append("Latitude:" + this.Lat + "\n");
		sb.append("Longitude:" + this.Long + "\n");
		sb.append("Pays:" + this.Cou + "\n");
		sb.append("Année: " + Integer.toString(this.Year));
		return sb.toString();
	}
}
