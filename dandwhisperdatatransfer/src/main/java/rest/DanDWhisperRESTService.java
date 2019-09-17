package rest;

import authentication.PasswordHasher;
import authentication.PasswordValidator;
import dal.AccountDataAccess;
import dal.CharacterDataAccess;
import dtos.AccountDto;
import dtos.CharacterDto;
import dtos.CharacterPermissionsDto;
import dtos.LoginDto;
import messaging.requests.LoginRequest;
import messaging.requests.RegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

@Path("/dandwhisper")
public class DanDWhisperRESTService {

    private static final Logger log = LoggerFactory.getLogger(DanDWhisperRESTService.class);
    private final AccountDataAccess accountDataAccess = new AccountDataAccess();
    private final CharacterDataAccess characterDataAccess = new CharacterDataAccess();
    public DanDWhisperRESTService(){}


    @PUT @Consumes(MediaType.APPLICATION_JSON)
    @Path("/account/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(final LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        System.out.println("Login request");

        if (username == null || password == null) {
            // Client error 400 - Bad Request
            return Response.status(400).entity(RestResponseHelper.getLoginResponseString(null, false, false)).build();
        }
        LoginDto loginDto = accountDataAccess.getCredentials(username);
        if(loginDto != null){
            try {
                // Check if credentials match.
                boolean match =  PasswordValidator.validatePassword(password, loginDto.getHashedPassword());
                if(match){
                    // Login to the account and return the Account details.
                    AccountDto accountDto = accountDataAccess.login(username);
                    return Response.status(200).entity(RestResponseHelper.getLoginResponseString(accountDto, true, true)).build();
                }
                else{
                    return Response.status(200).entity(RestResponseHelper.getLoginResponseString(null, false, true)).build();
                }
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
        else
            return Response.status(200).entity(RestResponseHelper.getLoginResponseString(null, false, true)).build();
        // Something went wrong
        return Response.status(400).entity(RestResponseHelper.getLoginResponseString(null, false, false)).build();
    }

    @POST @Consumes(MediaType.APPLICATION_JSON)
    @Path("/account/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(final RegisterRequest registerRequest) {
        System.out.println("Register request");

        String username = registerRequest.getUsername();

        // Check request
        if (username == null || registerRequest.getPassword() == null) {
            // Client error 400 - Bad Request
            return Response.status(400).entity(RestResponseHelper.getRegisterResponseString(username, false, false)).build();
        }
        if(accountDataAccess.getUserAlreadyExists(username)){
            return Response.status(200).entity(RestResponseHelper.getRegisterResponseString(username, true, true)).build();
        }
        else{
            try {  // Hash password
                String hashedPassword = PasswordHasher.generateStrongPasswordHash(registerRequest.getPassword());
                // Register user in database.
                boolean registerSuccess = accountDataAccess.registerUser(username, hashedPassword);

                return Response.status(200).entity(RestResponseHelper.getRegisterResponseString(username, false, registerSuccess)).build();
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
        // Something went wrong
        return Response.status(400).entity(RestResponseHelper.getRegisterResponseString(username, false, false)).build();
    }


    @GET
    @Path("/character/getallcharacters")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCharacters(){
        log.info("[Server getAllCharacters]");

        List<CharacterDto> characterDtos = characterDataAccess.getAllCharacters();

        // Check whether pet exists
        if (characterDtos == null) {
            // Client error 400 - Bad messaging.ChatMessageBuilder
            return Response.status(400).entity(RestResponseHelper.getAllCharactersResponseString(null, false)).build();
        }

        // Define response
        return Response.status(200).entity(RestResponseHelper.getAllCharactersResponseString(characterDtos, true)).build();
    }

//
//    @GET
//    @Path("/account/getcharacterpermissions/{accountid}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getCharacterPermissions(@PathParam("accountid") String accountIdString){
//        log.info("[Server getPermissions]");
//
//        // Find account
//        int accountId = Integer.parseInt(accountIdString);
//
//        CharacterPermissionsDto characterPermissionsDto = accountDataAccess.getCharacterPermissions(accountId);
//
//        // Check whether pet exists
//        if (characterPermissionsDto == null) {
//            // Client error 400 - Bad messaging.ChatMessageBuilder
//            return Response.status(400).entity(RestResponseHelper.getCharacterPermissionsResponseString(null, false)).build();
//        }
//
//        // Define response
//        return Response.status(200).entity(RestResponseHelper.getCharacterPermissionsResponseString(characterPermissionsDto, true)).build();
//    }
}
