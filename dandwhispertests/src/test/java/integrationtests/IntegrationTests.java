package integrationtests;

import com.google.gson.Gson;
import messaging.Message;
import messaging.communication.WebSocketMessage;
import messaging.requests.LoginRequest;
import messaging.responses.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import websocketsclient.Communicator;
import websocketsclient.DanDWhisperClientEndPoint;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationTests implements PropertyChangeListener {

    private DanDWhisperClientEndPoint communicator;
    private Gson gson;
    private LoginResponse loginResponse;
    @BeforeEach
    public void beforeEach(){
        communicator = DanDWhisperClientEndPoint.getInstance();
    }


    @Test
    public void try_Logging_In_With_Account() throws InterruptedException {
        WebSocketMessage webSocketMessage = new WebSocketMessage();

        webSocketMessage.setMessage(Message.LOGIN);
        webSocketMessage.setParameters(new Object[]{"test", "test"});
        communicator.addListener("login", this);
        communicator.start();
        communicator.sendRequestToServer(webSocketMessage);

        Thread.sleep(300);

        assertTrue(loginResponse != null);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        loginResponse = (LoginResponse)evt.getNewValue();
    }
}
