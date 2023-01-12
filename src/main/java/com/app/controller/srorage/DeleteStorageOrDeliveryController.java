package com.app.controller.srorage;

import com.app.DTO.StorageDTO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteStorageOrDeliveryController extends StorageAndDelivery implements Initializable {
    @FXML
    private Label baseText;

    @FXML
    private ComboBox<String> boxItems;

    @FXML
    private Button editButton;


    public void close() {
        storageDTO = new StorageDTO();
        choose = boxItems.getValue();
        stage.close();
    }
    public void cansel()
    {
        stage.close();
    }

    public void filingBox() {
        boxItems.getItems().addAll(list);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
