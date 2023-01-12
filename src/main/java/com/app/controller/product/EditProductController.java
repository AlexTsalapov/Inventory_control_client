package com.app.controller.product;

import com.app.DTO.ProductDTO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditProductController {
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
    private Button editButton;

    @FXML
    private TextField name;

    @FXML
    private TextField note;

    @FXML
    private TextField price;

    ProductDTO productDTO;
    Stage stage;
    private boolean flag=false;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setProduct(ProductDTO product)
    {
        note.setText(product.getNote());
        name.setText(product.getName());
        price.setText(product.getPrice()+"");
        amount.setText(product.getAmount()+"");
        dateOfManufacture.setText(product.getDateOfManufacture());
        dateOfExpiration.setText(product.getDateOfExpiration());

    }
    public void setCategory(ComboBox<String> menu)
    {
        for (int i = 1; i < menu.getItems().size(); i++) {
            category.getItems().add(menu.getItems().get(i));
        }
    }
    public void cansel()
    {
        stage.close();
    }

    public void close() {
        flag=true;
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

    public ProductDTO getProductDTO() {
        return productDTO;
    }
}
