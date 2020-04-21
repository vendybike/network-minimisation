package org.sedaq.scp.service.utils;

import java.util.Random;

/**
 * @author Pavel Seda
 */
public class RandomGenerator {

    private static final Random rand = new Random();

    public static int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public static Integer randInt(Integer min, Integer max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public static Random getRand() {
        return rand;
    }

    public static int getRandZeroOrOne() {
        return randInt(0, 1);
    }

    public static void main(String[] args) {
        System.out.println(getRandZeroOrOne());
        System.out.println(getRandZeroOrOne());

        System.out.println(getRandZeroOrOne());

        System.out.println(getRandZeroOrOne());

        System.out.println(getRandZeroOrOne());

        System.out.println(getRandZeroOrOne());

        System.out.println(getRandZeroOrOne());

        System.out.println(getRandZeroOrOne());

        System.out.println(getRandZeroOrOne());

    }
}
