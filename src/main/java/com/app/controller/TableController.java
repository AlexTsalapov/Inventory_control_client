package com.app.controller;

import com.app.other.Checker;
import com.app.client.ClientSocket;
import com.app.DTO.ProductDTO;
import com.app.DTO.StorageDTO;
import com.app.client.JSON;
import com.app.client.Request;
import com.app.controller.product.AddProductController;
import com.app.controller.product.DeleteProductController;
import com.app.controller.product.EditProductController;
import com.app.message.Error;
import com.app.message.Info;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class TableController extends MainController {
    static TableView<ProductDTO> table;
    static ContextMenu contextMenu;


    Stage stageMain;
    StorageDTO storageDTO;

    public TableView<ProductDTO> filingTable(TableView<ProductDTO> table, StorageDTO tempstorageDTO, String category) {

        this.storageDTO = tempstorageDTO;
        table.getItems().remove(0,table.getItems().size());
        ObservableList products = FXCollections.observableArrayList();
        products = FXCollections.observableArrayList((tempstorageDTO).getProducts());
        tempstorageDTO.setProducts(tempstorageDTO.getProducts());
        if(!category.equals("Все категории")) {
            for (int i = 0; i < tempstorageDTO.getProducts().size(); i++) {
                if (category.equals(tempstorageDTO.getProducts().get(i).getCategory())) {
                    table.getItems().add(tempstorageDTO.getProducts().get(i));
                }
            }

        }
        else {
            table.setItems(products);
        }
        return table;
    }

    public MenuItem menuItemDelete(TableView<ProductDTO> table, StorageDTO storage,Stage oldStage) {

        MenuItem add = new MenuItem("Удалить");
        add.setStyle("-fx-font-size: 22");


        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage newStage ;
                newStage = new Stage(StageStyle.UNDECORATED);
                newStage.initModality(Modality.APPLICATION_MODAL);

                newStage.initOwner(oldStage.getScene().getWindow());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/product/DeleteProductView.fxml"));
                newStage.setTitle("Удаление");
                try {
                    newStage.setScene(new Scene(loader.load()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                DeleteProductController deleteProductController = loader.<DeleteProductController>getController();
                deleteProductController.setStage(newStage);
                deleteProductController.setTextHelp("Вы уверены что хотите удалить продукт\n под названием " + table.getSelectionModel().getSelectedItem().getName() + "?");
                newStage.showAndWait();
                if (deleteProductController.isFlag()) {
                    ProductDTO productDTO = table.getSelectionModel().getSelectedItem();

                    productDTO.setStorage(storage);
                    try {
                        if (ClientSocket.getInstance().connect()) {
                            ClientSocket.getInstance().send(new Request(Request.RequestType.DeleteProduct, (new JSON<ProductDTO>()).toJson(productDTO)));
                            if (Boolean.parseBoolean(ClientSocket.getInstance().get().getRequestMessage())) {
                                Info info=new Info(oldStage,"Продукт удален");
                                info.showAndWait();
                                table.getItems().remove(productDTO);
                            } else {
                                Error error = new Error(newStage, "Удаление", "Ведутся технические работы на сервере");
                                error.showAndWait();
                            }

                        }
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });


        return add;

    }


    public MenuItem menuItemAdd(TableView<ProductDTO> table, StorageDTO storage, ComboBox<String> menu,Stage oldStage) {


            MenuItem add = new MenuItem("Добавить");
            add.setStyle("-fx-font-size: 22");


            add.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Stage stage ;
                    stage = new Stage(StageStyle.UNDECORATED);
                    stage.initModality(Modality.APPLICATION_MODAL);

                    stage.initOwner(oldStage.getScene().getWindow());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/product/AddProductView.fxml"));
                    stage.setTitle("Добавление");
                    try {
                        stage.setScene(new Scene(loader.load()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    AddProductController addProductController = loader.<AddProductController>getController();
                    addProductController.setStage(stage);
                    addProductController.setCategorys(menu);
                    stage.showAndWait();
                    ProductDTO productDTO=new ProductDTO();
                    try {
                         productDTO = addProductController.getProductDTO();

                    if (productDTO != null) {
                        if (Checker.checkDateManufacture(productDTO.getDateOfManufacture()) && Checker.checkDateExpiration(productDTO.getDateOfExpiration())) {
                            productDTO.setStorage(storage);

                            if (ClientSocket.getInstance().connect()) {
                                ClientSocket.getInstance().send(new Request(Request.RequestType.CreateProduct, (new JSON<ProductDTO>()).toJson(productDTO)));
                                if (Boolean.parseBoolean(ClientSocket.getInstance().get().getRequestMessage())) {
                                    Info info=new Info(oldStage,"Продукт","Продукт добавлен");
                                    info.showAndWait();
                                    table.getItems().add(productDTO);
                                } else {
                                    Error error = new Error(stage, "Добавление", "Данный продукт уже существует");
                                    error.showAndWait();
                                }
                            }
                        }
                    }

                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    catch (NumberFormatException e)
                    {
                        Error error = new Error(stage, "Добавление", "Ведутся технические работы на сервере");
                        error.showAndWait();
                    }
                    }

            });


            return add;

        }


    public MenuItem menuItemEdit(TableView<ProductDTO> table, StorageDTO storage, ComboBox<String> menu,Stage oldStage) {

        MenuItem add = new MenuItem("Редактировать");
        add.setStyle("-fx-font-size: 22");


        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage newStage ;
                newStage = new Stage(StageStyle.UNDECORATED);
                newStage.initModality(Modality.APPLICATION_MODAL);

                newStage.initOwner(oldStage.getScene().getWindow());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/product/EditProductView.fxml"));
                newStage.setTitle("Редактирование");
                try {
                    newStage.setScene(new Scene(loader.load()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                EditProductController editProductController = loader.<EditProductController>getController();
                editProductController.setStage(newStage);
                editProductController.setCategory(menu);
                ProductDTO product = table.getSelectionModel().getSelectedItem();
                editProductController.setProduct(product);
                newStage.showAndWait();

                if (editProductController.isFlag()) {
                    ProductDTO productDTO = editProductController.getProductDTO();
                    productDTO.setStorage(storage);
                    productDTO.setId(product.getId());
                    if (productDTO != null) {
                        productDTO.setStorage(storage);
                        try {
                            if (ClientSocket.getInstance().connect()) {
                                ClientSocket.getInstance().send(new Request(Request.RequestType.UpdateProduct, (new JSON<ProductDTO>()).toJson(productDTO)));
                                if (Boolean.parseBoolean(ClientSocket.getInstance().get().getRequestMessage())) {
                                    Info info = new Info(oldStage, "Продукт изменен");
                                    info.showAndWait();
                                    table.getItems().remove(product);
                                    table.getItems().add(productDTO);
                                } else {
                                    Error error = new Error(newStage, "Редактирование", "Ведутся технические работы на сервере");
                                    error.showAndWait();
                                }

                            }
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });


        return add;
    }


    public static TableView<ProductDTO> createTable(TableView<ProductDTO> table) {
        contextMenu = new ContextMenu();

        TableColumn<ProductDTO, String> noteColumn = new TableColumn<ProductDTO, String>("Накладная");
        // определяем фабрику для столбца с привязкой к свойству name
        noteColumn.setCellValueFactory(new PropertyValueFactory<ProductDTO, String>("note"));
        noteColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.20));
        noteColumn.setStyle("-fx-font-size: 16");
        // добавляем столбец
        table.getColumns().add(noteColumn);
        TableColumn<ProductDTO, String> nameColumn = new TableColumn<ProductDTO, String>("Название");
        // определяем фабрику для столбца с привязкой к свойству name
        nameColumn.setCellValueFactory(new PropertyValueFactory<ProductDTO, String>("name"));
        nameColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.10));
        nameColumn.setStyle("-fx-font-size: 16");
        // добавляем столбец
        table.getColumns().add(nameColumn);

        TableColumn<ProductDTO, String> priceColumn = new TableColumn<ProductDTO, String>("Цена");
        priceColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.10));
        priceColumn.setStyle("-fx-font-size: 16");
        priceColumn.setCellValueFactory(new PropertyValueFactory<ProductDTO, String>("price"));
        table.getColumns().add(priceColumn);
        TableColumn<ProductDTO, String> amountColumn = new TableColumn<ProductDTO, String>("Количество");
        amountColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
        amountColumn.setStyle("-fx-font-size: 16");
        amountColumn.setCellValueFactory(new PropertyValueFactory<ProductDTO, String>("amount"));
        table.getColumns().add(amountColumn);
        TableColumn<ProductDTO, String> dateColumn = new TableColumn<ProductDTO, String>("Дата производства");
        dateColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.175));
        dateColumn.setStyle("-fx-font-size: 16");
        dateColumn.setCellValueFactory(new PropertyValueFactory<ProductDTO, String>("dateOfManufacture"));
        table.getColumns().add(dateColumn);
        TableColumn<ProductDTO, String> date2Column = new TableColumn<ProductDTO, String>("Срок годности");
        date2Column.prefWidthProperty().bind(table.widthProperty().multiply(0.175));
        date2Column.setStyle("-fx-font-size: 16");
        date2Column.setCellValueFactory(new PropertyValueFactory<ProductDTO, String>("dateOfExpiration"));
        table.getColumns().add(date2Column);
        TableColumn<ProductDTO, String> categoryColumn = new TableColumn<ProductDTO, String>("Категория");
        categoryColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        categoryColumn.setStyle("-fx-font-size: 16");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<ProductDTO, String>("category"));
        table.getColumns().add(categoryColumn);

        // table.setItems(client.findAll());//проверить
        return table;
    }


}
