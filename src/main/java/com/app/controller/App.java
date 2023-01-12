package com.app.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App {
    Stage stage=new Stage();

    public void root() {
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/AutorizationView.fxml"));
            stage.setTitle("Авторизация");
            stage.setScene(new Scene(loader.load()));
            AuthorizationControler authorizationControler =loader.<AuthorizationControler>getController();
            authorizationControler.setStage(stage);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

}
