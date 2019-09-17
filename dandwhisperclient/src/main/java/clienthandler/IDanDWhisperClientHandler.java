package clienthandler;

public interface IDanDWhisperClientHandler {

    public void requestLogin(String username, String password);

    public void requestRegister(String username, String password);

    public void requestLogout();

    public void requestOpenChatSession(String speaker, String listener);

    public void requestSendChatMessage(String chatMessage);

    public void requestDiceRoll(String amount, String sides);

    public void requestCreateNewCharacter(String characterName);

    public void requestAddPermission(String username, String characterName);

    public void requestRemovePermission(String username, String characterName);

    public void requestKillCharacter(String characterName);

    public void requestGetAllCharacters();
}

