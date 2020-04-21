package org.sedaq.scp.service.algorithms.geneticalgorithm.selection;

import org.sedaq.scp.service.algorithms.geneticalgorithm.population.Individual;
import org.sedaq.scp.service.algorithms.geneticalgorithm.population.Population;
import org.sedaq.scp.service.utils.RandomGenerator;

import java.util.*;

/**
 * @author Pavel Seda
 */
public class RouleteWheelSelection implements Selection {

    @Override
    public List<Individual> selection(Population population) {
        Collections.sort(population.getPopulation(), (ind1, ind2) -> ((Integer) ind1.getFitnessValue()).compareTo(ind2.getFitnessValue()));

        int individualFitnessSum = getIndividualFitnessSum(population);
//        System.out.println("Individual fitness sum.." + individualFitnessSum);
        Set<Individual> selectedIndividuals = new HashSet<>();
        while ((selectedIndividuals.size() != 2)) {
            int tempSum = 0;
            int rndNumber = RandomGenerator.randInt(0, individualFitnessSum);
            for (int i = 0; i < population.getPopulation().size(); i++) {
                tempSum += population.getPopulation().get(i).getFitnessValue();
                if (tempSum >= rndNumber) {
                    // add to the selected list the list from
                    selectedIndividuals.add(population.getPopulation().get(i));
                    break;
                }
            }
        }
        return new ArrayList<>(selectedIndividuals);
    }

    private int getIndividualFitnessSum(Population population) {
        return population.getPopulation()
                .stream()
                .mapToInt(Individual::getFitnessValue)
                .sum();
    }
}
