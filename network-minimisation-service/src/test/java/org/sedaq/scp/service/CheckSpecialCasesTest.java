package org.sedaq.scp.service;

import org.sedaq.scp.service.datapreparation.CheckSpecialCases;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

/**
 * @author Pavel Seda
 */
@RunWith(SpringRunner.class)
public class CheckSpecialCasesTest {

    private CheckSpecialCases specialCases;

    @Before
    public void init() {
        specialCases = new CheckSpecialCases();
    }

    @Test
    public void testServiceCentresDoesNotCoverAnything() {
        int[][] zeroArrayReachAbilityMatrix = {{0, 0, 0, 0}, {0, 0, 0, 0}};
        assertEquals(false, specialCases.serviceCentreCoverSomething(zeroArrayReachAbilityMatrix));

        int[][] filledArrayReachAbilityMatrix = {{0, 0}, {1, 0}};
        assertEquals(true, specialCases.serviceCentreCoverSomething(filledArrayReachAbilityMatrix));
    }

    @Test
    public void testIsDMaxValid() {
        int[][] validReachAbilityMatrix = {{0, 1, 1}, {1, 0, 1}};
        assertEquals(true, specialCases.isDMaxValid(validReachAbilityMatrix));

        int[][] invalidReachAbilityMatrix = {{0, 1, 0}, {0, 1, 1}};
        assertEquals(false, specialCases.isDMaxValid(invalidReachAbilityMatrix));
    }

    @Test
    public void testRowsAsImmediateSolution() {
        //row 0 and 3 are immediate solutions (counted from 0)
        int[][] reachAbilityMatrixWithImmediateSolutions = {{0, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {0, 1, 0, 1, 1}, {1, 1, 1, 1, 1}};
        List<Integer> expected = List.of(1, 3);
        assertThat(expected, is(specialCases.serviceCentresAsImmediateSolution(reachAbilityMatrixWithImmediateSolutions)));
        // no row with immediate solution
        int[][] reachAbilityMatrixWithoutImmediateSolutions = {{0, 1, 1, 1, 1}, {1, 1, 0, 1, 1}, {0, 1, 0, 1, 1}, {0, 1, 1, 1, 0}};
        assertThat(Collections.emptyList(), is(specialCases.serviceCentresAsImmediateSolution(reachAbilityMatrixWithoutImmediateSolutions)));
    }

    @Test
    public void testNecessaryServiceCentres() {
        int[][] reachabilityMatrixNecessaryServiceCentres = {
                {0, 1, 0, 1},
                {0, 1, 0, 1},
                {0, 0, 1, 1},
                {1, 1, 0, 0}};
        List<Integer> actual = List.of(3, 2);
        assertThat(actual, is(specialCases.necessaryServiceCentres(reachabilityMatrixNecessaryServiceCentres)));
    }

    //TODO FIX THAT
//    @Test
//    public void rowsNotCoverAnything() {
//        int[][] reachAbilityMatrixWithRowNotCoverAnything = {{0, 1, 1}, {0, 0, 0}, {1, 1, 1}};
//        List<Integer> expected = List.of(1);
//        assertThat(expected, is(specialCases.serviceCentresNotCoverAnything(reachAbilityMatrixWithRowNotCoverAnything)));
//
//        int[][] reachAbilityMatrixWithoutRowNotCoverAnything = {{0, 1, 1}, {1, 0, 0}, {1, 1, 1}};
//        assertThat(Collections.emptyList(), is(specialCases.serviceCentreCoverSomething(reachAbilityMatrixWithoutRowNotCoverAnything)));
//    }

}
