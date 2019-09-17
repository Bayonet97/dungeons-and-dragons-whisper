package websocketsclient;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import messaging.communication.WebSocketMessage;
import messaging.Message;
import messaging.responses.*;

import javax.websocket.*;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Nico Kuijpers, based on example project
 */

@ClientEndpoint
public class DanDWhisperClientEndPoint extends Communicator{

    private static DanDWhisperClientEndPoint instance = null;

    private final String uri = "ws://localhost:8095/servercommunicator/";

    private Session session;

    private String message;

    private Gson gson = null;

    // Status of the webSocket client
    boolean  isRunning = false;

    public DanDWhisperClientEndPoint() {
        gson = new Gson();
    }

    public void addListener(String eventName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(eventName, listener);
        System.out.println("New listener count after adding {%s}, count: %s" +  listener.getClass().getSimpleName() + propertyChangeSupport.getPropertyChangeListeners().length);
    }

    public void removeListener(String eventName, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(eventName, listener);
        System.out.println("New listener count after removing {%s}, count: %s" + listener.getClass().getSimpleName() + propertyChangeSupport.getPropertyChangeListeners().length);
    }
    /**
     * Get singleton instance of this class.
     * Ensure that only one instance of this class is created.
     * @return instance of client web socket
     */
    public static DanDWhisperClientEndPoint getInstance() {
        if (instance == null) {
            System.out.println("[WebSocket Client create singleton instance]");
            instance = new DanDWhisperClientEndPoint();
        }
        return instance;
    }
    /**
     *  Start the connection.
     */
    public void start() {
        System.out.println("[WebSocket Client start connection]");
        if (!isRunning) {
            startClient();
            isRunning = true;
        }
    }
    /**
     *  Stop the connection.
     */
    public void stop() {
        System.out.println("[WebSocket Client stop]");
        if (isRunning) {
            stopClient();
            isRunning = false;
        }
    }

    @OnOpen
    public void onWebSocketConnect(Session session){
        System.out.println("[WebSocket Client open session] " + session.getRequestURI());
        this.session = session;
    }

    @OnMessage
    public void onWebSocketText(String message, Session session){
        this.message = message;
        System.out.println("[WebSocket Client message received] " + message);
        processMessage(message);
    }

    @OnError
    public void onWebSocketError(Session session, Throwable cause) {
        System.out.println("[WebSocket Client connection error] " + cause.toString());
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason){
        System.out.print("[WebSocket Client close session] " + session.getRequestURI());
        System.out.println(" for reason " + reason);
        session = null;
    }

    public void sendRequestToServer(WebSocketMessage webSocketMessage) {

        String jsonMessage = gson.toJson(webSocketMessage);
        // Use asynchronous messaging.communication
        session.getAsyncRemote().sendText(jsonMessage);
    }

    /**
     * Get the latest message received from the websocket messaging.communication.
     * @return The message from the websocket messaging.communication
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message, but no action is taken when the message is changed.
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Start a WebSocket client.
     */
    private void startClient() {
        System.out.println("[WebSocket Client start]");
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(uri));

        } catch (IOException | URISyntaxException | DeploymentException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Stop the client when it is running.
     */
    private void stopClient(){
        System.out.println("[WebSocket Client stop]");
        try {
            session.close();

        } catch (IOException ex){
            // do something useful eventually
            ex.printStackTrace();
        }
    }

    // Process incoming json message
    private void processMessage(String jsonMessage) {

        // Parse incoming message
        WebSocketMessage webSocketMessage;
        try {
            webSocketMessage = gson.fromJson(jsonMessage, WebSocketMessage.class);
        }
        catch (JsonSyntaxException ex) {
            System.out.println("[WebSocket Client ERROR: cannot parse Json message " + jsonMessage);
            return;
        }

        // Get the type of response
        Message message;
        message = webSocketMessage.getMessage();

        // Obtain content from message
        Object[] paramaters = webSocketMessage.getParameters();

        if (paramaters == null || paramaters.length == 0) {
            System.out.println("[WebSocket Client ERROR: message without content]");
            return;
        }

        if (null != message) {
            switch (message) {
                case LOGIN:
                    LoginResponse loginResponse = new LoginResponse();
                    loginResponse.setUsername((String)paramaters[1]);
                    loginResponse.setGameMaster((boolean)paramaters[2]);
                    loginResponse.setCharacterPermissions((ArrayList<String>)paramaters[3]);
                    loginResponse.setCredentialsCorrect((boolean)paramaters[4]);
                    loginResponse.setSuccess((boolean)paramaters[0]);
                    System.out.println("Attempted login with " + loginResponse.getUsername() + ", Is game master: " + loginResponse.isGameMaster() + ", Has character access to: " + loginResponse.getCharacterPermissions());

                    propertyChangeSupport.firePropertyChange("login", null, loginResponse);
                    break;
                case REGISTER:
                    RegisterResponse registerResponse = new RegisterResponse();
                    registerResponse.setUsername((String)paramaters[1]);
                    registerResponse.setUsernameInUse((boolean)paramaters[2]);
                    registerResponse.setSuccess((boolean)paramaters[0]);
                    System.out.println("Attempted register with " + registerResponse.getUsername() + ", Username already in use: " + registerResponse.isUsernameInUse());

                    propertyChangeSupport.firePropertyChange("register", null, registerResponse);
                    break;
                case LOGOUT:
                    break;
                case GET_ALL_CHARACTERS :
                    GetAllCharactersResponse getAllCharactersResponse = new GetAllCharactersResponse();
                    getAllCharactersResponse.setSuccess((boolean)paramaters[0]);
                    getAllCharactersResponse.setCharacters((List<String>)paramaters[1]);
                    propertyChangeSupport.firePropertyChange("selectcharacter", null, getAllCharactersResponse);
                    break;
                case OPEN_CHAT_SESSION:
                    OpenChatSessionResponse openChatSessionResponse = new OpenChatSessionResponse();
                    if((boolean)paramaters[0]){
                        openChatSessionResponse.setSpeaker((String)paramaters[1]);
                        openChatSessionResponse.setListener((String)paramaters[2]);
                    }
                    propertyChangeSupport.firePropertyChange("openchatsession", null, openChatSessionResponse);
                    break;
                case SEND_MESSAGE:
                    SendChatMessageResponse sendChatMessageResponse = new SendChatMessageResponse();
                    sendChatMessageResponse.setChatMessage((String)paramaters[0]);
                    System.out.println("Received Message:" + sendChatMessageResponse.getChatMessage());
                    propertyChangeSupport.firePropertyChange("receivemessage", null, sendChatMessageResponse);
                    break;
                case DICE_ROLL:
                    break;
                case CREATE_NEW_CHARACTER:
                    break;
                case ADD_PERMISSION:
                    break;
                case REMOVE_PERMISSION:
                    break;
                case KILL_CHARACTER:
                    break;
                default:
                    System.out.println("[WebSocket ERROR: cannot process Json message " + jsonMessage);
                    break;
            }
        }

    }
}
