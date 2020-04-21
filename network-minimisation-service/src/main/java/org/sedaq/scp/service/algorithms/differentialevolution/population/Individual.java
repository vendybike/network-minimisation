package org.sedaq.scp.service.algorithms.differentialevolution.population;

import org.sedaq.scp.service.utils.RandomGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Individual {

    private int fitness;
    private List<Integer> chromosome;

    public Individual() {
        this.chromosome = new ArrayList<Integer>();
        this.fitness = Integer.MAX_VALUE;
    }

    public Individual(List<Integer> chromosome) {
        this.chromosome = chromosome;
        this.fitness = getFitness(chromosome);
    }

    private int getFitness(List<Integer> chromosome) {
        int fitness = 0;
        for (int i = 0; i < chromosome.size(); i++) {
            if (chromosome.get(i) == 1) {
                fitness++;
            }
        }
        return fitness;
    }

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
        return this.fitness;
    }

    public void setFitnessValue(int fitnessValue) {
        this.fitness = fitnessValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Individual)) return false;
        Individual that = (Individual) o;
        return fitness == that.fitness &&
                Objects.equals(getChromosome(), that.getChromosome());
    }

    @Override
    public int hashCode() {
        return Objects.hash(fitness, getChromosome());
    }

    @Override
    public String toString() {
        return "Individual{" +
                "chromosome=" + chromosome +
                ", fitnessValue=" + fitness +
                '}';
    }


}
