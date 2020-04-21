package org.sedaq.scp.service.algorithms.geneticalgorithm.selection;

import org.sedaq.scp.service.algorithms.geneticalgorithm.population.Individual;
import org.sedaq.scp.service.algorithms.geneticalgorithm.population.Population;

import java.util.List;

/**
 * Selects two individuals from the population for the upcoming crossover and mutation operations
 *
 * @author Pavel Seda
 */
public interface Selection {
    List<Individual> selection(Population pop);
}
