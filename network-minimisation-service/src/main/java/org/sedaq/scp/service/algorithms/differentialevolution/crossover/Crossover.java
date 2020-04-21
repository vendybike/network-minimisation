package org.sedaq.scp.service.algorithms.differentialevolution.crossover;

import org.sedaq.scp.service.algorithms.differentialevolution.population.Individual;
import org.sedaq.scp.service.algorithms.differentialevolution.population.Population;

public interface Crossover {

    Individual crossover(Population population, Individual mutetedIndividual, Individual targetIndividual, double CR1, double CR2);
}

