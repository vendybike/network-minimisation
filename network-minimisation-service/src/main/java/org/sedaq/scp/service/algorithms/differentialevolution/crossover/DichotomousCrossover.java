package org.sedaq.scp.service.algorithms.differentialevolution.crossover;

import org.sedaq.scp.service.algorithms.differentialevolution.population.Individual;
import org.sedaq.scp.service.algorithms.differentialevolution.population.Population;
import org.sedaq.scp.service.utils.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

public class DichotomousCrossover implements Crossover {

    @Override
    public Individual crossover(Population population, Individual mutatedIndividual, Individual targetIndividual, double CR1, double CR2) {
        Individual ind1 = getRandomIndividual(population);
        Individual ind2 = getRandomIndividual(population);
        RandomGenerator rnd = new RandomGenerator();
        List<Integer> chromosome = new ArrayList<Integer>();
        double CR = 0;
        for (int indexOfChromosome : mutatedIndividual.getChromosome()) {
            if (ind1.getChromosome().get(indexOfChromosome) == ind2.getChromosome().get(indexOfChromosome)) {
                CR = CR1;
            } else {
                CR = CR2;
            }
            double randomValue = rnd.randInt(0, 10) * 0.1;
            if (randomValue <= CR) {
                chromosome.add(mutatedIndividual.getChromosome().get(indexOfChromosome));
            } else {
                chromosome.add(targetIndividual.getChromosome().get(indexOfChromosome));
            }
        }
        return new Individual(chromosome);
    }

    private Individual getRandomIndividual(Population population) {
        RandomGenerator rnd = new RandomGenerator();
        return population.getPopulation().get(rnd.randInt(0, population.getPopulation().size() - 1));
    }

}
