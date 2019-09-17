package diceroller;

public interface IRandomSource {
    /**
     * Returns a single random number.
     *
     * @param annotation Used by dice servers as the email subject when dice roll results are emailed. Active for
     *        PBEM games. @see HttpDiceRollerDialog
     */
    Integer getRandom(Integer max, String annotation);

}
