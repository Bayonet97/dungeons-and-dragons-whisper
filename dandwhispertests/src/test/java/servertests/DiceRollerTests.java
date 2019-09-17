package servertests;

import messaging.requests.DiceRollRequest;
import messaging.responses.DiceRollResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import serverhandler.DanDWhisperServerHandler;
import serverhandler.IDanDWhisperServerHandler;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiceRollerTests {

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
    public void roll_Tenthousand_Dice_With_Six_Sides_Where_All_Results_Are_Between_One_And_Six(){
        //Arrange
        DiceRollRequest request = new DiceRollRequest();
        request.setDiceCount(1000);
        request.setSides(6);

        //Act
        DiceRollResponse response = serverHandler.processDiceRoll(request);

        //Assert
        for (int result: response.getRollResults()) {
            assertTrue(result > 0 && result < 7);
        }
    }

    @Test
    public void roll_One_Die_With_One_Side_Where_Result_Is_One(){
        //Arrange
        DiceRollRequest request = new DiceRollRequest();
        request.setDiceCount(1);
        request.setSides(1);

        //Act
        DiceRollResponse response = serverHandler.processDiceRoll(request);

        //Assert
        assertTrue(response.getRollResults().contains(1));
        // Also check if console shows "A one sided die cannot be rolled."
    }

    @Test
    public void roll_Onehundredthousand_Six_Sided_Dice_With_Results_Averaging_Correctly(){
        //Arrange
        DiceRollRequest request = new DiceRollRequest();
        request.setDiceCount(100000);
        request.setSides(6);

        //Act
        DiceRollResponse response = serverHandler.processDiceRoll(request);
        Integer sum = 0;

        for (Integer result: response.getRollResults()) {
            sum += result;
        }
        //Assert
        System.out.println("The average roll on a d6 is: " + sum.doubleValue() / response.getRollResults().size());
        assertTrue(sum.doubleValue() / response.getRollResults().size() > 3.4 && sum.doubleValue() / response.getRollResults().size() < 3.6);
    }
}
