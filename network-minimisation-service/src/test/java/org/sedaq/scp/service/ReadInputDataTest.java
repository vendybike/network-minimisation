package org.sedaq.scp.service;

import org.sedaq.scp.service.io.ReadInputData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author Pavel Seda
 */
@RunWith(SpringRunner.class)
public class ReadInputDataTest {

    private ReadInputData readInputData;

    @Before
    public void init() {
        readInputData = new ReadInputData();
    }

    @Test
    public void testConvertCsvStringToIntArray() {
        int[] expected = {1, 2, 3, 4, 5};
        String csvString = "1;2;3;4;5";
        boolean result = Arrays.equals(expected, readInputData.convertCsvStringToIntArray(csvString, ";"));
        assertTrue(result);
        String csvStringWrong = "2;2;3;4;5";
        assertFalse(Arrays.equals(expected, readInputData.convertCsvStringToIntArray(csvStringWrong, ";")));
    }

}
