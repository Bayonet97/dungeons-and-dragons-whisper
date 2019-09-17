package gui.controllers;

import clienthandler.DanDWhisperClientHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import messaging.responses.GetAllCharactersResponse;

import java.util.List;

public class CharacterSelectOtherController extends Controller{

    public Button selectCharacterToButton;
    public ListView<String> characterSelectOtherListView;
    public ObservableList<String> observableListSelectableCharactersOther;
    public Button backButton;
    private String selectedCharacter;

    public void initialize(){
        observableListSelectableCharactersOther = FXCollections.observableArrayList();
        clientHandler = new DanDWhisperClientHandler(this);
    }
    public void initData(String selectedCharacter){
        this.selectedCharacter = selectedCharacter;
        clientHandler.requestGetAllCharacters();
    }

    public void selectCharacterToButtonClicked(ActionEvent actionEvent) {
        sceneSwitcher.switchToChatScene(selectCharacterToButton, selectedCharacter, characterSelectOtherListView.getSelectionModel().getSelectedItem(), observableListSelectableCharactersOther);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
      //  sceneSwitcher.switchToCharacterSelectSelfScene(selectCharacterToButton);
    }

    public void loadCharactersList(GetAllCharactersResponse getAllCharactersResponse) {
        List<String> selectableCharacters = getAllCharactersResponse.getCharacters();
        selectableCharacters.remove(selectedCharacter);
        observableListSelectableCharactersOther.setAll(selectableCharacters);
        characterSelectOtherListView.setItems(observableListSelectableCharactersOther);
    }
}
