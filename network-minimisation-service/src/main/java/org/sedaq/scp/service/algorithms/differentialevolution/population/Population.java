package org.sedaq.scp.service.algorithms.differentialevolution.population;

import java.util.ArrayList;
import java.util.List;

public class Population {
    private List<Individual> population;

    public Population() {
    }

    public Population(List<Individual> population) {
        this.population = new ArrayList<>(population);
    }

    public List<Individual> getPopulation() {
        return population;
    }

    public void setPopulation(List<Individual> population) {
        this.population = new ArrayList<>(population);
    }

    public void addIndividualToPopulation(Individual individual) {
        this.population.add(individual);
    }

    @Override
    public String toString() {
        return "Population{" +
                "population=" + population +
                '}';
    }
}
