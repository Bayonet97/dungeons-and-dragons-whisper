package diceroller;

import java.util.ArrayList;
import java.util.List;

public interface Rollable {
    List<Integer> rollDice(Integer amount, Integer sides);
}