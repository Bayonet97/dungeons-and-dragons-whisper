package messaging.requests;

public class DiceRollRequest extends ClientRequest {
    private int diceCount;
    private int sides;

    public DiceRollRequest(){
    }

    public Integer getDiceCount() {
        return diceCount;
    }

    public Integer getSides() {
        return sides;
    }

    public void setDiceCount(Integer diceCount) {
        this.diceCount = diceCount;
    }

    public void setSides(Integer sides) {
        this.sides = sides;
    }
}
