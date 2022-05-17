package com.albertmercade;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Load application View from FXML file
        Parent root = FXMLLoader.load(getClass().getResource("fxml/MainView.fxml"));
        Scene scene = new Scene(root);
        // Link CSS stylesheet
        scene.getStylesheets().add(getClass().getResource("css/styles.css").toExternalForm());

        stage.setTitle("Password Generator");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
