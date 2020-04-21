package org.sedaq.scp.service.algorithms;

import org.sedaq.scp.service.representation.InputMatrix;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Pavel Seda
 */
@Service
public class RepairOperator {

    public void repairOperator(List<Integer> possibleSolution, InputMatrix inputMatrix, Double givenPercentage) {
        int[] possibleSolutionCovering = getPossibleSolutionCovering(possibleSolution, inputMatrix);
        if (givenPercentage != null) {
            while (!this.givenPercentageIsTrue(inputMatrix, possibleSolutionCovering, givenPercentage)) {
                this.repairOperatorAlgorithm(possibleSolution, inputMatrix, possibleSolutionCovering);
            }
        } else {
            while (!this.allAreTrue(inputMatrix, possibleSolutionCovering)) {
                this.repairOperatorAlgorithm(possibleSolution, inputMatrix, possibleSolutionCovering);
            }
        }
    }

    private void repairOperatorAlgorithm(List<Integer> possibleSolution, InputMatrix inputMatrix, int[] possibleSolutionCovering) {
        // sort service centres not in solution by covering statistics (service centre which covers the most uncovered customer locations will be added to the solution)
        Map<Integer, Integer> coverStatistics = getCoverStatistics(possibleSolution, inputMatrix, possibleSolutionCovering);
        Map<Integer, Integer> coverStatisticsSortedByValue = getCoverStatisticsSortedByValue(coverStatistics);
        addServiceToTheSolution(possibleSolution, inputMatrix, possibleSolutionCovering, coverStatisticsSortedByValue);
    }

    private void addServiceToTheSolution(List<Integer> possibleSolution, InputMatrix inputMatrix, int[] possibleSolutionCovering, Map<Integer, Integer> coverStatisticsSortedByNumberOfAdditionalCoverings) {
        Iterator<Map.Entry<Integer, Integer>> iterator = coverStatisticsSortedByNumberOfAdditionalCoverings.entrySet().iterator();
        if (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            if (entry != null) { // entry does not contain any values, probably all the service centres are in the solution
                Integer serviceCentreToAdd = entry.getKey();
                // add specific service centre to the solution
                possibleSolution.set(serviceCentreToAdd, 1);

                //remove already covered customer locations from uncovered customer locations list
                int[] serviceCentreRowInMatrix = inputMatrix.getMatrix()[serviceCentreToAdd];
                for (int j = 0; j < serviceCentreRowInMatrix.length; j++) {
                    if (serviceCentreRowInMatrix[j] == 1 && possibleSolutionCovering[j] == 0) {
                        possibleSolutionCovering[j] = possibleSolutionCovering[j] + 1;
                    }
                }
            }
        }
    }

    private Map<Integer, Integer> getCoverStatisticsSortedByValue(Map<Integer, Integer> coverStatistics) {
        return coverStatistics.entrySet()
                .stream()
                .sorted((Map.Entry.<Integer, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private Map<Integer, Integer> getCoverStatistics(List<Integer> possibleSolution, InputMatrix inputMatrix, int[] possibleSolutionCovering) {
        Map<Integer, Integer> coverStatistics = new HashMap<>();
        for (int i = 0; i < possibleSolution.size(); i++) {
            int coversUncoveredCustomerLocationsSum = 0;
            if (possibleSolution.get(i) == 0) {
                int[] inputDataColumns = inputMatrix.getMatrix()[i];
                for (int j = 0; j < inputDataColumns.length; j++) {
                    if (inputDataColumns[j] == 1 && possibleSolutionCovering[j] == 0) {
                        coversUncoveredCustomerLocationsSum++;
                    }
                }
                coverStatistics.put(i, coversUncoveredCustomerLocationsSum);
            }
        }
        return coverStatistics;
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

    public boolean allAreTrue(InputMatrix inputMatrix, int[] possibleSolutionCovering) {
        for (int i = 0; i < possibleSolutionCovering.length; i++) {
            if (possibleSolutionCovering[i] < inputMatrix.getNumbersOfRequiredServices().get(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean givenPercentageIsTrue(InputMatrix inputMatrix, int[] possibleSolutionCovering, double percentageToCover) {
        if (percentageToCover > 1 || percentageToCover < 0)
            throw new IllegalArgumentException("Percentage to cover must be between 0 and 1, e.g., 0.6 which refers to 60% cover percentage.");
        int necessaryCoverage = (int) Math.round(possibleSolutionCovering.length * percentageToCover);
        int solutionSum = 0;
        for (int i = 0; i < possibleSolutionCovering.length; i++) {
            if (possibleSolutionCovering[i] >= inputMatrix.getNumbersOfRequiredServices().get(i)) {
                solutionSum = solutionSum + 1;
            }
        }
        if (solutionSum >= necessaryCoverage) {
            return true;
        } else {
            return false;
        }
    }

}
