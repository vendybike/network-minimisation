package org.sedaq.scp.service.algorithms.geneticalgorithm.population;

import org.sedaq.scp.service.utils.RandomGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Pavel Seda
 */
public class Individual {

    private List<Integer> chromosome;
    private int fitnessValue;

    public Individual() {
        this.chromosome = new ArrayList<>();
        this.fitnessValue = Integer.MAX_VALUE;
    }

    /**
     * generates values of chromosome
     */
    public void generateChromosome(int chromosomeLength) {
        for (int i = 0; i < chromosomeLength; i++) {
            this.chromosome.add(RandomGenerator.getRandZeroOrOne());
        }
    }

    public void setGene(int index, int value) {
        this.getChromosome().set(index, value);
    }

    public int getGene(int index) {
        return this.getChromosome().get(index);
    }

    public List<Integer> getChromosome() {
        return chromosome;
    }

    public void setChromosome(List<Integer> chromosome) {
        this.chromosome = new ArrayList<>(chromosome);
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
        if (!(o instanceof Individual)) return false;
        Individual that = (Individual) o;
        return getFitnessValue() == that.getFitnessValue() &&
                Objects.equals(getChromosome(), that.getChromosome());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChromosome(), getFitnessValue());
    }

    @Override
    public String toString() {
        return "Individual{" +
                "chromosome=" + chromosome +
                ", fitnessValue=" + fitnessValue +
                '}';
    }
}
