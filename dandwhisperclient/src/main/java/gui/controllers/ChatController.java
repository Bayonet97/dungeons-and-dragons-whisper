package gui.controllers;

import clienthandler.DanDWhisperClientHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import messaging.responses.OpenChatSessionResponse;
import messaging.responses.SendChatMessageResponse;

import static java.lang.Integer.parseInt;

public class ChatController extends Controller{
    public ListView<String> chatPane;
    public TextArea messageBox;
    public Button sendMessageButton;
    public ObservableList<String> chatMessages = FXCollections.observableArrayList();
    public Label chattingNamesLabel;
    public TextField amountOfDiceTextField;
    public TextField numberOfSidesTextField;
    public ListView<String> characterSelectOtherListView;
    private String speaker;
    private String listener;

    public void initialize(){
        clientHandler = new DanDWhisperClientHandler(this);
    }
    public void initData(String speaker, String listener, ObservableList<String> observableListSelectableCharactersOther){
        observableListSelectableCharactersOther.remove(listener);
        characterSelectOtherListView.setItems(observableListSelectableCharactersOther);
        this.speaker = speaker;
        this.listener = listener;
        clientHandler.requestOpenChatSession(speaker, listener);
    }
    public void initializeChat(OpenChatSessionResponse openChatSessionResponse) {
        chattingNamesLabel.setText("Chatting as: " + openChatSessionResponse.getSpeaker() + " to: " + openChatSessionResponse.getListener());
        chatMessages.clear();
        chatPane.setItems(chatMessages);
        chatMessages.add("You are now talking to " + openChatSessionResponse.getListener() + " as " + openChatSessionResponse.getSpeaker() + ".");
        chatPane.setItems(chatMessages);
    }
    private void sendMessage(String message){
        clientHandler.requestSendChatMessage(messageBox.getText());
    }

    public void enterPressed(KeyEvent keyEvent) {
            if(keyEvent.getCode().equals(KeyCode.ENTER)){
                sendMessage(messageBox.getText());
                messageBox.setText("");
            }
        }

    public void sendMessageButtonClicked(ActionEvent actionEvent) {
        sendMessage(messageBox.getText());
        messageBox.setText("");
    }

    public void updateChat(SendChatMessageResponse sendChatMessageResponse){
        chatMessages.add(sendChatMessageResponse.getChatMessage());
        chatPane.setItems(chatMessages);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        //sceneSwitcher.switchToCharacterSelectOtherScene(chatPane);
    }

    public void switchToOtherCharacterChatButtonClicked(ActionEvent actionEvent) {
        clientHandler.requestOpenChatSession(speaker, characterSelectOtherListView.getSelectionModel().getSelectedItem());
    }

    public void rollDiceButtonClicked(ActionEvent actionEvent) {
        clientHandler.requestDiceRoll(amountOfDiceTextField.getText(), numberOfSidesTextField.getText());
    }


}
