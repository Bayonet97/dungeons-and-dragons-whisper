package messaging.responses;

import domain.ChatMessageBuilder;

import java.util.List;

public class SendChatMessageResponse extends ServerResponse {
        private List<Integer> receivers;
        private String chatMessage;

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public List<Integer> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<Integer> receivers) {
        this.receivers = receivers;
    }
}
