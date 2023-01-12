package com.app.controller;

import com.app.client.ClientSocket;
import com.app.DTO.StorageDTO;
import com.app.DTO.UserDTO;
import com.app.client.JSON;
import com.app.client.Request;
import com.app.other.SHA_256;
import com.app.message.Error;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AuthorizationControler implements Initializable  {//передача всей инфы и закрытие окна
    @FXML
    private Button button;

    @FXML
    private TextField login;

    @FXML
    private PasswordField password;

    @FXML
    private Label registration;

    @FXML
    private Label textAction;

    @FXML
    private Label textBase;
    UserDTO userDTO = new UserDTO();
    Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @FXML
    public void authorization() {
        boolean flag = false;

        if (login.getText().length() != 0 && password.getText().length() != 0) {
            userDTO.setLogin(SHA_256.hashCode(login.getText()));
            userDTO.setPassword(SHA_256.hashCode(password.getText()));
            try {
                if (ClientSocket.getInstance().connect()) {
                    ClientSocket.getInstance().send(new Request(Request.RequestType.Authorization, (new JSON<UserDTO>()).toJson(userDTO)));
                    UserDTO user = (new JSON<UserDTO>()).fromJson(ClientSocket.getInstance().get().getRequestMessage(), UserDTO.class);
                    if (user.getId() != 0) {
                        user.setStorages((List<StorageDTO>) new JSON<StorageDTO>().fromJsonArray(ClientSocket.getInstance().get().getRequestMessage(), StorageDTO.class));
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
                        stage.setTitle("MyStorage");
                        stage.getScene().setRoot(loader.load());
                        MainController mainControler = loader.<MainController>getController();
                        mainControler.setStage(stage);
                        mainControler.setUserDTO(user);
                        mainControler.filingStorage();
                        stage.setFullScreen(true);
                        stage.show();
                    }
                    else{
                        Error error = new Error(stage, "Авторизация", "Введен неверный логин или пароль");
                        error.showAndWait();
                    }
                }

            } catch (Exception e) {
                Error error = new Error(stage, "Авторизация", "Введен неверный логин или пароль");
                error.showAndWait();

            }
        }
     else{
                Error error = new Error(stage, "Авторизация", "Введен неверный логин или пароль");
                error.showAndWait();
            }

    }


    @FXML
    public void toRegistration() {

        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/RegistrationView.fxml"));

            stage.getScene().setRoot(loader.load());
            RegistrationController registrationControler =loader.<RegistrationController>getController();
            registrationControler.setStage(stage);
            stage.setFullScreen(true);
            stage.setTitle("Регистрация");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}


