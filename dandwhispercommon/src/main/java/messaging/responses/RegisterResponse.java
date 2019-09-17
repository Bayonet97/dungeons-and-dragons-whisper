package messaging.responses;

public class RegisterResponse extends ServerResponse {
    private String username;
    private boolean usernameInUse;

    public RegisterResponse(){
    }

    public String getUsername() {
        return username;
    }

    public boolean isUsernameInUse() {
        return usernameInUse;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUsernameInUse(boolean usernameInUse) {
        this.usernameInUse = usernameInUse;
    }
}
