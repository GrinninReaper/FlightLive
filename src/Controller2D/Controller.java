package Controller2D;

import java.awt.Desktop;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Dsl;

import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import AirLegislation.Aeroport;
import AirLegislation.Avion;
import AirLegislation.FlightList;
import FlightLive.FlightLive;
import Geography.Pays;
import Geography.Ville;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;

public class Controller implements Initializable{
	
	private static final float TEXTURE_LAT_OFFSET = -0.2f;
	private static final float TEXTURE_LON_OFFSET = 2.8f;
	
	private boolean modifD = false;
	private boolean modifA = false;
	
	private SubScene aeroports = null;
	Group group2 = null;
	ArrayList<Group> listGroupAero = new ArrayList<Group>();
	ArrayList<Group> listGroupAeroA = new ArrayList<Group>();
	
	private ArrayList<Aeroport> tempDAero = new ArrayList<Aeroport>();
	private ArrayList<Aeroport> tempAAero = new ArrayList<Aeroport>();
	
	private ArrayList<Ville> tempDVille = new ArrayList<Ville>();
	private ArrayList<Ville> tempAVille = new ArrayList<Ville>();
	
	
	/*
	private ArrayList<Pays> tempDPays = new ArrayList<Pays>();
	private ArrayList<Pays> tempAPays = new ArrayList<Pays>();
 	*/
	
	@FXML
	private Pane pane3D;
	
	@FXML
	private ComboBox<String> depVille;
	
	@FXML
	private ComboBox<String> depPays;
	
	@FXML
	private ComboBox<String> depAero;
	
	@FXML
	private ComboBox<String> arrVille;
	
	@FXML
	private ComboBox<String> arrPays;
	
	@FXML
	private ComboBox<String> arrAero;
	
	@FXML
	private ComboBox<String> cmbBxID;
	
	@FXML
	private ComboBox<String> cmbBxCmpg;
	
	@FXML 
	private Button btnReglage;
	
	@FXML 
	private Button Valid;
	
	@FXML
	private ListView<String> listAvions;
	//private TableView<Avion> listColumnAvions;
	
	/*
	@FXML
	private TableColumn<Avion, String> avionIDCol;
	
	@FXML
	private TableColumn avionManCol;
	
	@FXML
	private TableColumn avionFromCol;
	
	@FXML
	private TableColumn avionToCol;
	*/
	
	@FXML
	private TextArea detailAvion;
	
	@FXML
	private Label txtInstru;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//Create a Pane et graph scene root for the 3D content
        Group root3D = new Group();
        //Pane pane3D = new Pane(root3D);
        URL modelUrl = this.getClass().getResource("Earth/earth.obj");
        ObjModelImporter objImporter = new ObjModelImporter();
    	objImporter.read(modelUrl);

        // Load geometry
		MeshView[] meshViews = objImporter.getImport();
        Group earth = new Group(meshViews);
        root3D.getChildren().add(earth);


        // Add a camera group
        PerspectiveCamera camera = new PerspectiveCamera(true);
        new CameraManager(camera, pane3D, root3D);

        // Add point light
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(-180);
        light.setTranslateY(-90);
        light.setTranslateZ(-120);
        light.getScope().addAll(root3D);
        root3D.getChildren().add(light);

        // Add ambient light
        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
        ambientLight.getScope().addAll(root3D);
        root3D.getChildren().add(ambientLight);

        // Create scene
        
        
        //pane3D = new Pane(root3D);
        
        SubScene scene = new SubScene(root3D, 755.0, 593.0, true, SceneAntialiasing.BALANCED);
        scene.setCamera(camera);
        //scene.setFill(Color.GREY);
        scene.setFill(Color.BLACK);
        pane3D.getChildren().addAll(scene);
        
        this.group2 = new Group();
        
        
        FlightLive FL = new FlightLive();
        try {
			FL.read("src/airports.csv");
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        /*
         * avionIDCol.setCellValueFactory(new PropertyValueFactory<Avion, String>("Man"));
        avionIDCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        avionManCol.setCellValueFactory(new PropertyValueFactory<>("Man"));
        avionFromCol.setCellValueFactory(new PropertyValueFactory<>("From"));
        avionToCol.setCellValueFactory(new PropertyValueFactory<>("To"));
        */
        
        
       
        initPays(FL);
        controlItemVille(FL);
        controlItemAero(FL);
        controlItemPane(FL);
        drawAero(FL,root3D);
        affDetailsAvion(FL);
        //pane3D.getChildren().add(root1);
	}
	
