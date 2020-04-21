package org.sedaq.scp.service.algorithms.geneticalgorithm.crossover;

import org.sedaq.scp.service.algorithms.geneticalgorithm.population.Individual;

import java.util.List;

/**
 * @author Pavel Seda
 */
public interface Crossover {
    List<Individual> crossover(Individual ind1, Individual ind2);
}
