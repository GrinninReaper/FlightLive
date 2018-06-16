package AirLegislation;


import Geography.*;

public class Aeroport implements Cloneable {
	
	private String name;
	private double lng;
	private double lat;
	private String codeICAO;
	private Ville ville;
	
	
	
	public Aeroport(){
		
	}
	
	public Aeroport(String name){
		this.name = name;
	}
	
	public Aeroport(String name, double lng, double lat, Ville ville){
		this.name = name;
		this.lng = lng;
		this.lat = lat;
		this.ville = ville;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public String getCodeICAO() {
		return codeICAO;
	}

	public void setCodeICAO(String codeICAO) {
		this.codeICAO = codeICAO;
	}

	public Ville getVille() {
		return ville;
	}
	public void setVille(Ville ville) {
		this.ville = ville;
	}


	public Aeroport clone() throws CloneNotSupportedException{
		Aeroport aeroport = null;
		aeroport = (Aeroport) super.clone();
		return aeroport;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Name of the aeroport: " + name);
		sb.append(" Name of the city: " + ville.getNom());
		sb.append(" Name of the country: " + ville.getPays().getName());
		sb.append(" ICAO code: " + codeICAO);
		sb.append(" Latitude: " + lat);
		sb.append(" Longitude: " + lng);
		return sb.toString();
	}

}
