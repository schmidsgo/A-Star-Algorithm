public class Node {

    private final int id;
    private final int x;
    private final int y;
    private double heuristic = Double.POSITIVE_INFINITY;
    private boolean wall;
    static int idCount = 0;

    Node(boolean wall, int x, int y){
        this.wall = wall;
        this.x = x;
        this.y = y;
        this.id = idCount;
        idCount++;
    }

    public int getId() {
        return id;
    }

    public boolean getWall(){
        return wall;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getHeuristic() {
        return heuristic;
    }

    public void setWall(boolean wall){
        this.wall = wall;
    }

    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }
}
