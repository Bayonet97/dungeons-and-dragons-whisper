package listeners;

import gui.controllers.CharacterSelectOtherController;
import javafx.application.Platform;
import messaging.responses.GetAllCharactersResponse;
import messaging.responses.SendChatMessageResponse;
import websocketsclient.DanDWhisperClientEndPoint;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GetAllCharactersChangeListener implements PropertyChangeListener {

    private CharacterSelectOtherController guiController;
    private final DanDWhisperClientEndPoint clientEndpoint;

    public GetAllCharactersChangeListener(CharacterSelectOtherController guiController, DanDWhisperClientEndPoint clientEndpoint) {
        this.guiController = guiController;
        this.clientEndpoint = clientEndpoint;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                guiController.loadCharactersList((GetAllCharactersResponse)evt.getNewValue());
            }
        });


        clientEndpoint.removeListener("selectcharacter", this);
    }
}
