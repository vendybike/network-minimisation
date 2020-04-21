package org.sedaq.scp.service.datapreparation;

import org.sedaq.scp.service.exceptions.SpecialCasesException;
import org.sedaq.scp.service.representation.InputMatrix;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Pavel Seda
 */
@Service
public class CheckSpecialCases {

    /**
     * <p>
     * Check if it is possible to provide solution for the request with particular number of multiple services for selected customer locations.
     * In case that it is not possible to cover particular customer location with predefined number of service centres it returns the column number of the customer location
     * plus the service centres that are missing to satisfy the pre-defined requirements.
     * </p>
     *
     * @param inputMatrix
     * @return
     */
    public boolean multipleServicesSatisfiable(InputMatrix inputMatrix) {
        Map<Integer, Integer> multipleServicesSatisfiable = isItPossibleToSatisfyMultipleServices(inputMatrix);
        if (multipleServicesSatisfiable.size() == 0) {
            return true;
        }
        throw new SpecialCasesException("It is not possible to satisfy the requirement to cover specific customer locations with the required number of service centres: " + System.lineSeparator() + Arrays.toString(multipleServicesSatisfiable.entrySet().toArray()));
    }

    private Map<Integer, Integer> isItPossibleToSatisfyMultipleServices(InputMatrix inputMatrix) {
        List<Integer> possibleSolution = new ArrayList<>();
        for (int i = 0; i < inputMatrix.getMatrix().length; i++) {
            possibleSolution.add(1);
        }
        int[] possibleSolutionCovering = getPossibleSolutionCovering(possibleSolution, inputMatrix);
        Map<Integer, Integer> servicesThatDoNotSatisfyMultipleServices = new HashMap<>();
        for (int i = 0; i < inputMatrix.getMatrix()[0].length; i++) {
            if (possibleSolutionCovering[i] < inputMatrix.getNumbersOfRequiredServices().get(i)) {
                int numberOfNecessaryServicesToExtend = inputMatrix.getNumbersOfRequiredServices().get(i) - possibleSolutionCovering[i];
                servicesThatDoNotSatisfyMultipleServices.put(i, numberOfNecessaryServicesToExtend);
            }
        }
        return servicesThatDoNotSatisfyMultipleServices;
    }

    public int[] getPossibleSolutionCovering(List<Integer> possibleSolution, InputMatrix inputMatrix) {
        int[][] matrix = inputMatrix.getMatrix();
        int[] possibleSolutionCovering = new int[matrix[0].length]; // initialize covering with all 0

        for (int i = 0; i < possibleSolution.size(); i++) {
            if (possibleSolution.get(i) == 1) {
                int[] inputDataColumns = matrix[i];
                for (int j = 0; j < inputDataColumns.length; j++) {
                    if (inputDataColumns[j] == 1) {
                        possibleSolutionCovering[j] = possibleSolutionCovering[j] + 1; // this customer location is covered by this possible solution
                    }
                }
            }
        }
        return possibleSolutionCovering;
    }

    private boolean allAreTrue(InputMatrix inputMatrix, int[] possibleSolutionCovering) {
        for (int i = 0; i < possibleSolutionCovering.length; i++) {
            if (possibleSolutionCovering[i] < inputMatrix.getNumbersOfRequiredServices().get(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if service centres cover anything. In case that not, the dMax parameter should be increased.
     *
     * @param reachabilityMatrix
     * @return
     */
    public boolean serviceCentreCoverSomething(int[][] reachabilityMatrix) {
        Objects.requireNonNull(reachabilityMatrix, "reachabilityMatrix in serviceCentreCoverSomething method must not be null.");
        for (int i = 0; i < reachabilityMatrix.length; i++) {
            for (int j = 0; j < reachabilityMatrix[i].length; j++) {
                if (reachabilityMatrix[i][j] == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if reachability matrix does not contain columns only with zeroes
     *
     * @param reachabilityMatrix
     * @return
     */
    public boolean isDMaxValid(int[][] reachabilityMatrix) {
        Objects.requireNonNull(reachabilityMatrix, "reachabilityMatrix in isDMaxValid method must not be null.");
        int columnSum = 0;
        for (int j = 0; j < reachabilityMatrix[0].length; j++) {
            for (int i = 0; i < reachabilityMatrix.length; i++) {
                if (reachabilityMatrix[i][j] == 1) {
                    columnSum++;
                }
            }
            if (columnSum == 0) {
                //matrix contains column with only zeroes... it is necessary to extend dMax value to cover this customer location
                return false;
            }
            //reset column sum counting for new column
            columnSum = 0;
        }
        return true;
    }

    /**
     * Rows as immediate solution (counted from 0)
     *
     * @param reachabilityMatrix
     * @return return List of integers representing rows which could be an immediate solution
     */
    public List<Integer> serviceCentresAsImmediateSolution(int[][] reachabilityMatrix) {
        Objects.requireNonNull(reachabilityMatrix, "reachabilityMatrix in serviceCentresAsImmediateSolution method must not be null.");
        List<Integer> rowsAsImmediateSolution = new ArrayList<>();
        int rowSum = 0;
        for (int i = 0; i < reachabilityMatrix.length; i++) {
            for (int j = 0; j < reachabilityMatrix[i].length; j++) {
                if (reachabilityMatrix[i][j] == 1) {
                    rowSum++;
                }
            }
            if (rowSum == reachabilityMatrix[i].length) {
                rowsAsImmediateSolution.add(i);
            }
            //reset row sum counting for new row
            rowSum = 0;
        }
        return rowsAsImmediateSolution;
    }

    public List<Integer> necessaryServiceCentres(int[][] reachabilityMatrix) {
        Objects.requireNonNull(reachabilityMatrix, "reachabilityMatrix in necessaryServiceCentres method must not be null.");
        List<Integer> necessaryServiceCentreRows = new ArrayList<>();
        int columnSum = 0;
        for (int j = 0; j < reachabilityMatrix[0].length; j++) {
            Integer necessaryRow = null;
            for (int i = 0; i < reachabilityMatrix.length; i++) {
                if (reachabilityMatrix[i][j] == 1) {
                    columnSum++;
                    necessaryRow = i;
                }
            }
            if (columnSum == 1) {
                necessaryServiceCentreRows.add(necessaryRow);
            }
            //reset column sum counting for new column
            columnSum = 0;
        }
        return necessaryServiceCentreRows;
    }

    public List<Integer> serviceCentresNotCoverAnything(int[][] reachabilityMatrix) {
        Objects.requireNonNull(reachabilityMatrix, "reachabilityMatrix in serviceCentresNotCoverAnything method must not be null.");
        List<Integer> rowsToRemove = new ArrayList<>();
        int rowSum = 0;
        for (int i = 0; i < reachabilityMatrix.length; i++) {
            for (int j = 0; j < reachabilityMatrix[i].length; j++) {
                if (reachabilityMatrix[i][j] == 1) {
                    rowSum++;
                }
            }
            if (rowSum == 0) {
                rowsToRemove.add(i);
            }
            //reset row sum counting for new row
            rowSum = 0;
        }
        return rowsToRemove;
    }
}
