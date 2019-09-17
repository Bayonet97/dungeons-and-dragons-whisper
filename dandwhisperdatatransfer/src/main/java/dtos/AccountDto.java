package dtos;


import java.util.List;

public class AccountDto {

    private int accountId;

    private String username;

    private boolean gameMaster;

    private List<CharacterDto> characters;

    public AccountDto(int accountId, String username, boolean gameMaster, List<CharacterDto> characters){
        this.accountId = accountId;
        this.username = username;
        this.gameMaster = gameMaster;
        this.characters = characters;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getUsername() {
        return username;
    }

    public boolean isGameMaster() {
        return gameMaster;
    }

    public List<CharacterDto> getCharacters() {
        return characters;
    }
}
