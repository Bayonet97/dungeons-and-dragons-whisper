package domain;

import java.util.ArrayList;
import java.util.List;

public class ChatSession{
    private List<Character> speakers = new ArrayList<>(2);
    private List<UserSession> userSessions = new ArrayList<>();


    public List<Character> getSpeakers() {
        return speakers;
    }

    public void addSpeaker(Character speaker) {
        this.speakers.add(speaker);
    }

    public List<UserSession> getUserSessions() {
        return userSessions;
    }

    public void addUserSession(UserSession userSession) {
        this.userSessions.add(userSession);
    }

    public String createMessage(Character speaker, String message) {
        return ChatMessageBuilder.buildMessage(speaker.getName(), message);
    }
}
