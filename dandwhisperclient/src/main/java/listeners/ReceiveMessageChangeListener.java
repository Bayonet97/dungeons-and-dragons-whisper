package listeners;

import clienthandler.DanDWhisperClientHandler;
import gui.controllers.ChatController;
import javafx.application.Platform;
import messaging.responses.OpenChatSessionResponse;
import messaging.responses.SendChatMessageResponse;
import websocketsclient.DanDWhisperClientEndPoint;

import javax.websocket.ClientEndpoint;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ReceiveMessageChangeListener implements PropertyChangeListener {

    private final ChatController guiController;
    private final DanDWhisperClientEndPoint clientEndpoint;

    public ReceiveMessageChangeListener(ChatController guiController, DanDWhisperClientEndPoint clientEndpoint) {
        this.guiController = guiController;
        this.clientEndpoint = clientEndpoint;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                guiController.updateChat((SendChatMessageResponse) evt.getNewValue());
            }
        });
    }
}
