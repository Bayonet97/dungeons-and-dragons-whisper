package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {

    private final String username;
    private final String password;
    private boolean gameMaster;
    private ArrayList<Character> characterPermissions;

    public Account(String username, String password, boolean isGameMaster, ArrayList<Character> characterPermissions){
        this.username = username;
        this.password = password;
        this.gameMaster = isGameMaster;
        this.characterPermissions = characterPermissions;
    }

    public String getUsername() {
        return username;
    }

    public void setIsGameMaster(boolean isGameMaster){ this.gameMaster = isGameMaster; }

    public ArrayList<Character> getCharacterPermissions(){return characterPermissions;}

    public boolean getIsGameMaster(){ return gameMaster; }

    public boolean verifyPassword(String password){ return this.password.equals(password); }

    public void addCharacterPermission(Character character){
        if(!characterPermissions.contains(character))
            characterPermissions.add(character);
    }

    public void removeCharacterPermission(Character character){
        characterPermissions.remove(character);
    }
}
