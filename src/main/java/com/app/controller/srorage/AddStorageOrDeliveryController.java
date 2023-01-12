package com.app.controller.srorage;

import com.app.DTO.StorageDTO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public  class AddStorageOrDeliveryController  implements Initializable {
    @FXML
    private Button addButton;

    @FXML
    private Label baseText;

    @FXML
    private TextField name;
    private StorageDTO storageDTO;
    private Stage stage;
    public StorageDTO getStorageDTO()
    {
        return  storageDTO;
    }

    public void setStage(Stage stage)
    {
        this.stage=stage;
    }

    public void close()
    {
        storageDTO=new StorageDTO();
        storageDTO.setName(name.getText());
        stage.close();
    }
    public void cansel()
    {
        stage.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
