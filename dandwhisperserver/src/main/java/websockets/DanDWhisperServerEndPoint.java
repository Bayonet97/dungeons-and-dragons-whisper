//package websockets;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//import messaging.communication.WebSocketMessage;
//import messaging.ChatMessageBuilder;
//
//import javax.websocket.*;
//import javax.websocket.server.ServerEndpoint;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author Nico Kuijpers, based on example project
// */
//@ServerEndpoint(value="/servercommunicator/")
//public class DanDWhisperServerEndPoint {
//
//    // All sessions
//    private static final List<Session> sessions = new ArrayList<>();
//
//    // Map each property to list of sessions that are subscribed to that property
//    private static final Map<String,List<Session>> propertySessions = new HashMap<>();
//
//    @OnOpen
//    public void onConnect(Session session) {
//        System.out.println("[WebSocket Connected] SessionID: " + session.getId());
//        String message = String.format("[New client with client side session ID]: %s", session.getId());
//        sessions.add(session);
//        System.out.println("[#sessions]: " + sessions.size());
//    }
//
//    @OnMessage
//    public void onText(String message, Session session) {
//        System.out.println("[WebSocket Session ID] : " + session.getId() + " [Received] : " + message);
//        handleMessageFromClient(message, session);
//    }
//
//    @OnClose
//    public void onClose(CloseReason reason, Session session) {
//        System.out.println("[WebSocket Session ID] : " + session.getId() + " [Socket Closed]: " + reason);
//        sessions.remove(session);
//    }
//
//    @OnError
//    public void onError(Throwable cause, Session session) {
//        System.out.println("[WebSocket Session ID] : " + session.getId() + "[ERROR]: ");
//        cause.printStackTrace(System.err);
//    }
//
//    // Handle incoming message from client
//    private void handleMessageFromClient(String jsonMessage, Session session) {
//        Gson gson = new Gson();
//        WebSocketMessage webSocketMessage = null;
//        try {
//            webSocketMessage = gson.fromJson(jsonMessage, WebSocketMessage.class);
//        }
//        catch (JsonSyntaxException ex) {
//            System.out.println("[WebSocket ERROR: cannot parse Json message " + jsonMessage);
//            return;
//        }
//
//        // Operation defined in message
//        messaging.ChatMessageBuilder operation;
//        operation = webSocketMessage.getMessage();
//
//        // Process message based on operation
//        String property = webSocketMessage.getCharacter();
//        if (null != operation && null != property && !"".equals(property)) {
//            switch (operation) {
//                case REGISTER:
//                    // Register property if not registered yet
//                    if (propertySessions.get(property) == null) {
//                        propertySessions.put(property, new ArrayList<Session>());
//                    }
//                    break;
//                case UNREGISTER:
//                    // Do nothing as property may also have been registered by
//                    // another client
//                    break;
//                case SUBSCRIBE:
//                    // Subsribe to property if the property has been registered
//                    if (propertySessions.get(property) != null) {
//                        propertySessions.get(property).add(session);
//                    }
//                    break;
//                case UNSUBSCRIBE:
//                    // Unsubsribe from property if the property has been registered
//                    if (propertySessions.get(property) != null) {
//                        propertySessions.get(property).remove(session);
//                    }
//                    break;
//                case UPDATE:
//                    // Send the message to all clients that are subscribed to this property
//                    if (propertySessions.get(property) != null) {
//                        System.out.println("[WebSocket send ] " + jsonMessage + " to:");
//                        for (Session sess : propertySessions.get(property)) {
//                            // Use asynchronous messaging.communication
//                            System.out.println("\t\t >> Client associated with server side session ID: " + sess.getId());
//                            sess.getAsyncRemote().sendText(jsonMessage);
//                        }
//                        System.out.println("[WebSocket end sending message to subscribers]");
//                    }
//                    break;
//                default:
//                    System.out.println("[WebSocket ERROR: cannot process Json message " + jsonMessage);
//                    break;
//            }
//        }
//    }
//}

package websockets;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import messaging.communication.WebSocketMessage;
import messaging.Message;
import messaging.requests.*;
import messaging.responses.*;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import rest.RestCaller;
import serverhandler.DanDWhisperServerHandler;
import serverhandler.IDanDWhisperServerHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


