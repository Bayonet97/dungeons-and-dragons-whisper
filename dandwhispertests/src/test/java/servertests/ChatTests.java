package servertests;

import domain.Character;
import domain.UserSession;
import messaging.requests.DiceRollRequest;
import messaging.requests.LoginRequest;
import messaging.requests.OpenChatSessionRequest;
import messaging.requests.SendChatMessageRequest;
import messaging.responses.DiceRollResponse;
import messaging.responses.LoginResponse;
import messaging.responses.OpenChatSessionResponse;
import messaging.responses.SendChatMessageResponse;
import org.eclipse.jetty.websocket.common.WebSocketSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import serverhandler.DanDWhisperServerHandler;
import serverhandler.IDanDWhisperServerHandler;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChatTests {

    static IDanDWhisperServerHandler serverHandler;
    private List<String> characters;

    @BeforeAll
    public void setUp(){
        characters = new ArrayList<>();
        characters.add("Omnia");
        characters.add("Liva");
        characters.add("Ornn");

        serverHandler = new DanDWhisperServerHandler(characters);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUsername("Test");
        loginResponse.setSuccess(true);
        loginResponse.setCredentialsCorrect(true);

        serverHandler.processLogin(loginResponse, 1);
    }

    @Test
    public void should_Create_New_Chat_Successfully(){
        //Arrange
        Character speaker = new Character("Liva");
        Character listener = new Character("Omnia");

        OpenChatSessionRequest openChatSessionRequest = new OpenChatSessionRequest();
        openChatSessionRequest.setListener(listener.getName());
        openChatSessionRequest.setSpeaker(speaker.getName());

        UserSession userSession = new UserSession("test", 1);
        userSession.setSpeaker(speaker);

        //Act
        OpenChatSessionResponse openChatSessionResponse = serverHandler.processOpenChatSession(openChatSessionRequest, userSession.getSessionId());

        //Assert
        assertTrue(openChatSessionResponse.isSuccess() && openChatSessionResponse.getListener().equals(listener.getName()) && openChatSessionResponse.getSpeaker().equals(speaker.getName()));
    }

    @Test
    public void should_Join_Existing_Chat_Successfully(){
        //Arrange
        Character speaker = new Character("Liva");
        Character listener = new Character("Omnia");

        OpenChatSessionRequest openChatSessionRequest = new OpenChatSessionRequest();
        openChatSessionRequest.setListener(listener.getName());
        openChatSessionRequest.setSpeaker(speaker.getName());

        UserSession userSession = new UserSession("test", 1);
        userSession.setSpeaker(speaker);
        serverHandler.processOpenChatSession(openChatSessionRequest, 1);

        //Act
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUsername("Test1");
        loginResponse.setSuccess(true);
        loginResponse.setCredentialsCorrect(true);
        serverHandler.processLogin(loginResponse, 2);

        OpenChatSessionRequest openChatSessionRequest1 = new OpenChatSessionRequest();
        openChatSessionRequest1.setListener(listener.getName());
        openChatSessionRequest1.setSpeaker(speaker.getName());
        OpenChatSessionResponse openChatSessionResponse = serverHandler.processOpenChatSession(openChatSessionRequest1, 2);

        //Assert
        assertTrue(openChatSessionResponse.isSuccess() && openChatSessionResponse.getListener().equals(listener.getName()) && openChatSessionResponse.getSpeaker().equals(speaker.getName()));
    }

    //TODO: Fix from here
    @Test
    public void should_Send_Message_Successfully(){
        //Arrange
        SendChatMessageRequest sendChatMessageRequest = new SendChatMessageRequest();
        sendChatMessageRequest.setSender("Omnia");
        sendChatMessageRequest.setReceiver("Liva");
        sendChatMessageRequest.setMessage("Test Message");

        //Act
        SendChatMessageResponse sendChatMessageResponse = new SendChatMessageResponse();
        //sendChatMessageResponse = serverHandler.processSendChatMessage(sendChatMessageRequest);

        //Assert
        assertTrue(sendChatMessageResponse.isSuccess());
    }

    @Test
    public void should_Send_Dice_Roll_Result_To_Chat_Successfully(){
        //Arrange
        DiceRollRequest diceRollRequest = new DiceRollRequest();
        diceRollRequest.setDiceCount(6);
        diceRollRequest.setSides(6);

        //Act
        DiceRollResponse diceRollResponse = serverHandler.processDiceRoll(diceRollRequest);

        //Assert
        assertTrue(diceRollResponse.isSuccess());
    }
    @Test
    public void should_Log_Out_Successfully(){
      //  serverHandler.processLogout();
    }
}
