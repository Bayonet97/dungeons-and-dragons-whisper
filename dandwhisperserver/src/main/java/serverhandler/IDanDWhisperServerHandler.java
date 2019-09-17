package serverhandler;

import messaging.requests.*;
import messaging.responses.*;

import javax.websocket.Session;

public interface IDanDWhisperServerHandler {

    public LoginResponse processLogin(LoginResponse loginResponse, int sessionId);

    public RegisterResponse processRegister(RegisterRequest registerRequest);

    public void processLogout( int sessionId);

    public OpenChatSessionResponse processOpenChatSession(OpenChatSessionRequest openChatSessionRequest,  int sessionId);

    public SendChatMessageResponse processSendChatMessage(SendChatMessageRequest sendChatMessageRequest,  int sessionId);

    public DiceRollResponse processDiceRoll(DiceRollRequest diceRollRequest);

    public GetAllCharactersResponse processGetAllCharacters();

    public void processCreateNewCharacter(CreateNewCharacterRequest createNewCharacterRequest);

    public void processAddPermission(AddPermissionRequest addPermissionRequest);

    public void processRemovePermission(RemovePermissionRequest removePermissionRequest);

    public void processKillCharacter(KillCharacterRequest killCharacterRequest);
}

