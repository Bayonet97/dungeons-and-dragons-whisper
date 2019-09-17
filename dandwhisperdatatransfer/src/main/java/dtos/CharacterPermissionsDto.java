package dtos;

import domain.Account;

import java.util.List;

public class CharacterPermissionsDto {

    private final List<CharacterDto> characterPermissions;

    public CharacterPermissionsDto(List<CharacterDto> characterPermissions){
        this.characterPermissions = characterPermissions;
    }


    public List<CharacterDto> getCharacterPermissions(){return characterPermissions;}
}
