import java.lang.reflect.Array;
import java.util.*;

public class Algorithm {

    private final int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    private final Node start;
    private final Node end;
    private final Node[][] array;

    Algorithm(Node[][] array, int[] start, int[] end){
        this.start = array[start[0]][start[1]];
        this.end = array[end[0]][end[1]];
        this.array = array;

        for (Node[] row : array){
            for (Node node : row){
                node.setHeuristic(calculateHeuristic(node));
            }
        }
    }

    public double calculateHeuristic(Node node){
        double heurisitc = Math.sqrt(Math.pow(Math.abs(node.getX() - end.getX()), 2) +
                           Math.pow(Math.abs(node.getY() - end.getY()), 2));
        heurisitc = (int)(heurisitc*100)/100f;
        return heurisitc;
    }

    public boolean isPath(){
        Queue<Node> queue = new LinkedList<>();
        ArrayList<Node> watchedNodes = new ArrayList<Node>();
        queue.add(start);

        while (!queue.isEmpty()){
            Node current = queue.poll();

            if (current == end){
                return true;
            }

            for (int[] direction : directions){
                int x = current.getX() + direction[0];
                int y = current.getY() + direction[1];

                try {
                    if (!array[x][y].getWall() && !watchedNodes.contains(array[x][y])) {
                        queue.add(array[x][y]);
                        watchedNodes.add(array[x][y]);
                    }
                } catch (IndexOutOfBoundsException e) {

                }

            }

        }
        return false;
    }

    public ArrayList<PriorityQueueElement> aStar(){
        PriorityQueue<PriorityQueueElement> openList = new PriorityQueue<PriorityQueueElement>(new NodeComperator());
        ArrayList<PriorityQueueElement> closedList = new ArrayList<PriorityQueueElement>();
        //ArrayList<Node> watchedNodes = new ArrayList<Node>();
        openList.add(new PriorityQueueElement(start, null, 0, start.getHeuristic()));


        while (!openList.isEmpty()){
            PriorityQueueElement current = openList.poll();

            if (current.node == end){
                ArrayList<PriorityQueueElement> output = new ArrayList<PriorityQueueElement>();
                PriorityQueueElement next = current;
                while (next.previous != null) {
                    final PriorityQueueElement tempData = next;
                    output.add(next);
                    next = closedList.stream().filter(o -> o.node.getId() == tempData.previous.getId()).findFirst().get();
                }
                for(int i = 0, j = output.size() - 1; i < j; i++) {
                    output.add(i, output.remove(j));
                }
                return output;
            }

            for (int[] direction : directions){
                int x = current.node.getX() + direction[0];
                int y = current.node.getY() + direction[1];

                try {
                    if (!array[x][y].getWall()) {
                        Node neighbour = array[x][y];
                        int gScore = current.gScore++;
                        double fscore = gScore + neighbour.getHeuristic();

                        PriorityQueueElement existingEntry = null;
                        try {
                            existingEntry = closedList.stream().filter(o -> o.node.getId() == neighbour.getId()).findFirst().get();
                        } catch (NoSuchElementException e) {

                        }
                        if (existingEntry == null){
                            openList.add(new PriorityQueueElement(neighbour, current.node,gScore ,fscore));
                        }

                    }
                } catch (IndexOutOfBoundsException e) {

                }
            }
            closedList.add(current);
        }

        return null;


    }
    
}


class PriorityQueueElement {

    Node node;
    Node previous;
    int gScore;
    double fScore;

    /**
     *
     * @param node      the representing Node in the graph
     * @param previous  previos Node in Graph
     * @param gScore    cost from start
     * @param fScore    cost from start + heuristic
     */

    PriorityQueueElement(Node node, Node previous, int gScore, double fScore){
        this.node = node;
        this.previous = previous;
        this.gScore = gScore;
        this.fScore = fScore;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n\t\t");
        stringBuilder.append("Node: ").append(node.getId());
        if (previous != null) {
            stringBuilder.append(", Previous: ").append(previous.getId());
        } else {
            stringBuilder.append(", Previous: ").append(previous);
        }
        stringBuilder.append(", G-Score: ").append(gScore);
        stringBuilder.append(", F-Score: ").append(fScore);

        return stringBuilder.toString();
    }
}

class NodeComperator implements Comparator<PriorityQueueElement>{

    @Override
    public int compare(PriorityQueueElement n1, PriorityQueueElement n2) {
        return Double.compare(n1.node.getHeuristic(), n2.node.getHeuristic());
    }
}

