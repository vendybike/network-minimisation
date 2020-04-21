package org.sedaq.scp.service;

import org.sedaq.scp.service.algorithms.geneticalgorithm.GeneticAlgorithm;
import org.sedaq.scp.service.datapreparation.DataPreparation;
import org.sedaq.scp.service.representation.InputMatrix;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * @author Pavel Seda
 */
@SpringBootApplication
public class AppSpringRun {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(AppSpringRun.class, args);

        DataPreparation dataPreparation = applicationContext.getBean(DataPreparation.class);

        Path paths = Paths.get("./network-minimisation-service/src/main/resources/blansko-pure-data.csv");
        InputMatrix inputMatrix = dataPreparation.readData(paths, ";", 15, true);

//        List<Integer> possibleImmediateSolution = dataPreparation.checkImmediateSolutionAndDataValidity(inputMatrix);
//        if (!possibleImmediateSolution.equals(Collections.emptyList())) {
//            System.out.println("This data set has immediate solution" + possibleImmediateSolution);
//        }
        List<Integer> necessaryServiceCentres = dataPreparation.prepareData(inputMatrix);

        GeneticAlgorithm geneticAlgorithm = applicationContext.getBean(GeneticAlgorithm.class);
        geneticAlgorithm.startAlgorithm(inputMatrix, necessaryServiceCentres);

//        SimulatedAnnealing simulatedAnnealing = applicationContext.getBean(SimulatedAnnealing.class);
//        simulatedAnnealing.startAlgorithm(inputMatrix, necessaryServiceCentres);
    }

}
