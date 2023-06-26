import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.*;
import javax.swing.Timer;


public class MazePanel extends JPanel implements KeyListener, ActionListener {
    private static char[][] maze;
    private int player1X;
    private int player1Y;
	private int player2X;
    private int player2Y;
	private boolean player1Move;
	private boolean player2Move;
	private int player1HP;
	private int player1BulletNumber;
	private int keyCode1;
	private int keyCode2;
	private int endX;
	private int endY;
	private char invisableDoor ='G';
	private int startX;
	private int startY;
	private static int SIZE_X;
	private static int SIZE_Y;
	private boolean[][] path;
	public static int cellSize = 10;
	public static int windowWidth = 500;
	public static int windowHeight = 500;
	private boolean win;
	private boolean win2;
	private boolean easy;
	private boolean medium;
	private boolean hard;
	private boolean mutiplePlay;
	private static MazePrinter mazePrinter = new MazePrinter();
	private static JFrame frame;
	private static JButton saveButton;
	private static JButton menuButton;
	private List<Bullet> player1BulletList;
	private List<Bullet> enemyBulletList;
	private List<Enemy> enemyList;
	private Timer enemyMoveTimer;
	private Timer enemShootTimer;
	
	private Timer bulletTimer;

	private int enemyX;
    private int enemyY;
	
