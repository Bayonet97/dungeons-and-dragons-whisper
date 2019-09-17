package servertests;

import messaging.responses.GetAllCharactersResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import serverhandler.DanDWhisperServerHandler;
import serverhandler.IDanDWhisperServerHandler;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CharacterSelectTests {

    static IDanDWhisperServerHandler serverHandler;
    private List<String> characters = new ArrayList<>();

    @BeforeAll
    public void setUp(){
        characters.add("Omnia");
        characters.add("Liva");
        characters.add("Ornn");

        serverHandler = new DanDWhisperServerHandler(characters);
    }

    @Test
    public void should_Get_All_Characters_From_Database(){
        //Act
        GetAllCharactersResponse response = serverHandler.processGetAllCharacters();
        //Assert
        assertTrue(response.isSuccess());
    }

}
