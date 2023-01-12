package com.app.controller.category;

import com.app.DTO.CategoryDTO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteCategoryController   implements Initializable {

    @FXML
    private Label baseText;


    @FXML
    protected ComboBox<String> boxItems=new ComboBox<>();

    @FXML
    private Button editButton;
    private  boolean flag=false;
    public boolean isFlag() {
        return flag;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    CategoryDTO categoryDTO=new CategoryDTO();
    Stage stage=new Stage();



    public void close() {
        flag=true;
        categoryDTO = new CategoryDTO();
        categoryDTO.setName(boxItems.getValue());
        stage.close();
    }

    public CategoryDTO getCategoryDTO() {
        return categoryDTO;
    }

    public void filingBox(ComboBox<String> menu)
    {
        for (int i = 1; i < menu.getItems().size(); i++) {
            boxItems.getItems().add(menu.getItems().get(i));
        }

    }
    public void cansel()
    {
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
