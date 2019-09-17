package messaging.responses;

import java.util.ArrayList;

public class LoginResponse extends ServerResponse {

    private String username;

    private boolean credentialsCorrect;

    private boolean gameMaster;

    private ArrayList<String> characterPermissions;

    public LoginResponse(){
    }
    public String getUsername(){return username;}

    public ArrayList<String> getCharacterPermissions() {
        return characterPermissions;
    }

    public boolean isGameMaster() {
        return gameMaster;
    }

    public boolean isCredentialsCorrect() {
        return credentialsCorrect;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCredentialsCorrect(boolean credentialsCorrect) {
        this.credentialsCorrect = credentialsCorrect;
    }

    public void setGameMaster(boolean gameMaster) {
        this.gameMaster = gameMaster;
    }

    public void setCharacterPermissions(ArrayList<String> characterPermissions) {
        this.characterPermissions = characterPermissions;
    }
}
