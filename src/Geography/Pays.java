package Geography;

import java.util.ArrayList;

public class Pays {
	
	private String name;
	private float lat;
	private float lng;
	
	public Pays(){
		super();
	}
	
	public Pays(String name){
		this.name = name;
	}
	
	public Pays(String name, float lng, float lat){
		this.name = name;
		this.lng = lng;
		this.lat = lat;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(!(o instanceof Pays)) return false;
		else{
			return ((Pays) o).getName().equals(this.name);
		}
	}
}
