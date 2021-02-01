package ca.onepoint.yul.classes;

import ca.onepoint.yul.dto.SquareDto;
import lombok.Data;

import java.util.*;

/*
 * PathFinder Class inspired from our own previous work (https://github.com/Concordia-Campus-Guide/Concordia-Campus-Guide/blob/master/app/src/main/java/com/example/concordia_campus_guide/helper/PathFinder.java)
 * */
@Data
public class PathFinder {

    public static final int MAP_SIZE = 30;
    private HashMap<Integer, SquareNode> allNodesMap;
    private PriorityQueue<SquareNode> nodesToVisit;
    private HashMap<SquareNode, Double> nodesVisited;

    private SquareNode startNode;
    private SquareNode targetNode;

    public PathFinder(SquareDto[][] map, int startX, int startY, int endX, int endY) {
        this.nodesToVisit = new PriorityQueue<>(new SquareComparator());
        this.nodesVisited = new HashMap<>();

        populateGraph(map);

        this.startNode = allNodesMap.get(computeID(startX, startY));
        this.targetNode = allNodesMap.get(computeID(endX, endY));
    }

    // This is the method to call to get the list of squares that will lead to the destination [A* Search Algorithm]
    public List<SquareNode> getPathToDestination() {

        addInitialPointToMap();

        while (!nodesToVisit.isEmpty()) {
            final SquareNode currentNode = nodesToVisit.poll();

            if (nodesVisited.containsKey(currentNode))
                continue;
            else
                nodesVisited.put(currentNode, currentNode.getCost());

            if (currentNode.equals(targetNode)) {
                return getSolutionPath(currentNode);
            }
            addNearestSquareNodes(currentNode);
        }
        return new ArrayList<>();
    }

    // Helper methods

    private void populateGraph(SquareDto[][] map) {
        allNodesMap = new HashMap<>();
        // Populate hashmap
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                SquareDto squareDto = map[i][j];

                SquareNode node = new SquareNode();
                node.setX(j);
                node.setY(i);
                node.setCost(0);
                node.setHeuristic(0);
                node.setSquare(squareDto);
                node.setId(computeID(node));

                // Populate the adjacent list
                ArrayList<Integer> adjacentIds = new ArrayList<>();

                adjacentIds = addToList(adjacentIds, getLeftSquareId(map, node));
                adjacentIds = addToList(adjacentIds, getRightSquareId(map, node));
                adjacentIds = addToList(adjacentIds, getUpSquareId(map, node));
                adjacentIds = addToList(adjacentIds, getDownSquareId(map, node));

                node.setAdjacentSquareIds(adjacentIds);
                allNodesMap.put(node.getId(), node);
            }
        }
    }

    private ArrayList<Integer> addToList(ArrayList<Integer> adjacentIds, int adjacentId) {
        if (adjacentId >= 0) {
            adjacentIds.add(adjacentId);
        }
        return adjacentIds;
    }

    private int getLeftSquareId(SquareDto[][] map, SquareNode node) {
        int adjacentX = node.getX() - 1;
        int adjacentY = node.getY();
        if (adjacentX >= 0 && map[adjacentY][adjacentX].isWalkable()) {
            return computeID(adjacentX, adjacentY);
        }
        return -1;
    }

    private int getRightSquareId(SquareDto[][] map, SquareNode node) {
        int adjacentX = node.getX() + 1;
        int adjacentY = node.getY();
        if (adjacentX < MAP_SIZE && map[adjacentY][adjacentX].isWalkable()) {
            return computeID(adjacentX, adjacentY);
        }
        return -1;
    }

    private int getUpSquareId(SquareDto[][] map, SquareNode node) {
        int adjacentX = node.getX();
        int adjacentY = node.getY() - 1;
        if (adjacentY >= 0 && map[adjacentY][adjacentX].isWalkable()) {
            return computeID(adjacentX, adjacentY);
        }
        return -1;
    }

    private int getDownSquareId(SquareDto[][] map, SquareNode node) {
        int adjacentX = node.getX();
        int adjacentY = node.getY() + 1;
        if (adjacentY < MAP_SIZE && map[adjacentY][adjacentX].isWalkable()) {
            return computeID(adjacentX, adjacentY);
        }
        return -1;
    }


    private void addNearestSquareNodes(final SquareNode currentNode) {
        for (final int id : currentNode.getAdjacentSquareIds()) {

            final SquareNode adjacentNode = allNodesMap.get(id);
            final double currentCost = currentNode.getCost() + getEuclideanDistance(currentNode, adjacentNode);
            final double previousCost = adjacentNode.getCost();

            if (nodesVisited.containsKey(adjacentNode)) {
                if (currentCost < previousCost) {
                    updateNode(adjacentNode, currentNode, currentCost);
                }
            } else if (currentCost < previousCost && previousCost > 0 || previousCost == 0) {
                updateNode(adjacentNode, currentNode, currentCost);
                nodesToVisit.add(adjacentNode);
            }
        }
    }

    private void updateNode(final SquareNode node, final SquareNode parent, final double cost) {
        node.setParent(parent);
        node.setCost(cost);
        if (node.getHeuristic() == 0.0)
            node.setHeuristic(computeHeuristic(node));
    }

    protected List<SquareNode> getSolutionPath(SquareNode goalNode) {
        final List<SquareNode> solutionPath = new ArrayList<>();
        do {
            solutionPath.add(goalNode);
            goalNode = goalNode.getParent();
        } while (goalNode != null);

        Collections.reverse(solutionPath);
        return solutionPath;
    }

    public void addInitialPointToMap() {
        startNode.setHeuristic(computeHeuristic(startNode));
        nodesToVisit.add(startNode);
    }

    public int computeID(SquareNode node) {
        return computeID(node.getX(), node.getY());
    }

    public int computeID(int x, int y) {
        return y * MAP_SIZE + x + 1;
    }

    public static double getEuclideanDistance(final SquareNode start, final SquareNode finish) {
        double diffX = finish.getX() - start.getX();
        double diffY = finish.getY() - start.getY();
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    public double computeHeuristic(SquareNode node) {
        return getEuclideanDistance(node, targetNode);
    }

    protected double computeEstimatedCostFromInitialToDestination(final SquareNode currentNode) {
        return currentNode.getCost() + computeHeuristic(currentNode);
    }


    // Comparator used to compare the heuristics of 2 square nodes
    public class SquareComparator implements Comparator<SquareNode> {
        @Override
        public int compare(final SquareNode o1, final SquareNode o2) {
            return Double.compare(computeEstimatedCostFromInitialToDestination(o1), computeEstimatedCostFromInitialToDestination(o2));
        }
    }
}
