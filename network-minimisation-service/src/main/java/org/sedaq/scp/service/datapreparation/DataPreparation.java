package org.sedaq.scp.service.datapreparation;

import org.sedaq.scp.service.exceptions.SpecialCasesException;
import org.sedaq.scp.service.io.ReadInputData;
import org.sedaq.scp.service.representation.InputMatrix;
import org.sedaq.scp.service.utils.MatrixUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Service
public class DataPreparation {

    private MatrixUtils matrixUtils;
    private CheckSpecialCases checkSpecialCases;
    private ReadInputData readInputData;

    @Autowired
    public DataPreparation(ReadInputData readInputData, MatrixUtils matrixUtils, CheckSpecialCases checkSpecialCases) {
        this.readInputData = readInputData;
        this.matrixUtils = matrixUtils;
        this.checkSpecialCases = checkSpecialCases;
    }

    public InputMatrix readData(Path pathToInputMatrix, String delimiter, int dMax, boolean needDistanceToReachabilityMatrixConversion) {
        InputMatrix inputMatrix = readInputData.readInputDataInputMatrix(pathToInputMatrix, delimiter);
        if (needDistanceToReachabilityMatrixConversion) {
            inputMatrix.setMatrix(matrixUtils.convertDistanceMatrixToReachabilityMatrix(inputMatrix.getMatrix(), dMax));
//            writeReachAbilityMatrixFromDistanceMatrix(inputMatrix);
        }
        return inputMatrix;
    }

    private void writeReachAbilityMatrixFromDistanceMatrix(InputMatrix inputMatrix){
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("./network-minimisation-service/src/main/resources/reach-ability-matrix-distance.csv"))) {
            int[][] reachabilityMatrix = inputMatrix.getMatrix();
            for (int i = 0; i < reachabilityMatrix.length; i++) {
                for (int j = 0; j < reachabilityMatrix[i].length; j++) {
                    bw.write(reachabilityMatrix[i][j] + ",");
                }
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> checkImmediateSolutionAndDataValidity(InputMatrix inputMatrix) {
        if (!checkSpecialCases.serviceCentreCoverSomething(inputMatrix.getMatrix())) {
            throw new SpecialCasesException("Reachability matrix contains only zeros, service centres does not cover anything. Please consider different communication technology or increase dMax");
        }
        if (!checkSpecialCases.isDMaxValid(inputMatrix.getMatrix())) {
            throw new SpecialCasesException("Parameter dMax is not valid.");
        }
        List<Integer> serviceCentresAsImmediateSolution = checkSpecialCases.serviceCentresAsImmediateSolution(inputMatrix.getMatrix());
        if (serviceCentresAsImmediateSolution != null && serviceCentresAsImmediateSolution.size() > 0) {
            return serviceCentresAsImmediateSolution;
        }
        return Collections.emptyList();
    }

    public List<Integer> prepareData(InputMatrix inputMatrix) {
        List<Integer> necessaryServiceCentres = checkSpecialCases.necessaryServiceCentres(inputMatrix.getMatrix());
//        List<Integer> serviceCentresToRemove = checkDataValidity.serviceCentresNotCoverAnything(inputMatrix.getMatrix());
//        inputMatrix.setMatrix(matrixUtils.removeGivenServiceCentres(inputMatrix.getMatrix(), serviceCentresToRemove));
//        inputMatrix.setMatrix(matrixUtils.convertReachabilityMatrixToCapacityMatrix(inputMatrix.getMatrix(), inputMatrix.getCustomerLocationsCapacities()));

        return necessaryServiceCentres;
    }


}
