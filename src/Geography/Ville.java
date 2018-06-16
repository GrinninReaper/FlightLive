package Geography;

import java.util.ArrayList;

import AirLegislation.Aeroport;

public class Ville {
	
	private String nom;
	private Pays pays;
	private float lng;
	private float lat;
	private ArrayList<Aeroport> listeAeroports;
	
	public Ville(){
		super();
	}
	
	public Ville (String nom, Pays pays){
		this.nom = nom;
		this.pays = pays;
		this.setListeAeroports(new ArrayList<Aeroport>());
	}
	
	public Ville(String nom){
		this.nom = nom;
		this.setListeAeroports(new ArrayList<Aeroport>());
	}
	
	public Ville(String nom, Pays pays, float lng, float lat){
		this.nom = nom;
		this.pays = pays;
		this.lat = lat;
		this.lng = lng;
		this.setListeAeroports(new ArrayList<Aeroport>());
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Pays getPays() {
		return pays;
	}

	public void setPays(Pays pays) {
		this.pays = pays;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLng() {
		return lng;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}

	public ArrayList<Aeroport> getListeAeroports() {
		return listeAeroports;
	}

	public void setListeAeroports(ArrayList<Aeroport> listeAeroports) {
		this.listeAeroports = listeAeroports;
	}
	
	

}