    public MazePanel(boolean easy,boolean medium, boolean hard,boolean loadMap, boolean secondPlayer, char[][] newMap) {
		frame = new JFrame("Maze");
		printDebug("MazePanel constructor start create");
		player1BulletList = new ArrayList<>();
		enemyBulletList = new ArrayList<>();
		enemyList = new ArrayList<>();
		setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
				keyPress(e);
            }
			@Override
			public void keyReleased(KeyEvent e) {
				keyRelease(e);
			}
        });
		addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getX()+" "+e.getY());
            }
        });
		
		if(loadMap){
			printDebug("Load map");
			SIZE_X = newMap.length;
			SIZE_Y = newMap[0].length;
			maze = newMap;
		}
		else{
			printDebug("create map");
			SIZE_X = mazePrinter.getSizeX();
			SIZE_Y = mazePrinter.getSizeY();
			maze = mazePrinter.charToSymbol();
			int r = 0;
			r = (int)(Math.random()*4);
			for(int i =0; i < r; i++){
				maze  = mazePrinter.rotateMaze(maze);
			}

			
		}
		if(secondPlayer){
			printDebug("MutiplePlay enable");
			mutiplePlay = secondPlayer;
		}
		this.win = false;
		printDebug("win flag: "+win);
		this.win2 = false;
		printDebug("win2 flag: "+win);
		this.maze = maze;
		this.easy = easy;
		this.medium = medium;
		this.hard = hard;
		
		
		//mazePrinter.print2d(maze);
		initializePath();
		initializeEndPosition();
        initializeplayer1Position();

		for(int i = 0; i < 50; i++){
			int futureX = (int)(Math.random()*SIZE_X);
			int futureY = (int)(Math.random()*SIZE_Y);
			if(maze[futureX][futureY] == ' '){
				maze[futureX][futureY] = 'E';
			}
			else{
				i--;
			}

		}
		initializeEnemyPosition();
		
		
        frame.add(this);
		windowWidth = (maze[0].length * cellSize) + 10*cellSize;
		windowHeight = maze.length * cellSize + 5*cellSize;
        frame.setSize(windowWidth, windowHeight); // Adjust this value to change the size of the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		saveButton = new JButton("Save map");
        saveButton.setVisible(false);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MazeFileWriter mazeFileWriter = new MazeFileWriter();
				mazeFileWriter.writeMazeToFile(maze,"maze.txt");
				mazeFileWriter = null;
            }
        });
		
		menuButton = new JButton("Menu");
        menuButton.setVisible(false);
        menuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				frame = null;
                MenuExample menuexample = new MenuExample();
				menuexample.frameShow();
            }
        });
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(saveButton);
		buttonPanel.add(menuButton);
		
		frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
		

		enemyMoveTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveEnemy();
				enmeyShoot();
            }
        });

		enemShootTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				enmeyShoot();
            }
        });

		bulletTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveBullet(player1BulletList);
				moveBullet(enemyBulletList);
				pickBullet();
				playerBulletCheckCollision();
				enemyBulletCheckCollision();
				BulletCheckCollision();
				openDoor();
				
            }
        });

		enemyMoveTimer.start();
		enemShootTimer.start();
        bulletTimer.start();


		printDebug("MazePanel constructor done");
    }


		
	private void initializePath() {
		path = new boolean[SIZE_X][SIZE_Y];
        for (int i = 0; i < path.length; i++) {
            for (int j = 0; j < path[0].length; j++) {
				path[i][j]=false;
            }
        }
    }
	
    private void initializeplayer1Position() {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                if (maze[row][col] == 'S') {
                    player1X = col;
                    player1Y = row;
					path[player1X][player1Y] =true;
					
					startX = player1X;
					startY = player1Y;
					
					player1HP = 5;
					player1BulletNumber = 5;
					printDebug("Find out player1 postition ( "+player1X+" , " + player1Y+" )" );
                    return;
                }
            }
        }
		printDebug("player1 postition not finded.");

    }
	private void initializeEndPosition() {

		

		while(invisableDoor == 'G') {
			int future = (int)(Math.random()*SIZE_X);
			int r = (int)(Math.random()*2);
			if(r==0){
				//System.out.println(maze[SIZE_Y-1][future]);
				if(maze[SIZE_Y-1][future] == '─' ){
					invisableDoor = maze[SIZE_Y-1][future];
					//maze[SIZE_Y-1][future] ='G';
					endX = future;
					endY = SIZE_Y-1;
					return;
				}
			}
			else if (r==1){
				//System.out.println(maze[future][SIZE_X-1]);
				if(maze[future][SIZE_X-1] == '│'){
					invisableDoor = maze[future][SIZE_X-1];
					//maze[future][SIZE_X-1]= 'G';
					endX = SIZE_X-1;
					endY = future;
					return;
				}
			}
		}

		
		/*	
			
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
				//System.out.println(maze[row][col]);
				
				
		


                if (maze[row][col] == 'G') {
                    endX = col;
                    endY = row;
					if(mutiplePlay){
						player2X=endX;
						player2Y=endY;
						printDebug("Find out player2 postition ( "+player2X+" , " + player2Y+" )" );
					}
					printDebug("Find out end postition ( "+endX+" , " + endY+" )" );
					path[endX][endY] =true;
                    return;
                }
            
			}
        }
		*/
		//printDebug("end postition not finded.");

    }
	
	private void openDoor(){
		if(enemyList.isEmpty()){
			maze[endY][endX] ='G';
			path[endY][endX] = true;
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
	
	private void handlePlayer1Movement(KeyEvent e) {
		int next1X = player1X;
        int next1Y = player1Y;
		if(player1Move){
			printDebug("player1: "+ keyCode1);
			if (keyCode1 == KeyEvent.VK_UP) {
				next1Y--;
			} else if (keyCode1 == KeyEvent.VK_DOWN) {
				next1Y++;
			} else if (keyCode1 == KeyEvent.VK_LEFT) {
				next1X--;
			} else if (keyCode1 == KeyEvent.VK_RIGHT) {
				next1X++;
			}
			
			if (isValidMove(next1X, next1Y) && (!win)&&(!win2)) {
				player1X = next1X;
				player1Y = next1Y;
				Iterator<Enemy> iterator = enemyList.iterator();
				printDebug(Integer.toString(enemyList.size()));
				while (iterator.hasNext()) {
					Enemy enemy = iterator.next();
					enemy.setPlayerX(player1X);
					enemy.setPlayerY(player1Y);

				}
				printDebug("current position: ( "+player1X+" , " + player1Y+" )" );
				if(player1X == endX && player1Y == endY){
					win = true;
				}
				repaint();
			}
		}
	}

	private void handlePlayer2Movement(KeyEvent e) {
		int next2X = player2X;
        int next2Y = player2Y;
		if(player2Move){
			printDebug("player2: "+ keyCode2);
			if (keyCode2 == KeyEvent.VK_W) {
				next2Y--;
			} else if (keyCode2 == KeyEvent.VK_S) {
				next2Y++;
			} else if (keyCode2 == KeyEvent.VK_A) {
				next2X--;
			} else if (keyCode2 == KeyEvent.VK_D) {
				next2X++;
			}
			if(mutiplePlay){
				if ( isValidMove(next2X, next2Y) && (!win2) && (!win)) {
					player2X = next2X;
					player2Y = next2Y;
					printDebug("current player2 position: ( "+player2X+" , " + player2Y+" )" );
					if(player2X == startX && player2Y == startY){
						win2 = true;
					}
					repaint();
				}
			}
		}
	}

	private boolean inVision(int col, int row, int playerX, int playerY){
		if(  (col > playerX-5 && col < playerX+5 && row > playerY-5 && row < playerY+5)){
			return true;
		}
		return false;
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellSize = 10; // Adjust this value to change the size of each cell

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setStroke(new BasicStroke(1.0f)); // Adjust the line width (1.0f = 1 pixel)

        for (int row = 0; row < maze.length; row++) { //draw maze
            for (int col = 0; col < maze[row].length; col++) {
                int x = col * cellSize;
                int y = row * cellSize;
				boolean player1Vision = inVision(col , row , player1X, player1Y);
				boolean player2Vision = false;
				if(mutiplePlay){
					player2Vision = inVision(col , row , player2X, player2Y);
				}
				if(  player1Vision || player2Vision || path[row][col] || easy ){
					//System.out.println("repaint");
					if(medium){
						path[row][col] = true;
					}
					switch (maze[row][col]) {
						case '─':
							g2d.setColor(Color.BLACK);
							g2d.drawLine(x, y + cellSize / 2, x + cellSize, y + cellSize / 2);
							break;
						case '│':
							g2d.setColor(Color.BLACK);
							g2d.drawLine(x + cellSize / 2, y, x + cellSize / 2, y + cellSize);
							break;
						case '┌':
							g2d.setColor(Color.BLACK);
							g2d.drawLine(x + cellSize / 2, y+ cellSize / 2, x + cellSize, y+ cellSize / 2);
							g2d.drawLine(x + cellSize/2 , y + cellSize/2, x + cellSize/2, y + cellSize);
							break;
						case '┐':
							g2d.setColor(Color.BLACK);
							g2d.drawLine(x , y + cellSize/2, x + cellSize/2, y + cellSize/2);
							g2d.drawLine(x + cellSize/2 , y + cellSize/2, x + cellSize/2, y + cellSize);
							break;
						case '└':
							g2d.setColor(Color.BLACK);
							g2d.drawLine(x + cellSize / 2, y+ cellSize / 2, x + cellSize, y+ cellSize / 2);
							g2d.drawLine(x + cellSize/ 2, y, x + cellSize/2, y + cellSize / 2);
							break;
						case '┘':
							g2d.setColor(Color.BLACK);
							g2d.drawLine(x, y + cellSize/2, x + cellSize / 2, y + cellSize/2);
							g2d.drawLine(x + cellSize / 2, y, x + cellSize / 2, y + cellSize / 2);
							break;
						case '┬':
							g2d.setColor(Color.BLACK);
							g2d.drawLine(x, y + cellSize / 2, x + cellSize, y + cellSize / 2);
							g2d.drawLine(x + cellSize / 2, y+ cellSize / 2, x + cellSize/2, y+ cellSize);
							break;
						case '┴':
							g2d.setColor(Color.BLACK);
							g2d.drawLine(x, y + cellSize / 2, x + cellSize, y + cellSize / 2);
							g2d.drawLine(x + cellSize / 2, y+ cellSize / 2, x + cellSize/2, y);
							break;
						case '├':
							g2d.setColor(Color.BLACK);
							g2d.drawLine(x + cellSize / 2, y, x + cellSize / 2, y + cellSize);
							g2d.drawLine(x + cellSize / 2, y + cellSize / 2, x + cellSize, y + cellSize / 2);
							break;
						case '┤':
							g2d.setColor(Color.BLACK);
							g2d.drawLine(x + cellSize / 2, y, x + cellSize / 2, y + cellSize);
							g2d.drawLine(x, y + cellSize / 2, x + cellSize / 2, y + cellSize / 2);
							break;
						case '┼':
							g2d.setColor(Color.BLACK);
							g2d.drawLine(x + cellSize / 2, y, x + cellSize / 2, y + cellSize);
							g2d.drawLine(x, y + cellSize / 2, x + cellSize, y + cellSize / 2);
							break;
						case 'S':
							g2d.setColor(Color.GREEN);
							g2d.fillRect(x, y, cellSize, cellSize);
							break; 
						case 'G':
							g2d.setColor(Color.RED);
							g2d.fillRect(x, y, cellSize, cellSize);
							break;
						case 'H':
							g2d.setColor(Color.RED);
							g2d.fillRect(x, y, cellSize, cellSize);
							break;
						case 'B':
							g2d.setColor(Color.YELLOW);
							g2d.fillRect(x, y, cellSize, cellSize);
							break;
						default:
							g2d.setColor(Color.WHITE);
							g2d.fillRect(x, y, cellSize, cellSize);
							break;
					}
				}
			}
        }
		

		
		if(mutiplePlay){
			g2d.setColor(Color.RED);
			int player2PixelX = player2X * cellSize;
			int player2PixelY = player2Y * cellSize;
			//g2d.fillRect(player2PixelX, player2PixelY, cellSize, cellSize);
			g2d.fillOval(player2PixelX, player2PixelY, cellSize, cellSize);
		}

		Font font = new Font("Arial", Font.BOLD, 32);
		FontMetrics fontMetrics = g.getFontMetrics(font);
		int messageWidth = fontMetrics.stringWidth(Integer.toString(player1BulletList.size()));
		int messageHeight = fontMetrics.getHeight();

		int x = (windowWidth - 5*cellSize);
		int y = (windowHeight - 6*cellSize);

		g.setColor(Color.green);
		g.setFont(font);
		g.drawString(Integer.toString(player1BulletNumber), x, y);

		g.setColor(Color.red);
		g.drawString(Integer.toString(player1HP), x-4*cellSize, y);
	
		
		if(player1HP==0){
			String message="YOU LOSE";

			 font = new Font("Arial", Font.BOLD, 32);
			 fontMetrics = g.getFontMetrics(font);
			 messageWidth = fontMetrics.stringWidth(message);
			 messageHeight = fontMetrics.getHeight();

			 x = (windowWidth - messageWidth) / 2;
			 y = (windowHeight - messageHeight) / 2;

			g.setColor(Color.RED);
			g.setFont(font);
			g.drawString(message, x, y);
			enemyMoveTimer.stop();
			bulletTimer.stop();
			enemShootTimer.stop();
		}

		if(win || win2 ){
			
			
			String message="";
			if(!mutiplePlay){
				message = "YOU WIN!";
			}
			else if(win){
				message = "PLAYER1 WIN!";
			}
			else if(win2){
				message = "PLAYER2 WIN!";
			}
			
			 font = new Font("Arial", Font.BOLD, 32);
			 fontMetrics = g.getFontMetrics(font);
			 messageWidth = fontMetrics.stringWidth(message);
			 messageHeight = fontMetrics.getHeight();

			 x = (windowWidth - messageWidth) / 2;
			 y = (windowHeight - messageHeight) / 2;

			g.setColor(Color.RED);
			g.setFont(font);
			g.drawString(message, x, y);
			
			saveButton.setVisible(true);
			menuButton.setVisible(true);
		
		}

		
		
		if(!player1BulletList.isEmpty()){
			for (Bullet bullet : player1BulletList) {
				if (bullet.isActive()) {
					g2d.setColor(Color.BLACK);
					g2d.fillOval(bullet.getX()*cellSize, bullet.getY()*cellSize, 5, 5);
				}
			} 
		}

		if(!enemyBulletList.isEmpty()){
			for (Bullet bullet : enemyBulletList) {
				if (bullet.isActive()) {
					g2d.setColor(Color.BLACK);
					g2d.fillOval(bullet.getX()*cellSize, bullet.getY()*cellSize, 5, 5);
				}
			} 
		}
		

		Iterator<Enemy> iterator = enemyList.iterator();
			while (iterator.hasNext()) {
				Enemy enemy = iterator.next();
				g2d.setColor(Color.BLUE);
				int enemyPixelX = enemy.getX() * cellSize;
				int enemyPixelY = enemy.getY() * cellSize;
				g2d.fillRect(enemyPixelX, enemyPixelY, cellSize, cellSize);
			}

	

		g2d.setColor(Color.GREEN);
		int player1PixelX = player1X * cellSize;
        int player1PixelY = player1Y * cellSize;
		g2d.fillOval(player1PixelX, player1PixelY, cellSize, cellSize);
       
        //g2d.fillRect(player1PixelX, player1PixelY, cellSize, cellSize);
        // Calculate the top-left corner coordinates of the bounding rectangle

        // Draw and fill the circle
       
        g2d.dispose();
    }
	
	
    public static void main(String[] args) {
		
		MazePanel mazePanel= new MazePanel(true,false,false,false,false,maze);
		
    }
	
	private void keyRelease(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_UP) {
			player1Move = false;
		} else if (keyCode == KeyEvent.VK_DOWN) {
			player1Move = false;
		} else if (keyCode == KeyEvent.VK_LEFT) {
			player1Move = false;
		} else if (keyCode == KeyEvent.VK_RIGHT) {
			player1Move = false;
		} else if (keyCode == KeyEvent.VK_W) {
			player2Move = false;
		} else if (keyCode == KeyEvent.VK_S) {
			player2Move = false;
		} else if (keyCode == KeyEvent.VK_A) {
			player2Move = false;
		} else if (keyCode == KeyEvent.VK_D) {
			player2Move = false;
		}
	}
	
	private void keyPress(KeyEvent e) {

		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_UP) {
			player1Move = true;
			keyCode1 = keyCode;
		} else if (keyCode == KeyEvent.VK_DOWN) {
			player1Move = true;
			keyCode1 = keyCode;
		} else if (keyCode == KeyEvent.VK_LEFT) {
			player1Move = true;
			keyCode1 = keyCode;
		} else if (keyCode == KeyEvent.VK_RIGHT) {
			player1Move = true;
			keyCode1 = keyCode;
		} else if (keyCode == KeyEvent.VK_W) {
			player2Move = true;
			keyCode2 = keyCode;
		} else if (keyCode == KeyEvent.VK_S) {
			player2Move = true;
			keyCode2 = keyCode;
		} else if (keyCode == KeyEvent.VK_A) {
			player2Move = true;
			keyCode2 = keyCode;
		} else if (keyCode == KeyEvent.VK_D) {
			player2Move = true;
			keyCode2 = keyCode;
		}else if (keyCode == KeyEvent.VK_SPACE) {
			shoot();
			printDebug("player shoot");
		}
		
		
		handlePlayer1Movement(e);
		if(mutiplePlay){
			handlePlayer2Movement(e);
		}
		
	}

	private void playerBulletCheckCollision() {
        Iterator<Bullet> bulletIterator = player1BulletList.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Iterator<Enemy> enemyIterator = enemyList.iterator();
            while (enemyIterator.hasNext()) {
                Enemy enemy = enemyIterator.next();
                if(enemy.getX() == bullet.getX() && enemy.getY() == bullet.getY()){
					printDebug("enemy hit at: "+Integer.toString(enemy.getX())+ " " + Integer.toString(enemy.getY()));
                    for(int i = 0; i < 1; i++){
						int futureX = (int)(Math.random()*SIZE_X);
						int futureY = (int)(Math.random()*SIZE_Y);
						if(distanceSmaller20(player1X,player1Y,futureX,futureY) && isValidMove(futureX,futureY)){
							maze[futureY][futureX] = 'B';
							path[futureY][futureX] = true;
						}
						else{
							i--;
						}
						if(futureX%10==0){
							futureX = (int)(Math.random()*SIZE_X);
							futureY = (int)(Math.random()*SIZE_Y);
							if(distanceSmaller20(player1X,player1Y,futureX,futureY) && isValidMove(futureX,futureY)){
								maze[futureY][futureX] = 'H';
								path[futureY][futureX] = true;
							}
						}
		
					}
					enemyIterator.remove();
					bulletIterator.remove();
					//player1BulletNumber+=3;
					break;
                    // Handle bullet hit enemy collision
                }
            }
        }
    }

	private void BulletCheckCollision() {
        Iterator<Bullet> player1BulletIterator = player1BulletList.iterator();
		Iterator<Bullet> enemyBulletIterator = enemyBulletList.iterator();
        while (player1BulletIterator.hasNext()) {
			Bullet playerBullet = player1BulletIterator.next();
			while (enemyBulletIterator.hasNext()) {
			Bullet enemyBullet = enemyBulletIterator.next();
                if(playerBullet.getX() == enemyBullet.getX() && playerBullet.getY() == enemyBullet.getY()){
					printDebug("BulletCheckCollision");
					player1BulletIterator.remove();
					enemyBulletIterator.remove();
					//player1BulletNumber+=3;
					break;
                    // Handle bullet hit enemy collision
                }
            }
        }
    }

	private void enemyBulletCheckCollision() {
        Iterator<Bullet> bulletIterator = enemyBulletList.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
			if(player1X == bullet.getX() && player1Y == bullet.getY()){
				printDebug("player hit at: "+Integer.toString(player1X)+ " " + Integer.toString(player1Y));
				player1HP--;
				bulletIterator.remove();
				//player1BulletNumber+=3;
				break;
				// Handle bullet hit enemy collision
			}
		}
        
    }

	


	private void moveEnemy(){
		
		if(!enemyList.isEmpty()){
			Iterator<Enemy> iterator = enemyList.iterator();
			printDebug(Integer.toString(enemyList.size()));
			while (iterator.hasNext()) {
				Enemy enemy = iterator.next();
				if(distanceSmaller20(player1X, player1Y, enemy.getX(), enemy.getY())){

					Queue<Point> queue = new LinkedList<>();
					queue.add(new Point(enemy.getX(), enemy.getY()));

					// 創建訪問過的位置的布林陣列，以跟踪已訪問的位置
					boolean[][] visited = new boolean[SIZE_X][SIZE_Y];
					visited[enemy.getX()][enemy.getY()] = true;

					// 創建父節點陣列，以跟踪從敵人到玩家的路徑
					Point[][] parent = new Point[SIZE_X][SIZE_Y];
					parent[enemy.getX()][enemy.getY()] = null;

					
					// 執行BFS
					while (!queue.isEmpty()) {
						Point current = queue.poll();

						if (current.x == player1X && current.y == player1Y) {
							//printDebug("找到玩家，回溯以找到敵人的下一個位置");
							Point next = current;
							while (parent[next.x][next.y] != null) {
								current = next;
								next = parent[current.x][current.y];
							}
							enemy.setX(current.x);
							enemy.setY(current.y);
							break;
						}

						// 檢查鄰近的位置，如果有效，則將其添加到佇列中
						for (int i = 0; i < 4; i++) {
							int[] dx = {0, 0, 1, -1};
							int[] dy = {1, -1, 0, 0};
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
				else{
					enemy.patrol();
				}

			}

		
            // 在地圖上更新敵人的位置
            // maze[enemyY][enemyX] = 'E';

            // 重新繪製地圖
            repaint();
        }
	}

	public boolean distanceSmaller20(int playerX, int playerY, int enemyX, int enemyY) {
		int distanceX = Math.abs(playerX - enemyX);
		int distanceY = Math.abs(playerY - enemyY);
		double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
		return distance <= 20;
	}

	private void moveBullet( List<Bullet> bulletList){
		if(!bulletList.isEmpty()){
			Iterator<Bullet> iterator = bulletList.iterator();
			while (iterator.hasNext()) {
				Bullet bullet = iterator.next();
				
				int tmpx = bullet.getX();
				int tmpy = bullet.getY();

				if (!(isValidMove(bullet.getX(), bullet.getY())) ) {
					iterator.remove();
					continue;
				}     
				bullet.update();
				if(tmpx == bullet.getX() && tmpy == bullet.getY()){
					iterator.remove();
					continue;
				}

				// Remove bulletList that are out of bounds
				/*if (!(isValidMove(bullet.getX(), bullet.getY()))) {
					iterator.remove();
				}*/
				
				/*Iterator<Enemy> iteratorEnemy = enemyList.iterator();
				printDebug(Integer.toString(enemyList.size()));
				while (iterator.hasNext()) {
					Enemy enemy = iteratorEnemy.next();
					if(enemy.getX() == bullet.getX() && enemy.getY() == bullet.getY()){
						enemyList.remove(enemy);
					}
				}*/


			}
			// Repaint the panel
		}
		repaint();
	}


	


	private void enmeyShoot() {

		Iterator<Enemy> iteratorEnemy = enemyList.iterator();
		printDebug(Integer.toString(enemyList.size()));
		while (iteratorEnemy.hasNext()) {
			Enemy enemy = iteratorEnemy.next();
			int enmeyBulletListpeedX = 1; // Adjust the bullet speed as needed
			int enmeyBulletListpeedY = 1; // Adjust the bullet speed as needed
		
			// Determine the starting position and direction of the bullet based on player's position and shooting direction
			int enmeyBulletListtartX = enemy.getX();
			int enmeyBulletListtartY = enemy.getY();
		
			int speedX = 0;
			int speedY = 0;

			if(!isWallBetween(enmeyBulletListtartX, enmeyBulletListtartY, player1X, player1Y)){
				if (enmeyBulletListtartY > player1Y) {//
					enmeyBulletListtartY--;
					speedY = -enmeyBulletListpeedY;
				} else if (enmeyBulletListtartY < player1Y) {
					enmeyBulletListtartY++;
					speedY = enmeyBulletListpeedY;
				} else if (enmeyBulletListtartX > player1X) {
					enmeyBulletListtartX--;
					speedX = -enmeyBulletListpeedX;
				} else if (enmeyBulletListtartX < player1X) {
					enmeyBulletListtartX++;
					speedX = enmeyBulletListpeedX;
				}
				
				// Create a new bullet and add it to the enmeyBulletList list
				Bullet bullet = new Bullet(enmeyBulletListtartX, enmeyBulletListtartY, speedX, speedY);
				printDebug(bullet.toString());
				enemyBulletList.add(bullet);
				//rintDebug(Integer.toString(enemyBulletList.size()));
			}
		
			
					
		}

		
	}


	boolean isWallBetween(int x1, int y1, int x2, int y2) {
		int dx = Math.abs(x2 - x1);
		int dy = Math.abs(y2 - y1);
		int sx = x1 < x2 ? 1 : -1;
		int sy = y1 < y2 ? 1 : -1;
		int err = dx - dy;
	
		while (x1 != x2 || y1 != y2) {
			if (!(isValidMove(x1, y1))) {
				return true; // Wall found, return true
			}
	
			int e2 = 2 * err;
			if (e2 > -dy) {
				err -= dy;
				x1 += sx;
			}
			if (e2 < dx) {
				err += dx;
				y1 += sy;
			}
		}
	
		return false; // No wall found, return false
	}
	


	private void shoot() {

		if(player1BulletNumber > 0 && !(player1X == startX && player1Y == startY)){
			player1BulletNumber--;
			int player1BulletListpeedX = 1; // Adjust the bullet speed as needed
			int player1BulletListpeedY = 1; // Adjust the bullet speed as needed
		
			// Determine the starting position and direction of the bullet based on player's position and shooting direction
			int player1BulletListtartX = player1X;
			int player1BulletListtartY = player1Y;
			int direction = keyCode1; // Assuming keyCode1 represents the shooting direction
		
			int speedX = 0;
			int speedY = 0;

			if (direction == KeyEvent.VK_UP) {
				player1BulletListtartY--;
				speedY = -player1BulletListpeedY;
			} else if (direction == KeyEvent.VK_DOWN) {
				player1BulletListtartY++;
				speedY = player1BulletListpeedY;
			} else if (direction == KeyEvent.VK_LEFT) {
				player1BulletListtartX--;
				speedX = -player1BulletListpeedX;
			} else if (direction == KeyEvent.VK_RIGHT) {
				player1BulletListtartX++;
				speedX = player1BulletListpeedX;
			}
		
			// Create a new bullet and add it to the player1BulletList list
			Bullet bullet = new Bullet(player1BulletListtartX, player1BulletListtartY, speedX, speedY);
			printDebug(bullet.toString());
			player1BulletList.add(bullet);
			
		}
	}

	private void pickBullet() {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                if (maze[row][col] == 'B' && player1X == col && player1Y ==	row) {
					maze[row][col] = ' ';
					player1BulletNumber+=3;
					//enemyList.add(new Enemy(maze, enemyX, enemyY, player1X, player1Y, SIZE_X,SIZE_Y)); 

                }
				else if (maze[row][col] == 'H' && player1X == col && player1Y ==row){
					maze[row][col]=' ';
					player1HP += 3;
				}
            }
        }
    }



	private void initializeEnemyPosition() {
        // 類似於initializeplayer1Position()，找到敵人（E）的初始位置
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                if (maze[row][col] == 'E') {
                    enemyX = col;
                    enemyY = row;
					maze[row][col] = ' ';
					enemyList.add(new Enemy(maze, enemyX, enemyY, player1X, player1Y, SIZE_X,SIZE_Y)); 
                    //path[enemyX][enemyY] = true;

                }
            }
        }
    }

   


	public static void printDebug (String str) {
        System.out.println(">>>"+str);
    }
	public static void setCharMaze( char[][] newMaze){
		maze = newMaze;
	}
	
	public static char[][] getCharMaze(){
		return maze;
	}



	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
	}
	
	
	
}
