package org.sedaq.scp.service.algorithms.differentialevolution.mutation;

import org.sedaq.scp.service.algorithms.differentialevolution.population.Individual;
import org.sedaq.scp.service.algorithms.differentialevolution.population.Population;
import org.sedaq.scp.service.utils.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

public class Mutation {

    public Individual mutation(Population population) {
        Individual mutatedIndividual = createMutatedIndividual(getRandomIndividual(population), getRandomIndividual(population));
        return mutatedIndividual;
    }

    private Individual getRandomIndividual(Population population) {
        RandomGenerator rnd = new RandomGenerator();
        return population.getPopulation().get(rnd.randInt(0, population.getPopulation().size() - 1));
    }

    private Individual createMutatedIndividual(Individual ind1, Individual ind2) {
        RandomGenerator rnd = new RandomGenerator();
        List<Integer> chromosome = new ArrayList<Integer>();
        for (int i = 0; i < ind1.getChromosome().size(); i++) {
            if (ind1.getChromosome().get(i) != ind2.getChromosome().get(i)) {
                chromosome.add(rnd.getRandZeroOrOne());
            } else {
                chromosome.add(ind1.getChromosome().get(i));
            }
        }
        return new Individual(chromosome);
    }
}
