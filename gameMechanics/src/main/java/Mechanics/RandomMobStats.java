package Mechanics;

import java.util.Random;

public class RandomMobStats {

    public static int makeDefaultHealth(Random random) {
        final int basicHealth = 100;
        final int additionalHealth = 100;
        return basicHealth + random.nextInt(additionalHealth);
    }

    public static int makeDefaultDamage(Random random) {
        final int basicDamage = 10;
        final int additionalDamage = 10;
        return basicDamage + random.nextInt(additionalDamage);
    }

}
