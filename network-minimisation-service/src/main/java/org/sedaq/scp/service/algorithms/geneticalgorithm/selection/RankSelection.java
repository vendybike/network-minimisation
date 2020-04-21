package org.sedaq.scp.service.algorithms.geneticalgorithm.selection;

import org.sedaq.scp.service.algorithms.geneticalgorithm.population.Individual;
import org.sedaq.scp.service.algorithms.geneticalgorithm.population.Population;
import org.sedaq.scp.service.utils.RandomGenerator;

import java.util.*;

/**
 * @author Pavel Seda
 */
public class RankSelection implements Selection {

    @Override
    public List<Individual> selection(Population population) {
        Collections.sort(population.getPopulation(), (ind1, ind2) -> ((Integer) ind1.getFitnessValue()).compareTo(ind2.getFitnessValue()));

        Set<Individual> selectedIndividuals = new HashSet<>();
        // pomoci ruletove selekce vybere dva
        selectedIndividuals.add(rouletteForRankSelection(population.getPopulation()));
        selectedIndividuals.add(rouletteForRankSelection(population.getPopulation()));

        // this selection must select exactly two individuals
        while (selectedIndividuals.size() != 2) {
            selectedIndividuals.add(rouletteForRankSelection(population.getPopulation()));
        }

        return new ArrayList<>(selectedIndividuals);
    }

    private Individual rouletteForRankSelection(List<Individual> individuals) {
        // dle rank selection secte celkovou fitness, tak ze nejhorsi jedinec má fitness hodnotu 1 druhý nejhorší 2 atd.
        int suma = count(individuals.size());

        // urci nahodne cislo z intervalu 0 az celkova fitness
        int rndNumber = RandomGenerator.randInt(0, suma);
        int soucet = 0;

        //prochazi populaci a pocita sumu ucelovych funkci od hodnoty nula pokud se hodnota stane vetsi nebo rovnou rndNumber,
        //tak je vybran dany jedinec do reprodukcniho pole
        for (int i = 0; i < individuals.size(); i++) {
            soucet += i;
            if (soucet >= rndNumber) {
                return individuals.get(i);
            }
        }

        // nemelo by sem nikdy dojit
        return null;
    }

    public int count(int limit) {
        int suma = 0;
        for (int i = 0; i < limit; i++) {
            suma += i;
        }
        return suma;
    }

}
