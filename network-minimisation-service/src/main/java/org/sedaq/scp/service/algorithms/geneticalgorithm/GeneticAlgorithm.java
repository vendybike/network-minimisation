package org.sedaq.scp.service.algorithms.geneticalgorithm;

import org.sedaq.scp.service.algorithms.MetaHeuristicAlgorithm;
import org.sedaq.scp.service.algorithms.RepairOperator;
import org.sedaq.scp.service.algorithms.geneticalgorithm.crossover.Crossover;
import org.sedaq.scp.service.algorithms.geneticalgorithm.crossover.UniformCrossover;
import org.sedaq.scp.service.algorithms.geneticalgorithm.mutation.Mutation;
import org.sedaq.scp.service.algorithms.geneticalgorithm.population.Individual;
import org.sedaq.scp.service.algorithms.geneticalgorithm.population.Population;
import org.sedaq.scp.service.algorithms.geneticalgorithm.selection.RouleteWheelSelection;
import org.sedaq.scp.service.algorithms.geneticalgorithm.selection.Selection;
import org.sedaq.scp.service.exceptions.GeneticAlgorithmException;
import org.sedaq.scp.service.io.WriteToCSVFile;
import org.sedaq.scp.service.representation.InputMatrix;
import org.sedaq.scp.service.utils.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Pavel Seda
 */
@Service
public class GeneticAlgorithm implements MetaHeuristicAlgorithm {

    private WriteToCSVFile writeToCSVFile;
    private RepairOperator repairOperator;

    @Autowired
    public GeneticAlgorithm(WriteToCSVFile writeToCSVFile, RepairOperator repairOperator) {
        this.writeToCSVFile = writeToCSVFile;
        this.repairOperator = repairOperator;
    }

    /**
     * Default values:
     * <p>
     * <ul>
     * <li>generations: 10000</li>
     * <li>population size: 200</li>
     * <li>selection: Roulete wheel selection</li>
     * <li>crossover: Uniformcrossover</li>
     * <li>mutation: 0.5 percent mutation</li>
     * <li>coverage percentage: 1.0 by default which refers to 100% coverage percentage</li>
     * </ul>
     * </p>
     *
     * @param inputMatrix             the given matrix
     * @param necessaryServiceCentres the list of necessary service centres which could not be ommited
     */
    public void startAlgorithm(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres) {
        startAlgorithm(inputMatrix, necessaryServiceCentres, 10000, 10, new RouleteWheelSelection(), new UniformCrossover(), new Mutation(), 1.0, Paths.get("./network-minimisation-service/src/main/resources/results/ga/"));
    }

    public void startAlgorithm(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres, Double coveragePercentage, Path resultsBasePath) {
        startAlgorithm(inputMatrix, necessaryServiceCentres, 10000, 10, new RouleteWheelSelection(), new UniformCrossover(), new Mutation(), coveragePercentage, resultsBasePath);
    }

    public void startAlgorithm(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres, int generations, int populationSize, Selection gaSelection, Crossover gaCrossover, Mutation mutation, Double coveragePercentage, Path resultsBasePath) {
        Population population = new Population(generatePopulation(inputMatrix, necessaryServiceCentres, populationSize, coveragePercentage));

        List<Integer> theBestIndividualsList = new ArrayList<>();
        for (int i = 0; i < generations; i++) {
            List<Individual> ind = gaSelection.selection(population);
            List<Individual> offsprings = gaCrossover.crossover(ind.get(0), ind.get(1));
            mutateOffsprings(mutation, offsprings);
            checkCovering(offsprings, inputMatrix, coveragePercentage);
            evaluateOffsprings(offsprings, inputMatrix.getCosts());
            insertOffspringsToPopulation(population, offsprings);
            // insert the best individual from each iteration to the list
            Individual theBestIndividualAfterIteration = findBestIndividual(population);
            theBestIndividualsList.add(theBestIndividualAfterIteration.getFitnessValue());
        }

        //write results to the file
        Individual theBestIndividual = findBestIndividual(population);
        writeResultsToTheFile(resultsBasePath, theBestIndividualsList, theBestIndividual);
    }

    /**
     * in case that the solution didnt cover all customers
     *
     * @param offsprings
     */
    public void checkCovering(List<Individual> offsprings, InputMatrix inputMatrix, Double coveragePercentage) {
        repairOperator.repairOperator(offsprings.get(0).getChromosome(), inputMatrix, coveragePercentage);
        repairOperator.repairOperator(offsprings.get(1).getChromosome(), inputMatrix, coveragePercentage);
    }

