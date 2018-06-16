package FlightLive;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.ws.Response;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Dsl;
//import org.asynchttpclient.Response;

import AirLegislation.*;
import Geography.*;

public class FlightLive {
	private static ArrayList<Aeroport> aeroports = new ArrayList<Aeroport>();
	private static FlightList flights; 
	private static Vol[] listVol = null;
	//FlightList flights = mapper.readValue(json, FlightList.class); 
	private static HashMap<Pays, ArrayList<Ville>> hmPays = new HashMap<Pays, ArrayList<Ville>>();
	private final String AC_URL = "https://public-api.adsbexchange.com/VirtualRadar/AircraftList.json";
	private boolean isReady = false;
	private int nb = 0;

	public void requestGetVol(String acURL){
		//Configurer le client http
		DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
						  .setConnectTimeout(500)
						  .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
						  .setKeepAlive(false);
		AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);

		//Créer une requête de type GET
		BoundRequestBuilder getRequest = client.prepareGet(acURL);

		
		//Exécuter la requête et récupérer le résultat
		getRequest.execute(new AsyncCompletionHandler<Object>() {
			@Override
			public Object onCompleted(org.asynchttpclient.Response response) throws Exception {
				// TODO Auto-generated method stub
				//System.out.println(response.getResponseBody());
		    	//Traiter la réponse
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); //Ignorer les champs inutiles
				flights = mapper.readValue(response.getResponseBody(), FlightList.class); //Créer l'objet de plus haut niveau dans le dictionnaire json
				for(Avion a : flights.getAcList()){
					//rslt.add(a);
					//System.out.println(a.Long);
				}
		        return response;
			}
		});
		
		//return rslt;
	}
	
	
	public FlightLive(){
		
	}

	public ArrayList<Aeroport> getAeroports() {
		return aeroports;
	}

	public void setAeroports(ArrayList<Aeroport> aeroports) {
		this.aeroports = aeroports;
	}

	public static Vol[] getListVol() {
		return listVol;
	}

	public static void setListVol(Vol[] listVol) {
		FlightLive.listVol = listVol;
	}
	
	public static HashMap<Pays, ArrayList<Ville>> getHmPays() {
		return hmPays;
	}


	public static void setHmPays(HashMap<Pays, ArrayList<Ville>> hmPays) {
		FlightLive.hmPays = hmPays;
	}


	public String getAC_URL() {
		return AC_URL;
	}


	public int getNb() {
		return nb;
	}


	public void setNb(int nb) {
		this.nb = nb;
	}

	public static FlightList getFlights() {
		return flights;
	}


	public void addNb(){
		this.nb++;
	}

	/**
	 * fonctionnalité 1
	 * @param filepath
	 * @throws CloneNotSupportedException
	 */
	public static void read(String filepath) throws CloneNotSupportedException{
		Aeroport aero = new Aeroport();
		
		try {
			FileReader file = new FileReader(filepath);
			BufferedReader bufRead = new BufferedReader(file);

			String line = bufRead.readLine();
			while ( line != null) {
			   	String[] array = line.split(",");
			   	
			   	aero.setName(array[0]);
			   	String nomVille = array[1];
			   	
			   	if(nomVille.length() < 1)
			   		nomVille = "No-city's land";
			   	
			    aero.setVille(new Ville(nomVille,new Pays(array[2])));
			    aero.setCodeICAO(array[3]);
			    aero.setLat(Double.parseDouble(array[4]));
			    aero.setLng(Double.parseDouble(array[5]));
			    
			    aeroports.add(aero.clone());
			    
			   
			    
			    Pays pa = new Pays(array[2]);

			    if(hmPays.isEmpty()){
			    	ArrayList<Ville> arV = new ArrayList<Ville>();
			    	arV.add(new Ville(nomVille));
			    	hmPays.put(pa, arV);
			    }
			    else{
			    	boolean couFound = false;
				    for(Pays p:hmPays.keySet()){
				    	//System.out.println(pa.toString());
					    if(p.equals(pa)){
					    	couFound = true;
					    	Ville v = new Ville(nomVille);

					    	boolean villeFound = false;
					    	for(Ville ville : hmPays.get(p)){
					    		if(ville.getNom().equals(nomVille)){
					    			villeFound = true;
					    			ville.getListeAeroports().add(aero.clone());
						    		//hmPays.get(p).add(v);
					    		}
					    	}
				    		if(!villeFound){
					    		v.getListeAeroports().add(aero.clone());
					    		hmPays.get(p).add(v);
					    	}
					    }
				    }
				    if(!couFound){
					    	ArrayList<Ville> arrayV = new ArrayList<Ville>();
					    	Ville v = new Ville(nomVille);
					    	v.getListeAeroports().add(aero.clone());
					    	arrayV.add(v);
					      	hmPays.put(pa, arrayV);
				    }
			    }
			    line = bufRead.readLine();
			}

			bufRead.close();
			file.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * fonctionnalité 2 partie 1
	 * @param pays
	 * @return
	 */
	public ArrayList<Ville> getVilleDePays(String pays){
		for(Pays p : hmPays.keySet()){
			if(p.getName().equals(pays)){
				return hmPays.get(p);
			}
		}
		return null;
	}
	
	/**
	 * Fonctionnalité 2 partie 2
	 * @param ville
	 * @param pays
	 * @return
	 */
	public ArrayList<Aeroport> getAeroportDeVille(String ville, String pays){
		for(Pays pa : hmPays.keySet()){
			if(pays.equals(pa.getName())){
				for(Ville v : hmPays.get(pa)){
					if(v.getNom().equals(ville)){
						return v.getListeAeroports();
					}
				}
			}
		}
		return null;
	}
	
	/*
	public ArrayList<Avion> avionFrom(Aeroport aero){
		this.avionComplet(aero);
		ArrayList<Avion> rslt = new ArrayList<Avion>();
		for(int i =0 ; i < 10 ; i++){
			Avion a = flights.getAcList()[i];
			String from = a.From.split(" ")[0];
			if(from.equals(aero.getCodeICAO())){
				rslt.add(a);
				System.out.println(a.toString());
			}
		}
		return rslt;
	}*/
	
	public ArrayList<Avion> avionComplet(Aeroport aero){
		//Configurer le client http
		DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
						  .setConnectTimeout(500)
						  .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
						  .setKeepAlive(false);
		AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);

		//Créer une requête de type GET
		String acURL = this.getAC_URL() + "?fAirQ=" + aero.getCodeICAO();
		System.out.println(acURL + "----------------------");
		BoundRequestBuilder getRequest = client.prepareGet(acURL);
		ArrayList<Avion> rslt = new ArrayList<Avion>();
		
		//Exécuter la requête et récupérer le résultat
		getRequest.execute(new AsyncCompletionHandler<Object>() {
			@Override
			public Object onCompleted(org.asynchttpclient.Response response) throws Exception {
				// TODO Auto-generated method stub
				//System.out.println(response.getResponseBody());
		    	//Traiter la réponse
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); //Ignorer les champs inutiles
				flights = mapper.readValue(response.getResponseBody(), FlightList.class); //Créer l'objet de plus haut niveau dans le dictionnaire json
				
				for(Avion a : flights.getAcList()){
					rslt.add(a);
					System.out.println(a.Id);
				}
		        return response;
			}
		});
		return rslt;
	}
	
	public ArrayList<Avion> avionFrom(Aeroport aero){
		//Configurer le client http
		DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
						  .setConnectTimeout(500)
						  .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
						  .setKeepAlive(false);
		AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);

		//Créer une requête de type GET
		String acURL = this.getAC_URL() + "?fAirQ=" + aero.getCodeICAO();
		System.out.println(acURL + "----------------------");
		BoundRequestBuilder getRequest = client.prepareGet(acURL);
		ArrayList<Avion> rslt = new ArrayList<Avion>();
		
		//Exécuter la requête et récupérer le résultat
		getRequest.execute(new AsyncCompletionHandler<Object>() {
			@Override
			public Object onCompleted(org.asynchttpclient.Response response) throws Exception {
				// TODO Auto-generated method stub
				//System.out.println(response.getResponseBody());
		    	//Traiter la réponse
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); //Ignorer les champs inutiles
				flights = mapper.readValue(response.getResponseBody(), FlightList.class); //Créer l'objet de plus haut niveau dans le dictionnaire json
				
				for(Avion a : flights.getAcList()){
					if(a.From.split(" ")[0].equals(aero.getCodeICAO())){
						rslt.add(a);
						//System.out.println(a.Id);
					}
				}
		        return response;
			}
		});
		return rslt;
	}
	
	
	public ArrayList<Avion> avionTo(Aeroport aero){
		//Configurer le client http
		DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
						  .setConnectTimeout(500)
						  .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
						  .setKeepAlive(false);
		AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);

		//Créer une requête de type GET
		String acURL = this.getAC_URL() + "?fAirQ=" + aero.getCodeICAO();
		System.out.println(acURL + "----------------------");
		BoundRequestBuilder getRequest = client.prepareGet(acURL);
		ArrayList<Avion> rslt = new ArrayList<Avion>();
		
		//Exécuter la requête et récupérer le résultat
		getRequest.execute(new AsyncCompletionHandler<Object>() {
			@Override
			public Object onCompleted(org.asynchttpclient.Response response) throws Exception {
				// TODO Auto-generated method stub
				//System.out.println(response.getResponseBody());
		    	//Traiter la réponse
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); //Ignorer les champs inutiles
				flights = mapper.readValue(response.getResponseBody(), FlightList.class); //Créer l'objet de plus haut niveau dans le dictionnaire json
				
				for(Avion a : flights.getAcList()){
					if(a.To.split(" ")[0].equals(aero.getCodeICAO())){
						rslt.add(a);
						//System.out.println(a.Id);
					}
				}
				//toStringVol(flights.getAcArrayList());
		        return response;
			}
		});
		return rslt;
	}
	
	public ArrayList<Avion> avionTotal(Aeroport dep, Aeroport arr){
		ArrayList<Avion> depList = this.avionFrom(dep);
		ArrayList<Avion> arrList = this.avionTo(arr);
		ArrayList<Avion> rslt = new ArrayList<Avion>();
		for(Avion a:depList){
			if(arrList.contains(a)){
				rslt.add(a);
			}
		}
		return rslt;
	}
	
	public void toStringVol(ArrayList<Avion> listAvion){
		for(Avion a : listAvion)
			System.out.println(a.toString());
	}
	
	public ArrayList<Avion> villeComplet(String ville){
		setNb(0);
		//Configurer le client http
		DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
						  .setConnectTimeout(500)
						  .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
						  .setKeepAlive(false);
		AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);

		//Créer une requête de type GET
		//String acURL = this.getAC_URL() + "?fAirQ=" + aero.getCodeICAO();
		//System.out.println(acURL + "----------------------");
		
		ArrayList<Avion> rslt = new ArrayList<Avion>();
		ArrayList<Aeroport> src = new ArrayList<Aeroport>();
		
		for(Aeroport a : this.aeroports){
			if(a.getVille().getNom().equals(ville)){
				src.add(a);
			}
		}
		
		for(Aeroport aero : src){
			String acURL = this.getAC_URL() + "?fAirQ=" + aero.getCodeICAO();
			
			BoundRequestBuilder getRequest = client.prepareGet(acURL);
			//Exécuter la requête et récupérer le résultat
			getRequest.execute(new AsyncCompletionHandler<Object>() {
				@Override
				public Object onCompleted(org.asynchttpclient.Response response) throws Exception {
					// TODO Auto-generated method stub
					//System.out.println(response.getResponseBody());
			    	//Traiter la réponse
					ObjectMapper mapper = new ObjectMapper();
					mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); //Ignorer les champs inutiles
					flights = mapper.readValue(response.getResponseBody(), FlightList.class); //Créer l'objet de plus haut niveau dans le dictionnaire json
					
					for(Avion a : flights.getAcList()){
						rslt.add(a);
					}
					//toStringVol(flights.getAcArrayList());
					addNb();
			        return response;
				}
			});
		}
		return rslt;
	}
	
	public ArrayList<Avion> villeFrom(String ville){
		setNb(0);
		//Configurer le client http
		DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
						  .setConnectTimeout(500)
						  .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
						  .setKeepAlive(false);
		AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);

		//Créer une requête de type GET
		//String acURL = this.getAC_URL() + "?fAirQ=" + aero.getCodeICAO();
		//System.out.println(acURL + "----------------------");
		
		ArrayList<Avion> rslt = new ArrayList<Avion>();
		ArrayList<Aeroport> src = new ArrayList<Aeroport>();
		
		for(Aeroport a : this.aeroports){
			if(a.getVille().getNom().equals(ville)){
				src.add(a);
			}
		}
		
		for(Aeroport aero : src){
			String acURL = this.getAC_URL() + "?fAirQ=" + aero.getCodeICAO();
			
			BoundRequestBuilder getRequest = client.prepareGet(acURL);
			//Exécuter la requête et récupérer le résultat
			getRequest.execute(new AsyncCompletionHandler<Object>() {
				@Override
				public Object onCompleted(org.asynchttpclient.Response response) throws Exception {
					// TODO Auto-generated method stub
					//System.out.println(response.getResponseBody());
			    	//Traiter la réponse
					ObjectMapper mapper = new ObjectMapper();
					mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); //Ignorer les champs inutiles
					flights = mapper.readValue(response.getResponseBody(), FlightList.class); //Créer l'objet de plus haut niveau dans le dictionnaire json			
					for(Avion a : flights.getAcList()){
						if(a.From.split(" ")[0].equals(aero.getCodeICAO())){
							rslt.add(a);
							//System.out.println(a.Id);
						}
					}
					toStringVol(rslt);
					addNb();
			        return response;
				}
			});
		}
		return rslt;
	}
	
	public ArrayList<Avion> villeTo(String ville){
		setNb(0);
		//Configurer le client http
		DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
						  .setConnectTimeout(500)
						  .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
						  .setKeepAlive(false);
		AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);

		//Créer une requête de type GET
		//String acURL = this.getAC_URL() + "?fAirQ=" + aero.getCodeICAO();
		//System.out.println(acURL + "----------------------");
		
		ArrayList<Avion> rslt = new ArrayList<Avion>();
		ArrayList<Aeroport> src = new ArrayList<Aeroport>();
		
		for(Aeroport a : this.aeroports){
			if(a.getVille().getNom().equals(ville)){
				src.add(a);
			}
		}
		
		for(Aeroport aero : src){
			String acURL = this.getAC_URL() + "?fAirQ=" + aero.getCodeICAO();
			
			BoundRequestBuilder getRequest = client.prepareGet(acURL);
			//Exécuter la requête et récupérer le résultat
			getRequest.execute(new AsyncCompletionHandler<Object>() {
				@Override
				public Object onCompleted(org.asynchttpclient.Response response) throws Exception {
					// TODO Auto-generated method stub
					//System.out.println(response.getResponseBody());
			    	//Traiter la réponse
					ObjectMapper mapper = new ObjectMapper();
					mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); //Ignorer les champs inutiles
					flights = mapper.readValue(response.getResponseBody(), FlightList.class); //Créer l'objet de plus haut niveau dans le dictionnaire json			
					for(Avion a : flights.getAcList()){
						if(a.To.split(" ")[0].equals(aero.getCodeICAO())){
							rslt.add(a);
							//System.out.println(a.Id);
						}
					}
					toStringVol(rslt);
					addNb();
			        return response;
				}
			});
		}
		return rslt;
	}
	
	
	public static void main(String[] args){
		
	}
	

		
		

}
