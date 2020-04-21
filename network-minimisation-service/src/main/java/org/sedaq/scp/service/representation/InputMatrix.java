package org.sedaq.scp.service.representation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pavel Seda
 */
public class InputMatrix {

    private int[][] matrix;
    private List<Integer> costs;
    private List<Integer> numbersOfRequiredServices;
    private List<BigDecimal> serviceCentresCapacities;
    private List<BigDecimal> customerLocationsCapacities;

    public InputMatrix(int[][] capacityMatrix, List<Integer> costs, List<BigDecimal> serviceCentresCapacities, List<BigDecimal> customerLocationsCapacities) {
        this.matrix = capacityMatrix;
        this.costs = new ArrayList<>(costs);
        this.numbersOfRequiredServices = new ArrayList<>();
        this.serviceCentresCapacities = new ArrayList<>(serviceCentresCapacities);
        this.customerLocationsCapacities = new ArrayList<>(customerLocationsCapacities);
    }

    public InputMatrix(int[][] capacityMatrix, List<Integer> costs, List<Integer> numbersOfRequiredServices, List<BigDecimal> serviceCentresCapacities, List<BigDecimal> customerLocationsCapacities) {
        this.matrix = capacityMatrix;
        this.costs = new ArrayList<>(costs);
        this.numbersOfRequiredServices = new ArrayList<>(numbersOfRequiredServices);
        this.serviceCentresCapacities = new ArrayList<>(serviceCentresCapacities);
        this.customerLocationsCapacities = new ArrayList<>(customerLocationsCapacities);
    }

    public InputMatrix(int[][] reachAbilityMatrix) {
        this.matrix = reachAbilityMatrix;
    }

    public InputMatrix(int[][] reachAbilityMatrix, List<Integer> costs) {
        this.matrix = reachAbilityMatrix;
        this.costs = new ArrayList<>(costs);
    }

    public void printMatrix(){
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix[i].length; j++) {
                System.out.print(this.matrix[i][j]);
            }
            System.out.print("\n");
        }
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public List<Integer> getCosts() {
        return costs;
    }

    public List<BigDecimal> getServiceCentresCapacities() {
        return serviceCentresCapacities;
    }

    public void setServiceCentresCapacities(List<BigDecimal> serviceCentresCapacities) {
        this.serviceCentresCapacities = new ArrayList<>(serviceCentresCapacities);
    }

    public List<BigDecimal> getCustomerLocationsCapacities() {
        return customerLocationsCapacities;
    }

    public void setCustomerLocationsCapacities(List<BigDecimal> customerLocationsCapacities) {
        this.customerLocationsCapacities = new ArrayList<>(customerLocationsCapacities);
    }

    public void setCosts(List<Integer> costs) {
        this.costs = costs;
    }

    public List<Integer> getNumbersOfRequiredServices() {
        return numbersOfRequiredServices;
    }

    public void setNumbersOfRequiredServices(List<Integer> numbersOfRequiredServices) {
        this.numbersOfRequiredServices = numbersOfRequiredServices;
    }

    public void setNumberOfRequiredServicesForAllLocations(int numberOfRequiredServices) {
        List<Integer> requiredServices = new ArrayList<>();
        if (getMatrix() != null) {
            for (int i = 0; i < getMatrix()[0].length; i++) {
                requiredServices.add(numberOfRequiredServices);
            }
            setNumbersOfRequiredServices(requiredServices);
        }
    }

    @Override
    public String toString() {
        return "InputMatrix{" +
                "matrix=" + Arrays.toString(matrix) +
                ", costs=" + costs +
                ", numbersOfRequiredServices=" + numbersOfRequiredServices +
                ", serviceCentresCapacities=" + serviceCentresCapacities +
                ", customerLocationsCapacities=" + customerLocationsCapacities +
                '}';
    }
}
