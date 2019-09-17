package domain;


public class UserSession {
    private final String _username;
    private final int _sessionId;
    private Character speaker;

    public UserSession(String username, int _sessionId) {
        this._sessionId = _sessionId;
        _username = username;
    }

    public Character getSpeaker(){
        return speaker;
    }
    public void setSpeaker(Character speaker) {
        this.speaker = speaker;
    }


    public int getSessionId() {
        return _sessionId;
    }
}
