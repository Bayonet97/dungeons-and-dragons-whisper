package listeners;

import clienthandler.IDanDWhisperClientHandler;
import gui.controllers.RegisterLoginController;
import javafx.application.Platform;
import messaging.responses.LoginResponse;
import websocketsclient.DanDWhisperClientEndPoint;

import javax.websocket.ClientEndpoint;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoginChangeListener implements PropertyChangeListener {

    private final RegisterLoginController registerLoginController;
    private final DanDWhisperClientEndPoint clientEndpoint;

    public LoginChangeListener(RegisterLoginController registerLoginController, DanDWhisperClientEndPoint clientEndpoint) {
        this.registerLoginController = registerLoginController;
        this.clientEndpoint = clientEndpoint;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                registerLoginController.login((LoginResponse)evt.getNewValue());
            }
        });
        clientEndpoint.removeListener("login", this);
    }
}
