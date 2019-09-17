package gui.controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import messaging.responses.SendChatMessageResponse;

import java.io.IOException;
import java.util.List;

public class SceneSwitcher {

    //Note that the button from the scene is given in the methods. This is because a Control object is required to find the stage to switch scenes on.

    void switchToRegisterLoginScene(Control objFromCurrentScene){
        String registerLoginScene = "/dandwhisperregisterlogin.fxml";
        switchScene(registerLoginScene, objFromCurrentScene);
    }
    void switchToCharacterSelectSelfScene(Control objFromCurrentScene, List<String> characterPermissions){
        String characterSelectSelfScene = "/dandwhispercharacterselectself.fxml";
        CharacterSelectSelfController controller = (CharacterSelectSelfController)switchScene(characterSelectSelfScene, objFromCurrentScene);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.initData(characterPermissions);
            }
        });
    }

    void switchToCharacterSelectOtherScene(Control objFromCurrentScene, String selectedCharacter){
        String characterSelectOtherScene = "/dandwhispercharacterselectother.fxml";
        CharacterSelectOtherController controller = (CharacterSelectOtherController)switchScene(characterSelectOtherScene, objFromCurrentScene);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.initData(selectedCharacter);
            }
        });

    }

    void switchToChatScene(Control objFromCurrentScene, String speaker, String listener, ObservableList<String> observableListSelectableCharactersOther){
        String chatScene = "/dandwhisperchat.fxml";
        ChatController controller = (ChatController)switchScene(chatScene, objFromCurrentScene);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.initData(speaker, listener, observableListSelectableCharactersOther);
            }
        });
    }


    private Controller switchScene(String newScene, Control objFromCurrentScene){
        Controller controller = null;
        try {
            BorderPane root = new BorderPane();
            Stage stage = (Stage)objFromCurrentScene.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(newScene));

            root.setCenter(loader.load());

            Scene scene = new Scene(root, 500, 700);
            stage.setScene(scene);

            stage.show();
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return controller;
    };
}