/**
 * @author Nico Kuijpers
 */
@WebSocket(maxIdleTime = 100000000)
@ServerEndpoint(value="/servercommunicator/")
public class DanDWhisperServerEndPoint {

    // All sessions
    private static final List<Session> sessions = new ArrayList<>();

    private static RestCaller restCaller = new RestCaller();
    private static final IDanDWhisperServerHandler serverHandler = new DanDWhisperServerHandler(restCaller.getAllCharacters().getCharacters());
    private static final HashMap<Integer, Session> usersById = new HashMap<>();
    private static final HashMap<Session, Integer> usersBySession = new HashMap<>();
    private static Integer id = 0;

    @OnOpen
    public void onConnect(Session session) {
        System.out.println("[WebSocket Connected] SessionID: " + session.getId());
        String message = String.format("[New client with client side session ID]: %s", session.getId());
        sessions.add(session);
        if(usersBySession.containsKey(session)){
            // nothing
        }
        else{
            id++;
            usersById.put(id, session);
            usersBySession.put(session, id);
        }
        System.out.println("[#sessions]: " + sessions.size());
    }

    @OnMessage
    public void onText(String message, Session session) {
        System.out.println("[WebSocket Session ID] : " + session.getId() + " [Received] : " + message);
        handleMessageFromClient(message, session);
    }

    @OnClose
    public void onClose(CloseReason reason, Session session) {
        System.out.println("[WebSocket Session ID] : " + session.getId() + " [Socket Closed]: " + reason);
        sessions.remove(session);
    }

    @OnError
    public void onError(Throwable cause, Session session) {
        System.out.println("[WebSocket Session ID] : " + session.getId() + "[ERROR]: ");
        cause.printStackTrace(System.err);
    }

