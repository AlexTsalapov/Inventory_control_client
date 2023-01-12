package com.app.controller.category;

import com.app.DTO.CategoryDTO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditCategoryController implements Initializable {

    @FXML
    private Label baseText;

    public TextField getName() {
        return name;
    }
    boolean flag=false;

    @FXML
    private Button editButton;
    @FXML
    private ComboBox<String> boxItems;

    @FXML
    private TextField name;
    CategoryDTO categoryDTO=new CategoryDTO();
    Stage stage=new Stage();

    public CategoryDTO getCategoryDTO() {
        return categoryDTO;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean isFlag() {
        return flag;
    }
    public void cansel()
    {
        stage.close();
    }

    public void filingBox(ComboBox<String> menu)
    {
        for (int i = 1; i < menu.getItems().size(); i++) {
            boxItems.getItems().add(menu.getItems().get(i));
        }

    }
    public void close() {
        flag=true;
        categoryDTO = new CategoryDTO();
        categoryDTO.setName(boxItems.getValue());
        stage.close();
    }






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
