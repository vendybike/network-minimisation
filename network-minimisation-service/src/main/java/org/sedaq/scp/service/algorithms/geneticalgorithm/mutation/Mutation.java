package org.sedaq.scp.service.algorithms.geneticalgorithm.mutation;

import org.sedaq.scp.service.algorithms.geneticalgorithm.population.Individual;
import org.sedaq.scp.service.utils.RandomGenerator;
import org.springframework.stereotype.Service;

/**
 * @author Seda
 */
@Service
public class Mutation {

    /**
     * Mutate a specific gen of Individual (it basically means that we reverse a value of int from 1 to 0 or from 0 to 1)
     *
     * @param individual to mutate a specific gen
     */
   public void mutation(Individual individual, double ratio) {
        double rnd = RandomGenerator.getRand().nextDouble();
        if (rnd <= ratio) {
            int index = RandomGenerator.getRand().nextInt(individual.getChromosome().size() - 1);
            individual.getChromosome().set(index, reverseValue(individual.getChromosome().get(index)));    // flip
        }
    }



    /**
     * Converts 1 to 0 or 0 to 1
     * @param i represents value to be reversed.
     * @return reversed value
     */
    public int reverseValue(int i) {
        return i ^= 1;
    }

}