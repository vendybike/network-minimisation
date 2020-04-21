package org.sedaq.scp.service.utils;

import org.sedaq.scp.service.exceptions.MatricesUtilsException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Pavel Seda
 */
@Service
public class MatrixUtils {

    /**
     * First step of computation is to convert the distance matrix (the matrix composed from distances from each service centre to each customer location to reachability matrix containin only 1 or 0 depends on the given distance)
     *
     * @param distanceMatrix the given distance matrix (readed from .csv file)
     * @param dMax           the given dMax parameter representing the distance between each service centre to each customer location
     * @return the reachability matrix
     */
    public int[][] convertDistanceMatrixToReachabilityMatrix(int[][] distanceMatrix, int dMax) {
        int[][] reachAbilityMatrix = new int[distanceMatrix.length][distanceMatrix[0].length];
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                if (distanceMatrix[i][j] < dMax) {
                    reachAbilityMatrix[i][j] = 1;
                } else {
                    reachAbilityMatrix[i][j] = 0;
                }
            }
        }
        return reachAbilityMatrix;
    }

    /**
     * Remove rows (service centres) which do not cover anything
     *
     * @param reachabilityMatrix     the given reachability matrix
     * @param serviceCentresToRemove the list of rows (service centres) to remove
     * @return reduced reachability matrix
     */
    public int[][] removeGivenServiceCentres(int[][] reachabilityMatrix, List<Integer> serviceCentresToRemove) {
        Objects.requireNonNull(serviceCentresToRemove, "customerLocationCapacities must not be null in convertReachabilityMatrixToCapacityMatrix");
        List<int[]> reducedReachabilityMatrixTemp = new ArrayList<>();
        for (int i = 0; i < reachabilityMatrix.length; i++) {
            if (serviceCentresToRemove.contains(i)) {
                continue; // service centre i is predicted to be removed
            }
            reducedReachabilityMatrixTemp.add(reachabilityMatrix[i]);
        }
        int[][] reducedReachabilityMatrix = new int[reducedReachabilityMatrixTemp.size()][reachabilityMatrix[0].length];
        for (int i = 0; i < reducedReachabilityMatrixTemp.size(); i++) {
            reducedReachabilityMatrix[i] = reducedReachabilityMatrixTemp.get(i);
        }
        return reducedReachabilityMatrix;
    }


    /**
     * Converts reachability matrix to capacity matrix
     *
     * @param reachabilityMatrix         the given reachability matrix
     * @param customerLocationCapacities required capacities for each customer location
     * @return capacity matrix
     */
    public int[][] convertReachabilityMatrixToCapacityMatrix(int[][] reachabilityMatrix, List<Integer> customerLocationCapacities) {
        Objects.requireNonNull(reachabilityMatrix, "reachability matrix must not be null in convertReachabilityMatrixToCapacityMatrix");
        Objects.requireNonNull(customerLocationCapacities, "customerLocationCapacities must not be null in convertReachabilityMatrixToCapacityMatrix");
        if (reachabilityMatrix[0].length != customerLocationCapacities.size()) {
            throw new MatricesUtilsException("The number of columns in reachabilityMatrix must be same as the number of capacities in the list of capacities for customer locations.");
        }
        for (int j = 0; j < reachabilityMatrix[0].length; j++) {
            for (int i = 0; i < reachabilityMatrix.length; i++) {
                if (reachabilityMatrix[i][j] == 1) {
                    reachabilityMatrix[i][j] = customerLocationCapacities.get(j);
                }
            }
        }
        return reachabilityMatrix;
    }

}
