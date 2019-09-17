package listeners;

import clienthandler.IDanDWhisperClientHandler;
import gui.controllers.RegisterLoginController;
import javafx.application.Platform;
import messaging.responses.RegisterResponse;
import websocketsclient.DanDWhisperClientEndPoint;

import javax.websocket.ClientEndpoint;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RegisterChangeListener implements PropertyChangeListener {


    private final RegisterLoginController registerLoginController;
    private final DanDWhisperClientEndPoint clientEndpoint;

    public RegisterChangeListener(RegisterLoginController registerLoginController, DanDWhisperClientEndPoint clientEndpoint) {
        this.registerLoginController = registerLoginController;
        this.clientEndpoint = clientEndpoint;
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                registerLoginController.register((RegisterResponse) evt.getNewValue());
            }
        });
         clientEndpoint.removeListener("register", this);
    }
}
