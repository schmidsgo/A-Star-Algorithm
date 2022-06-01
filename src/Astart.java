import java.util.*;

public class Astart {

    private final int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    private final Node start;
    private final Node end;
    private final Node[][] array;
    private int iterations = 0;

    Astart(Node[][] array, int[] start, int[] end){
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
        double heuristic = Math.sqrt(Math.pow(Math.abs(node.getX() - end.getX()), 2) +
                           Math.pow(Math.abs(node.getY() - end.getY()), 2));
        heuristic = (int)(heuristic*100)/100f;
        return heuristic;
    }

    public ArrayList<PriorityQueueElement> findPath(){
        PriorityQueue<PriorityQueueElement> openList = new PriorityQueue<>(new NodeComparator());
        ArrayList<PriorityQueueElement> closedList = new ArrayList<>();
        openList.add(new PriorityQueueElement(start, null, 0, start.getHeuristic()));


        while (!openList.isEmpty()){
            PriorityQueueElement current = openList.poll();

            if (current.node == end){
                ArrayList<PriorityQueueElement> output = new ArrayList<>();
                PriorityQueueElement next = current;
                while (next.previous != null) {
                    final PriorityQueueElement tempData = next;
                    output.add(next);
                    //noinspection OptionalGetWithoutIsPresent
                    next = closedList.stream().filter(o -> o.node.getId() == tempData.previous.getId()).findFirst().get();
                }
                for(int i = 0, j = output.size() - 1; i < j; i++) {
                    output.add(i, output.remove(j));
                }
                return output;
            }

            iterations++;

            for (int[] direction : directions){
                int x = current.node.getX() + direction[0];
                int y = current.node.getY() + direction[1];

                try {
                    if (!array[x][y].getWall()) {
                        Node neighbour = array[x][y];
                        int gScore = current.gScore + 1;
                        double fScore = gScore + neighbour.getHeuristic();

                        PriorityQueueElement existingEntry = null;
                        try {
                            //noinspection OptionalGetWithoutIsPresent
                            existingEntry = closedList.stream().filter(o -> o.node.getId() == neighbour.getId()).findFirst().get();
                        } catch (NoSuchElementException ignored){}

                        if (existingEntry == null){
                            openList.add(new PriorityQueueElement(neighbour, current.node,gScore ,fScore));
                        }

                    }
                } catch (IndexOutOfBoundsException ignore) {}
            }
            closedList.add(current);
        }
        return null;
    }

    public int getIterations() {
        return iterations;
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
     * @param previous  previous Node in Graph
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
            stringBuilder.append(", Previous: ").append((Object) null);
        }
        stringBuilder.append(", G-Score: ").append(gScore);
        stringBuilder.append(", F-Score: ").append(fScore);

        return stringBuilder.toString();
    }
}

class NodeComparator implements Comparator<PriorityQueueElement>{
    /**
     * Sorts the Queue ascending to F-Score
     */
    @Override
    public int compare(PriorityQueueElement n1, PriorityQueueElement n2) {
        return Double.compare(n1.fScore, n2.fScore);
    }
}

