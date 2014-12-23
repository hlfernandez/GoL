package hlfernandez.sing.ei.uvigo.es.gameoflife;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Board {

    private Set<Cell> cells;

    public Board(Cell... cells) {
        this.setCells(cells);
    }

    public Set<Cell> getCells() {
        return this.cells;
    }

    public void setCells(Cell ... cells) {
        this.cells = new HashSet<Cell>(Arrays.asList(cells));
    }

    public void nextGeneration() {
        Set<Cell> nextGenerationCells = new HashSet<Cell>();
        Map<Cell, Integer> cellNeighbourds = computeNeighbourds();
        for (Cell c : cellNeighbourds.keySet()) {
            Integer neigbhourds = cellNeighbourds.get(c);
            if(neigbhourds == 3 ||
                    ((neigbhourds == 3 || neigbhourds == 2) && this.cells.contains(c))){
                nextGenerationCells.add(c);
            }
        }
        this.cells = nextGenerationCells;
    }

    private Map<Cell, Integer> computeNeighbourds() {
        Map<Cell, Integer> toret = new HashMap<Cell, Integer>();
        List<Cell> allCells = new LinkedList<Cell>();
        for (Cell cell : this.cells) {
            allCells.addAll(cell.getNeighbourds());
        }
        for (Cell cell : allCells) {
            if(toret.get(cell) == null) {
                toret.put(cell,0);
            }
            toret.put(cell, toret.get(cell) + 1);
        }
        return toret;
    }

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder();
        for (Cell c : this.cells) {
            toString.append(c).append("\n");
        }
        return toString.toString();
    }

    public static final Cell[] randomCells(int rows, int cols){
        Set<Cell> cells = new HashSet<Cell>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (Math.random() > 0.7) {
                    cells.add(new Cell(i, j));
                }
            }
        }
        return cells.toArray(new Cell[cells.size()]);
    }
}
