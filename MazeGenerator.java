import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MazeGenerator {
    private static int SIZE_X = 61;
    private static int SIZE_Y = 61;

    private static final char WALL = '1';
    private static final char PATH = '0';

    private static final int[] DX = {0, 0, 1, -1};
    private static final int[] DY = {1, -1, 0, 0};
    private static final Random RANDOM = new Random();
	
	public static void changeSize(int inputX, int inputY) {
		SIZE_X = inputX;
		SIZE_Y = inputY;
    }
	

    public static char[][] generateMaze() {
		
        char[][] maze = new char[SIZE_X][SIZE_Y];

        printDebug("Initialize all cells as walls");
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                maze[i][j] = WALL;
            }
        }

        printDebug("Set the start point");
        int startX = 1;
        int startY = 1;
        maze[startX][startY] = PATH;

        printDebug("Generate the maze using Randomized Prim's Algorithm");
        List<int[]> walls = new ArrayList<>();
        walls.add(new int[]{startX, startY});

        while (!walls.isEmpty()) {
            		       
            int randomWallIndex = RANDOM.nextInt(walls.size());
			int[] currentWall = walls.get(randomWallIndex);
			int currentX = currentWall[0];
			int currentY = currentWall[1];
			
            int passageCount = 0;
            for (int i = 0; i < 4; i++) {
                int nextX = currentX + DX[i] * 2;
                int nextY = currentY + DY[i] * 2;

                if (isValidCell(nextX, nextY, maze)) {
                    maze[nextX][nextY] = PATH;
                    maze[currentX + DX[i]][currentY + DY[i]] = PATH;

                    walls.add(new int[]{nextX, nextY});
                    passageCount++;
                }
            }

            if (passageCount == 0) {
                walls.remove(randomWallIndex);
            }
        }
		printDebug("Maze generated complete!");
	
		
        printDebug("Set the end point in the last row.");
        int endX = SIZE_X - 2;
        int endY = SIZE_Y - 2;
        maze[endX][endY] = PATH;
        maze[SIZE_X-1][SIZE_Y-2] = PATH;
		printMaze(maze);
        return maze;
    }

    private static boolean isValidCell(int x, int y, char[][] maze) {
        return x >= 1 && x < SIZE_X - 1 && y >= 1 && y < SIZE_Y - 1 && maze[x][y] == WALL;
    }

    private static void printMaze(char[][] maze) {
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
    }
	
	public static void printDebug (String str) {
        System.out.println(">>>"+str);
    }
	
    public static void main(String[] args) {
        char[][] maze = generateMaze();
        printMaze(maze);
		
    }
	public static int getSizeX(){
		return SIZE_X;
	}
	public static int getSizeY(){
		return SIZE_Y;
	}
}
