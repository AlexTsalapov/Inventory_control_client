package com.app.controller.product;

import com.app.DTO.ProductDTO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private TextField amount;

    @FXML
    private Label baseText;

    @FXML
    private ComboBox<String> category;

    @FXML
    private TextField dateOfExpiration;

    @FXML
    private TextField dateOfManufacture;

    @FXML
    private TextField name;

    @FXML
    private TextField note;

    @FXML
    private TextField price;

    ProductDTO productDTO;
    Stage stage;
    ArrayList<String> categorys=new ArrayList<>();

    public void setCategorys(ComboBox<String> menu)
    {
        for (int i = 1; i <menu.getItems().size() ; i++) {
            category.getItems().add(menu.getItems().get(i));
        }

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ProductDTO getProductDTO() {
        return productDTO;
    }
    public void cansel()
    {
        stage.close();
    }

    public void close() {
        try {

            productDTO = new ProductDTO();
            productDTO.setNote(note.getText());
            productDTO.setName(name.getText());
            productDTO.setAmount(Integer.parseInt(amount.getText()));
            productDTO.setPrice(Double.parseDouble(price.getText()));
            productDTO.setDateOfExpiration(dateOfExpiration.getText());
            productDTO.setDateOfManufacture(dateOfManufacture.getText());
            productDTO.setCategory((String) category.getValue());
            stage.close();
        }
        catch (NumberFormatException e)
        {
         //   new Error("Добавление", "Склад не добавлен", "Неккоректный ввод данных", stage);
            productDTO=null;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
