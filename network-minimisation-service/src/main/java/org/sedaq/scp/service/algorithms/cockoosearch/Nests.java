package org.sedaq.scp.service.algorithms.cockoosearch;

import java.util.ArrayList;
import java.util.List;

/**
 * That is the variant where each nest contains one egg.
 *
 * @author Pavel Seda
 */
public class Nests {

    private List<Egg> eggs = new ArrayList<>();

    public Nests() {
    }

    public Nests(List<Egg> eggs) {
        this.eggs = new ArrayList<>(eggs);
    }

    public List<Egg> getEggs() {
        return eggs;
    }

    public void setEggs(List<Egg> eggs) {
        this.eggs = new ArrayList<>(eggs);
    }

    @Override
    public String toString() {
        return "Nests{" +
                "eggs=" + eggs +
                '}';
    }
}
