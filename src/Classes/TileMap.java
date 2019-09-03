package Classes;

public class TileMap {
	public class Cell{
		int col;
		int row;
		int value;
		
		/**
		 * Cell constructor
		 * 
		 * @param c		Column value
		 * @param r		Row value
		 */
		public Cell(int c, int r){
			col = c;
			row = r;
			value = 0;
		}
		
		/**
		 * Cell constructor
		 * 
		 * @param c		Column value
		 * @param r		Row Value
		 * @param val	Cell's value
		 */
		public Cell(int c, int r, int val){
			col = c;
			row = r;
			value = val;
		}	
		
		/**
		 * Copy constructor
		 * 
		 * @param c		Cell to be copied
		 */
		public Cell(Cell c){
			col = c.col;
			row = c.row;
			value = c.value;
		}
		
		/**
		 * Calculates the Manhattan Distance from this Cell to some target Cell
		 * 
		 * @param a		Target Cell
		 * @return		The Manhattan Distance from this Cell to a target
		 */
		public int manhattanDistance(Cell a){		
			return Math.abs(a.col - this.col)+Math.abs(a.row - this.row);
		}
	}
	
	Cell[][] cells;
	public final int COLS;
	public final int ROWS;
	
	/**
	 * Constructor
	 * 
	 * @param cols		Number of columns in the grid
	 * @param rows		Number of rows in the grid
	 */
	public TileMap(int cols,int rows){
		COLS = cols;
		ROWS = rows;
		int steps = 0;
		int total = COLS * ROWS;
		cells = new Cell[COLS][ROWS];
		for(int y=0;y<ROWS;y++)
			for(int x=0;x<COLS;x++)			
				cells[x][y] = new Cell(x,y,++steps % total);
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param other		TileMap to copy
	 */
	public TileMap(TileMap other){
		COLS = other.COLS;
		ROWS = other.ROWS;
		cells = new Cell[COLS][ROWS];
		this.copy(other);
	}
	
	/**
	 * Determines if this TileMap is equivalent to another TileMap instance.
	 * 
	 * @param other		TileMap to compare
	 * @return			True if and only if all values contained in this TileMap's Cells are equal to each value 
	 * 					to the other TileMap's corresponding Cells
	 */
	public boolean equals(TileMap other){
		for(int x = 0; x<COLS;x++)
			for(int y = 0; y<ROWS;y++)
				if(cells[x][y].value != other.cells[x][y].value) return false;
		
		return true;		
	}
	
	/**
	 * Method to provide deep copy of another TileMap instance
	 * 
	 * @param map 		TileMap to copy
	 */
	public void copy(TileMap map){
		for(int x=0; x<COLS; x++)
			for(int y=0;y<ROWS;y++)
				cells[x][y] = new Cell(x,y,map.cells[x][y].value);		
	}
	
	/**
	 * Method used to set a Cell's value. The method uses a Cell's location to assign value.
	 * @param c			Cell
	 * @param value		Value to be set
	 */
	public void set(Cell c, int value){
		cells[c.col][c.row].value = value;
	}
	
	/**
	 * The purpose of this method is twofold. It can be used to determine if a cell has a neighbor with blank value. If the cell has a 
	 * blank neighbor, this method will return the blank Cell.
	 * 
	 * @param c		Cell to check
	 * @return		null if it does not have a blank neighbor. Otherwise, returns the blank Cell.
	 */
	public Cell getBlankNeighbor(Cell c){
		int left = c.col-1;
		int right = c.col+1;
		int up = c.row-1;
		int down = c.row+1;
		
		// Check for bounds; return null if out of bounds.
		if(up>=0 && cells[c.col][up].value == 0) return cells[c.col][up]; 
		if(down<COLS && cells[c.col][down].value == 0) return cells[c.col][down];
		if(left>=0 && cells[left][c.row].value == 0) return cells[left][c.row];
		if(right<ROWS && cells[right][c.row].value == 0) return cells[right][c.row];
		
		return null;
	}
	
	/**
	 * Method used for debugging. Flattens out the matrix into a single string. Not very helpful, honestly.
	 */
	public String toString(){
		String str = "";
		for(int y=0;y<ROWS;y++)
			for(int x=0;x<COLS;x++)
				str+=cells[x][y].value;
			
		return str;
	}


}
