package org.sedaq.scp.service.io;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pavel Seda
 */
@Service
public class WriteToCSVFile {

    public void writeTheAlgorithmCalculation(Path fileResultsOfAlgorithmCalculations, List<Integer> bestFitnessList) {
        try {
            Files.createDirectories(fileResultsOfAlgorithmCalculations.getParent());
            Files.createFile(fileResultsOfAlgorithmCalculations);
            StringBuilder sb = new StringBuilder();
            sb.append("#id,best-individual").append(System.lineSeparator());
            for (int i = 0; i < bestFitnessList.size(); i++) {
                sb.append(i).append(",").append(bestFitnessList.get(i)).append(System.lineSeparator());
            }
            Files.write(fileResultsOfAlgorithmCalculations, sb.toString().getBytes("UTF-8"), StandardOpenOption.APPEND);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void writeTheAlgorithmSolution(Path serviceCentreNamesFile, Path serviceCentreNamesResult, List<Integer> solution) {
        StringBuilder sbServiceCentreNames = new StringBuilder();
        System.out.println("solution contains: " + solution);
        int i = 0;
        String line = "";
        try (BufferedReader br = Files.newBufferedReader(serviceCentreNamesFile)) {
            while ((line = br.readLine()) != null) {
                if (solution.get(i) == 1) {
                    sbServiceCentreNames.append(line).append(System.lineSeparator());
                }
                i++;
            }
            Files.write(serviceCentreNamesResult, (Arrays.toString(solution.toArray()) + System.lineSeparator() + sbServiceCentreNames.toString()).getBytes(), StandardOpenOption.CREATE);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void writeTheAlgorithmAlgorithmIds(Path serviceCentreOrderIdsResultFile, List<Integer> solution) {
        StringBuilder sbServiceCentreNames = new StringBuilder();
        System.out.println("solution contains: " + solution);
        List<Integer> listOfIds = new ArrayList<>();
        for (int i = 0; i < solution.size(); i++) {
            if (solution.get(i) == 1) {
                listOfIds.add(i);
            }
        }
        List<Integer> listOfIdsFromOne = new ArrayList<>();
        for (int i = 0; i < listOfIds.size(); i++) {
            listOfIdsFromOne.add(listOfIds.get(i) + 1);
        }
        long numberOfNecessaryService = computeSumOfNecessaryServices(solution);
        try {
            Files.write(serviceCentreOrderIdsResultFile, ("The number of necessary services: " + (numberOfNecessaryService + System.lineSeparator())).getBytes(), StandardOpenOption.CREATE);
            Files.write(serviceCentreOrderIdsResultFile, ("Final Chromosome: " + Arrays.toString(solution.toArray()) + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            Files.write(serviceCentreOrderIdsResultFile, ("The list of resulting service order Ids started from 0: " + (Arrays.toString(listOfIds.toArray())) + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            Files.write(serviceCentreOrderIdsResultFile, ("The list of resulting service order Ids started from 1: " + (Arrays.toString(listOfIdsFromOne.toArray()) + System.lineSeparator())).getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long computeSumOfNecessaryServices(List<Integer> solution) {
        return solution.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

}
