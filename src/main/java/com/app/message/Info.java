package com.app.message;

import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Info extends Message {
    // Конструктор
    public Info(Stage primaryStage, String text) {
        super(primaryStage, "", text);
    }

    // Конструктор с заголовком
    public Info(Stage primaryStage, String header, String text) {
        super(primaryStage, header, text);
    }
}
