package com.example.mypuzzl.client;

import com.example.mypuzzl.HelloApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
//			System.out.println(primaryStage);
//            Parent root = FXMLLoader.load(Main.class.getResource("/settings.fxml"));

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("settings.fxml"));
//            Parent root = FXMLLoader.load(Main.class.getResource("/com/example/mypuzzl/client/settings.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
//            Scene scene = new Scene(root);
            scene.getStylesheets().add(HelloApplication.class.getResource("application.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
            primaryStage.setTitle(" \"Пазл\" ");
            primaryStage.show();
//			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//		          public void handle(WindowEvent we) {
//		              System.out.println("Stage is closing");
//		              Platform.exit();
//		          }
//		    });        

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws InterruptedException {
        System.out.println("Stop called: try to let background threads complete...");
        System.out.println("Stage is closing");
        Platform.exit();
        System.exit(0);
    }

}
