package clienthandler;

import gui.controllers.CharacterSelectOtherController;
import gui.controllers.ChatController;
import gui.controllers.Controller;
import gui.controllers.RegisterLoginController;
import listeners.*;
import messaging.communication.WebSocketMessage;
import messaging.Message;
import websocketsclient.DanDWhisperClientEndPoint;


public class DanDWhisperClientHandler implements IDanDWhisperClientHandler {

    private final DanDWhisperClientEndPoint clientEndPoint;
    private final Controller guiController;
    private ReceiveMessageChangeListener receiveMessageChangeListener;
    private OpenChatChangeListener openChatChangeListener;

    public DanDWhisperClientHandler(Controller controller){
        clientEndPoint = DanDWhisperClientEndPoint.getInstance();
        this.guiController = controller;
    }

    @Override
    public void requestLogin(String username, String password) {
        clientEndPoint.start();

        clientEndPoint.addListener("login", new LoginChangeListener((RegisterLoginController)guiController, clientEndPoint));

        WebSocketMessage webSocketMessage = new WebSocketMessage();
        webSocketMessage.setMessage(Message.LOGIN);

        Object[] params = new Object[]{ username, password};
        webSocketMessage.setParameters(params);

        clientEndPoint.sendRequestToServer(webSocketMessage);

    }

    @Override
    public void requestRegister(String username, String password) {
        clientEndPoint.start();
        clientEndPoint.addListener("register", new RegisterChangeListener((RegisterLoginController)guiController, clientEndPoint));

        WebSocketMessage webSocketMessage = new WebSocketMessage();
        webSocketMessage.setMessage(Message.REGISTER);

        Object[] params = new Object[]{ username, password};
        webSocketMessage.setParameters(params);

        clientEndPoint.sendRequestToServer(webSocketMessage);

    }

    @Override
    public void requestLogout() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void requestOpenChatSession(String speaker, String listener) {
        if(receiveMessageChangeListener != null){
            clientEndPoint.removeListener("receivemessage", receiveMessageChangeListener);
        }
        if(openChatChangeListener != null){
            clientEndPoint.removeListener("openchatsession", openChatChangeListener);
        }

        openChatChangeListener = new OpenChatChangeListener((ChatController)guiController, clientEndPoint);
        receiveMessageChangeListener = new ReceiveMessageChangeListener((ChatController)guiController, clientEndPoint);
        clientEndPoint.addListener("openchatsession", new OpenChatChangeListener((ChatController)guiController, clientEndPoint));
        clientEndPoint.addListener("receivemessage", receiveMessageChangeListener);
        WebSocketMessage webSocketMessage = new WebSocketMessage();
        webSocketMessage.setMessage(Message.OPEN_CHAT_SESSION);

        Object[] params = new Object[]{speaker, listener};
        webSocketMessage.setParameters(params);

        clientEndPoint.sendRequestToServer(webSocketMessage);

    }

    @Override
    public void requestSendChatMessage(String chatMessage) {
        WebSocketMessage webSocketMessage = new WebSocketMessage();
        webSocketMessage.setMessage(Message.SEND_MESSAGE);

        Object[] params = new Object[]{chatMessage};
        webSocketMessage.setParameters(params);

        clientEndPoint.sendRequestToServer(webSocketMessage);
    }

    @Override
    public void requestDiceRoll(String amount, String sides) {
        WebSocketMessage webSocketMessage = new WebSocketMessage();
        webSocketMessage.setMessage(Message.DICE_ROLL);

        Object[] params = new Object[]{amount, sides};
        webSocketMessage.setParameters(params);

        clientEndPoint.sendRequestToServer(webSocketMessage);
    }

    @Override
    public void requestCreateNewCharacter(String characterName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void requestAddPermission(String username, String characterName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void requestRemovePermission(String username, String characterName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void requestKillCharacter(String characterName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void requestGetAllCharacters() {
        clientEndPoint.addListener("selectcharacter", new GetAllCharactersChangeListener((CharacterSelectOtherController)guiController, clientEndPoint));
        WebSocketMessage webSocketMessage = new WebSocketMessage();
        webSocketMessage.setMessage(Message.GET_ALL_CHARACTERS);
        clientEndPoint.sendRequestToServer(webSocketMessage);
    }

}
