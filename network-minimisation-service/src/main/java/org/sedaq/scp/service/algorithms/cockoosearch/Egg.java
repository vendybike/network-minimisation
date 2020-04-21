package org.sedaq.scp.service.algorithms.cockoosearch;

import org.sedaq.scp.service.utils.RandomGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Pavel Seda
 */
public class Egg {

    private List<Integer> egg;
    private int fitnessValue;

    public Egg() {
        this.egg = new ArrayList<>();
        this.fitnessValue = Integer.MAX_VALUE;
    }

    /**
     * Generates values of chromosome for first currentOccupant.
     *
     * @param eggSize Basically the number of service centres (the number of rows in the matrix)
     */
    public void generateInitialEgg(int eggSize) {
        for (int i = 0; i < eggSize; i++) {
            this.egg.add(RandomGenerator.randInt(0, 1));
        }
    }

    public void provideNewSolutionBasedOnOld(Egg selectedEgg, int i) {
        for (int j = 0; j < selectedEgg.getEgg().size(); j++) {
            if (i != j)
                this.egg.add(selectedEgg.getEgg().get(j));
            else
                this.egg.add(reverseValue(selectedEgg.getEgg().get(j)));
        }
    }

    public int reverseValue(int i) {
        return i ^= 1;
    }

    public List<Integer> getEgg() {
        return egg;
    }

    public void setEgg(List<Integer> egg) {
        this.egg = new ArrayList<>(egg);
    }

    public int getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(int fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Egg)) return false;
        Egg egg1 = (Egg) o;
        return getFitnessValue() == egg1.getFitnessValue() &&
                getEgg().equals(egg1.getEgg());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEgg(), getFitnessValue());
    }

    @Override
    public String toString() {
        return "Egg{" +
                "egg=" + egg +
                ", fitnessValue=" + fitnessValue +
                '}';
    }
}