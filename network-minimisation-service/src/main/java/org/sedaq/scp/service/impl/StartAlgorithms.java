package org.sedaq.scp.service.impl;

import org.sedaq.scp.service.algorithms.MetaHeuristicAlgorithm;
import org.sedaq.scp.service.algorithms.cockoosearch.CuckooSearch;
import org.sedaq.scp.service.algorithms.differentialevolution.DifferentialEvolution;
import org.sedaq.scp.service.algorithms.geneticalgorithm.GeneticAlgorithm;
import org.sedaq.scp.service.algorithms.hillclimbing.HillClimbing;
import org.sedaq.scp.service.algorithms.simulatedannealing.SimulatedAnnealing;
import org.sedaq.scp.service.algorithms.tabusearch.TabuSearch;
import org.sedaq.scp.service.representation.InputMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Pavel Seda
 */
@Service
public class StartAlgorithms {

    private FindBestSolution findBestSolution;

    @Autowired
    public StartAlgorithms(FindBestSolution findBestSolution) {
        this.findBestSolution = findBestSolution;
    }

    public void startAlgorithmsConcurrently(MetaHeuristicAlgorithm metaHeuristicAlgorithm, int numberOfThreads, InputMatrix inputMatrix, List<Integer> necessaryServiceCentres, Double coveragePercentage, Path resultsBasePath) {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            startAlgorithm(metaHeuristicAlgorithm, inputMatrix, necessaryServiceCentres, coveragePercentage, resultsBasePath);
        }
        if (executorService != null) {
            executorService.shutdown();
        }
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            findBestSolution.findTheBestResult(resultsBasePath);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startAlgorithm(MetaHeuristicAlgorithm metaHeuristicAlgorithm, InputMatrix inputMatrix, List<Integer> necessaryServiceCentres, Double coveragePercentage, Path resultsBasePath) {
        if (metaHeuristicAlgorithm instanceof CuckooSearch) {
            CuckooSearch cuckooSearchAlgorithm = (CuckooSearch) metaHeuristicAlgorithm;
            cuckooSearchAlgorithm.startAlgorithm(inputMatrix, necessaryServiceCentres, coveragePercentage, resultsBasePath);
        } else if (metaHeuristicAlgorithm instanceof GeneticAlgorithm) {
            GeneticAlgorithm geneticAlgorithm = (GeneticAlgorithm) metaHeuristicAlgorithm;
            geneticAlgorithm.startAlgorithm(inputMatrix, necessaryServiceCentres, coveragePercentage, resultsBasePath);
        } else if (metaHeuristicAlgorithm instanceof HillClimbing) {
            HillClimbing hillClimbing = (HillClimbing) metaHeuristicAlgorithm;
            hillClimbing.startAlgorithm(inputMatrix, necessaryServiceCentres, coveragePercentage, resultsBasePath);
        } else if (metaHeuristicAlgorithm instanceof SimulatedAnnealing) {
            SimulatedAnnealing simulatedAnnealing = (SimulatedAnnealing) metaHeuristicAlgorithm;
            simulatedAnnealing.startAlgorithm(inputMatrix, necessaryServiceCentres, coveragePercentage, resultsBasePath);
        } else if (metaHeuristicAlgorithm instanceof TabuSearch) {
            TabuSearch tabuSearch = (TabuSearch) metaHeuristicAlgorithm;
            tabuSearch.startAlgorithm(inputMatrix, necessaryServiceCentres, coveragePercentage, resultsBasePath);
        }else if (metaHeuristicAlgorithm instanceof DifferentialEvolution){
            DifferentialEvolution differentialEvolution = (DifferentialEvolution) metaHeuristicAlgorithm;
            differentialEvolution.startAlgorithm(inputMatrix, necessaryServiceCentres, coveragePercentage, resultsBasePath);
        }
    }
}
