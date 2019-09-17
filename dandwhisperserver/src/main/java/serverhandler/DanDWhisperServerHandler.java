package serverhandler;

import diceroller.DiceRoller;
import domain.*;
import domain.Character;
import messaging.requests.*;
import messaging.responses.*;
import rest.RestCaller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DanDWhisperServerHandler implements IDanDWhisperServerHandler{

    private HashMap<String, Character> charactersByCharacterName;
    private List<ChatSession> chatSessions;
    private HashMap<Integer, UserSession> loggedInUsersBySessionId;
    private HashMap<UserSession, ChatSession> chatSessionByUserSession;
    private static final RestCaller restCaller = new RestCaller();

    public DanDWhisperServerHandler(List<String> characterNames) {
        charactersByCharacterName = new HashMap<>();
        List<Character> characters;
        if((characters  = getCharacterObjects(characterNames)) != null){
            for (Character c : characters){
                charactersByCharacterName.put(c.getName(), c);
            }
        }
        loggedInUsersBySessionId = new HashMap<>();
        chatSessionByUserSession = new HashMap<>();
        chatSessions = new ArrayList<>();
    }

    @Override
    public LoginResponse processLogin(LoginResponse loginResponse, int sessionId) {

        if(loginResponse.isSuccess() && loginResponse.isCredentialsCorrect()){
            System.out.println("Successful login with account: " + loginResponse.getUsername());

            loggedInUsersBySessionId.put(sessionId, new UserSession(loginResponse.getUsername(), sessionId));
        }
        else if(!loginResponse.isCredentialsCorrect() && loginResponse.isSuccess()){
            System.out.println("Login failed with account: " + loginResponse.getUsername() + "Credentials incorrect.");
        }
        else if (!loginResponse.isSuccess()) {
            System.out.println("Login failed with account: " + loginResponse.getUsername() + "Something went wrong.");
        }
        return loginResponse;
    }

    @Override
    public RegisterResponse processRegister(RegisterRequest registerRequest) {
        RegisterResponse registerResponse = restCaller.register(registerRequest);

        return registerResponse;
    }

    @Override
    public void processLogout(int sessionId) {
            UserSession user = loggedInUsersBySessionId.get(sessionId);
            ChatSession chatSession = chatSessionByUserSession.get(user);

            chatSession.getUserSessions().remove(user);
            chatSessionByUserSession.remove(user);
            loggedInUsersBySessionId.remove(sessionId);
    }

    @Override
    public OpenChatSessionResponse processOpenChatSession(OpenChatSessionRequest openChatSessionRequest,  int sessionId) {
        Character speakerCharacter = charactersByCharacterName.get(openChatSessionRequest.getSpeaker());
        Character listenerCharacter = charactersByCharacterName.get(openChatSessionRequest.getListener());

        UserSession thisSession = loggedInUsersBySessionId.get(sessionId);
        thisSession.setSpeaker(speakerCharacter);

        OpenChatSessionResponse openChatSessionResponse = new OpenChatSessionResponse();
        openChatSessionResponse.setSpeaker(speakerCharacter.getName());
        openChatSessionResponse.setListener(listenerCharacter.getName());

        //TODO: LOAD CHAT FROM DATABASE

        ChatSession activeChat = chatSessionByUserSession.get(thisSession);
        if (activeChat != null) {
            activeChat.getUserSessions().remove(thisSession);
        }

        for(ChatSession existingChat : chatSessions){
            if(existingChat.getSpeakers().contains(speakerCharacter) && existingChat.getSpeakers().contains(listenerCharacter)){
                existingChat.addUserSession(thisSession);
                chatSessionByUserSession.put(thisSession, existingChat);
                openChatSessionResponse.setSuccess(true);
                return openChatSessionResponse;
            }
        }

        ChatSession newChatSession = new ChatSession();
        newChatSession.addSpeaker(speakerCharacter);
        newChatSession.addSpeaker(listenerCharacter);
        // ...Add that user session as listener to the new chat...
        newChatSession.addUserSession(thisSession);

        // ...Add the new chat to the list of chatSessions.
        chatSessions.add(newChatSession);
        chatSessionByUserSession.put(thisSession, newChatSession);
        openChatSessionResponse.setSuccess(true);
        return openChatSessionResponse;
    }

    @Override
    public SendChatMessageResponse processSendChatMessage(SendChatMessageRequest sendChatMessageRequest, int sessionId) {
        UserSession thisSession = loggedInUsersBySessionId.get(sessionId);;
        SendChatMessageResponse sendChatMessageResponse = new SendChatMessageResponse();

        if(thisSession != null){
            ChatSession chatSession = chatSessionByUserSession.get(thisSession);
            sendChatMessageResponse.setChatMessage(chatSession.createMessage(thisSession.getSpeaker(), sendChatMessageRequest.getMessage()));
            List<Integer> userIds = new ArrayList<>();
            for(UserSession userSession : chatSession.getUserSessions()){
                userIds.add(userSession.getSessionId());
            }

            sendChatMessageResponse.setReceivers(userIds);
        }
        //TODO: SAVE TO DATABASE

        sendChatMessageResponse.setSuccess(true);

        return sendChatMessageResponse;
    }

    @Override
    public DiceRollResponse processDiceRoll(DiceRollRequest diceRollRequest) {
        DiceRoller diceRoller = new DiceRoller();

        List<Integer> results = diceRoller.rollDice(diceRollRequest.getDiceCount(), diceRollRequest.getSides());
        DiceRollResponse diceRollResponse = new DiceRollResponse();

        diceRollResponse.setRollResults(results);

        return diceRollResponse;
    }

    @Override
    public GetAllCharactersResponse processGetAllCharacters() {
        GetAllCharactersResponse getAllCharactersResponse = restCaller.getAllCharacters();

        return getAllCharactersResponse;
    }

    @Override
    public void processCreateNewCharacter(CreateNewCharacterRequest createNewCharacterRequest) {

    }

    @Override
    public void processAddPermission(AddPermissionRequest addPermissionRequest) {

    }

    @Override
    public void processRemovePermission(RemovePermissionRequest removePermissionRequest) {

    }

    @Override
    public void processKillCharacter(KillCharacterRequest killCharacterRequest) {

    }

    private List<Character> getCharacterObjects(List<String> characterNames){
        List<Character> characters = new ArrayList<>();
        for(String characterName : characterNames){
            characters.add(new Character(characterName));
        }
        return characters;
    }
}
