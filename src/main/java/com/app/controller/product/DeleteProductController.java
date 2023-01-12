package com.app.controller.product;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class DeleteProductController {
    @FXML
    private Label baseText;

    @FXML
    private Button no;

    @FXML
    private Label textHelp;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setTextHelp(String string) {
        this.textHelp.setText(string);
    }

    @FXML
    private Button yes;
    boolean flag = false;
    Stage stage;

    public boolean isFlag() {

        return flag;
    }
    public void cansel()
    {
        stage.close();
    }
    @FXML
    public void flagTrue()
    {
        flag=true;
        stage.close();
    }
    @FXML
    public void flagFalse()
    {
        flag=false;
        stage.close();
    }

    public void close() {

        stage.close();
    }

}
