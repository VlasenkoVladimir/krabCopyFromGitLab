package com.krab51.webapp.utilities.demo;

import java.text.DecimalFormat;
import java.util.Random;

import static java.lang.String.format;

public class RandomUtilites {
    private static final Random rand = new Random();

    public static String getRandomPhoneNumber() {
        int num1 = (rand.nextInt(7) + 1) * 100 + (rand.nextInt(8) * 10) + rand.nextInt(8);
        int num2 = rand.nextInt(743);
        int num3 = rand.nextInt(10000);

        DecimalFormat df3 = new DecimalFormat("000");
        DecimalFormat df4 = new DecimalFormat("0000");

        return "+7" + " (" + df3.format(num1) + ") " + df3.format(num2) + "-" + df4.format(num3);
    }

    public static String getRandomDate(long min, long max) {
        return getRandomNumber(min, max)
                + format("%02d", getRandomNumber(1, 12))
                + format("%02d", getRandomNumber(1, 29));
    }

    public static long getRandomNumber(long min, long max) {
        max -= min;
        return (long) (Math.random() * ++max) + min;
    }
}
