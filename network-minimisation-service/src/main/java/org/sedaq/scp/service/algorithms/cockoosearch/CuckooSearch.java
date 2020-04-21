package org.sedaq.scp.service.algorithms.cockoosearch;

import org.sedaq.scp.service.algorithms.MetaHeuristicAlgorithm;
import org.sedaq.scp.service.algorithms.RepairOperator;
import org.sedaq.scp.service.exceptions.CuckooSearchAlgorithmException;
import org.sedaq.scp.service.io.WriteToCSVFile;
import org.sedaq.scp.service.representation.InputMatrix;
import org.sedaq.scp.service.utils.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * That is the simplest variant of Cuckoo search algorithm with random-walk and with scenario that each nest contains one egg.
 *
 * @author Pavel Seda
 */
@Service
public class CuckooSearch implements MetaHeuristicAlgorithm {

    private WriteToCSVFile writeToCSVFile;
    private RepairOperator repairOperator;

    @Autowired
    public CuckooSearch(WriteToCSVFile writeToCSVFile, RepairOperator repairOperator) {
        this.writeToCSVFile = writeToCSVFile;
        this.repairOperator = repairOperator;
    }

    public List<Integer> startAlgorithm(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres) {
        return startAlgorithm(inputMatrix, necessaryServiceCentres, 10000L, 1.0, Paths.get("./network-minimisation-service/src/main/resources/results/cs/"));
    }

    public List<Integer> startAlgorithm(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres, Double coveragePercentage, Path resultsBasePath) {
        return startAlgorithm(inputMatrix, necessaryServiceCentres, 10000L, coveragePercentage, resultsBasePath);
    }

    public List<Integer> startAlgorithm(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres, long numberOfIterations, Double coveragePercentage, Path resultsBasePath) {
        List<Integer> theBestEggsList = new ArrayList<>();
        Egg egg = generateCurrentEgg(inputMatrix, necessaryServiceCentres, coveragePercentage);
        Nests nests = new Nests(generateNests(inputMatrix, egg, necessaryServiceCentres, coveragePercentage));
        double fractionOfNestsPa = 0.2;
        Egg theBestEgg = egg;

        for (int i = 0; i < numberOfIterations; i++) {
            // generate a new solution (Cuckoo) xi randomly by Lévy flights
            // Evaluate the fitness function of a solution xi
            Egg cuckooEgg = levyFlight(nests, inputMatrix, necessaryServiceCentres, coveragePercentage);
            // Choose a nest xj among n solutions randomly
            int randomNestIndex = RandomGenerator.getRand().nextInt(nests.getEggs().size() - 1);
            Egg randomlyChoosenNest = randomlyChooseANest(nests, randomNestIndex);
            // if nest is better then original nest egg replace the solution xj with the solution xi
            if (cuckooEgg.getFitnessValue() < randomlyChoosenNest.getFitnessValue()) {
                nests.getEggs().set(randomNestIndex, cuckooEgg);
            }
            // abandon a franction Pa of worse nests
            // build new nests at new locations using Lévy flight a fraction pa of worse nests
            abandonAFractionOfNests(fractionOfNestsPa, nests, inputMatrix, necessaryServiceCentres, coveragePercentage);
            // Keep the best solutions (nests with quality solutions)
            // Rank the solutions and find the current best solution
            Egg theBestEggInIteration = findTheBestEgg(nests);
            if (theBestEgg.getFitnessValue() > theBestEggInIteration.getFitnessValue()) {
                theBestEgg = theBestEggInIteration;
            }
            theBestEggsList.add(theBestEgg.getFitnessValue());
        }

        writeResultsToTheFile(resultsBasePath, theBestEggsList, theBestEgg);
        return theBestEggsList;
    }

    private Egg levyFlight(Nests nests, InputMatrix inputMatrix, List<Integer> necessaryServiceCentres, Double coveragePercentage) {
        int theNumberOfNests = nests.getEggs().size();
        // find random nest and it's egg by random-walk
        int randomNestIndex = RandomGenerator.getRand().nextInt(theNumberOfNests - 1);
        Egg randomNest = nests.getEggs().get(randomNestIndex);

        // reverse random value in an egg to provide a new solution
        int randomBitReverseToGenerateANewSolution = RandomGenerator.getRand().nextInt(theNumberOfNests - 1);
        Egg cuckooEgg = new Egg();
        cuckooEgg.provideNewSolutionBasedOnOld(randomNest, randomBitReverseToGenerateANewSolution);
        addNecessaryServiceCentres(cuckooEgg, necessaryServiceCentres);
        // novelty defined repair operator
        repairOperator.repairOperator(cuckooEgg.getEgg(), inputMatrix, coveragePercentage);
        cuckooEgg.setFitnessValue(computeFitnessValue(cuckooEgg, inputMatrix.getCosts()));
        return cuckooEgg;
    }

