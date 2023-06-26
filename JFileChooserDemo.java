import java.awt.Frame;
import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;

public class JFileChooserDemo extends Frame {
	private static char[][] maze;
	private static boolean first=true;
	
    public JFileChooserDemo() {
        setTitle("File Viewer");
        chooseAndReadFile();
    }

    public void chooseAndReadFile() {
        JFileChooser fileChooser = new JFileChooser(".");
        int option = fileChooser.showOpenDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            readFile(file);
        } else {
            System.out.println("No file selected.");
        }
    }

    public void readFile(File file) {
		int row = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
				
				//textArea.append(line + "\n");
				storeInAry(line , row);
				row++;
				//System.out.println(line);
            }
			System.out.println("File contents displayed.");
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
    }
	
	
	
	public static void storeInAry(String str, int row) {
		if(first){
			maze = new char[str.length()][str.length()];
			first = false;
		}  
        for (int i = 0; i < str.length(); i++) {		
            maze[row][i] = str.charAt(i);
        }
    }
	
	public static void printDebug (String str) {
			System.out.println(">>>"+str);
		}
		
    public static void main(String[] args) {
        JFileChooserDemo JFileChooserDemo = new JFileChooserDemo();
    }
	
	public static char[][] getMaze(){
		return maze;
	}
}
