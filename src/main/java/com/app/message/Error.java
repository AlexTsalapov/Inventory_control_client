package com.app.message;

import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Error extends Message {
    private final String code;  // код ошибки


    // Конструктор неизвестной ошибки
    public Error(Stage primaryStage, String code) {
        super(primaryStage, "Неизвестная ошибка", "Текст ошибки: " + code + ".");
        this.code = code;
    }

    // Конструктор
    public Error(Stage primaryStage, String header, String code) {
        super(primaryStage, header, "Текст ошибки: " + code + ".");
        this.code = code;
    }
}
