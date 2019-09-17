package diceroller;

import java.util.ArrayList;
import java.util.List;
//enum CalculationType {
//    ADDITION,
//    SUBSTRACTION,
//    NOCALCULATION
//    // More
//}

public class DiceRoller implements Rollable {

    public List<Integer> rollDice(Integer amount, Integer sides) {

        List<Die> dice = new ArrayList<Die>();

        for (int i = 0; i < amount; i++){
            dice.add(new Die(sides));
        }

        List<Integer> results = new ArrayList<Integer>();

        for (Die die : dice) {
            if (die.getSides() == 1) {
                System.out.println("A one sided die cannot be rolled.");
                results.add(1);
            } else if (die.getSides() != 0)
                results.add(rollSingleDie(die.getSides()) + 1);
        }

        if (results.size() == 0) {
            System.out.println("No die was rolled");
            return null;
        }

        return results;
    }

    private Integer rollSingleDie(Integer sides){
        if(sides == 0)
            return 0;

        IRandomSource randomNumberGenerator = new RandomNumberGenerator();
        return randomNumberGenerator.getRandom(sides, "Rolling die.");
    }

//    private void calculate(CalculationType calculationType){
//        int[] results = new int[5];
//
//        double calculatedValue = 0;
//        switch (calculationType) {
//            case NOCALCULATION:
//                for (int result : results)
//                    rollResults.add(result);
//                break;
//            case ADDITION:
//                for (int result : results) {
//                    rollResults.add(result);
//                    calculatedValue += result;
//                }
//                break;
//            case SUBSTRACTION:
//                for (int result : results) {
//                    rollResults.add(result);
//                    calculatedValue -= result;
//                }
//                break;
//        }
//    }

}
