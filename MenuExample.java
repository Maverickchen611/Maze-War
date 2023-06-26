import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuExample {
    private static JRadioButton easyButton;
    private static JRadioButton mediumButton;
    private static JRadioButton hardButton;
	private static boolean isEasySelected;
    private static boolean isMediumSelected;
    private static boolean isHardSelected;
	private static boolean loadMap=false;
	private static boolean mutiplePlayer=false;
	private static JFrame frame = new JFrame("Maze Game");

	private static JLabel titleLabel;
	private static JLabel startLabel;
	private static JLabel mutiplePlayerLabel;
	private static JLabel loadMazeLabel;
	private static JFileChooserDemo fileChoose;
	private static char[][] maze;
    public static void main(String[] args) {
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 0, 10, 0);

        titleLabel = createLabel("MAZE GAME", 32, true);
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.PAGE_START;
        frame.add(titleLabel, gbc);

        startLabel = createLabel("START", 20, false);
        startLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startLabelMouseExited(evt);
            }
        });
		
		
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
        frame.add(startLabel, gbc);
		
        /*mutiplePlayerLabel = createLabel("PLAYER 2 JOIN", 20, false);
        mutiplePlayerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
                mutiplePlayerLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mutiplePlayerLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mutiplePlayerLabelMouseExited(evt);
            }
        });
		
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(mutiplePlayerLabel, gbc);*/

        loadMazeLabel = createLabel("LOAD MAZE", 20, false);
        loadMazeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
                loadMazeLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loadMazeLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loadMazeLabelMouseExited(evt);
            }
        });
        gbc.gridy = 3;
        frame.add(loadMazeLabel, gbc);

        JPanel difficultyPanel = new JPanel();
        difficultyPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        ButtonGroup difficultyGroup = new ButtonGroup();

        easyButton = createRadioButton("EASY", difficultyGroup);
		easyButton.setSelected(true);
		
        mediumButton = createRadioButton("NORMAL", difficultyGroup);
        hardButton = createRadioButton("HARD", difficultyGroup);

        difficultyPanel.add(easyButton);
        difficultyPanel.add(mediumButton);
        difficultyPanel.add(hardButton);

        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(difficultyPanel, gbc);

        frame.setVisible(true);
    }

    private static JLabel createLabel(String text, int fontSize, boolean isBold) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font(Font.SANS_SERIF, isBold ? Font.BOLD : Font.PLAIN, fontSize));
        return label;
    }

    private static JRadioButton createRadioButton(String text, ButtonGroup group) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setHorizontalAlignment(JRadioButton.CENTER);
        group.add(radioButton);
        return radioButton;
    }

    private static void startLabelMouseClicked(java.awt.event.MouseEvent evt) {
        isEasySelected = easyButton.isSelected();
        isMediumSelected = mediumButton.isSelected();
        isHardSelected = hardButton.isSelected();

        // Use the selected values in your maze game logic
        System.out.println("Easy: " + isEasySelected);
        System.out.println("Medium: " + isMediumSelected);
        System.out.println("Hard: " + isHardSelected);

        // Start the maze game
        startMazeGame();
    }
	
	private static void mutiplePlayerLabelMouseClicked(java.awt.event.MouseEvent evt) {
        if(!mutiplePlayer){
			mutiplePlayer = true;
			mutiplePlayerLabel.setForeground(Color.YELLOW);
		}else{
			mutiplePlayer = false;
			mutiplePlayerLabel.setForeground(Color.BLACK);
		}
    }
	
	private static void loadMazeLabelMouseClicked(java.awt.event.MouseEvent evt) {
        loadMazeMap();
    }
	

    private static void startMazeGame() {
		
		if(loadMap){
			maze = fileChoose.getMaze();
		}
		MazePanel mazePanel= new MazePanel(isEasySelected, isMediumSelected ,isHardSelected,loadMap,mutiplePlayer, maze);
		frame.setVisible(false);
    }
	
	private static void loadMazeMap() {
		fileChoose = new JFileChooserDemo();
		loadMap = true;
    }
	
	public static void frameShow(){
		frame.setVisible(true);
	}

    private static void startLabelMouseEntered(java.awt.event.MouseEvent evt) {
        startLabel.setForeground(Color.YELLOW);
    }

    private static void startLabelMouseExited(java.awt.event.MouseEvent evt) {
        startLabel.setForeground(Color.BLACK);
    }
	  private static void mutiplePlayerLabelMouseEntered(java.awt.event.MouseEvent evt) {
        mutiplePlayerLabel.setForeground(Color.YELLOW);
    }

    private static void mutiplePlayerLabelMouseExited(java.awt.event.MouseEvent evt) {
        if(!mutiplePlayer){
			mutiplePlayerLabel.setForeground(Color.BLACK);
		}
	}
    private static void loadMazeLabelMouseEntered(java.awt.event.MouseEvent evt) {
        loadMazeLabel.setForeground(Color.YELLOW);
    }

    private static void loadMazeLabelMouseExited(java.awt.event.MouseEvent evt) {
        loadMazeLabel.setForeground(Color.BLACK);
    }
}
