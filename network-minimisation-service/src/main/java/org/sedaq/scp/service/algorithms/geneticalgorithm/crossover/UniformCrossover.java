package org.sedaq.scp.service.algorithms.geneticalgorithm.crossover;

import org.sedaq.scp.service.algorithms.geneticalgorithm.population.Individual;
import org.sedaq.scp.service.utils.RandomGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavel Seda
 */
@Service("uniformCrossover")
public class UniformCrossover implements Crossover {

    private double ratio = 0.5;

    @Override
    public List<Individual> crossover(Individual ind1, Individual ind2) {
        int sizeOfIndividual = ind1.getChromosome().size();
        Individual offspring1 = new Individual();
        offspring1.generateChromosome(sizeOfIndividual);
        Individual offspring2 = new Individual();
        offspring2.generateChromosome(sizeOfIndividual);
        for (int i = 0; i < sizeOfIndividual; i++) {
            if (RandomGenerator.getRand().nextDouble() < this.ratio) {
                offspring1.setGene(i, ind2.getGene(i));
                offspring2.setGene(i, ind1.getGene(i));
            } else {
                offspring1.setGene(i, ind1.getGene(i));
                offspring2.setGene(i, ind2.getGene(i));
            }
        }
        return new ArrayList<>(List.of(offspring1, offspring2));
    }

    public int reverseValue(int i) {
        return i ^= 1;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

}
