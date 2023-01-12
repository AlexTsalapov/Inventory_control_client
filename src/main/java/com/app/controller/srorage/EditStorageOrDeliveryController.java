package com.app.controller.srorage;

import com.app.DTO.StorageDTO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class EditStorageOrDeliveryController extends StorageAndDelivery implements Initializable {
    @FXML
    private Label baseText;

    @FXML
    private Button editButton;
    @FXML
    private ComboBox<String> boxItems;

    @FXML
    private TextField name;
    public void close() {
        storageDTO = new StorageDTO();
        choose = boxItems.getValue();
        stage.close();
    }
    public StorageDTO findStorage()
    {
        for (int i = 0; i < listStorage.size(); i++) {
            if(listStorage.get(i).getName().equals(choose))
            {
                storageDTO=listStorage.get(i);
                storageDTO.setName(name.getText());
                return storageDTO;
            }

        }
        return null;
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

