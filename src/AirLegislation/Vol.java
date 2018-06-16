package AirLegislation;

import java.util.Date;

import Geography.*;

public class Vol {
	//NON UTILISEE 
	
	private String name;
	private String type;
	private String id;
	private String modele;
	private float alt;
	private float speed;
	private float trak;
	private String fabric;
	private String FSeen;
	
	private Date date;
	private Aeroport from;
	private Aeroport to;
	private String hDepart;
	private String hArrive;
	private float lng;
	private float lat;
	private boolean mil;
	private Pays count;
	private String ICAO;
	
	
	public Vol(){
		
	}
	
	public Vol(Date date, Aeroport from, Aeroport to, String name){
		this.name = name;
		this.date = date;
		this.from = from;
		this.to = to;
	}
	
	public Vol(Date date, Aeroport from, Aeroport to, String hDepart, String hArrive, String name){
		this.name = name;
		this.date = date;
		this.from = from;
		this.to = to;
		this.hDepart = hDepart;
		this.hArrive = hArrive;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getModele() {
		return modele;
	}
	public void setModele(String modele) {
		this.modele = modele;
	}
	public float getAlt() {
		return alt;
	}
	public void setAlt(float alt) {
		this.alt = alt;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public float getTrak() {
		return trak;
	}
	public void setTrak(float trak) {
		this.trak = trak;
	}
	public String getFabric() {
		return fabric;
	}
	public void setFabric(String fabric) {
		this.fabric = fabric;
	}
	public String getFSeen() {
		return FSeen;
	}
	public void setFSeen(String fSeen) {
		FSeen = fSeen;
	}
	
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Aeroport getFrom() {
		return from;
	}
	public void setFrom(Aeroport from) {
		this.from = from;
	}
	public Aeroport getTo() {
		return to;
	}
	public void setTo(Aeroport to) {
		this.to = to;
	}
	public String gethDepart() {
		return hDepart;
	}
	public void sethDepart(String hDepart) {
		this.hDepart = hDepart;
	}
	public String gethArrive() {
		return hArrive;
	}
	public void sethArrive(String hArrive) {
		this.hArrive = hArrive;
	}
	public float getLng() {
		return lng;
	}
	public void setLng(float lng) {
		this.lng = lng;
	}
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	

	public boolean isMil() {
		return mil;
	}
	public void setMil(boolean mil) {
		this.mil = mil;
	}

	public Pays getCount() {
		return count;
	}

	public void setCount(Pays count) {
		this.count = count;
	}
	
	public String getICAO() {
		return ICAO;
	}

	public void setICAO(String iCAO) {
		ICAO = iCAO;
	}

}
