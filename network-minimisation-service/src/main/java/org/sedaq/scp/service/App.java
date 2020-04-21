package org.sedaq.scp.service;

import org.sedaq.scp.service.algorithms.RepairOperator;
import org.sedaq.scp.service.algorithms.differentialevolution.DifferentialEvolution;
import org.sedaq.scp.service.algorithms.geneticalgorithm.GeneticAlgorithm;
import org.sedaq.scp.service.datapreparation.DataPreparation;
import org.sedaq.scp.service.impl.FindBestSolution;
import org.sedaq.scp.service.impl.StartAlgorithms;
import org.sedaq.scp.service.io.ReadInputData;
import org.sedaq.scp.service.io.WriteToCSVFile;
import org.sedaq.scp.service.datapreparation.CheckSpecialCases;
import org.sedaq.scp.service.representation.InputMatrix;
import org.sedaq.scp.service.utils.MatrixUtils;

import java.nio.file.*;
import java.util.List;

/**
 * @author Pavel Seda
 */
public class App {

    public static void main(String[] args) {
        MatrixUtils matrixUtils = new MatrixUtils();
        ReadInputData readInputData = new ReadInputData();
        WriteToCSVFile writeToCSVFile = new WriteToCSVFile();
        CheckSpecialCases checkSpecialCases = new CheckSpecialCases();
        RepairOperator repairOperator = new RepairOperator();
        StartAlgorithms startAlgorithms = new StartAlgorithms(new FindBestSolution());

        Path paths = Paths.get("./network-minimisation-service/src/main/resources/final-data.csv");

        DataPreparation dataPreparation = new DataPreparation(readInputData, matrixUtils, checkSpecialCases);
        InputMatrix inputMatrix = dataPreparation.readData(paths, ";", 15, true);

        // for the use-case that every customer location needs to be covered directly by one service centre (multiple services problem)
        inputMatrix.setNumberOfRequiredServicesForAllLocations(1);

        checkSpecialCases.multipleServicesSatisfiable(inputMatrix);

        List<Integer> necessaryServiceCentres = dataPreparation.prepareData(inputMatrix);

        Path resultsBasePathGA = Paths.get("./network-minimisation-service/src/main/resources/results/ga/");
//        Path resultsBasePathSA = Paths.get("./network-minimisation-service/src/main/resources/results/sa");
//        Path resultsBasePathTS = Paths.get("./network-minimisation-service/src/main/resources/results/ts");
//        Path resultsBasePathHC = Paths.get("./network-minimisation-service/src/main/resources/results/hc");
//        Path resultsBasePathCS = Paths.get("./network-minimisation-service/src/main/resources/results/cs");
       // Path resultsBasePathDE = Paths.get("./network-minimisation-service/src/main/resources/results/de");

        int numberOfThreads = 2;
        startAlgorithms.startAlgorithmsConcurrently(new GeneticAlgorithm(writeToCSVFile, repairOperator), numberOfThreads, inputMatrix, necessaryServiceCentres, 1.0, resultsBasePathGA);
     //   startAlgorithms.startAlgorithmsConcurrently(new DifferentialEvolution(writeToCSVFile, repairOperator), numberOfThreads, inputMatrix, necessaryServiceCentres, 1.0, resultsBasePathDE);
      // startAlgorithms.startAlgorithmsConcurrently(new CuckooSearch(writeToCSVFile, repairOperator), numberOfThreads, inputMatrix, necessaryServiceCentres, 1.0, resultsBasePathCS);
    //   startAlgorithms.startAlgorithmsConcurrently(new HillClimbing(writeToCSVFile, repairOperator), numberOfThreads, inputMatrix, necessaryServiceCentres, 1.0, resultsBasePathHC);
//        startAlgorithms.startAlgorithmsConcurrently(new SimulatedAnnealing(writeToCSVFile, repairOperator), numberOfThreads, inputMatrix, necessaryServiceCentres, 1.0, resultsBasePathSA);
//        startAlgorithms.startAlgorithmsConcurrently(new TabuSearch(writeToCSVFile, repairOperator), numberOfThreads, inputMatrix, necessaryServiceCentres, 1.0, resultsBasePathTS);
    }

}