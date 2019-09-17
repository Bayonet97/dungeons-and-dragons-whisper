package messaging.requests;

public class OpenChatSessionRequest extends ClientRequest {
    private String speaker;
    private String listener;

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getListener() {
        return listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }
}
