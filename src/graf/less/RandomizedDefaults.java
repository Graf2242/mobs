package graf.less;

import java.util.Random;

class RandomizedDefaults {
    static int makeDefaultHealth(Random random) {
        final int basicHealth = 100;
        final int additionalHealth = 100;
        return basicHealth + random.nextInt(additionalHealth);
    }

    static int makeDefaultDamage(Random random) {
        final int basicDamage = 10;
        final int additionalDamage = 10;
        return basicDamage + random.nextInt(additionalDamage);
    }

}