	public void initPays(FlightLive FL){
		ArrayList<String> listPays = new ArrayList<String>();
		for(Entry<Pays, ArrayList<Ville>> entry:FL.getHmPays().entrySet()){

				listPays.add(entry.getKey().getName());
		}
		listPays.sort(String::compareToIgnoreCase);
		ObservableList<String> list = FXCollections.observableArrayList(listPays);
		depPays.setItems(list);
		arrPays.setItems(list);
		//ArrayList<Pays> listPays = (ArrayList<Pays>) FL.getHmPays().keySet();
	}
	
	
	public void controlItemVille(FlightLive FL){
		depPays.setOnAction(event ->{
			modifD = true;
			ArrayList<String> listVille = new ArrayList<String>();
			ArrayList<Ville> arrVille = FL.getVilleDePays(depPays.getValue());
			tempDVille = arrVille;
			for (Ville v : arrVille){
				listVille.add(v.getNom());
			}
			listVille.sort(String::compareToIgnoreCase);
			ObservableList<String> obsVille = FXCollections.observableArrayList(listVille);
			depVille.setItems(obsVille);
			txtInstru.setText("Veuillez choisir une ville de départ");
		});
		
		arrPays.setOnAction(event ->{
			modifA = true;
			ArrayList<String> listVille = new ArrayList<String>();
			ArrayList<Ville> arrayVille = FL.getVilleDePays(arrPays.getValue());
			tempAVille = arrayVille;
			for (Ville v : arrayVille){
				listVille.add(v.getNom());
			}
			listVille.sort(String::compareToIgnoreCase);
			ObservableList<String> obsVille = FXCollections.observableArrayList(listVille);
			arrVille.setItems(obsVille);
			txtInstru.setText("Veuillez choisir une ville d'arrivée");
		});
	}

	public void controlItemAero(FlightLive FL){
		depVille.setOnAction(event ->{
			modifD= true;
			ArrayList<Aeroport> listAero = FL.getAeroportDeVille(depVille.getValue(), depPays.getValue());
			this.tempDAero = listAero;
			ArrayList<String> arrayAero = new ArrayList<String>();
			for(Aeroport aero : listAero){
				arrayAero.add(aero.getName());
			}
			arrayAero.sort(String::compareToIgnoreCase);
			ObservableList<String> obsAero = FXCollections.observableArrayList(arrayAero);
			depAero.setItems(obsAero);
			txtInstru.setText("Veuillez choisir un aéroport de départ");
		});
		
		arrVille.setOnAction(event ->{
			modifA = true;
			ArrayList<Aeroport> listAero = FL.getAeroportDeVille(arrVille.getValue(), arrPays.getValue());
			this.tempAAero = listAero;
			ArrayList<String> arrayAero = new ArrayList<String>();
			for(Aeroport aero : listAero){
				arrayAero.add(aero.getName());
			}
			arrayAero.sort(String::compareToIgnoreCase);
			ObservableList<String> obsAero = FXCollections.observableArrayList(arrayAero);
			arrAero.setItems(obsAero);
			txtInstru.setText("Veuillez choisir un aéroport d'arrivée");
		});
	}
	
	public void controlItemPane(FlightLive FL) {
		depAero.setOnAction(event ->{
			modifD= true;
		});
		arrAero.setOnAction(event ->{
			modifA= true;
		});
	}
	
