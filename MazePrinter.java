public class MazePrinter {
	
	public static MazeGenerator mazeGenerator = new MazeGenerator();
	private static  int SIZE_X = mazeGenerator.getSizeX();
    private static  int SIZE_Y = mazeGenerator.getSizeY();
	private static final char WALL = '1';
		
	private static char[][] numberMaze = new char[SIZE_X][SIZE_Y];
	private static char[][] maze = new char[SIZE_X][SIZE_Y];
	

	private static char[] symbols = {
		'─',  // Symbol: ─ (horizontalLine)
		'─',  // Symbol: ─ (horizontalLine)
		'─',  // Symbol: ─ (horizontalLine)
		'│',  // Symbol: │ (verticalLine)
		'│',  // Symbol: │ (verticalLine)
		'│',  // Symbol: │ (verticalLine)
		'┐',  // Symbol: ┐ (upperRightCorner)
		'└',  // Symbol: └ (lowerLeftCorner)
		'┘',  // Symbol: ┘ (lowerRightCorner)
		'┌',  // Symbol: ┌ (upperLeftCorner)
		'┴',  // Symbol: ┴ (tJunctionUp)
		'┬',  // Symbol: ┬ (tJunctionDown)
		'├',  // Symbol: ├ (tJunctionLeft)
		'┤',  // Symbol: ┤ (tJunctionRight)
		'┼'   // Symbol: ┼ (crossJunction)
	};
	
	private static char[][][] symbolArray = {
		// Symbol: ─ (horizontalLine)
		{
			{'0', '0', '0'},
			{'0', '1', '1'},
			{'0', '0', '0'}
		},
		// Symbol: ─ (horizontalLine) 2
		{
			{'0', '0', '0'},
			{'1', '1', '0'},
			{'0', '0', '0'}
		},
		// Symbol: ─ (horizontalLine) 3
		{
			{'0', '0', '0'},
			{'1', '1', '1'},
			{'0', '0', '0'}
		},
		// Symbol: │ (verticalLine)
		{
			{'0', '0', '0'},
			{'0', '1', '0'},
			{'0', '1', '0'}
		},
		// Symbol: │ (verticalLine) 2
		{
			{'0', '1', '0'},
			{'0', '1', '0'},
			{'0', '0', '0'}
		},
		// Symbol: │ (verticalLine) 3
		{
			{'0', '1', '0'},
			{'0', '1', '0'},
			{'0', '1', '0'}
		},
		// Symbol: ┐ (upperRightCorner)
		{
			{'0', '0', '0'},
			{'1', '1', '0'},
			{'0', '1', '0'}
		},
		// Symbol: └ (lowerLeftCorner)
		{
			{'0', '1', '0'},
			{'0', '1', '1'},
			{'0', '0', '0'}
		},
		// Symbol: ┘ (lowerRightCorner)
		{
			{'0', '1', '0'},
			{'1', '1', '0'},
			{'0', '0', '0'}
		},
		// Symbol: ┌ (upperLeftCorner)
		{
			{'0', '0', '0'},
			{'0', '1', '1'},
			{'0', '1', '0'}
		},
		// Symbol: ┴ (tJunctionUp)
		{
			{'0', '1', '0'},
			{'1', '1', '1'},
			{'0', '0', '0'}
		},
		// Symbol: ┬ (tJunctionDown)
		{
			{'0', '0', '0'},
			{'1', '1', '1'},
			{'0', '1', '0'}
		},
		// Symbol: ├ (tJunctionLeft)
		{
			{'0', '1', '0'},
			{'0', '1', '1'},
			{'0', '1', '0'}
		},
		// Symbol: ┤ (tJunctionRight)
		{
			{'0', '1', '0'},
			{'1', '1', '0'},
			{'0', '1', '0'}
		},
		// Symbol: ┼ (crossJunction)
		{
			{'0', '1', '0'},
			{'1', '1', '1'},
			{'0', '1', '0'}
		}
	};


	
	public static char[][] charToSymbol() {
		
		printDebug("charToSymbol start");
		numberMaze = mazeGenerator.generateMaze();
		for (int i = 0; i < maze.length-2; i++) {
            for (int j = 0; j < maze[i].length-2; j++ ){
				extractMaze3x3(numberMaze,i,j);
            }
        }
		
		print2d(maze);
		printDebug("charToSymbol done");
        return maze;
		
    }
	
	
	public static void main(String[] args) {
		
		
		MazeGenerator mazeGenerator = new MazeGenerator();
		maze = mazeGenerator.generateMaze();
		print2d(maze);
        
		
		//System.out.println(symbolArray.length);
		
		
		
		for (int i = 0; i < maze.length-2; i++) {
            for (int j = 0; j < maze[i].length-2; j++ ){
				extractMaze3x3(maze,i,j);
            }
        }
    }

    public static void extractMaze3x3(char[][] maze, int row, int col) {
		char[][] tempMaze = new char[3][3];
		
        for (int i = row; i < row+3; i++) {
            for (int j = col; j < col+3; j++) {
                tempMaze[i-row][j-col] = maze[i][j];
            }
        }
		printSymbolMaze(tempMaze , row+1,col+1 );
    }
	

	public static char[][] rotateMaze(char[][] maze) {
		int rows = maze.length;
		int columns = maze[0].length;

		char[][] rotatedMaze = new char[columns][rows];
		printDebug("rotatedMaze start");
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				char temp=' ';
				switch (maze[i][j]) {
					case '─':
						temp = '│';
						break;
					case '│':
						temp = '─';
						break;
					case '┌':
						temp = '┐';
						break;
					case '┐':
						temp = '┘';
						break;
					case '└':
						temp = '┌';
						break;
					case '┘':
						temp = '└';
						break;
					case '┬':
						temp = '┤';
						break;
					case '┴':
						temp = '├';
						break;
					case '├':
						temp = '┬';
						break;
					case '┤':
						temp = '┴';
						break;
					case '┼':
						temp = '┼';
						break;
					case 'S':
						temp = 'S';
						break; 
					case 'G':
						temp = 'G';
						break;
					case 'H':
						temp = 'H';
						break;
					case 'B':
						temp = 'B';
						break;
					default:
						break;
				}
				rotatedMaze[j][maze.length - 1 - i] = temp;
			}
		}
		printDebug("rotatedMaze done");
		return rotatedMaze;
	}

	
	
	public static void printSymbolMaze(char tempMaze[][], int row, int col) {
		
		boolean change = false;
		for(int i =0; i < symbolArray.length; i++){
			char[][] tempSymbol = symbolArray[i];
			boolean flag = true;
			for(int j=0; j<tempSymbol.length;j++){
				for(int k=0; k<tempSymbol[j].length;k++){
					// skip corner number
					if(   (j==0 && k == 0)   ||   (j==0 && k == 2)   ||   (j==2 && k == 0)   ||   (j==2 && k == 2)){
						continue;
					}
					if(tempMaze[j][k] != tempSymbol[j][k]){
						flag = false;
						break;
					}
				}
				if(!flag){
					break;
				}
			}
			
			if(flag){
				//System.out.println("row "+row + " col "+col + " symbol "+ symbols[i]);
				
				maze[row][col] = symbols[i];
				change = true;
				//print2d(tempMaze);
				break;
			}
			
		}
		if(!change){
			//System.out.println("row "+row + " col "+col + " symbol is empty ");
			//print2d(tempMaze);
			maze[row][col] = ' ';
			change = true;
		}
		
		
		//printDebug("build wall");
		for(int j=0; j<maze.length;j++){
			for(int k=0; k<maze[j].length;k++){
				if(j==0 || j == maze.length-1 ){
					maze[j][k] = '─';
				}
				if(k==0 || k == maze[0].length-1 ){
					maze[j][k] = '│';
				}
			}
		}
		//printDebug("modify T wall");
		for (int j = 0; j < numberMaze[0].length-1; j++ ){
			if(numberMaze[1][j] == WALL ){

				maze[0][j] = '┬';
			}
			if(numberMaze[numberMaze.length-2][j] == WALL){
				maze[maze.length-1][j] = '┴';
			}
			if(numberMaze[j][1] == WALL ){
				maze[j][0] = '├';
			}
			if(numberMaze[j][numberMaze.length-2] == '1' ){
				maze[j][maze.length-1] = '┤';
			}
		}
		//printDebug("make corner and start & end");
		maze[0][0] = '┌';
		maze[0][(maze[0].length)-1] = '┐';
		maze[(maze.length-1)][0] = '└';
		maze[(maze.length-1)][(maze[0].length)-1] = '┘';
		maze[0][1] = 'S';
		//maze[(maze.length-1)][(maze[0].length)-2] = 'G';
		
	}
	
	public static void print2d(char[][] temp){
		printDebug("print2dCharAry");
		for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[0].length; j++) {
                System.out.print(temp[i][j]);
            }
            System.out.println();
        }
	}
	public static void print2d(int[][] temp){
		printDebug("print2dIntAry");
		for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[0].length; j++) {
                System.out.print(temp[i][j]);
            }
			System.out.println();
        }
		System.out.println();

	}
	public static void print3x3Maze(int row, int col) {
		if(row-1 >= 0){
			row--;
		}
		if(col-1 >= 0){
			col--;
		}
	}
	
	public static char[][] getmaze(){
		return	maze;
	}
	
	public static int getSizeX(){
		return SIZE_X;
	}
	public static int getSizeY(){
		return SIZE_Y;
	}
	
	public static void printDebug (String str) {
        System.out.println(">>>"+str);
    }
}
