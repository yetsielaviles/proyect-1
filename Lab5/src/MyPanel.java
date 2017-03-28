import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 25;
	private static final int GRID_Y = 25;
	private static final int INNER_CELL_SIZE = 29;
	private static final int TOTAL_COLUMNS = 10;
	private static final int TOTAL_ROWS = 11;   
	public int x = -1;
	public int y = -1;
	int surroundingMines;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;	
	public boolean gameOver = false;
	public  Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	public int[][] hiddenMines = new int [TOTAL_COLUMNS][TOTAL_ROWS];
	public int[][] minesSurroundingOurSquare = new int [TOTAL_COLUMNS][TOTAL_ROWS];
	public boolean[][] minesUncovered = new boolean[TOTAL_COLUMNS][TOTAL_ROWS];
	
	Random random = new Random();
	
	
	
	public MyPanel() {   //This is the constructor... this code runs first to initialize
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}
		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //Top row
			colorArray[x][0] = Color.WHITE;
		
		}
		for (int y = 0; y < TOTAL_ROWS; y++) {   //Left column
			colorArray[0][y] = Color.WHITE;
		}
		for (int x = 1; x < TOTAL_COLUMNS; x++) {   //The rest of the grid
			for (int y = 1; y < TOTAL_ROWS; y++) {
				colorArray[x][y] = Color.WHITE;
			}
			
		}
	
	
		for (int x = 1; x < TOTAL_COLUMNS; x++) {   
			for (int y = 1; y < TOTAL_ROWS - 1; y++) {
				if(random.nextInt(30) < 6){
					hiddenMines[x][y] = 1;               
				}
				else{
					hiddenMines[x][y] = 0;
					
				}
				minesUncovered[x][y] = false;            
			}
			}
			
		for (int x = 1; x < TOTAL_COLUMNS; x++) {          
			for (int y = 1; y < TOTAL_ROWS - 1; y++) {
				surroundingMines = 0;
				for (int m = 1; m < TOTAL_COLUMNS; m++) {          
					for (int n = 1; n < TOTAL_ROWS - 1; n++) {
						if(!(x == m &&  y == n)){
							if(isNeightbor(x, y, m, n)){
								surroundingMines++;
						
						}
			         }
                 }
					
					minesSurroundingOurSquare[x][y] = surroundingMines;
			
				}
			}			
		}
	}
		
	


	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Compute interior coordinates
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		int x2 = getWidth() - myInsets.right - 1;
		int y2 = getHeight() - myInsets.bottom - 1;
		int width = x2 - x1;
		int height = y2 - y1;

		//Paint the background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x1, y1, width + 1, height + 1);

		//By default, the grid will be 10x10 (see above: TOTAL_COLUMNS and TOTAL_ROWS) 
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS - 1; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)));
		}

		//Draw an additional cell at the bottom left
		//g.drawRect(x1 + GRID_X, y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)), INNER_CELL_SIZE + 1, INNER_CELL_SIZE + 1);

		//Paint cell colors
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				if ((x == 0) || (y != TOTAL_ROWS - 1)) {
					Color c = colorArray[x][y];
					g.setColor(c);
					g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
				}
				
				if(minesUncovered[x][y] == true){
					if(hiddenMines[x][y] == 0){
						g.setColor(Color.GRAY);
						g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
					}
					else{
						g.setColor(Color.BLACK);
						for (int i = 0; i < TOTAL_COLUMNS; i++) {
							for (int j = 0; j < TOTAL_ROWS; j++) {
								if(hiddenMines[i][j]==1){
									minesUncovered[i][j]=true;
									g.fillRect(x1 + GRID_X + (i * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (j * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
									}
								}
							}
					    }
					
				}
				if(minesUncovered[x][y] == true){
				
					if(hiddenMines[x][y] == 0 && minesSurroundingOurSquare[x][y] != 0){
						if(minesSurroundingOurSquare[x][y] == 1){
							g.setColor(Color.BLUE);
						}
						else if(minesSurroundingOurSquare[x][y] == 2){
							g.setColor(Color.GREEN);
						}
						else if(minesSurroundingOurSquare[x][y] == 3){
							g.setColor(Color.RED);
						}
						else if(minesSurroundingOurSquare[x][y] == 4){
							g.setColor(Color.DARK_GRAY);
						}
						else if(minesSurroundingOurSquare[x][y] == 5){
							g.setColor(Color.PINK);
						}
						else if(minesSurroundingOurSquare[x][y] == 6){
							g.setColor(Color.CYAN);
						}
					     g.setFont(new Font("Times New Roman", Font.BOLD, 20));
					     g.drawString(Integer.toString(minesSurroundingOurSquare[x][y]), x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 5, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 20);
					}
				}
			}
		}
		
		if(gameOver == true){
			JOptionPane.showMessageDialog(null, "GAME OVER!");
			System.exit(0);
		}
	
	}

	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return x;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return x;
	}
	public int getGridY(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return y;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}
	public static int getTotalColumns() {
		return TOTAL_COLUMNS;
	}
	public static int getTotalRows() {
		return TOTAL_ROWS;
	}
	public boolean isNeightbor(int squareX, int squareY, int neightborX, int neightborY ){
		if(squareX - neightborX < 2 && squareX - neightborX > -2 && squareY - neightborY < 2 && squareY - neightborY > -2 && hiddenMines[neightborX][neightborY] == 1){
			return true;
		}
		return false;
		
	}
	public void resetGame(){
		
		for (int x = 1; x < TOTAL_COLUMNS; x++) {   
			for (int y = 1; y < TOTAL_ROWS - 1; y++) {
				if(random.nextInt(81) < 16){
					hiddenMines[x][y] = 1;               
				}
				else{
					hiddenMines[x][y] = 0;
					
				}
				minesUncovered[x][y] = false;           
			}
		}
		
			
		
			
		for (int x = 1; x < TOTAL_COLUMNS; x++) {          
			for (int y = 1; y < TOTAL_ROWS - 1; y++) {
				surroundingMines = 0;
				for (int m = 1; m < TOTAL_COLUMNS; m++) {          
					for (int n = 1; n < TOTAL_ROWS - 1; n++) {
						if(!(x == m &&  y == n)){
							if(isNeightbor(x, y, m, n)){
								surroundingMines++;
						
						}
			         }
                 }
					
					minesSurroundingOurSquare[x][y] = surroundingMines;
			
				}
				}
			}
		
		
	}
	
	
}


