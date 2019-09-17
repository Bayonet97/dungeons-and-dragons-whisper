package domain;

public class ChatMessageBuilder {

    public static String buildMessage(String sender, String message) {
        return sender + ": " + message;
    }
}
