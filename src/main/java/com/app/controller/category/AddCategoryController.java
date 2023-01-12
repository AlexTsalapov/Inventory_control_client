package com.app.controller.category;

import com.app.DTO.CategoryDTO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddCategoryController {
    @FXML
    private Button addButton;
    @FXML
    private Label baseText;

    @FXML
    private TextField name;

    CategoryDTO categoryDTO;
    boolean flag=false;

    public boolean isFlag() {
        return flag;
    }

    Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;

    }
    public String getName()
    {
        return name.getText();
    }
    public void cansel()
    {
        stage.close();
    }
    public void close()
    {
        flag=true;
       categoryDTO=new CategoryDTO();
       categoryDTO.setName(name.getText());
       stage.close();
    }

}
