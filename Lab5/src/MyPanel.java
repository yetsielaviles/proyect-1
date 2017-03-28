
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;


public class MyPanel extends JPanel {
	int numberOfMines;
	int squaresWithoutMines;
	int minesFounded;
	int squareNeightbors;
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 25;
	private static final int GRID_Y = 25;
	private static final int INNER_CELL_SIZE = 29;
	private static final int TOTAL_COLUMNS = 10;
	private static final int TOTAL_ROWS = 11;   //Last row has only one cell
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;	
	public int[][] hiddenMines = new int [TOTAL_COLUMNS][TOTAL_ROWS];
	public int[][] minesSurroundingOurSquare = new int [TOTAL_COLUMNS][TOTAL_ROWS];
	public  Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	public boolean[][] minesUncovered = new boolean[TOTAL_COLUMNS][TOTAL_ROWS];
	public static boolean gameIsOver = false;
	public static boolean youWON = false;
	
	Random random = new Random();
	


	public MyPanel() {   
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
			colorArray[x][0] = Color.LIGHT_GRAY;

		}
		for (int y = 0; y < 2; y++) {   //Left column
			colorArray[0][y] = Color.LIGHT_GRAY;
		}
		for (int x = 1; x < TOTAL_COLUMNS; x++) {   //The rest of the grid
			for (int y = 1; y < TOTAL_ROWS; y++) {
				colorArray[x][y] = Color.WHITE;
			}

		}

		this.deployingMines();				

		//Deploys and counts the mines that are near our clicked square.
		for (int x = 1; x < TOTAL_COLUMNS; x++) {           
			for (int y = 1; y < TOTAL_ROWS - 1; y++) {
				squareNeightbors = 0;
				for (int m = 1; m < TOTAL_COLUMNS; m++) {          
					for (int n = 1; n < TOTAL_ROWS - 1; n++) {
						if(!(x == m &&  y == n)){
							if(minesThatAreNear(x, y, m, n)){
								squareNeightbors++;
							}
						}
					}
					minesSurroundingOurSquare[x][y] = squareNeightbors;
				}
			}			
		}
	}

	public void deployingMines(){
		for (int x = 1; x < TOTAL_COLUMNS; x++) {   // create minimum 16 mines for 81 squares
			for (int y = 1; y < TOTAL_ROWS - 1; y++) {
				if(random.nextInt(81) < 16){
					hiddenMines[x][y] = 1;                

					this.numberOfMines += 1;        // Keeps the count of number of mines
				}
				else{
					hiddenMines[x][y] = 0;
					this.squaresWithoutMines+= 1;

				}
				minesUncovered[x][y] = false;            // No uncovered mines at the start
			}
		}
	}
	public static int getTotalColumns() {
		return TOTAL_COLUMNS;
	}
	public static int getTotalRows() {
		return TOTAL_ROWS;
	}
	public boolean minesThatAreNear(int clickedCellXcoord, int clickedCellYcoord, int mineNearXcoord, int mineNearYcoord ){
		if(clickedCellXcoord - mineNearXcoord < 2 && clickedCellXcoord - mineNearXcoord > -2 && clickedCellYcoord - mineNearYcoord < 2 && clickedCellYcoord - mineNearYcoord > -2 && hiddenMines[mineNearXcoord][mineNearXcoord] == 1){
			return true;
		}
		return false;
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

		//Draw the grid minus the bottom row (which has only one cell)
		//By default, the grid will be 10x10 (see above: TOTAL_COLUMNS and TOTAL_ROWS) 
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS - 1; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)));
		}


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

						this.mineSeeker(x, y);    // Checks to see if any near square doesn't has a mine

						g.setColor(Color.GRAY);
						g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);

					}
					else{
						g.setColor(Color.BLACK);         // Uncovers all mines when we click on one.
						for (int i = 0; i < TOTAL_COLUMNS; i++) {
							for (int j = 0; j < TOTAL_ROWS; j++) {
								if(hiddenMines[i][j]==1){
									minesUncovered[i][j]=true;
									g.fillRect(x1 + GRID_X + (i * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (j * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
									
									gameIsOver = true;

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
							g.setColor(Color.MAGENTA);
						}
						else if(minesSurroundingOurSquare[x][y] == 4){
							g.setColor(Color.CYAN);
						}
						
						
						g.setFont(new Font("Times New Roman", Font.BOLD, 20));
						g.drawString(Integer.toString(minesSurroundingOurSquare[x][y]), x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 7, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 20);
					}
				}
			}
		}

		
		if(gameIsOver == true){
			JOptionPane.showMessageDialog(null, "GAME OVER!");
			System.exit(0);
		}

		if(youWON== true){
			JOptionPane.showMessageDialog(null, "YOU WON");
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
	
	
	public void minesweeperEnds(){
		youWON = false;
		gameIsOver = false;
		this.numberOfMines = 0;
		this.squaresWithoutMines = 0;
		this.minesFounded = 0;

		// Remove all flags
		for (int x = 1; x < TOTAL_COLUMNS; x++) {          
			for (int y = 1; y < TOTAL_ROWS - 1; y++) {
				if(colorArray[x][y].equals(Color.RED)){
					colorArray[x][y] = Color.WHITE;
				}
			}
		}

		this.deployingMines();    // deploys mines across the board

		for (int x = 1; x < TOTAL_COLUMNS; x++) {            
			for (int y = 1; y < TOTAL_ROWS - 1; y++) {
				squareNeightbors = 0;
				for (int m = 1; m < TOTAL_COLUMNS; m++) {          
					for (int n = 1; n < TOTAL_ROWS - 1; n++) {
						if(!(x == m &&  y == n)){
							if(minesThatAreNear(x, y, m, n)){
								squareNeightbors++;
						    }
					    }
				    }    

					minesSurroundingOurSquare[x][y] = squareNeightbors;

				}
			}
		}
	}


	public void mineSeeker(int x, int y) { 	
		//Seeks for any near bombs
		if(minesSurroundingOurSquare[x][y] == 0) {
			for(int i=x-1; i<=x+1; i++) {
				for (int j=y-1; j<=y+1; j++) {
					if(i < getTotalColumns() && i > 0 && j < getTotalRows() - 1 && j > 0) {
						if(minesUncovered[i][j]==false){
							minesUncovered[i][j] = true;
							this.minesFounded+=1;

							mineSeeker(i, j);
						}
					}
				}
			}
		}
		else{
			if(x < getTotalColumns() && x > 0 && y < getTotalRows() - 1 && y > 0) {
				if(minesUncovered[x][y]==false){
					minesUncovered[x][y] = true;
					this.minesFounded+=1;

				}
			}
		}

		if(this.minesFounded == this.squaresWithoutMines){
			youWON = true;
		}
	}
}
