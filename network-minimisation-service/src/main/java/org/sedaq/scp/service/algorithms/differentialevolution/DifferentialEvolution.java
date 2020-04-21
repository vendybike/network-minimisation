package org.sedaq.scp.service.algorithms.differentialevolution;

import org.sedaq.scp.service.algorithms.MetaHeuristicAlgorithm;
import org.sedaq.scp.service.algorithms.RepairOperator;
import org.sedaq.scp.service.algorithms.differentialevolution.crossover.Crossover;
import org.sedaq.scp.service.algorithms.differentialevolution.crossover.DichotomousCrossover;
import org.sedaq.scp.service.algorithms.differentialevolution.mutation.Mutation;
import org.sedaq.scp.service.algorithms.differentialevolution.population.Individual;
import org.sedaq.scp.service.algorithms.differentialevolution.population.Population;
import org.sedaq.scp.service.exceptions.DifferentialEvolutionException;
import org.sedaq.scp.service.io.WriteToCSVFile;
import org.sedaq.scp.service.representation.InputMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class DifferentialEvolution implements MetaHeuristicAlgorithm {

    private WriteToCSVFile writeToCSVFile;
    private RepairOperator repairOperator;

    @Autowired
    public DifferentialEvolution(WriteToCSVFile writeToCSVFile, RepairOperator repairOperator) {
        this.writeToCSVFile = writeToCSVFile;
        this.repairOperator = repairOperator;
    }

    public void startAlgorithm(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres) {
        startAlgorithm(inputMatrix, necessaryServiceCentres, 1000, 200, new DichotomousCrossover(), new Mutation(), 1.0, Paths.get("./network-minimisation-service/src/main/resources/results/de/"));
    }

    public void startAlgorithm(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres, Double coveragePercentage, Path resultsBasePath) {
        startAlgorithm(inputMatrix, necessaryServiceCentres, 1000, 200, new DichotomousCrossover(), new Mutation(), coveragePercentage, resultsBasePath);
    }

    public void startAlgorithm(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres, int generations, int populationSize, Crossover crossover, Mutation mutation, Double coveragePercentage, Path resultsBasePath) {
        Population population = new Population(generatePopulation(inputMatrix, necessaryServiceCentres, populationSize, coveragePercentage));
        List<Integer> theBestIndividualsList = new ArrayList<>();
        Population newPopulation = new Population();
        for (int i = 0; i < generations; i++) {
            Individual mutatedIndividual = mutation.mutation(population);
            for (Individual individual : population.getPopulation()) {
                Individual trialIndividual = crossover.crossover(population, mutatedIndividual, individual, 0.4, 0.8);
                repairOperator.repairOperator(trialIndividual.getChromosome(), inputMatrix, coveragePercentage);
                individual.setFitnessValue(computeFitnessValue(individual, inputMatrix.getCosts()));
                selectIndividual(individual, trialIndividual);
            }
            // insert the best individual from each iteration to the list
            Individual theBestIndividualAfterIteration = findBestIndividual(population);
            theBestIndividualsList.add(theBestIndividualAfterIteration.getFitnessValue());
        }
        //write results to the file
        Individual theBestIndividual = findBestIndividual(population);
        writeResultsToTheFile(resultsBasePath, theBestIndividualsList, theBestIndividual);
    }

    private void selectIndividual(Individual individual, Individual trialIndividual) {
        if (individual.getFitnessValue() < trialIndividual.getFitnessValue()) {
            individual = trialIndividual;
        }
    }

    public List<Individual> generatePopulation(InputMatrix inputMatrix, List<Integer> necessaryServiceCentres, int populationSize, Double coveragePercentage) {
        List<Individual> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            Individual individual = new Individual();
            individual.generateChromosome(inputMatrix.getMatrix().length);
            addNecessaryServiceCentres(individual, necessaryServiceCentres);
            repairOperator.repairOperator(individual.getChromosome(), inputMatrix, coveragePercentage);
            individual.setFitnessValue(computeFitnessValue(individual, inputMatrix.getCosts()));
            population.add(individual);
        }
        return population;
    }

    private int computeFitnessValue(Individual individual, List<Integer> costs) {
        List<Integer> individualData = individual.getChromosome();
        if (individualData == null) {
            throw new DifferentialEvolutionException("it is not possible to compute fitness because the neighbour was not generated..");
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
        Path results = Paths.get(resultsBasePath.toAbsolutePath().toString() + "/" + currentTime + "-de-results-algorithm-iterations" + ".csv");
        writeToCSVFile.writeTheAlgorithmCalculation(results, currentOccupantList);

//        "./network-minimisation-service/src/main/resources/results/ga/"
//        Path serviceCentreNamesFile = Paths.get("./network-minimisation-service/src/main/resources/results/services/service-centres-names.txt");
        Path resultList = Paths.get(resultsBasePath.toAbsolutePath().toString() + "/" + currentTime + "-de-results-info" + ".txt");
        writeToCSVFile.writeTheAlgorithmAlgorithmIds(resultList, individual.getChromosome());
    }
}


