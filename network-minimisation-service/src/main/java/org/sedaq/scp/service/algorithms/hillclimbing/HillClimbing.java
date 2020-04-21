package org.sedaq.scp.service.algorithms.hillclimbing;

import org.sedaq.scp.service.algorithms.MetaHeuristicAlgorithm;
import org.sedaq.scp.service.algorithms.RepairOperator;
import org.sedaq.scp.service.exceptions.HillClimbingAlgorithmException;
import org.sedaq.scp.service.io.WriteToCSVFile;
import org.sedaq.scp.service.representation.InputMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavel Seda
 */
@Service
public class HillClimbing implements MetaHeuristicAlgorithm {

    private WriteToCSVFile writeToCSVFile;
    private RepairOperator repairOperator;

    @Autowired
    public HillClimbing(WriteToCSVFile writeToCSVFile, RepairOperator repairOperator) {
        this.writeToCSVFile = writeToCSVFile;
        this.repairOperator = repairOperator;
    }

    public List<Integer> startAlgorithm(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres) {
        return startAlgorithm(inputMatrix, necessaryServiceCentres, 10000L, 1.0, Paths.get("./network-minimisation-service/src/main/resources/results/hc/"));
    }

    public List<Integer> startAlgorithm(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres, Double coveragePercentage, Path resultsBasePath) {
        return startAlgorithm(inputMatrix, necessaryServiceCentres, 10000L, coveragePercentage, resultsBasePath);
    }

    public List<Integer> startAlgorithm(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres, long numberOfIterations, Double coveragePercentage, Path resultsBasePath) {
        List<Integer> theBestCandidateList = new ArrayList<>();
        Neighbour neighbour = generateCurrentOccupant(inputMatrix, necessaryServiceCentres, coveragePercentage);
        NeighbourHood neighbours = new NeighbourHood(generateNeighbourHood(inputMatrix, neighbour, necessaryServiceCentres, coveragePercentage));

        Neighbour theBest = findTheBestNeighbour(neighbours);

        long maxTabuSize = neighbours.getNeighbours().size();
        for (int i = 0; i < numberOfIterations; i++) {
            NeighbourHood sNeighbourHood = new NeighbourHood(generateNeighbourHood(inputMatrix, theBest, necessaryServiceCentres, coveragePercentage));
            for (Neighbour sCandidate : sNeighbourHood.getNeighbours()) {
                if ((sCandidate.getFitnessValue() < theBest.getFitnessValue())) {
                    theBest = sCandidate;
                }
            }
            theBestCandidateList.add(theBest.getFitnessValue());
        }
        writeResultsToTheFile(resultsBasePath, theBestCandidateList, theBest);
        return theBestCandidateList;
    }

    /**
     * Add necessary service centres to the solution
     *
     * @param neighbour
     * @param necessaryRows
     */
    private void addNecessaryServiceCentres(Neighbour neighbour, List<Integer> necessaryRows) {
        List<Integer> neighbourData = neighbour.getNeighbour();
        for (int i = 0; i < necessaryRows.size(); i++) {
            neighbourData.set(necessaryRows.get(i), 1);
        }
    }

    private int computeFitnessValue(Neighbour neighbour, List<Integer> costs) {
        List<Integer> neighbourData = neighbour.getNeighbour();
        if (neighbourData == null) {
            throw new HillClimbingAlgorithmException("it is not possible to compute fitness because the neighbour was not generated..");
        }
        int sum = 0;
        for (int i = 0; i < neighbourData.size(); i++) {
            if (costs == null || costs.isEmpty()) {
                sum += neighbourData.get(i) * 1;
            } else {
                sum += neighbourData.get(i) * costs.get(i);
            }
        }
        return sum;
    }

    public Neighbour findTheBestNeighbour(NeighbourHood neighbourHood) {
        int rank = 0;
        int bestFitnessValue = Integer.MAX_VALUE;
        for (int i = 0; i < neighbourHood.getNeighbours().size(); i++) {
            if (neighbourHood.getNeighbours().get(i).getFitnessValue() < bestFitnessValue) {
                bestFitnessValue = neighbourHood.getNeighbours().get(i).getFitnessValue();
                rank = i;
            }
        }
        return neighbourHood.getNeighbours().get(rank);
    }

    private Neighbour generateCurrentOccupant(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres, Double coveragePercentage) {
        Neighbour occupant = new Neighbour();
        occupant.generateNeighbour(inputMatrix.getMatrix().length);
        addNecessaryServiceCentres(occupant, necessaryServiceCentres);
        // novelty defined repair operator
        repairOperator.repairOperator(occupant.getNeighbour(), inputMatrix, coveragePercentage);
        occupant.setFitnessValue(computeFitnessValue(occupant, inputMatrix.getCosts()));
        return occupant;
    }

    private List<Neighbour> generateNeighbourHood(InputMatrix inputMatrix, Neighbour currentOccupant, List<Integer> necessaryServiceCentres, Double coveragePercentage) {
        List<Neighbour> neighbourhood = new ArrayList<>();
        for (int i = 0; i < inputMatrix.getMatrix().length; i++) {
            Neighbour neighbour = new Neighbour();
            neighbour.generateNeighbour(currentOccupant, i);
            addNecessaryServiceCentres(neighbour, necessaryServiceCentres);
            // novelty defined repair operator
            repairOperator.repairOperator(neighbour.getNeighbour(), inputMatrix, coveragePercentage);
            neighbour.setFitnessValue(computeFitnessValue(neighbour, inputMatrix.getCosts()));
            neighbourhood.add(neighbour);
        }
        return neighbourhood;
    }

    public void writeResultsToTheFile(Path resultsBasePath, List<Integer> currentOccupantList, Neighbour currentOccupant) {
        long currentTime = System.currentTimeMillis();
        Path results = Paths.get(resultsBasePath.toAbsolutePath().toString() + "/" + currentTime + "-hc-results-algorithm-iterations" + ".csv");
        writeToCSVFile.writeTheAlgorithmCalculation(results, currentOccupantList);

//        Path serviceCentreNamesFile = Paths.get("./network-minimisation-service/src/main/resources/results/services/service-centres-names.txt");
        Path resultList = Paths.get(resultsBasePath.toAbsolutePath().toString() + "/" + currentTime + "-hc-results-info" + ".txt");
        writeToCSVFile.writeTheAlgorithmAlgorithmIds(resultList, currentOccupant.getNeighbour());
    }

}
