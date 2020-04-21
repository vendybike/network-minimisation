package org.sedaq.scp.service.algorithms.simulatedannealing;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavel Seda
 */
public class NeighbourHood {

    private List<Neighbour> neighbours = new ArrayList<>();

    public NeighbourHood() {
    }

    public NeighbourHood(List<Neighbour> neighbours) {
        this.neighbours = new ArrayList<>(neighbours);
    }

    public List<Neighbour> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<Neighbour> neighbours) {
        this.neighbours = new ArrayList<>(neighbours);
    }

    @Override
    public String toString() {
        return "NeighbourHood{" +
                "neighbours=" + neighbours +
                '}';
    }
}
