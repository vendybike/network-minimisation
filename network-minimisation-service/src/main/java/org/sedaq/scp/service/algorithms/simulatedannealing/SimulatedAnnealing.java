package org.sedaq.scp.service.algorithms.simulatedannealing;

import org.sedaq.scp.service.algorithms.MetaHeuristicAlgorithm;
import org.sedaq.scp.service.algorithms.RepairOperator;
import org.sedaq.scp.service.exceptions.SimulatedAnnealingAlgorithmException;
import org.sedaq.scp.service.io.WriteToCSVFile;
import org.sedaq.scp.service.representation.InputMatrix;
import org.sedaq.scp.service.utils.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Pavel Seda
 */
@Service
public class SimulatedAnnealing implements MetaHeuristicAlgorithm {

    private WriteToCSVFile writeToCSVFile;
    private RepairOperator repairOperator;

    @Autowired
    public SimulatedAnnealing(WriteToCSVFile writeToCSVFile, RepairOperator repairOperator) {
        this.writeToCSVFile = writeToCSVFile;
        this.repairOperator = repairOperator;
    }

    public List<Integer> startAlgorithm(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres) {
        return startAlgorithm(inputMatrix, necessaryServiceCentres, 10000.0, 1, 0.999, 1.0, Paths.get("./network-minimisation-service/src/main/resources/results/sa/"));
    }

    public List<Integer> startAlgorithm(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres, Double coveragePercentage, Path resultsBasePath) {
        return startAlgorithm(inputMatrix, necessaryServiceCentres, 10000.0, 1, 0.999, coveragePercentage, resultsBasePath);
    }

    public List<Integer> startAlgorithm(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres, double initTemperature, int finalTemperature, double coolingRate, Double coveragePercentage, Path resultsBasePath) {
        List<Integer> currentOccupantList = new ArrayList<>();
        int nT = 1;
        Neighbour currentOccupant = generateCurrentOccupant(inputMatrix, necessaryServiceCentres, coveragePercentage);
        NeighbourHood neighbours = new NeighbourHood(generateNeighbourHood(inputMatrix, currentOccupant, necessaryServiceCentres, coveragePercentage));

        while (initTemperature > finalTemperature) {
            for (int i = 0; i < nT; i++) {
                //get random neighbour from the neighbourhood
                Neighbour neighbour = neighbours.getNeighbours().get(RandomGenerator.getRand().nextInt(neighbours.getNeighbours().size()));
                int f = currentOccupant.getFitnessValue();
                int f0 = computeFitnessValue(neighbour, inputMatrix.getCosts());
                if (f > f0) { // když je soused lepší než současný obyvatel (currentOccupant ma vetsi hodnotu, coz vzhledem k minimalizacni uloze znaci, ze je horsi)
                    currentOccupant = neighbour;
                    neighbours = new NeighbourHood(generateNeighbourHood(inputMatrix, currentOccupant, necessaryServiceCentres, coveragePercentage));
                } else {
                    double r = RandomGenerator.getRand().nextDouble();
                    if (r < Math.exp((f - f0) / (initTemperature / 15))) {
                        currentOccupant = neighbour;
                        neighbours = new NeighbourHood(generateNeighbourHood(inputMatrix, currentOccupant, necessaryServiceCentres, coveragePercentage));
                    }
                }
            }
            initTemperature = initTemperature * coolingRate;
            currentOccupantList.add(currentOccupant.getFitnessValue());
        }
        writeResultsToTheFile(resultsBasePath, currentOccupantList, currentOccupant);
        return currentOccupantList;
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

    private Neighbour generateCurrentOccupant(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres, Double coveragePercentage) {
        Neighbour occupant = new Neighbour();
        occupant.generateNeighbour(inputMatrix.getMatrix().length);
        addNecessaryServiceCentres(occupant, necessaryServiceCentres);
        // novelty defined repair operator
        repairOperator.repairOperator(occupant.getNeighbour(), inputMatrix, coveragePercentage);
        occupant.setFitnessValue(computeFitnessValue(occupant, inputMatrix.getCosts()));
        return occupant;
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
            throw new SimulatedAnnealingAlgorithmException("it is not possible to compute fitness because the neighbour was not generated..");
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

    public void writeResultsToTheFile(Path resultsBasePath, List<Integer> currentOccupantList, Neighbour currentOccupant) {
        long currentTime = System.currentTimeMillis();
        Path results = Paths.get(resultsBasePath.toAbsolutePath().toString() + "/" + currentTime + "-sa-results-algorithm-iterations" + ".csv");
        writeToCSVFile.writeTheAlgorithmCalculation(results, currentOccupantList);

//        Path serviceCentreNamesFile = Paths.get("./network-minimisation-service/src/main/resources/results/services/service-centres-names.txt");
        Path resultList = Paths.get(resultsBasePath.toAbsolutePath().toString() + "/" + currentTime + "-sa-results-info" + ".txt");
        writeToCSVFile.writeTheAlgorithmAlgorithmIds(resultList, currentOccupant.getNeighbour());
    }

}
