package listeners;

import gui.controllers.ChatController;
import gui.controllers.SceneSwitcher;
import javafx.application.Platform;
import messaging.responses.OpenChatSessionResponse;
import websocketsclient.DanDWhisperClientEndPoint;

import javax.websocket.ClientEndpoint;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class OpenChatChangeListener implements PropertyChangeListener {
    private final ChatController chatController;
    private final DanDWhisperClientEndPoint clientEndpoint;

    public OpenChatChangeListener(ChatController chatController,  DanDWhisperClientEndPoint clientEndpoint) {
        this.chatController = chatController;
        this.clientEndpoint = clientEndpoint;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatController.initializeChat((OpenChatSessionResponse) evt.getNewValue());
            }
        });
        clientEndpoint.removeListener("openchatsession", this);
    }
}
