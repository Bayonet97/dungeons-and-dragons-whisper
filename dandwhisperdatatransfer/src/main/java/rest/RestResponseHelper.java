package rest;

import com.google.gson.Gson;
import dtos.AccountDto;
import dtos.CharacterDto;
import dtos.CharacterPermissionsDto;
import messaging.responses.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

class RestResponseHelper {

    private static final Logger log = LoggerFactory.getLogger(DanDWhisperRESTService.class);
    private static final Gson gson = new Gson();

    static String getErrorResponseString()
    {
        ServerResponse response = new ErrorResponse();

        response.setSuccess(false);
        String output = gson.toJson(response);
        log.info("[Server response] " + output);
        return output;
    }

    static String getLoginResponseString(AccountDto accountDto, boolean credentialsCorrect, boolean success){
        LoginResponse loginResponse = null;

        loginResponse = new LoginResponse();
        if(accountDto != null){
            if(accountDto.getCharacters() != null){
                ArrayList<String> characterNames = new ArrayList<>();
                for(CharacterDto character : accountDto.getCharacters()){
                    characterNames.add(character.getName());
                }
                loginResponse.setCharacterPermissions(characterNames);
            }
            loginResponse.setUsername(accountDto.getUsername());
            loginResponse.setGameMaster(accountDto.isGameMaster());
        }


        loginResponse.setCredentialsCorrect(credentialsCorrect);
        loginResponse.setSuccess(success);

        String output = gson.toJson(loginResponse);
        log.info("[Server response] " + output);
        return output;
    }
    static String getRegisterResponseString(String username, boolean usernameInUse, boolean success){
        RegisterResponse registerResponse = null;

        registerResponse = new RegisterResponse();
        registerResponse.setUsername(username);
        registerResponse.setUsernameInUse(usernameInUse);
        registerResponse.setSuccess(success);

        String output = gson.toJson(registerResponse);
        log.info("[Server response] " + output);
        return output;
    }

    static String getAllCharactersResponseString(List<CharacterDto> characterDtos, boolean success) {
        GetAllCharactersResponse getAllCharactersResponse = null;
        getAllCharactersResponse = new GetAllCharactersResponse();
        List<String> characters = new ArrayList<>();

        if (characterDtos != null) {
            for (CharacterDto characterDto : characterDtos) {
                characters.add(characterDto.getName());
            }

            getAllCharactersResponse.setCharacters(characters);
        }
        getAllCharactersResponse.setSuccess(success);

        String output = gson.toJson(getAllCharactersResponse);
        log.info("[Server response] " + output);
        return output;
    }

//    static String getCharacterPermissionsResponseString(CharacterPermissionsDto characterPermissionsDto, boolean success){
//        GetCharacterPermissionsSelfResponse getCharacterPermissionsSelfResponse = null;
//        getCharacterPermissionsSelfResponse = new GetCharacterPermissionsSelfResponse();
//
//        if(characterPermissionsDto != null)
//            getCharacterPermissionsSelfResponse.setCharacterPermissions(characterPermissionsDto.getCharacterPermissions());
//
//        getCharacterPermissionsSelfResponse.setSuccess(success);
//
//        String output = gson.toJson(getCharacterPermissionsSelfResponse);
//        log.info("[Server response] " + output);
//        return output;
//    }
}
