package org.sedaq.scp.service;

import org.sedaq.scp.service.utils.MatrixUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @author Pavel Seda
 */
@RunWith(SpringRunner.class)
public class MatrixUtilsTest {

    private MatrixUtils matrixUtils;

    @Before
    public void init() {
        matrixUtils = new MatrixUtils();
    }

    @Test
    public void testConvertReachabilityMatrixToCapacityMatrix() {
        List<Integer> customerLocationCapacities = List.of(19, 12, 15, 3);
        int[][] reachabilityMatrix = {{0, 0, 1, 0}, {1, 0, 0, 0}};
        int[][] capacityMatrix = {{0, 0, 15, 0}, {19, 0, 0, 0}};

        assertTrue(Arrays.deepEquals(capacityMatrix,
                matrixUtils.convertReachabilityMatrixToCapacityMatrix(reachabilityMatrix, customerLocationCapacities)));
    }

    @Test
    public void testConvertDistanceMatrixToReachabilityMatrix() {
        int[][] distanceMatrix = {{60, 42, 12, 97}, {11, 98, 23, 105}};
        int[][] reachabilityMatrix = {{0, 1, 1, 0}, {1, 0, 1, 0}};

        assertTrue(Arrays.deepEquals(reachabilityMatrix,
                matrixUtils.convertDistanceMatrixToReachabilityMatrix(distanceMatrix, 50)));
        assertFalse(Arrays.deepEquals(reachabilityMatrix,
                matrixUtils.convertDistanceMatrixToReachabilityMatrix(distanceMatrix, 180)));

        int[][] distanceMatrixAll = {{10, 9, 8, 5}, {1, 6, 1, 3}};
        int[][] reachabilityMatrixAll = {{1, 1, 1, 1}, {1, 1, 1, 1}};
        assertTrue(Arrays.deepEquals(reachabilityMatrixAll,
                matrixUtils.convertDistanceMatrixToReachabilityMatrix(distanceMatrixAll, 50)));

    }

    @Test
    public void testRemoveGivenServiceCentres() {
        int[][] reachabilityMatrix = {{0, 1, 1, 0}, {1, 0, 1, 0}, {0, 1, 1, 0}, {1, 0, 1, 1}, {1, 1, 1, 0}};
        List<Integer> removeRows = List.of(1, 3);
        int[][] reducedReachabilityMatrix = {{0, 1, 1, 0}, {0, 1, 1, 0}, {1, 1, 1, 0}};

        assertTrue(Arrays.deepEquals(reducedReachabilityMatrix,
                matrixUtils.removeGivenServiceCentres(reachabilityMatrix, removeRows)));

        int[][] reducedReachabilityMatrixWrong = {{0, 1, 1, 0}, {0, 1, 1, 0}, {1, 0, 1, 1}};
        assertFalse(Arrays.deepEquals(reducedReachabilityMatrixWrong,
                matrixUtils.removeGivenServiceCentres(reachabilityMatrix, removeRows)));
    }

}