    // Handle incoming message from client
    private void handleMessageFromClient(String jsonClientMessage, Session session) {
        Gson gson = new Gson();
        WebSocketMessage webSocketRequestMessage = null;
        try {
            webSocketRequestMessage = gson.fromJson(jsonClientMessage, WebSocketMessage.class);
        }
        catch (JsonSyntaxException ex) {
            System.out.println("[WebSocket ERROR: cannot parse Json message " + jsonClientMessage);
            return;
        }

        Message requestMessage = webSocketRequestMessage.getMessage();
        Object[] requestParameters = webSocketRequestMessage.getParameters();

        WebSocketMessage webSocketResponseMessage = new WebSocketMessage();
        Message responseMessage = null;
        Object[] responseParameters = null;

        if (null != requestMessage) {
            switch (requestMessage) {
                case LOGIN:
                    // Build Request
                    String username = (String) requestParameters[0];
                    String password = (String) requestParameters[1];
                    LoginRequest loginRequest = new LoginRequest();
                    loginRequest.setUsername(username);
                    loginRequest.setPassword(password);
                    LoginResponse loginResponse = restCaller.login(loginRequest);
                    // Process Request and get Response
                    serverHandler.processLogin(loginResponse, usersBySession.get(session));

                    // Set Response Values
                    responseMessage = Message.LOGIN;
                    responseParameters = new Object[]{
                      loginResponse.isSuccess(), loginResponse.getUsername(),  loginResponse.isGameMaster(), loginResponse.getCharacterPermissions(), loginResponse.isCredentialsCorrect()
                    };

                    break;
                case REGISTER:
                    username = (String) requestParameters[0];
                    password = (String) requestParameters[1];

                    RegisterRequest registerRequest = new RegisterRequest();
                    registerRequest.setUsername(username);
                    registerRequest.setPassword(password);

                    // Process Request and get Response
                    RegisterResponse registerResponse = serverHandler.processRegister(registerRequest);

                    // Set Response Values
                    responseMessage = Message.REGISTER;
                    responseParameters = new Object[]{
                            registerResponse.isSuccess(), registerResponse.getUsername(), registerResponse.isUsernameInUse()
                    };
                    break;
                case LOGOUT:
                    serverHandler.processLogout(usersBySession.get(session));
                    break;
                case OPEN_CHAT_SESSION:
                    OpenChatSessionRequest openChatSessionRequest = new OpenChatSessionRequest();
                    String speaker = (String) requestParameters[0];
                    String listener = (String) requestParameters[1];
                    openChatSessionRequest.setSpeaker(speaker);
                    openChatSessionRequest.setListener(listener);

                    OpenChatSessionResponse openChatSessionResponse = serverHandler.processOpenChatSession(openChatSessionRequest, usersBySession.get(session));

                    // Set Response Values
                    responseMessage = Message.OPEN_CHAT_SESSION;
                    responseParameters = new Object[]{
                            openChatSessionResponse.isSuccess(), openChatSessionResponse.getSpeaker(), openChatSessionResponse.getListener()
                    };
                    break;
                case SEND_MESSAGE:
                    SendChatMessageRequest sendChatMessageRequest = new SendChatMessageRequest();
                    sendChatMessageRequest.setMessage((String) requestParameters[0]);
                    SendChatMessageResponse sendChatMessageResponse = serverHandler.processSendChatMessage(sendChatMessageRequest, usersBySession.get(session));
                    responseMessage = Message.SEND_MESSAGE;
                    for(Integer sessionIdToReceive : sendChatMessageResponse.getReceivers()){
                        responseParameters = new Object[]{sendChatMessageResponse.getChatMessage()};

                        webSocketResponseMessage = new WebSocketMessage();
                        webSocketResponseMessage.setMessage(responseMessage);
                        webSocketResponseMessage.setParameters(responseParameters);

                        String jsonResponseMessage = gson.toJson(webSocketResponseMessage);
                        Session sessionToReceive = usersById.get(sessionIdToReceive);
                        sessionToReceive.getAsyncRemote().sendText(jsonResponseMessage);
                    }

                    break;
                case GET_ALL_CHARACTERS:
                    GetAllCharactersResponse getAllCharactersResponse = serverHandler.processGetAllCharacters();

                    responseMessage = Message.GET_ALL_CHARACTERS;
                    responseParameters = new Object[]{
                            getAllCharactersResponse.isSuccess(), getAllCharactersResponse.getCharacters()
                    };
                    break;
                case DICE_ROLL:
                    DiceRollRequest diceRollRequest = new DiceRollRequest();
                    diceRollRequest.setDiceCount(Integer.parseInt((String)requestParameters[0]));
                    diceRollRequest.setSides(Integer.parseInt((String)requestParameters[1]));
                    DiceRollResponse diceRollResponse = serverHandler.processDiceRoll(diceRollRequest);

                    SendChatMessageRequest sendDiceRollToChatRequest = new SendChatMessageRequest();
                    String rollResultMessage = "Rolled " + diceRollRequest.getDiceCount() + "d" + diceRollRequest.getSides() + ". Results: ";

                    for(int result : diceRollResponse.getRollResults()) {
                        rollResultMessage =  rollResultMessage.concat(result + " ");
                    }
                    sendDiceRollToChatRequest.setMessage(rollResultMessage);
                    SendChatMessageResponse sendDiceRollResponse = serverHandler.processSendChatMessage(sendDiceRollToChatRequest, usersBySession.get(session));
                    responseMessage = Message.SEND_MESSAGE;
                    for(Integer sessionIdToReceive : sendDiceRollResponse.getReceivers()){
                        responseParameters = new Object[]{sendDiceRollResponse.getChatMessage()};

                        webSocketResponseMessage = new WebSocketMessage();
                        webSocketResponseMessage.setMessage(responseMessage);
                        webSocketResponseMessage.setParameters(responseParameters);

                        String jsonResponseMessage = gson.toJson(webSocketResponseMessage);
                        Session sessionToReceive = usersById.get(sessionIdToReceive);
                        sessionToReceive.getAsyncRemote().sendText(jsonResponseMessage);
                    }
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
                    System.out.println("[WebSocket ERROR: cannot process Json message " + jsonClientMessage);
                    break;
            }
            if(responseMessage != Message.SEND_MESSAGE){
                webSocketResponseMessage.setMessage(responseMessage);
                webSocketResponseMessage.setParameters(responseParameters);
                String jsonResponseMessage = gson.toJson(webSocketResponseMessage);
                session.getAsyncRemote().sendText(jsonResponseMessage);
            }
        }

    }

    private void sendChatMessage(){

    }
}
