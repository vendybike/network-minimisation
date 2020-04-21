package org.sedaq.scp.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Pavel Seda
 */
@RunWith(SpringRunner.class)
public class SimulatedAnnealingTest {


    @Before
    public void init() {
    }

    @Test
    public void getNumberOfCoolings() {
        assertEquals(688, getNumberOfCoolings(1000.0, 1.0, 0.99));
        assertEquals(9206, getNumberOfCoolings(10000.0, 1.0, 0.999));

        System.out.println(System.lineSeparator() + "The number of iterations with this settings: " + getNumberOfCoolings(10000.0, 1.0, 0.9999) + System.lineSeparator());
    }

    private int getNumberOfCoolings(double initTemperature, double finalTemperature, double coolingRate) {
        int numberOfRepetitions = 0;

        while (initTemperature > finalTemperature) {
            initTemperature = initTemperature * coolingRate;
            numberOfRepetitions++;
        }
        return numberOfRepetitions;
    }

}