	public void drawAero(FlightLive FL, Group root){
		Valid.setOnAction(event ->{
			/*
			if(this.listGroupAero.size() < 1){
				modifD = true;
			}
			if(this.listGroupAeroA.size() < 1){
				modifA = true;
			}*/
			
			try {
				addListView(FL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(modifD){
				if(this.listGroupAero.size() >= 2){
					drawOnEarth(FL, root);
					if(root.getChildren().remove(this.listGroupAero.get(this.listGroupAero.size()-2))){
						System.out.print("Removed");
					}
					//root.getChildren().remove(this.listGroupAero.get(this.listGroupAero.size()-2));
					
					root.getChildren().add(this.listGroupAero.get(this.listGroupAero.size()-1));
					System.out.println("Aff 1D");
				}
				else if (this.listGroupAero.size() ==1){
					drawOnEarth(FL, root);
					root.getChildren().remove(this.listGroupAero.get(0));
					root.getChildren().add(this.listGroupAero.get(1));
					System.out.println("Aff 2D");
				}
				else if(this.listGroupAero.size() < 1 && depPays.getValue() != null){
					drawOnEarth(FL, root);
					root.getChildren().add(this.listGroupAero.get(0));
					System.out.println("Aff 3D");
				}
				modifD = false;
			}
			
			if(modifA){
				if(this.listGroupAeroA.size() >= 2){
					drawOnEarthA(FL, root);
					if(root.getChildren().remove(this.listGroupAeroA.get(this.listGroupAeroA.size()-2))){
						System.out.print("Removed");
					}
					//root.getChildren().remove(this.listGroupAero.get(this.listGroupAero.size()-2));
					
					root.getChildren().add(this.listGroupAeroA.get(this.listGroupAeroA.size()-1));
					System.out.println("Aff 1A");
				}
				else if (this.listGroupAeroA.size() == 1){
					drawOnEarthA(FL, root);
					root.getChildren().remove(this.listGroupAeroA.get(0));
					root.getChildren().add(this.listGroupAeroA.get(1));
					System.out.println("Aff 2A");
				}
				else if(this.listGroupAeroA.size() < 1 && arrPays.getValue() != null){
					drawOnEarthA(FL, root);
					root.getChildren().add(this.listGroupAeroA.get(0));
					System.out.println("Aff 3A");
				}
				modifA = false;
			}
		});
	}
	
	public static Point3D geoCoordTo3dCoord(float lat, float lon) {
        float lat_cor = lat + TEXTURE_LAT_OFFSET;
        float lon_cor = lon + TEXTURE_LON_OFFSET;
        return new Point3D(
                -java.lang.Math.sin(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)),
                -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor)),
                java.lang.Math.cos(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)));
    }
    
    public void displayAero(Group  parent,String name, float latitude, float longitude, PhongMaterial color){
    	Point3D newCoord = geoCoordTo3dCoord(latitude, longitude);
    	Sphere sphere = new Sphere(0.005);
    	Group group3 = new Group();
    	sphere.setMaterial(color);
    	sphere.setId(name);
    	group3.setTranslateX(newCoord.getX());
    	group3.setTranslateY(newCoord.getY());
    	group3.setTranslateZ(newCoord.getZ());
    	group3.getChildren().add(sphere);
    	//Group group3 = new Group();
    	//group3 = group2;
    	//this.aeroports = new SubScene(group2, 755.0, 593.0, true, SceneAntialiasing.BALANCED);
    	//this.listGroupAero.add(group2);
    	parent.getChildren().add(group3);
    }
    
    private void drawOnEarth(FlightLive FL, Group root) {
    	Group groupSearch = new Group();
    	
    	final PhongMaterial depMaterial = new PhongMaterial();
        depMaterial.setDiffuseColor(Color.BLUE);
        depMaterial.setSpecularColor(Color.BLUE);
        
       
        
		if(depPays.getValue()!=null){
			if(depVille.getValue() != null){
				if(depAero.getValue() != null){
					
					for(Aeroport aero:tempDAero){
						if(aero.getName().equals(depAero.getValue())){
							displayAero(groupSearch, aero.getName(), (float) aero.getLat(), (float)aero.getLng(), depMaterial);
							//modifD = true;
						}
					}
				}
				else{
					for(Ville v:tempDVille) {
						if(depVille.getValue().equals(v.getNom())) {
							for(Aeroport aero:v.getListeAeroports()) {
								displayAero(groupSearch, aero.getName(), (float) aero.getLat(), (float)aero.getLng(), depMaterial);
								//modifD = true;
							}
						}
					}
				}
			}
			else{
				for(Ville v : FL.getVilleDePays(depPays.getValue())) {
					for(Aeroport aero:v.getListeAeroports()) {
						displayAero(groupSearch, aero.getName(), (float) aero.getLat(), (float)aero.getLng(), depMaterial);
						//modifD = true;
					}
				}
			}
		}
		if(modifD){
			this.listGroupAero.add(groupSearch);
		}
    }
    
    private void drawOnEarthA(FlightLive FL, Group root){
    	Group groupSearchA = new Group();
    	
    	 final PhongMaterial arrMaterial = new PhongMaterial();
         arrMaterial.setDiffuseColor(Color.RED);
         arrMaterial.setSpecularColor(Color.RED);
    	
    	if(arrPays.getValue()!=null){
			if(arrVille.getValue() != null){
				if(arrAero.getValue() != null){
					for(Aeroport aero:tempAAero){
						if(aero.getName().equals(arrAero.getValue())){
							displayAero(groupSearchA, aero.getName(), (float) aero.getLat(), (float)aero.getLng(), arrMaterial);
							//modifA = true;
						}
					}
				}
				else{
					for(Ville v:tempAVille) {
						if(arrVille.getValue().equals(v.getNom())) {
							for(Aeroport aero:v.getListeAeroports()) {
								displayAero(groupSearchA, aero.getName(), (float) aero.getLat(), (float)aero.getLng(), arrMaterial);
								//modifA = true;
							}
						}
					}
				}
			}
			else{
				for(Ville v : FL.getVilleDePays(arrPays.getValue())) {
					for(Aeroport aero:v.getListeAeroports()) {
						displayAero(groupSearchA, aero.getName(), (float) aero.getLat(), (float)aero.getLng(), arrMaterial);
						//modifA = true;
					}
				}
			}
		}
		
		if(modifA){
			this.listGroupAeroA.add(groupSearchA);
		}
    }
    
    private void addListView(FlightLive FL) throws InterruptedException{
    	ArrayList<Avion> list = new ArrayList<Avion>();
    	ArrayList<Avion> depList = new ArrayList<Avion>();
    	ArrayList<Avion> arrList = new ArrayList<Avion>();
    	ArrayList<String> rslt = new ArrayList<String>();
    	if(depAero.getValue() != null && arrAero.getValue() != null){
    		DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
  				  .setConnectTimeout(500)
  				  .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
  				  .setKeepAlive(false);
	  		AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);
	  		
	  		Aeroport dep= new Aeroport();
	  		Aeroport arr= new Aeroport();
	  		
	  		for(Aeroport deptemp:tempDAero){
	      		if(depAero.getValue().equals(deptemp.getName())){
	      			dep = deptemp;
	      		}
	  		}
	  		
	
	  		for(Aeroport arrtemp:tempAAero){
	      		if(arrAero.getValue().equals(arrtemp.getName())){
	      			arr = arrtemp;
	      		}
	  		}
	
	  		//Créer une requête de type GET
	  		String acURL = FL.getAC_URL() + "?fAirQ=" + dep.getCodeICAO();
	  		System.out.println(acURL + "----------------------");
	  		BoundRequestBuilder getRequest = client.prepareGet(acURL);
	  		
	  		final Aeroport depF = dep;
	  		final Aeroport arrF = arr;
	  		
	  		//Exécuter la requête et récupérer le résultat
	  		getRequest.execute(new AsyncCompletionHandler<Object>() {
	  			@Override
	  			public Object onCompleted(org.asynchttpclient.Response response) throws Exception {
	  				// TODO Auto-generated method stub
	  				//System.out.println(response.getResponseBody());
	  		    	//Traiter la réponse
	  				ObjectMapper mapper = new ObjectMapper();
	  				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); //Ignorer les champs inutiles
	  				FlightList flightList =FL.getFlights() ; //Créer l'objet de plus haut niveau dans le dictionnaire json
	  				flightList = mapper.readValue(response.getResponseBody(), FlightList.class);
	  				for(Avion a : flightList.getAcList()){
	  					//System.out.println(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 1");
	
	  					if(a.From.split(" ")[0].equals(depF.getCodeICAO()) && a.To.split(" ")[0].equals(arrF.getCodeICAO())/*a.Id == 11393572*/){
	  						//for(Avion a:list){
	  						//System.out.println(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 2");
	  						
	  							StringBuilder sb = new StringBuilder();
	  							sb.append(a.Icao);
	  							sb.append("From:" + a.From);
	  							sb.append("To" + a.To);
	  							
	  							rslt.add("Id: " + a.Icao + " Manufacturer: " + a.Man + " From: " + depVille.getValue() + " To: " + arrVille.getValue());
	  							
	  							//System.out.println(sb.toString());
	  						//}
	  						
	  						ObservableList<String> listViewItems = FXCollections.observableArrayList(rslt);
	  						listAvions.setItems(listViewItems);
	  				    }
	  					
	  				}
	  				/*
	  				ObservableList<Avion> tableViewItems = FXCollections.observableArrayList(flightList.getAcList());
					listColumnAvions.setItems(tableViewItems);
					//listColumnAvions.getColumns().addAll(avionIDCol, avionManCol, avionFromCol, avionToCol);
					*/
	  				
	  				if(flightList.getAcList().length == 0) {
  	  					rslt.add("No result found");
  	  					ObservableList<String> listViewItems = FXCollections.observableArrayList(rslt);
						listAvions.setItems(listViewItems);
  	  				}
  	  				
	  		        return response;
	  			}
	  		});
    	}
    	else if(depAero.getValue() != null && arrAero.getValue() == null){
    		DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
    				  .setConnectTimeout(500)
    				  .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
    				  .setKeepAlive(false);
  	  		AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);
  	  		
  	  		Aeroport dep= new Aeroport();
  	  		
  	  		for(Aeroport deptemp:tempDAero){
  	      		if(depAero.getValue().equals(deptemp.getName())){
  	      			dep = deptemp;
  	      		}
  	  		}
  	
  	  		//Créer une requête de type GET
  	  		String acURL = FL.getAC_URL() + "?fAirQ=" + dep.getCodeICAO();
  	  		System.out.println(acURL + "----------------------");
  	  		BoundRequestBuilder getRequest = client.prepareGet(acURL);
  	  		
  	  		final Aeroport depF = dep;
  	  		
  	  		//Exécuter la requête et récupérer le résultat
  	  		getRequest.execute(new AsyncCompletionHandler<Object>() {
  	  			@Override
  	  			public Object onCompleted(org.asynchttpclient.Response response) throws Exception {
  	  				// TODO Auto-generated method stub
  	  				//System.out.println(response.getResponseBody());
  	  		    	//Traiter la réponse
  	  				ObjectMapper mapper = new ObjectMapper();
  	  				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); //Ignorer les champs inutiles
  	  				FlightList flightList =FL.getFlights() ; //Créer l'objet de plus haut niveau dans le dictionnaire json
  	  				flightList = mapper.readValue(response.getResponseBody(), FlightList.class);
  	  				for(Avion a : flightList.getAcList()){
  	  					//System.out.println(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 1");
  	
  	  					if(a.From.split(" ")[0].equals(depF.getCodeICAO())/*a.Id == 11393572*/){
  	  						//for(Avion a:list){
  	  						//System.out.println(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 2");
  	  						
  	  							StringBuilder sb = new StringBuilder();
  	  							sb.append(a.Icao);
  	  							sb.append("From:" + a.From);
  	  							sb.append("To" + a.To);
  	  							
  	  							rslt.add("Id: " + a.Icao + " Manufacturer: " + a.Man + " From: " + depVille.getValue() + " To: " + a.To.split(",")[1]);
  	  							//System.out.println(sb.toString());
  	  						//}
  	  						
  	  					
  	  						ObservableList<String> listViewItems = FXCollections.observableArrayList(rslt);
  	  						listAvions.setItems(listViewItems);
  	  						
  	  						
	  						 
  	  				    }
  	  					
  	  				}
  	  				/*
  	  				ObservableList<Avion> tableViewItems = FXCollections.observableArrayList(flightList.getAcList());
					listColumnAvions.setItems(tableViewItems);	
					//listColumnAvions.getColumns().addAll(avionIDCol, avionManCol, avionFromCol, avionToCol);
  	  				*/
  	  				if(flightList.getAcList().length == 0) {
	  					rslt.add("No result found");
	  					ObservableList<String> listViewItems = FXCollections.observableArrayList(rslt);
	  					listAvions.setItems(listViewItems);
	  				}
	  				
  	  		        return response;
  	  			}
  	  		});
    	}
    	
    	else if(depAero.getValue() == null && arrAero.getValue() != null){
    		DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
    				  .setConnectTimeout(500)
    				  .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
    				  .setKeepAlive(false);
  	  		AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);
  	  		
  	  		Aeroport arr= new Aeroport();
  	  		
  	
  	  		for(Aeroport arrtemp:tempAAero){
  	      		if(arrAero.getValue().equals(arrtemp.getName())){
  	      			arr = arrtemp;
  	      		}
  	  		}
  	
  	  		//Créer une requête de type GET
  	  		String acURL = FL.getAC_URL() + "?fAirQ=" + arr.getCodeICAO();
  	  		System.out.println(acURL + "----------------------");
  	  		BoundRequestBuilder getRequest = client.prepareGet(acURL);

  	  		
  	  		final Aeroport arrF = arr;
  	  		
  	  		//Exécuter la requête et récupérer le résultat
  	  		getRequest.execute(new AsyncCompletionHandler<Object>() {
  	  			@Override
  	  			public Object onCompleted(org.asynchttpclient.Response response) throws Exception {
  	  				// TODO Auto-generated method stub
  	  				//System.out.println(response.getResponseBody());
  	  		    	//Traiter la réponse
  	  				ObjectMapper mapper = new ObjectMapper();
  	  				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); //Ignorer les champs inutiles
  	  				FlightList flightList =FL.getFlights() ; //Créer l'objet de plus haut niveau dans le dictionnaire json
  	  				flightList = mapper.readValue(response.getResponseBody(), FlightList.class);
  	  				for(Avion a : flightList.getAcList()){
  	  					//System.out.println(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 1");
  	
  	  					if(a.To.split(" ")[0].equals(arrF.getCodeICAO())/*a.Id == 11393572*/){
  	  						//for(Avion a:list){
  	  						//System.out.println(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 2");
  	  						
  	  							StringBuilder sb = new StringBuilder();
  	  							sb.append(a.Icao);
  	  							sb.append("From:" + a.From);
  	  							sb.append("To" + a.To);
  	  							
  	  							rslt.add("Id: " + a.Icao + " Manufacturer: " + a.Man + " From: " + a.From.split(",")[1] + " To: " + arrVille.getValue());
  	  							//System.out.println(sb.toString());
  	  						//}
  	  						
  	  						ObservableList<String> listViewItems = FXCollections.observableArrayList(rslt);
  	  						listAvions.setItems(listViewItems);
  	  						
  	  						
  	  						
  	  						 
  	  				    }
  	  				}
  	  				/*
  	  				ObservableList<Avion> tableViewItems = FXCollections.observableArrayList(flightList.getAcList());
					listColumnAvions.setItems(tableViewItems);
					//listColumnAvions.getColumns().addAll(avionIDCol, avionManCol, avionFromCol, avionToCol);
  	  				*/
  	  				if(flightList.getAcList().length == 0) {
  	  					rslt.add("No result found");
  	  					ObservableList<String> listViewItems = FXCollections.observableArrayList(rslt);
						listAvions.setItems(listViewItems);
  	  				}
  	  				
  	  		        return response;
  	  			}
  	  		});
    	}
    }
	
	public void affDetailsAvion(FlightLive FL){
		
		listAvions.setOnMouseClicked(new EventHandler<MouseEvent>() {

		    @Override
		    public void handle(MouseEvent click) {

		        if (click.getClickCount() == 2) {
		        	
		           String selected = listAvions.getSelectionModel().getSelectedItem();
		           System.out.println("Double click detected");
		           selected = selected.split(" ")[1];
		          
		           DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
							  .setConnectTimeout(500)
							  .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
							  .setKeepAlive(false);
					AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);
		
					//Créer une requête de type GET
					String acURL = FL.getAC_URL() + "?fIcoQ=" + selected;
					//System.out.println(acURL + "----------------------");
					BoundRequestBuilder getRequest = client.prepareGet(acURL);
					//ArrayList<Avion> rslt = new ArrayList<Avion>();
					
					//Exécuter la requête et récupérer le résultat
					getRequest.execute(new AsyncCompletionHandler<Object>() {
					@Override
					public Object onCompleted(org.asynchttpclient.Response response) throws Exception {
					// TODO Auto-generated method stub
					//System.out.println(response.getResponseBody());
			    	//Traiter la réponse
					ObjectMapper mapper = new ObjectMapper();
					mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); //Ignorer les champs inutiles
					FlightList flightList = FL.getFlights();
					flightList = mapper.readValue(response.getResponseBody(), FlightList.class); //Créer l'objet de plus haut niveau dans le dictionnaire json
					
					detailAvion.setText(flightList.getAcArrayList().get(0).toString());
			        return response;
				}
			});
		        }
		    }
		});
		
	}
	
		/*list = FL.avionTotal(dep, arr);
		Thread.sleep(2000);
		System.out.println("------------------------------------------------------Searching" + Integer.toString(list.size()));
		*/
		
}
