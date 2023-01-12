package com.app.controller;

import com.app.client.ClientSocket;
import com.app.client.JSON;
import com.app.client.Request;
import com.app.controller.category.AddCategoryController;
import com.app.controller.category.DeleteCategoryController;
import com.app.controller.category.EditCategoryController;
import com.app.DTO.CategoryDTO;
import com.app.DTO.ProductDTO;
import com.app.DTO.StorageDTO;
import com.app.DTO.UserDTO;
import com.app.other.CreatePDF;
import com.app.controller.srorage.AddStorageOrDeliveryController;
import com.app.controller.srorage.DeleteStorageOrDeliveryController;
import com.app.controller.srorage.EditStorageOrDeliveryController;
import com.app.controller.srorage.StorageAndDelivery;
import com.app.message.Error;
import com.app.message.Info;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    public MainController() {

    }
    @FXML
    private ComboBox<String> deliveryBox;
    @FXML
    private ComboBox<String> storageBox;

    @FXML
    private ComboBox<String> categoryBox;

    @FXML
    private HBox hboxStorage, hboxDelivery;
    @FXML
    private Menu storageMenu;
    @FXML
    private Button btnToDelivery, btnOrder, btnExit, btnBack;
    @FXML
    private Label text;
    boolean chooseType;
    String baseText = "Выберите склад";


    @FXML
    private TableView<ProductDTO> table;

    private static ContextMenu contextMenu = new ContextMenu();

    private boolean type;

    private UserDTO userDTO = new UserDTO();
    Stage stage=new Stage();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }


    public void getActionStorage() {
        try {
            if (ClientSocket.getInstance().connect()) {
                StorageDTO storageDTO = getChooseStorage();
                ClientSocket.getInstance().send(new Request(Request.RequestType.GetProducts, (new JSON<StorageDTO>()).toJson(storageDTO)));
                storageDTO.setProducts((List<ProductDTO>) new JSON<ProductDTO>().fromJsonArray(ClientSocket.getInstance().get().getRequestMessage(), ProductDTO.class));
                table = TableController.createTable(table);
                TableController tableController = new TableController();
                table = tableController.filingTable(table, storageDTO, "Все категории");
                ClientSocket.getInstance().send(new Request(Request.RequestType.GetCategory, (new JSON<StorageDTO>()).toJson(storageDTO)));
                List<CategoryDTO> listCategory = (List<CategoryDTO>) new JSON<CategoryDTO>().fromJsonArray(ClientSocket.getInstance().get().getRequestMessage(), CategoryDTO.class);
                categoryBox.getItems().remove(1, categoryBox.getItems().size());
                for (int i = 0; i < listCategory.size(); i++) {
                    categoryBox.getItems().add(listCategory.get(i).getName());
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


    public MenuItem createCategoryAction(String name) {
        MenuItem item = new MenuItem(name);

        item.setOnAction(new EventHandler<ActionEvent>() {//edit
            @Override
            public void handle(ActionEvent actionEvent) {

                TableController tableController = new TableController();
                table = tableController.filingTable(table, getChooseStorage(), name);
            }
        });

        return item;
    }

    public void toAutorization() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AutorizationView.fxml"));
            baseText="Выберите склад";
            storageBox.getItems().remove(0,storageBox.getItems().size());
            categoryBox.getItems().remove(1,categoryBox.getItems().size());
            stage.setTitle("Авторизация");
            stage.setScene(new Scene(loader.load()));
            AuthorizationControler authorizationControler = loader.<AuthorizationControler>getController();
            authorizationControler.setStage(stage);
            storageBox=filingMenu(storageBox,true);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void toDelivery() {
        chooseType = false;
        baseText = "Выберите поставщика";
        categoryBox.getItems().remove(1,categoryBox.getItems().size());

       deliveryBox.getItems().remove(0,deliveryBox.getItems().size());
        hboxStorage.setVisible(false);
        hboxDelivery.setVisible(true);
        btnToDelivery.setVisible(false);
        btnOrder.setVisible(true);
        text.setText("Поставщик");
        deliveryBox = filingMenu(deliveryBox, false);
        btnBack.setVisible(true);
        btnExit.setVisible(false);
        table.getItems().clear();
        // ну и таблицу главную очистить

    }

    @FXML
    public void toStorage() {
        baseText = "Выберите склад";
        categoryBox.getItems().remove(1,categoryBox.getItems().size());
        chooseType = true;
        hboxStorage.setVisible(true);
        hboxDelivery.setVisible(false);
        btnToDelivery.setVisible(true);
        btnOrder.setVisible(false);
        text.setText("Склад");

        categoryBox.getItems().remove(1,categoryBox.getItems().size());
        btnBack.setVisible(false);
        btnExit.setVisible(true);

        table.getItems().remove(0,table.getItems().size());
    }

    @FXML
    public void addCategory() {
        String str;
        if (text.getText().equals("Склад")) {
            str = storageBox.getValue();
        } else {
            str = deliveryBox.getValue();
        }
        if (str!=null) {
            Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/category/AddCategoryView.fxml"));
            newStage.setTitle("Добавление");
            newStage = new Stage(StageStyle.UNDECORATED);
            newStage.initModality(Modality.APPLICATION_MODAL);

            newStage.initOwner(stage.getScene().getWindow());
            try {
                newStage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            AddCategoryController controler = loader.<AddCategoryController>getController();
            controler.setStage(newStage);
            newStage.showAndWait();
            if (controler.getName() != null) {
                if (!controler.getName().equals("") && controler.isFlag()) {
                    CategoryDTO categoryDTO = new CategoryDTO();
                    categoryDTO.setName(controler.getName());
                    categoryDTO.setStorageId(getChooseStorage().getId());
                    try {
                        if (ClientSocket.getInstance().connect()) {
                            ClientSocket.getInstance().send(new Request(Request.RequestType.CreateCategory, (new JSON<CategoryDTO>()).toJson(categoryDTO)));
                            if (Boolean.parseBoolean(ClientSocket.getInstance().get().getRequestMessage())) {
                                Info info = new Info(stage, "Категория", "Категория добавлена");
                                info.showAndWait();
                                categoryBox.getItems().add((categoryDTO.getName()));

                            } else {

                                Error error = new Error(stage, "Добавление", "Ведутся технические работы на сервере");
                                error.showAndWait();
                            }
                            //System.out.println(storageDTO.getName());
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

    @FXML
    public void editCategory() {
        String str;
        if (text.getText().equals("Склад")) {
            str = storageBox.getValue();
        } else {
            str = deliveryBox.getValue();
        }
        Stage newStage;
        newStage = new Stage(StageStyle.UNDECORATED);
        newStage.initModality(Modality.APPLICATION_MODAL);

        newStage.initOwner(stage.getScene().getWindow());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/category/EditCategoryView.fxml"));
        newStage.setTitle("Редактирование");
        try {
            newStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        EditCategoryController controler = loader.<EditCategoryController>getController();
        controler.setStage(newStage);
        if (str!=null) {
            controler.filingBox(categoryBox);
            newStage.showAndWait();
            CategoryDTO categoryDTO = controler.getCategoryDTO();
            String oldName = categoryDTO.getName();
            categoryDTO.setName(controler.getName().getText());

            if (controler.isFlag()) {
                if (categoryDTO.getName() != null) {
                    if (!categoryDTO.getName().equals("")) {
                        categoryDTO.setStorageId(getChooseStorage().getId());
                        try {
                            if (ClientSocket.getInstance().connect()) {
                                ClientSocket.getInstance().send(new Request(Request.RequestType.GetCategory, (new JSON<StorageDTO>()).toJson(getChooseStorage())));
                                List<CategoryDTO> listCategory = (List<CategoryDTO>) new JSON<CategoryDTO>().fromJsonArray(ClientSocket.getInstance().get().getRequestMessage(), CategoryDTO.class);

                                for (int i = 0; i < listCategory.size(); i++) {
                                    if (listCategory.get(i).getName().equals(oldName)) {
                                        categoryDTO.setId(listCategory.get(i).getId());
                                        break;
                                    }

                                }
                                ClientSocket.getInstance().send(new Request(Request.RequestType.UpdateCategory, (new JSON<CategoryDTO>()).toJson(categoryDTO)));
                                if (Boolean.parseBoolean(ClientSocket.getInstance().get().getRequestMessage())) {
                                    Info info = new Info(stage, "Категория", "Категория изменена");
                                    info.showAndWait();

                                    for (int i = 0; i < categoryBox.getItems().size(); i++) {
                                        if (categoryBox.getItems().get(i).equals(oldName)) {
                                            categoryBox.getItems().remove(i);
                                        }
                                    }
                                    categoryBox.getItems().add(categoryDTO.getName());


                                } else {
                                    Error error = new Error(stage, "Редактирование", "Ведутся технические работы на сервере");
                                    error.showAndWait();
                                }
                                //System.out.println(storageDTO.getName());
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
    }

    @FXML
    public void deleteCategory() {
        String str;
        if (text.getText().equals("Склад")) {
            str = storageBox.getValue();
        } else {
            str = deliveryBox.getValue();
        }
        Stage newStage ;
        newStage = new Stage(StageStyle.UNDECORATED);
        newStage.initModality(Modality.APPLICATION_MODAL);

        newStage.initOwner(stage.getScene().getWindow());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/category/DeleteCategoryView.fxml"));
        newStage.setTitle("Удаление");
        try {
            newStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DeleteCategoryController controler = loader.<DeleteCategoryController>getController();
        controler.setStage(newStage);
        if (str!=null) {
            controler.filingBox(categoryBox);
            newStage.showAndWait();

            CategoryDTO categoryDTO = controler.getCategoryDTO();
            if (controler.isFlag()) {
                if (categoryDTO.getName() != null) {
                    if (!categoryDTO.getName().equals("")) {
                        categoryDTO.setStorageId(getChooseStorage().getId());
                        try {
                            if (ClientSocket.getInstance().connect()) {
                                ClientSocket.getInstance().send(new Request(Request.RequestType.DeleteCategory, (new JSON<CategoryDTO>()).toJson(categoryDTO)));
                                if (Boolean.parseBoolean(ClientSocket.getInstance().get().getRequestMessage())) {
                                    Info info = new Info(stage, "Категория", "Категория удалена");
                                    info.showAndWait();

                                    for (int i = 0; i < categoryBox.getItems().size(); i++) {
                                        if (categoryBox.getItems().get(i).equals(categoryDTO.getName())) {
                                            categoryBox.getItems().remove(i);
                                        }
                                    }


                                } else {
                                    Error error = new Error(stage, "Удаление", "Ведутся технические работы на сервере");
                                    error.showAndWait();
                                }
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
                else {
                    Error error = new Error(stage, "Удаление", "Некоректный ввод данных");
                    error.showAndWait();
                }
            }

        }
    }

    public void getCategoryAction() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(categoryBox.getValue());
        table = new TableController().filingTable(table, getChooseStorage(), categoryBox.getValue());
    }

    public void filingStorage() {
        storageBox.getItems().remove(0, storageBox.getItems().size());
        storageBox = filingMenu(storageBox, true);
    }

    public void filingDelivery() {
        deliveryBox.getItems().remove(0, deliveryBox.getItems().size());
        deliveryBox = filingMenu(deliveryBox, false);
    }

    public ComboBox<String> filingMenu(ComboBox<String> comboBox, boolean type) {

        for (int i = 0; i < userDTO.getStorages().size(); i++) {
            if (userDTO.getStorages().get(i).isType() == type) {
                comboBox.getItems().add(userDTO.getStorages().get(i).getName());
            }

        }
        return comboBox;

    }

    public void getTableAction() {
        String str;
        if(text.getText().equals("Склад"))
        {
            str=storageBox.getValue();
        }
        else {
            str=deliveryBox.getValue();
        }
        TableController tableController = new TableController();
        table.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {


            @Override
            public void handle(MouseEvent mouseEvent) {
                contextMenu.hide();
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    contextMenu.getItems().clear();
                    if (table.getSelectionModel().getSelectedItem() != null) {
                        contextMenu.getItems().addAll(tableController.menuItemAdd(table, getChooseStorage(), categoryBox,stage), tableController.menuItemDelete(table, getChooseStorage(),stage), tableController.menuItemEdit(table, getChooseStorage(), categoryBox,stage));
                    } else if (!str.equals(baseText)) {
                        contextMenu.getItems().addAll(tableController.menuItemAdd(table, getChooseStorage(), categoryBox,stage));
                    }
                    contextMenu.show(table, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                }


            }
        });
    }

    public void createAllCategories() {
        TableController tableController = new TableController();
        if (!categoryBox.getValue().equals("")) {
            table = tableController.filingTable(table, getChooseStorage(), "Все категории");
        }

    }

    public void createOrder() {
        Stage newStage;
        newStage = new Stage(StageStyle.UNDECORATED);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.initOwner(stage.getScene().getWindow());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/other/OrderView.fxml"));
        newStage.setTitle("Заказ");
        try {
            newStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        OrderController controler = loader.<OrderController>getController();
        controler.setStage(newStage);
        controler.setBoxDeliverys(deliveryBox, userDTO);
        controler.setBoxStorages(storageBox);
        newStage.showAndWait();
        StorageDTO storageDTO = controler.getStorageDTO();
        String storageStr = controler.getStorageStr();
        String deliveryStr = controler.getDeliveryStr();
        if (storageDTO.getProducts().size() != 0 && storageStr != null && deliveryStr != null) {
            try {
                if (ClientSocket.getInstance().connect()) {
                    for (int i = 0; i < userDTO.getStorages().size(); i++) {
                        if (userDTO.getStorages().get(i).getName().equals(storageStr)) {
                            storageDTO.setId(userDTO.getStorages().get(i).getId());
                            break;
                        }
                    }


                    boolean flag = false;
                    for (int i = 0; i < storageDTO.getProducts().size(); i++) {
                        ProductDTO productDTO = storageDTO.getProducts().get(i);
                        productDTO.setStorage(storageDTO);
                        ClientSocket.getInstance().send(new Request(Request.RequestType.CreateProduct, (new JSON<ProductDTO>()).toJson(productDTO)));
                        flag = Boolean.parseBoolean(ClientSocket.getInstance().get().getRequestMessage());
                    }
                    if (flag) {
                        Info info = new Info(stage, "Заказ", "Заказ выполнен");
                        info.showAndWait();
                        CreatePDF.createPdf(storageDTO, deliveryStr, storageStr);

                    } else {
                        Error error = new Error(stage, "Заказ", "Ведутся технические работы на сервере");
                        error.showAndWait();
                    }

                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            Error error = new Error(stage, "Заказ", "Неккоректный ввод данных");
            error.showAndWait();
        }
    }

    public MenuItem createMenuItem(String name, String type) {
        MenuItem item = new MenuItem(name);

        item.setOnAction(new EventHandler<ActionEvent>() {//edit
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if (ClientSocket.getInstance().connect()) {
                        StorageDTO storageDTO = getChooseStorage();
                        ClientSocket.getInstance().send(new Request(Request.RequestType.GetProducts, (new JSON<StorageDTO>()).toJson(storageDTO)));
                        storageDTO.setProducts((List<ProductDTO>) new JSON<ProductDTO>().fromJsonArray(ClientSocket.getInstance().get().getRequestMessage(), ProductDTO.class));
                        table = TableController.createTable(table);
                        TableController tableController = new TableController();
                        table = tableController.filingTable(table, storageDTO, "Все категории");
                        ClientSocket.getInstance().send(new Request(Request.RequestType.GetCategory, (new JSON<StorageDTO>()).toJson(storageDTO)));
                        List<CategoryDTO> listCategory = (List<CategoryDTO>) new JSON<CategoryDTO>().fromJsonArray(ClientSocket.getInstance().get().getRequestMessage(), CategoryDTO.class);
                        categoryBox.getItems().remove(1, categoryBox.getItems().size());
                        for (int i = 0; i < listCategory.size(); i++) {
                            categoryBox.getItems().add(listCategory.get(i).getName());
                        }


                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        return item;
    }


    public StorageDTO getChooseStorage() {
        for (int i = 0; i < userDTO.getStorages().size(); i++) {
            if(text.getText().equals("Склад"))
            {
                if (userDTO.getStorages().get(i).getName().equals(storageBox.getValue())) {
                    return userDTO.getStorages().get(i);
                }
            }
            else {
                if (userDTO.getStorages().get(i).getName().equals(deliveryBox.getValue())) {
                    return userDTO.getStorages().get(i);
                }
            }
        }
        return null;
    }

    public <T extends StorageAndDelivery> StorageDTO createStage(T object, String name, String title) {
        Stage newStage ;
        newStage = new Stage(StageStyle.UNDECORATED);
        newStage.initModality(Modality.APPLICATION_MODAL);

        newStage.initOwner(stage.getScene().getWindow());
        FXMLLoader loader = new FXMLLoader(getClass().getResource(name));
        newStage.setTitle(title);
        try {
            newStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        T controler = loader.<T>getController();
        controler.setStage(newStage);

        controler.setList(getList(type));
        controler.filingBox();


        newStage.showAndWait();
        return controler.findStorage();

    }

    public ArrayList<StorageDTO> getList(boolean type) {
        ArrayList<StorageDTO> list = new ArrayList<>();
        for (int i = 0; i < userDTO.getStorages().size(); i++) {
            if (userDTO.getStorages().get(i).isType() == type) {
                list.add(userDTO.getStorages().get(i));

            }

        }
        return list;
    }


    @FXML
    public void addStorage() {

        Stage newStage ;
        newStage = new Stage(StageStyle.UNDECORATED);
        newStage.initModality(Modality.APPLICATION_MODAL);

        newStage.initOwner(stage.getScene().getWindow());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/storageOrDelivery/AddStorageOrDeliveryView.fxml"));
        newStage.setTitle("Авторизация");
        try {
            newStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AddStorageOrDeliveryController controler = loader.<AddStorageOrDeliveryController>getController();
        controler.setStage(newStage);
        newStage.showAndWait();
        StorageDTO storageDTO = controler.getStorageDTO();
        if (storageDTO != null) {
            storageDTO.setType(true);
            UserDTO usertemp = new UserDTO();
            usertemp.setId(userDTO.getId());
            storageDTO.setUser(usertemp);


            try {
                if (ClientSocket.getInstance().connect()) {
                    ClientSocket.getInstance().send(new Request(Request.RequestType.CreateStorage, (new JSON<StorageDTO>()).toJson(storageDTO)));
                    if (Boolean.parseBoolean(ClientSocket.getInstance().get().getRequestMessage())) {
                        Info info = new Info(stage, "Склад", "Склад добавлен");
                        info.showAndWait();
                        storageBox.getItems().add((storageDTO.getName()));
                        userDTO.addStorage(storageDTO);
                    } else {
                        Error error = new Error(stage, "Добавление", "Данный склад уже существует");
                        error.showAndWait();
                    }
                    System.out.println(storageDTO.getName());
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

    @FXML
    public void editStorage() {
        this.type = true;
        StorageDTO storageDTO = createStage(new EditStorageOrDeliveryController(), "/storageOrDelivery/EditStorageOrDeliveryView.fxml", "Редактирование");
        if (storageDTO != null) {
            storageDTO.setType(true);
            System.out.println(storageDTO.getName());
            try {
                if (ClientSocket.getInstance().connect()) {
                    ClientSocket.getInstance().send(new Request(Request.RequestType.UpdateStorage, (new JSON<StorageDTO>()).toJson(storageDTO)));
                    if (Boolean.parseBoolean(ClientSocket.getInstance().get().getRequestMessage())) {
                        Info info = new Info(stage, "Склад", "Склад изменен");
                        info.showAndWait();
                        for (int i = 0; i < userDTO.getStorages().size(); i++) {
                            if (userDTO.getStorages().get(i).getId() == (storageDTO.getId())) {
                                userDTO.getStorages().remove(i);
                                userDTO.getStorages().add(storageDTO);
                                storageBox.getItems().remove(0, storageBox.getItems().size());
                                break;
                            }
                        }
                        filingStorage();

                    } else {
                        Error error = new Error(stage, "Редактирование", "Ведутся технические работы на сервере");
                        error.showAndWait();
                    }
                    System.out.println(storageDTO.getName());
                }
                System.out.println(storageDTO.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    public void deleteStorage() {
        this.type = true;
        StorageDTO storageDTO = createStage(new DeleteStorageOrDeliveryController(), "/storageOrDelivery/DeleteStorageOrDeliveryView.fxml", "Удаление");
        if (storageDTO != null) {
            storageDTO.setType(true);

            try {
                if (ClientSocket.getInstance().connect()) {
                    ClientSocket.getInstance().send(new Request(Request.RequestType.DeleteStorage, (new JSON<StorageDTO>()).toJson(storageDTO)));
                    if (Boolean.parseBoolean(ClientSocket.getInstance().get().getRequestMessage())) {
                        Info info = new Info(stage, "Склад", "Склад удален");
                        info.showAndWait();
                        for (int i = 0; i < userDTO.getStorages().size(); i++) {
                            if (userDTO.getStorages().get(i).getName().equals(storageDTO.getName())) {
                                userDTO.getStorages().remove(i);
                                storageBox.getItems().remove(i - 1);
                                break;
                            }
                        }

                    } else {
                        Error error = new Error(stage, "Удаленик", "Ведутся технические работы на сервере");
                        error.showAndWait();
                    }
                    System.out.println(storageDTO.getName());
                }
                System.out.println(storageDTO.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    public void addDelivery() {
        Stage newStage ;
        newStage = new Stage(StageStyle.UNDECORATED);
        newStage.initModality(Modality.APPLICATION_MODAL);

        newStage.initOwner(stage.getScene().getWindow());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/storageOrDelivery/AddStorageOrDeliveryView.fxml"));
        newStage.setTitle("Авторизация");
        try {
            newStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AddStorageOrDeliveryController controler = loader.<AddStorageOrDeliveryController>getController();
        controler.setStage(newStage);
        newStage.showAndWait();
        StorageDTO storageDTO = controler.getStorageDTO();
        if (storageDTO != null) {
            storageDTO.setType(false);
            StorageDTO storageDTO1 = userDTO.getStorages().get(userDTO.getStorages().size() - 1);
            UserDTO tempUserDTO = userDTO;
            if (userDTO.getStorages().size() > 0) {
                tempUserDTO.getStorages().remove(storageDTO1);
            }

            storageDTO.setUser(tempUserDTO);
            try {
                if (ClientSocket.getInstance().connect()) {
                    ClientSocket.getInstance().send(new Request(Request.RequestType.CreateStorage, (new JSON<StorageDTO>()).toJson(storageDTO)));
                    if (Boolean.parseBoolean(ClientSocket.getInstance().get().getRequestMessage())) {
                        Info info = new Info(stage, "Склад", "Склад добавлен");
                        info.showAndWait();
                        deliveryBox.getItems().add(storageDTO.getName());
                        userDTO.addStorage(storageDTO);
                        userDTO.addStorage(storageDTO1);

                    } else {
                        Error error = new Error(stage, "Добавление", "Ведутся технические работы на сервере");
                        error.showAndWait();
                    }
                    System.out.println(storageDTO.getName());
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


    @FXML
    public void deleteDelivery() {
        this.type = false;
        Stage newStage;
        newStage = new Stage(StageStyle.UNDECORATED);
        newStage.initModality(Modality.APPLICATION_MODAL);

        newStage.initOwner(stage.getScene().getWindow());
        StorageDTO storageDTO = createStage(new DeleteStorageOrDeliveryController(), "/storageOrDelivery/DeleteStorageOrDeliveryView.fxml", "Удаление");
        if (storageDTO != null) {
            storageDTO.setType(false);

            try {
                if (ClientSocket.getInstance().connect()) {
                    ClientSocket.getInstance().send(new Request(Request.RequestType.DeleteStorage, (new JSON<StorageDTO>()).toJson(storageDTO)));
                    if (Boolean.parseBoolean(ClientSocket.getInstance().get().getRequestMessage())) {
                        Info info = new Info(newStage, "Поставщик", "Поставщик удален");
                        info.showAndWait();
                        for (int i = 0; i < userDTO.getStorages().size(); i++) {
                            if (userDTO.getStorages().get(i).getName().equals(storageDTO.getName())) {
                                userDTO.getStorages().remove(i);
                                deliveryBox.getItems().remove(i);
                                break;
                            }
                        }


                    } else {

                        Error error = new Error(newStage, "Удаление", "Ведутся технические работы на сервере");
                        error.showAndWait();

                    }
                    System.out.println(storageDTO.getName());
                }
                System.out.println(storageDTO.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }


    @FXML
    public void editDelivery() {
        this.type = false;
        Stage newStage;
        newStage = new Stage(StageStyle.UNDECORATED);
        newStage.initModality(Modality.APPLICATION_MODAL);

        newStage.initOwner(stage.getScene().getWindow());
        StorageDTO storageDTO = createStage(new EditStorageOrDeliveryController(), "/storageOrDelivery/EditStorageOrDeliveryView.fxml", "Редактирование");
        if (storageDTO != null) {
            if (storageDTO != null) {
                storageDTO.setType(false);
                System.out.println(storageDTO.getName());
                try {
                    if (ClientSocket.getInstance().connect()) {
                        ClientSocket.getInstance().send(new Request(Request.RequestType.UpdateStorage, (new JSON<StorageDTO>()).toJson(storageDTO)));
                        if (Boolean.parseBoolean(ClientSocket.getInstance().get().getRequestMessage())) {
                            Info info = new Info(newStage, "Поставщик", "Поставщик изменен");
                            info.showAndWait();
                            for (int i = 0; i < userDTO.getStorages().size(); i++) {
                                if (userDTO.getStorages().get(i).getId() == (storageDTO.getId())) {
                                    userDTO.getStorages().remove(i);
                                    userDTO.getStorages().add(storageDTO);
                                    deliveryBox.getItems().remove(0, deliveryBox.getItems().size());
                                    break;
                                }
                            }
                            filingDelivery();

                        } else {
                            Error error = new Error(stage, "Редактирование", "Ведутся технические работы на сервере");
                            error.showAndWait();
                        }
                        System.out.println(storageDTO.getName());
                    }
                    System.out.println(storageDTO.getName());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryBox.getItems().remove(0, categoryBox.getItems().size());
        categoryBox.getItems().add("Все категории");

    }

}

