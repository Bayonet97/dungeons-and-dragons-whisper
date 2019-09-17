package gui.controllers;

import clienthandler.DanDWhisperClientHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import messaging.responses.LoginResponse;
import messaging.responses.RegisterResponse;

import java.util.Observable;
import java.util.Observer;

public class RegisterLoginController extends Controller {
    public Button loginButton;
    public Button registerButton;
    public Pane loginPane;
    public Pane registerPane;
    public Button noAccountButton;
    public Button backToLoginButton;
    public TextField usernameTextField;
    public TextField passwordTextField;
    public TextField registerUsernameTextField;
    public TextField registerPasswordTextField;

    public RegisterLoginController(){
        clientHandler = new DanDWhisperClientHandler(this);
    }

    public void loginButtonClicked(javafx.event.ActionEvent actionEvent){
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        clientHandler.requestLogin(username,password);
    }

    public void login(LoginResponse loginResponse){
        if(loginResponse.isSuccess() && loginResponse.isCredentialsCorrect()){
            sceneSwitcher.switchToCharacterSelectSelfScene(loginButton, loginResponse.getCharacterPermissions());
            new Alert(Alert.AlertType.INFORMATION, "Logged in with username: " + loginResponse.getUsername() + "Character permissions: " + loginResponse.getCharacterPermissions());
        }
        else if(loginResponse.isSuccess() && !loginResponse.isCredentialsCorrect()){
            new Alert(Alert.AlertType.ERROR, "Username or Password incorrect!").showAndWait();
        }
        else if(!loginResponse.isSuccess()){
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").showAndWait();
        }
    }
    public void registerButtonClicked(ActionEvent actionEvent) {
        String username = registerUsernameTextField.getText();
        String password = registerPasswordTextField.getText();

        clientHandler.requestRegister(username, password);
    }
    public void register(RegisterResponse registerResponse){
        if(registerResponse.isSuccess() && !registerResponse.isUsernameInUse()){
            new Alert(Alert.AlertType.INFORMATION, "Registered in with username:" + registerResponse.getUsername()).showAndWait();
            registerPane.setVisible(false);
            registerPane.setDisable(true);
            loginPane.setVisible(true);
            loginPane.setDisable(false);
        }
        else if(registerResponse.isSuccess() && registerResponse.isUsernameInUse()){
            new Alert(Alert.AlertType.ERROR, "Failed to register. This username is already taken!");
        }
        else if(!registerResponse.isSuccess()){
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").showAndWait();
        }
    }

    public void noAccountButtonClicked(ActionEvent actionEvent) {
        registerPane.setVisible(true);
        registerPane.setDisable(false);
        loginPane.setVisible(false);
        loginPane.setDisable(true);
    }

    public void backToLoginButtonClicked(ActionEvent actionEvent) {
        registerPane.setVisible(false);
        registerPane.setDisable(true);
        loginPane.setVisible(true);
        loginPane.setDisable(false);
    }

}
