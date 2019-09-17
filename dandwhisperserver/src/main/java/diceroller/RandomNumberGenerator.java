package diceroller;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;



public class RandomNumberGenerator implements IRandomSource {
    private final Object lock = new Object();

    private final RandomGenerator random = new MersenneTwister();

    public Integer getRandom(final Integer max, final String annotation) {
        //TODO: Put back guava
     //   checkArgument(max > 0, String.format("max must be > 0 (%s)", annotation));

        synchronized (lock) {
            return random.nextInt(max);
        }
    }
}
