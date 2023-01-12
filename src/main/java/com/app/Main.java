package com.app;

import com.app.controller.App;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        App appControler=new App();
        appControler.root();


    }
}
