package org.sedaq.scp.service.io;

import org.sedaq.scp.service.exceptions.ReadIOException;
import org.sedaq.scp.service.representation.InputMatrix;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pavel Seda
 */
@Service
public class ReadInputData {

    /**
     * Reads .csv file and convert file to matrix represented by
     *
     * @param pathToFile path to given file
     * @param delimiter  delimeter of values (usually symbol ';')
     * @return
     */
    public InputMatrix readInputDataWithCapacitiesAndCostsToInputMatrix(Path pathToFile, String delimiter) {
        try {
            List<String> fileLines = Files.readAllLines(pathToFile);
            fileLines.removeIf(line -> (line == null || line.startsWith("#") || line.isBlank()));
            List<Integer> serviceCentreCosts = getCSVLineValues(fileLines, 0, delimiter);
            List<BigDecimal> serviceCentresCapacities = getCSVLineValuesCapacities(fileLines, 1, delimiter);
            List<BigDecimal> customerLocationCapacities = getCSVLineValuesCapacities(fileLines, 2, delimiter);

            // suppose that all rows have same number of columns
            int[][] inputMatrix = new int[fileLines.size() - 3][fileLines.get(0).length()];
            for (int i = 0; i < fileLines.size(); i++) {
                inputMatrix[i] = convertCsvStringToIntArray(fileLines.get(i), delimiter);
            }
            return new InputMatrix(inputMatrix, serviceCentreCosts, serviceCentresCapacities, customerLocationCapacities);
        } catch (IOException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new ReadIOException("Input array is not valid.");
        }
    }

    /**
     * Reads .csv file and convert file to matrix represented by int[][]
     *
     * @param pathToFile path to given file
     * @param delimiter  delimeter of values (usually symbol ';')
     * @return
     */
    public InputMatrix readInputDataInputMatrix(Path pathToFile, String delimiter) {
        try {
            List<String> fileLines = Files.readAllLines(pathToFile);
            fileLines.removeIf(line -> line == null || line.startsWith("#") || line.isBlank());
            List<Integer> serviceCentreCosts = new ArrayList<>();
            // suppose that all rows have same number of columns
            int[][] inputMatrix = new int[fileLines.size()][fileLines.get(0).length()];
            for (int i = 0; i < fileLines.size(); i++) {
                inputMatrix[i] = convertCsvStringToIntArray(fileLines.get(i), delimiter);
                serviceCentreCosts.add(1);
            }
            return new InputMatrix(inputMatrix, serviceCentreCosts);
        } catch (IOException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new ReadIOException("Input array is not valid.", e);
        }
    }

    public int[] convertCsvStringToIntArray(String csvString, String delimiter) {
        return Arrays.stream(csvString.split(delimiter)).map(String::trim).mapToInt(Integer::parseInt).toArray();
    }

    private List<Integer> getCSVLineValues(List<String> fileLines, int index, String delimiter) {
        List<Integer> values = new ArrayList<>();
        String[] line = fileLines.get(index).split(delimiter);
        for (String value : line) {
            values.add(Integer.parseInt(value));
        }
        return values;
    }

    private List<BigDecimal> getCSVLineValuesCapacities(List<String> fileLines, int index, String delimiter) {
        List<BigDecimal> values = new ArrayList<>();
        String[] line = fileLines.get(index).split(delimiter);
        for (String value : line) {
            values.add(BigDecimal.valueOf(Integer.parseInt(value)));
        }
        return values;
    }


}
