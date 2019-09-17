package rest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import messaging.Message;
import messaging.requests.LoginRequest;
import messaging.requests.RegisterRequest;
import messaging.responses.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class RestCaller {

    private final String url = "http://localhost:21075/dandwhisper";

    private final Gson gson = new Gson();
    private Message message;
    public RestCaller() {
        // Nothing
    }

    /**
     * Login with given username and password.
     * @param loginRequest object with username and unhashed password values
     * @return LoginResponse with username, isGameMaster, credentialsCorrect, Character permission names and success
     */
    public LoginResponse login(LoginRequest loginRequest) {
        message = Message.LOGIN;
        String queryPut = "/account/login";
        StringEntity params;
        LoginResponse response = null;
        try {
            params = new StringEntity(gson.toJson(loginRequest));
            response = (LoginResponse)executeQueryPut(params,queryPut);
        } catch (UnsupportedEncodingException ex) {
            System.out.println("Error Putting: " + ex);
        }
        return response;
    }

    public RegisterResponse register(RegisterRequest registerRequest){
        message = Message.REGISTER;
        String queryPost = "/account/register";
        RegisterResponse response = null;
        StringEntity params;
        try {
            params = new StringEntity(gson.toJson(registerRequest));
            response = (RegisterResponse)executeQueryPost(params,queryPost);
        } catch (UnsupportedEncodingException ex) {
            System.out.println("Error Posting: " + ex);
        }
        return response;
    }


    public GetAllCharactersResponse getAllCharacters() {
        message = Message.GET_ALL_CHARACTERS;
        String queryGet = "/character/getallcharacters";
        GetAllCharactersResponse response = null;
        StringEntity params;
        response = (GetAllCharactersResponse)executeQueryGet(queryGet);
        return response;
    }

    private ServerResponse executeQueryGet(String queryGet) {
        // Build the query for the REST service
        final String query = url + queryGet;
        System.out.println("[Query Get] : " + query);

        // Execute the HTTP GET request
        HttpGet httpGet = new HttpGet(query);
        return executeHttpUriRequest(httpGet);
    }

    private ServerResponse executeQueryPost(StringEntity params, String queryPost) {
        // Build the query for the REST service
        final String query = url + queryPost;
        System.out.println("[Query Post] : " + query);

        // Execute the HTTP POST request
        HttpPost httpPost = new HttpPost(query);
        httpPost.addHeader("content-type", "application/json");
        httpPost.setEntity(params);

        return executeHttpUriRequest(httpPost);
    }

    private ServerResponse executeQueryPut(StringEntity params, String queryPut) {
        // Build the query for the REST service
        final String query = url + queryPut;
        System.out.println("[Query Put] : " + query);

        // Execute the HTTP PUT request
        HttpPut httpPut = new HttpPut(query);
        httpPut.addHeader("content-type", "application/json");
        httpPut.setEntity(params);

        return executeHttpUriRequest(httpPut);
    }

    private ServerResponse executeQueryDelete(String queryDelete) {

        // Build the query for the REST service
        final String query = url + queryDelete;
        System.out.println("[Query Delete] : " + query);

        // Execute the HTTP DELETE request
        HttpDelete httpDelete = new HttpDelete(query);
        return executeHttpUriRequest(httpDelete);
    }

    private ServerResponse executeHttpUriRequest(HttpUriRequest httpUriRequest) {
        ServerResponse serverResponse = null;
        // Execute the HttpUriRequest
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpUriRequest)) {
            System.out.println("[Status Line] : " + response.getStatusLine());
            HttpEntity entity = response.getEntity();
            final String entityString = EntityUtils.toString(entity);
            System.out.println("[Entity] : " + entityString);
            //TODO: Change this to be of type (class variable?)
            switch(message){
                case LOGIN :
                    serverResponse = gson.fromJson(entityString, LoginResponse.class);
                    break;
                case REGISTER :
                    serverResponse = gson.fromJson(entityString, RegisterResponse.class);
                    break;
                case GET_ALL_CHARACTERS :
                    serverResponse = gson.fromJson(entityString, GetAllCharactersResponse.class);
                    break;
            }
        } catch (IOException e) {
            System.out.println("IOException : " + e.toString());
            serverResponse = new ErrorResponse();
            serverResponse.setSuccess(false);
        } catch (JsonSyntaxException e) {
            System.out.println("JsonSyntaxException : " + e.toString());
            serverResponse = new ErrorResponse();
            serverResponse.setSuccess(false);

        }
        return serverResponse;
    }

}