    private Egg randomlyChooseANest(Nests nests, int randomNestIndex) {
        return nests.getEggs().get(randomNestIndex);
    }

    private void abandonAFractionOfNests(double fractionPa, Nests nests, InputMatrix inputMatrix, List<Integer> necessaryServiceCentres, Double coveragePercentage) {
        List<Egg> eggs = nests.getEggs();
        // descending order
        Collections.sort(eggs, Comparator.comparingInt(Egg::getFitnessValue).reversed());
        int eggsSize = eggs.size();
        int theNumberOfEgsToReplace = (int) Math.round(eggsSize * fractionPa);
        for (int i = 0; i < theNumberOfEgsToReplace; i++) {
            nests.getEggs().set(i, levyFlight(nests, inputMatrix, necessaryServiceCentres, coveragePercentage));
        }
    }

    /**
     * Add necessary service centres to the solution
     *
     * @param egg
     * @param necessaryRows
     */
    private void addNecessaryServiceCentres(Egg egg, List<Integer> necessaryRows) {
        List<Integer> eggData = egg.getEgg();
        for (int i = 0; i < necessaryRows.size(); i++) {
            eggData.set(necessaryRows.get(i), 1);
        }
    }

    private int computeFitnessValue(Egg egg, List<Integer> costs) {
        List<Integer> eggData = egg.getEgg();
        if (eggData == null) {
            throw new CuckooSearchAlgorithmException("it is not possible to compute fitness because the egg data was not generated..");
        }
        int sum = 0;
        for (int i = 0; i < eggData.size(); i++) {
            if (costs == null || costs.isEmpty()) {
                sum += eggData.get(i) * 1;
            } else {
                sum += eggData.get(i) * costs.get(i);
            }
        }
        return sum;
    }

    public Egg findTheBestEgg(Nests nests) {
        int rank = 0;
        int bestFitnessValue = Integer.MAX_VALUE;
        for (int i = 0; i < nests.getEggs().size(); i++) {
            if (nests.getEggs().get(i).getFitnessValue() < bestFitnessValue) {
                bestFitnessValue = nests.getEggs().get(i).getFitnessValue();
                rank = i;
            }
        }
        return nests.getEggs().get(rank);
    }

    private Egg generateCurrentEgg(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres, Double coveragePercentage) {
        Egg egg = new Egg();
        egg.generateInitialEgg(inputMatrix.getMatrix().length);
        addNecessaryServiceCentres(egg, necessaryServiceCentres);
        // novelty defined repair operator
        repairOperator.repairOperator(egg.getEgg(), inputMatrix, coveragePercentage);
        egg.setFitnessValue(computeFitnessValue(egg, inputMatrix.getCosts()));
        return egg;
    }

    private List<Egg> generateNests(InputMatrix inputMatrix, Egg currentOccupant, List<Integer> necessaryServiceCentres, Double coveragePercentage) {
        List<Egg> nests = new ArrayList<>();
        for (int i = 0; i < inputMatrix.getMatrix().length; i++) {
            Egg egg = new Egg();
            egg.provideNewSolutionBasedOnOld(currentOccupant, i);
            addNecessaryServiceCentres(egg, necessaryServiceCentres);
            // novelty defined repair operator
            repairOperator.repairOperator(egg.getEgg(), inputMatrix, coveragePercentage);
            egg.setFitnessValue(computeFitnessValue(egg, inputMatrix.getCosts()));
            nests.add(egg);
        }
        return nests;
    }

    public void writeResultsToTheFile(Path resultsBasePath, List<Integer> currentOccupantList, Egg currentOccupant) {
        long currentTime = System.currentTimeMillis();
        Path results = Paths.get(resultsBasePath.toAbsolutePath().toString() + "/" + currentTime + "-cs-results-algorithm-iterations" + ".csv");
        writeToCSVFile.writeTheAlgorithmCalculation(results, currentOccupantList);

//        Path serviceCentreNamesFile = Paths.get("./network-minimisation-service/src/main/resources/results/services/service-centres-names.txt");
        Path resultList = Paths.get(resultsBasePath.toAbsolutePath().toString() + "/" + currentTime + "-cs-results-info" + ".txt");
        writeToCSVFile.writeTheAlgorithmAlgorithmIds(resultList, currentOccupant.getEgg());
    }
}