    public void mutateOffsprings(Mutation mutation, List<Individual> offsprings) {
        mutation.mutation(offsprings.get(0), 0.5);
        mutation.mutation(offsprings.get(1), 0.5);
    }

    public void evaluateOffsprings(List<Individual> offsprings, List<Integer> costs) {
        offsprings.get(0).setFitnessValue(computeFitnessValue(offsprings.get(0), costs));
        offsprings.get(1).setFitnessValue(computeFitnessValue(offsprings.get(1), costs));
    }

    public void insertOffspringsToPopulation(Population population, List<Individual> offsprings) {
        // seradim populaci od nejhorsich k nejlepsim
        Collections.sort(population.getPopulation(), (ind1, ind2) -> ((Integer) ind2.getFitnessValue()).compareTo(ind1.getFitnessValue()));
        //vyberu nahodneho jedince z podprumernych a zmenim jeho referenci na potomka1
        int replaceFirstIndividual = RandomGenerator.randInt(0, population.getPopulation().size() / 2);
        population.getPopulation().set(replaceFirstIndividual, offsprings.get(0));
        //vyberu nahodneho jedince z podprumernych a zmenim jeho referenci na potomka1
        int replaceSecondIndividual = RandomGenerator.randInt(0, population.getPopulation().size() / 2);
        population.getPopulation().set(replaceSecondIndividual, offsprings.get(1));
    }

    public List<Individual> generatePopulation(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres, int populationSize, Double coveragePercentage) {
        List<Individual> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            Individual individual = new Individual();
            individual.generateChromosome(inputMatrix.getMatrix().length);
            addNecessaryServiceCentres(individual, necessaryServiceCentres);
            // novelty defined repair operator
            repairOperator.repairOperator(individual.getChromosome(), inputMatrix, coveragePercentage);
            individual.setFitnessValue(computeFitnessValue(individual, inputMatrix.getCosts()));
            population.add(individual);
        }
        return population;
    }

    private int computeFitnessValue(Individual individual, List<Integer> costs) {
        List<Integer> individualData = individual.getChromosome();
        if (individualData == null) {
            throw new GeneticAlgorithmException("it is not possible to compute fitness because the neighbour was not generated..");
        }
        int sum = 0;
        for (int i = 0; i < individualData.size(); i++) {
            if (costs == null || costs.isEmpty()) {
                sum += individualData.get(i) * 1;
            } else {
                sum += individualData.get(i) * costs.get(i);
            }
        }
        return sum;
    }

    /**
     * Add necessary service centres to the solution
     *
     * @param individual
     * @param necessaryRows
     */
    private void addNecessaryServiceCentres(Individual individual, List<Integer> necessaryRows) {
        List<Integer> neighbourData = individual.getChromosome();
        for (int i = 0; i < necessaryRows.size(); i++) {
            neighbourData.set(necessaryRows.get(i), 1);
        }
    }

    public Individual findBestIndividual(Population population) {
        int rank = 0;
        int bestFitnessValue = Integer.MAX_VALUE;
        for (int i = 0; i < population.getPopulation().size(); i++) {
            if (population.getPopulation().get(i).getFitnessValue() < bestFitnessValue) {
                bestFitnessValue = population.getPopulation().get(i).getFitnessValue();
                rank = i;
            }
        }
        return population.getPopulation().get(rank);
    }

    public void writeResultsToTheFile(Path resultsBasePath, List<Integer> currentOccupantList, Individual individual) {
        long currentTime = System.currentTimeMillis();
        Path results = Paths.get(resultsBasePath.toAbsolutePath().toString() + "/" + currentTime + "-ga-results-algorithm-iterations" + ".csv");
        writeToCSVFile.writeTheAlgorithmCalculation(results, currentOccupantList);

//        "./network-minimisation-service/src/main/resources/results/ga/"
//        Path serviceCentreNamesFile = Paths.get("./network-minimisation-service/src/main/resources/results/services/service-centres-names.txt");
        Path resultList = Paths.get(resultsBasePath.toAbsolutePath().toString() + "/" + currentTime + "-ga-results-info" + ".txt");
        writeToCSVFile.writeTheAlgorithmAlgorithmIds(resultList, individual.getChromosome());
    }

}
