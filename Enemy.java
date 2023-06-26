import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Enemy {
    private int enemyX;
    private int enemyY;
    private int playerX;
    private int playerY;
    private char[][] maze;
    private int SIZE_X = 10; // Replace with your desired size
    private int SIZE_Y = 10; // Replace with your desired size

    public Enemy(char[][] maze, int startX, int startY, int playerX, int playerY, int SIZE_X, int SIZE_Y) {
        this.maze = maze;
        enemyX = startX;
        enemyY = startY;
        this.playerX = playerX;
        this.playerY = playerY;
        this.SIZE_X = SIZE_X;
        this.SIZE_Y = SIZE_Y;
    }

    public int getX() {
        return enemyX;
    }

    public int getY() {
        return enemyY;
    }

    public void setX(int x) {
       this.enemyX = x;
    }

    public void setY(int y) {
        this.enemyY = y;
     }

     public void setPlayerX(int x) {
        this.playerX = x;
     }
 
     public void setPlayerY(int y) {
         this.playerY = y;
      }

    public void move() {
        // Create BFS required queue
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(enemyX, enemyY));

        // Create boolean array to track visited positions
        boolean[][] visited = new boolean[SIZE_X][SIZE_Y];
        visited[enemyX][enemyY] = true;

        // Create parent array to track path from enemy to player
        Point[][] parent = new Point[SIZE_X][SIZE_Y];
        parent[enemyX][enemyY] = null;

        // Execute BFS
        while (!queue.isEmpty()) {
            Point current = queue.poll();

            if (current.x == playerX && current.y == playerY) {
                System.out.println("Found the player, backtracking to find the next enemy position");
                Point next = current;
                while (parent[next.x][next.y] != null) {
                    current = next;
                    next = parent[current.x][current.y];
                }
                enemyX = current.x;
                enemyY = current.y;
                break;
            }

            // Check adjacent positions and add them to the queue if valid
            int[] dx = {0, 0, 1, -1};
            int[] dy = {1, -1, 0, 0};
            for (int i = 0; i < 4; i++) {
                int nextX = current.x + dx[i];
                int nextY = current.y + dy[i];

                if (isValidMove(nextX, nextY) && !visited[nextX][nextY]) {
                    queue.add(new Point(nextX, nextY));
                    visited[nextX][nextY] = true;
                    parent[nextX][nextY] = current;
                }
            }
        }
    }

    public void patrol() {
        // Generate a random number between 0 and 3 (inclusive)
        //System.out.println("i am patroling");
        Random random = new Random();
        int direction = random.nextInt(4);

        // Calculate the next position based on the random direction
        int nextX = enemyX;
        int nextY = enemyY;
        if (direction == 0) {
            nextX += 1; // Move right
        } else if (direction == 1) {
            nextX -= 1; // Move left
        } else if (direction == 2) {
            nextY += 1; // Move down
        } else if (direction == 3) {
            nextY -= 1; // Move up
        }

        // Check if the next move is valid
        if (isValidMove(nextX, nextY)) {
            enemyX = nextX;
            enemyY = nextY;
        }
    }


    private boolean isValidMove(int x, int y) {
        if (x < 0 || y < 0 || x >= maze[0].length || y >= maze.length) {
            return false; // Out of bounds
        }
        return maze[y][x] != '─' && maze[y][x] != '│' && maze[y][x] != '┌' && maze[y][x] != '┐' &&
                maze[y][x] != '└' && maze[y][x] != '┘' && maze[y][x] != '┬' && maze[y][x] != '┴' &&
                maze[y][x] != '├' && maze[y][x] != '┤'&& maze[y][x] != '┼';
    }
}
