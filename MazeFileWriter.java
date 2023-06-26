import java.io.FileWriter;
import java.io.IOException;

public class MazeFileWriter {

    public static void writeMazeToFile(char[][] maze, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[0].length; j++) {
					writer.write(maze[i][j]);
                }
                writer.write(System.lineSeparator()); // Write line separator
            }
            System.out.println("Maze written to file: " + filePath);
        } catch (IOException e) {
            System.out.println("An error occurred while writing the maze to file.");
            e.printStackTrace();
        }
    }
}
