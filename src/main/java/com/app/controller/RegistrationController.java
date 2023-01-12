package com.app.controller;

import com.app.client.ClientSocket;
import com.app.DTO.UserDTO;
import com.app.client.JSON;
import com.app.client.Request;
import com.app.other.SHA_256;
import com.app.message.Info;
import com.app.message.Error;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {//закрытие окна
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
    Stage stage = new Stage();

    @FXML
    public void registration() {
        UserDTO userDTO = new UserDTO();
        if (login.getText().length() != 0 && password.getText().length() != 0) {
            userDTO.setLogin(SHA_256.hashCode(login.getText()));
            userDTO.setPassword(SHA_256.hashCode(password.getText()));
            try {
                if (ClientSocket.getInstance().connect())
                {
                    ClientSocket.getInstance().send(new Request(Request.RequestType.Registration,(new JSON<UserDTO>()).toJson(userDTO)));
                    if(Boolean.parseBoolean(ClientSocket.getInstance().get().getRequestMessage())) {
                        Info info=new Info(stage,"Регистрация","Аккаунт добавлен");
                        info.showAndWait();
                    }
                    else {
                        Error error=new Error(stage,"Регистрация","Аккаунт уже существуеет");
                        error.showAndWait();
                    }

                }
                } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void toAutorization() {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/AutorizationView.fxml"));


        try {
            stage.getScene().setRoot(loader.load());
            AuthorizationControler authorizationControler =loader.<AuthorizationControler>getController();
            authorizationControler.setStage(stage);

            stage.setFullScreen(true);
            stage.setTitle("Авторизация");


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
