package com.app.controller;

import com.app.client.ClientSocket;
import com.app.DTO.ProductDTO;
import com.app.DTO.StorageDTO;
import com.app.DTO.UserDTO;
import com.app.client.JSON;
import com.app.client.Request;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class OrderController {
    @FXML
    private ComboBox<String> boxDeliverys;

    @FXML
    private ComboBox<String> boxStorages;

    public String getDeliveryStr() {
        return boxDeliverys.getValue();
    }
    public String getStorageStr() {
        return boxStorages.getValue();
    }

    public ComboBox<String> getBoxStorages() {
        return boxStorages;
    }

    @FXML
    private TableView<ProductDTO> table;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    Stage stage=new Stage();
  StorageDTO storageDTO=new StorageDTO();
    public void setBoxDeliverys(ComboBox<String> menu, UserDTO userDTO) {
        for (int i = 0; i < menu.getItems().size(); i++) {
            boxDeliverys.getItems().add(menu.getItems().get(i));
            int finalI = i;
            boxDeliverys.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    for (int j = 0; j < userDTO.getStorages().size(); j++) {
                        if (!userDTO.getStorages().get(j).isType()) {
                            if (userDTO.getStorages().get(j).getName().equals(menu.getItems().get(finalI))) {
                                try {
                                    if (ClientSocket.getInstance().connect()) {
                                        StorageDTO storageDTO = userDTO.getStorages().get(j);
                                        ClientSocket.getInstance().send(new Request(Request.RequestType.GetProducts, (new JSON<StorageDTO>()).toJson(storageDTO)));
                                        storageDTO.setProducts((List<ProductDTO>) new JSON<ProductDTO>().fromJsonArray(ClientSocket.getInstance().get().getRequestMessage(), ProductDTO.class));
                                        table = TableController.createTable(table);
                                        table = new TableController().filingTable(table, storageDTO, "Все категории");
                                        storageDTO.setProducts(table.getItems());
                                    }
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                } catch (ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }

                    }
                }
            });
        }
    }
public void cansel()
{
    stage.close();
}

    public StorageDTO getStorageDTO() {
        return storageDTO;
    }

    public void setBoxStorages(ComboBox<String> menu)
    {
        for (int i = 0; i <menu.getItems().size() ; i++) {
            boxStorages.getItems().add(menu.getItems().get(i));
        }
    }



    public void close()
    {
        stage.close();
        for (int i = 0; i < table.getItems().size(); i++) {
            storageDTO.addProduct(table.getItems().get(i));
        }
    }

}
