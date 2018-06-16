package Parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import AirLegislation.Aeroport;
import FlightLive.FlightLive;
import Geography.*;

public class CSVReader {
	
	public void read(String filepath){
		Aeroport aero = new Aeroport();
		
		try {
			FileReader file = new FileReader(filepath);
			BufferedReader bufRead = new BufferedReader(file);

			String line = bufRead.readLine();
			while ( line != null) {
			   	String[] array = line.split(",");
			   
			    //int id = Integer.parseInt(array[0]);
			   	aero.setName(array[0]);
			    //String name = array[0];
			    aero.setVille(new Ville(array[1]));
			    //aero.setPays(new Pays(array[2]));
			    aero.setCodeICAO(array[3]);
			    aero.setLat(Double.parseDouble(array[4]));
			    aero.setLng(Double.parseDouble(array[5]));
			    
			    System.out.println(aero.toString());
			        		
			    line = bufRead.readLine();
			}

			bufRead.close();
			file.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
