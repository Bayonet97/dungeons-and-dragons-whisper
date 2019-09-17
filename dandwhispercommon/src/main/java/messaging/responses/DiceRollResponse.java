package messaging.responses;

import domain.Die;

import java.util.List;

public class DiceRollResponse extends ServerResponse {

    private List<Integer> rollResults;

    public List<Integer> getRollResults(){return rollResults;}

    public void setRollResults(List<Integer> rollResults){this.rollResults = rollResults;}
}
