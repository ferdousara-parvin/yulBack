package ca.onepoint.yul.classes;

import ca.onepoint.yul.dto.SquareDto;
import lombok.Data;

import java.util.*;

@Data
public class PathFinder {

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
            addNearestWalkingPoints(currentNode);
        }
        return new ArrayList<>();
    }

    // Helper methods

    private void populateGraph(SquareDto[][] map) {
        allNodesMap = new HashMap<>();
        // Populate hashmap
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
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

                // Left
                int adjacentX = node.getX() - 1;
                int adjacentY = node.getY();
                if (adjacentX >= 0 && map[adjacentY][adjacentX].getValue() != 0)
                    adjacentIds.add(computeID(adjacentX, adjacentY));

                //Right
                adjacentX = node.getX() + 1;
                adjacentY = node.getY();
                if (adjacentX <= 29 && map[adjacentY][adjacentX].getValue() != 0)
                    adjacentIds.add(computeID(adjacentX, adjacentY));

                //Up
                adjacentX = node.getX();
                adjacentY = node.getY() - 1;
                if (adjacentY >= 0 && map[adjacentY][adjacentX].getValue() != 0)
                    adjacentIds.add(computeID(adjacentX, adjacentY));

                //Down
                adjacentX = node.getX();
                adjacentY = node.getY() + 1;
                if (adjacentY <= 29 && map[adjacentY][adjacentX].getValue() != 0)
                    adjacentIds.add(computeID(adjacentX, adjacentY));

                node.setAdjacentSquareID(adjacentIds);
                allNodesMap.put(node.getId(), node);
            }
        }
    }


    private void addNearestWalkingPoints(final SquareNode currentNode) {
        for (final int id : currentNode.getAdjacentSquareID()) {

            final SquareNode adjacentNode = allNodesMap.get(id);
            final double currentCost = currentNode.getCost() + getEuclideanDistance(currentNode, adjacentNode);
            final double previousCost = adjacentNode.getCost();

            if (nodesVisited.containsKey(adjacentNode)) {
                if (currentCost < previousCost) {
                    updateWalkingNode(adjacentNode, currentNode, currentCost);
                }
            } else if (currentCost < previousCost && previousCost > 0 || previousCost == 0) {
                updateWalkingNode(adjacentNode, currentNode, currentCost);
                nodesToVisit.add(adjacentNode);
            }
        }
    }

    private void updateWalkingNode(final SquareNode node, final SquareNode parent, final double cost) {
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


    public int getLeftNeighbourID(SquareNode node) {
        int x = node.getX() - 1;
        int y = node.getY();
        return computeID(x, y);
    }

    public int getRightNeighbourID(SquareNode node) {
        int x = node.getX() + 1;
        int y = node.getY();
        return computeID(x, y);
    }

    public int getUpNeighbourID(SquareNode node) {
        int x = node.getX();
        int y = node.getY() - 1;
        return computeID(x, y);
    }

    public int getDownNeighbourID(SquareNode node) {
        int x = node.getX();
        int y = node.getY() + 1;
        return computeID(x, y);
    }

    public int computeID(SquareNode node) {
        return computeID(node.getX(), node.getY());
    }

    public int computeID(int x, int y) {
        return y * 30 + x + 1;
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

    /**
     * This is a comparator object that will compare our Walking point based on a heuristic for a priorityQueue.
     */
    public class SquareComparator implements Comparator<SquareNode> {
        @Override
        public int compare(final SquareNode o1, final SquareNode o2) {
            if (computeEstimatedCostFromInitialToDestination(o1) < computeEstimatedCostFromInitialToDestination(o2)) {
                return -1;
            }
            if (computeEstimatedCostFromInitialToDestination(o1) > computeEstimatedCostFromInitialToDestination(o2)) {
                return 1;
            }
            return 0;
        }
    }
}