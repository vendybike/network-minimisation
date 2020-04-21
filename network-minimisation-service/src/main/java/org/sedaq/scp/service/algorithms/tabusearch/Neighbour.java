package org.sedaq.scp.service.algorithms.tabusearch;

import org.sedaq.scp.service.utils.RandomGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Pavel Seda
 */
public class Neighbour {

    private List<Integer> neighbour;
    private int fitnessValue;

    public Neighbour() {
        this.neighbour = new ArrayList<>();
        this.fitnessValue = Integer.MAX_VALUE;
    }

    /**
     * Generates values of chromosome for first currentOccupant.
     *
     * @param neighbourSize Basically the number of service centres (the number of rows in the matrix)
     */
    public void generateNeighbour(int neighbourSize) {
        for (int i = 0; i < neighbourSize; i++) {
            this.neighbour.add(RandomGenerator.randInt(0, 1));
        }
    }

    public void generateNeighbour(Neighbour currentOccupant, int i) {
        for (int j = 0; j < currentOccupant.getNeighbour().size(); j++) {
            if (i != j)
                this.neighbour.add(currentOccupant.getNeighbour().get(j));
            else
                this.neighbour.add(reverseValue(currentOccupant.getNeighbour().get(j)));
        }
    }

    public int reverseValue(int i) {
        return i ^= 1;
    }

    public List<Integer> getNeighbour() {
        return neighbour;
    }

    public void setNeighbour(List<Integer> neighbour) {
        this.neighbour = new ArrayList<>(neighbour);
    }

    public int getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(int fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Neighbour)) return false;
        Neighbour neighbour1 = (Neighbour) o;
        return getFitnessValue() == neighbour1.getFitnessValue() &&
                getNeighbour().equals(neighbour1.getNeighbour());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNeighbour(), getFitnessValue());
    }

    @Override
    public String toString() {
        return "Neighbour{" +
                "neighbour=" + neighbour +
                ", fitnessValue=" + fitnessValue +
                '}';
    }

}
