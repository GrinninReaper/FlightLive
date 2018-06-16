package Controller2D;

import java.io.IOException;
import java.net.URL;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Interface extends Application{

	private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;

    @Override
    public void start(Stage primaryStage) throws IOException {

       
		
        try{
        	Parent content = FXMLLoader.load(getClass().getResource("Interface2D.fxml"));
        	
        	primaryStage.setMinHeight(625.0);
        	primaryStage.setMinWidth(959.0);
        	//Add the scene to the stage and show it
            primaryStage.setTitle("Earth Test");
            primaryStage.setScene(new Scene(content));
            primaryStage.show();
        }
        catch(ImportException e){
        	//handle excepton
        	System.out.println(e.getMessage());
        }
        
        
    }

    public static void main(String[] args) {
        launch(args);
    }


    // From Rahel Lüthy : https://netzwerg.ch/blog/2015/03/22/javafx-3d-line/
    public Cylinder createLine(Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(0.01f, height);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
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
    
    public void displayTown(Group parent, String name, float latitude, float longitude){
    	Point3D newCoord = geoCoordTo3dCoord(latitude, longitude);
    	Sphere sphere = new Sphere(0.005);
    	sphere.setId(name);
    	Group group2 = new Group();
    	group2.setTranslateX(newCoord.getX());
    	group2.setTranslateY(newCoord.getY());
    	group2.setTranslateZ(newCoord.getZ());
    	group2.getChildren().add(sphere);
    	
    	parent.getChildren().add(group2);
    }
    
}
