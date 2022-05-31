import java.lang.reflect.Array;
import java.util.ArrayList;

public class Pathfinding {
    public static void main(String[] args){
        int xSize = 1000;
        int ySize = 1000;
        int[][] walls = {{1, 1}, {0, 1}, {4, 0}, {3, 1}, {4, 1}, {1, 2}};

        Node[][] array = createArray(xSize, ySize, walls);

        Algorithm binarySearch = new Algorithm(array, new int[]{0, 0}, new int[]{10, 10});
        ArrayList<PriorityQueueElement> result = binarySearch.aStar();
        System.out.println("Path: " + result);
        System.out.println("Lengt: " + result.size());
    }

    public static Node[][] createArray(int xSize, int ySize, int[][] walls){
        Node[][] array = new Node[xSize][ySize];

        for (int i = 0; i < xSize; i++){
            for (int j = 0; j < ySize; j++){
                array[i][j] = new Node(false, i ,j);
            }
        }

        for (int[] wall : walls){
            array[wall[0]][wall[1]].setWall(true);
        }

        return array;
    }
}
