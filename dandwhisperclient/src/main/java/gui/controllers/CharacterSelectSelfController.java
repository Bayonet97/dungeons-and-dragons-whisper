package gui.controllers;

import clienthandler.DanDWhisperClientHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;

public class CharacterSelectSelfController extends Controller{

    public ListView<String> characterSelectSelfListView;
    public ObservableList<String> observableListSelectableCharactersSelf;
    public Button selectCharacterSelfButton;
    public Button backButton;

    public void initialize(){
        observableListSelectableCharactersSelf =  FXCollections.observableArrayList();
    }

    public void initData(List<String> characterPermissions){
        observableListSelectableCharactersSelf.setAll(characterPermissions);
        characterSelectSelfListView.setItems(observableListSelectableCharactersSelf);
    }
    public void selectCharacterSelfButtonClicked(ActionEvent actionEvent) {
        String selectedCharacter = characterSelectSelfListView.getFocusModel().getFocusedItem();
        sceneSwitcher.switchToCharacterSelectOtherScene(selectCharacterSelfButton, selectedCharacter);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        clientHandler.requestLogout();
        sceneSwitcher.switchToRegisterLoginScene(selectCharacterSelfButton);
    }
}
