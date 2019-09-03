package Classes;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import Classes.TileMap.Cell;

public class Player {
	public final TileMap FINAL_STATE;
	public final int COLS;
	public final int ROWS;
	public int nodeCount;
	public ArrayList<Node> closedList;
	public Node current;
	
	ArrayList<Node> openList;	
	Grid grid;	
	Node root;	
	
	/**
	 * Constructor
	 * 
	 * @param g Grid containing the Player object
	 */
	public Player(Grid g){
		COLS = g.COLS;
		ROWS = g.ROWS;
							
		FINAL_STATE = new TileMap(COLS,ROWS);
		root = new Node(g.map,null,0);	
		current = root;
		nodeCount = 0;
		grid = g;
		openList = new ArrayList<Node>();
		closedList = new ArrayList<Node>();
		openList.add(root);
	}	
		
	/**
	 * Distance from the root node + Evaluated cost.
	 * 
	 * @param	 	target Node
	 * @return		cost
	 */
	public int cost(Node target, TileMap goal, boolean BFS){		
		return (BFS) ? costH(target.state,goal): target.cost + costH(target.state,goal); // A* uses path cost + heuristic, BFS only uses heuristic cost
	}
	
	/**
	 * Method used to search the openList and retrieve the Node with least cost.
	 * 
	 * @return		The Node with min value of: Distance from the root + Evaluated cost
	 */
	public Node findMinNode(boolean BFS, TileMap goal){
		Node minNode = openList.get(0); // assign to first node in list
		
		for(Node node: openList){ // iterate through list
			if(cost(node,goal,BFS)<cost(minNode,goal,BFS) && goalLevel(node.state) >= goalLevel(minNode.state)) 	// goalLevel(Node) here is not necessary,
																										// but does help in finding quicker solutions.
				minNode = node; // new minimum node found
		}
		
		return minNode;
	}
	
	/**
	 * Cost calculated using the sum of Manhattan Distances of each tile to its correct position.
	 * 
	 * @param map 	TileMap to evaluate
	 * @return		Sum of Manhattan Distances
	 */
	public int costH(TileMap map, TileMap goal){
		int cost = 0; // best-case-scenario
		
		for(Cell[] cells_a: map.cells)  				// First Nested loop: Iterate through every cell in the TileMap
			for(Cell a:cells_a){
				boolean found = false;
				for(Cell[] cells_b:goal.cells){ 	// Second Nested loop: Find the value of the current cell in the goal state. 
					for(Cell b: cells_b){ 		
						if(b.value == -1)
							continue;
									// logical example: Suppose map[0][0] = 9, find where the value 9 is located in FINAL_STATE and calculate
						if(a.value == b.value){			//					the Manhattan distance to that location.
							cost+=a.manhattanDistance(b);
							found = true;
							break;
						}
						if(found) 
							break;
					}
				}
			}
						
		
		return cost;
	}
	
	
	/**
	 * This is an alternative heuristic cost I was working on for my own amusement. Not relevant for this assignment. 
	 * 
	 * The idea is to combine Manhattan distances with trying to solve the puzzle by parts. You can imagine trying to solve for the outter 
	 * layer first (meaning the first row and column), then the next, and so on. Eventually, you arrive to a 2x2 matrix where you simply need 
	 * to rotate until the puzzle is completed (assuming it is solvable).
	 * 
	 * @param map 		TileMap to evaluate heuristic cost
	 * @return 			The estimated cost
	 */
	public int heurCost(TileMap map){
		int cost = 0;
		int level = goalLevel(map);
		TileMap currentGoal = new TileMap(COLS,ROWS);
		for(int x=0; x<COLS;x++)
			for(int y=0;y<ROWS;y++){
				if(x>level && y>level)
					currentGoal.cells[x][y].value = -1; // This cell is out of the current goal's "level", set it to -1 to ignore it
			}
		
		for(Cell[] cells_a: map.cells)  				// First Nested loop: Iterate through every cell in the TileMap
			for(Cell a:cells_a){
				boolean found = false;
				for(Cell[] cells_b:currentGoal.cells){ 	// Second Nested loop: Find the value of the current cell in the currentGoal state. 
					for(Cell b: cells_b){
						if(b.col > level && b.row>level) // we don't care about this cell for now
							continue;						
														// logical example: Suppose map[0][0] = 9, find where the value 9 is located in FINAL_STATE and calculate
						if(a.value == b.value){			//					the Manhattan distance to that location.
							cost+=a.manhattanDistance(b);
							found = true;
							break;
						}
						if(found) 
							break;
					}
				}
			}
				
		return cost;
	}
	
	/**
	 * This is really just a method based on a hunch to improve the AI's performance when solving the puzzle.
	 * 
	 * Used on heurCost method.
	 * 
	 * @return Integer representing the level at which the cost should focus on.
	 */
	public int goalLevel(TileMap state){
		int level;
		
		for(level = 0;level<state.COLS;level++){
			for(int x=level;x<state.COLS;x++){
				if(state.cells[level][x].value != FINAL_STATE.cells[level][x].value &&
						state.cells[x][level].value != FINAL_STATE.cells[x][level].value)
					return level;
			}
		}
			
		
		return level;
	}
	
	
	/**
	 * Manhattan distance from a Cell of one TileMap to another Cell of same value in another TileMap
	 * 
	 * @param a			Cell in TileMap
	 * @param compare	TileMap containing another Cell with same value as `a`, but possibly in a different [col,row] than `a` 
	 * @return			Manhattan Distance cost 
	 */
	public int costFrom(Cell a, TileMap compare){
		int cost = 0;
		
		for(Cell [] cells: compare.cells)
			for(Cell cell: cells)
				if(cell.value == a.value)
					cost+= a.manhattanDistance(cell);
				
				
		return cost;
	}
	
	
	/**
	 * Cost calculated using the number of misplaced tiles.
	 * 
	 * @param map 	TileMap to be evaluated
	 * @return		The number of misplaced tiles in the TileMap
	 */
	public int costH2(TileMap map){
		int cost = 0;
		for(int x = 0; x<COLS;x++)
			for(int y = 0; y<ROWS;y++)
				if(map.cells[x][y].value != FINAL_STATE.cells[x][y].value) cost++;
		
		return cost;
	}
	
	
	/**
	 * Method used to determine whether a Node's state is currently stored in the openList or closedList
	 * 
	 * @param curr		Node containing the state to check
	 * @param list		Either the openList or closedList	
	 * @return			True if the Node's state is not in the provided list AND (given a collision was found) the Node is more costly than 
	 * 					another Node containing the same state
	 */
	public boolean inList(Node curr,ArrayList<Node> list, TileMap goal,boolean BFS){
		for(Node node:list)
			if(curr.state.equals(node.state) /*&& (BFS ||cost(node,goal,BFS)<cost(curr,goal,BFS))*/)
				return true;
		
		return false;
	}
	
	public Node mySolution(Node start, boolean BFS){
		TileMap[] levels = new TileMap[COLS-1];
		java.util.Arrays.fill(levels, new TileMap(COLS,ROWS));
		Node currentStart = start;
		for(int level = 0; level<levels.length;level++){
			for(int x = level+1; x<COLS;x++)
				for(int y = level+1;y<ROWS;y++)
					levels[level].cells[x][y].value = -1;
			

			System.out.print(levels[level]);			
			currentStart = Astar(currentStart,levels[level],BFS);
			sleep(100);
		}
		return currentStart;
	}
	
	public Node Astar(Node start, boolean BFS){
		return Astar(start,FINAL_STATE,BFS);
	}
	
	/**
	 * A* algorithm implementation
	 * 
	 * @param start		Starting position
	 */
	public Node Astar(Node start, TileMap goalState, boolean BFS){		
		root = start;
		openList = new ArrayList<Node>();
		closedList = new ArrayList<Node>();
		
		openList.add(root);
		
		while(!openList.isEmpty()){
			
			current = findMinNode(BFS,goalState); // First find the Minimal Node in openList 
			
			grid.map = current.state; // This just allows the user to see the animation
			grid.repaint();
			
			if(FINAL_STATE.equals(current.state)){
				return current;
			}
					
			openList.remove(current); // Pop the minNode from openList and mark it "explored" by moving it to the closedList
			closedList.add(current);
			
			ArrayList<Node> children = getChildren(current); // find every possible move in current state
			
			for(Node node: children){
				if(inList(node,closedList,goalState,BFS)) // do not add if the state is already in current (unless its cost is lower)
					continue;
				
				if(inList(node,openList,goalState,BFS)) // do not add if the state is in the openList (unless its cost is lower)
					continue;
									
				openList.add(node); // by here, we know the Node is not in either list or that its cost is lower than a possible "collision" state
			}
			
			closedList.add(current); // current has been explored, add it to closedList		
		}
		return null; // getting here means there is no path...
	}
	
	/**
	 * Method to get the possible movements made by the provided Node
	 * 
	 * @param parent	Node containing the current state
	 * @return			Nodes containing the states after a possible move was made.
	 */
	public ArrayList<Node> getChildren(Node parent){
		ArrayList<Node> children = new ArrayList<Node>();
		
		for(Cell[] cells: parent.state.cells)
			for(Cell cell: cells)
				if(parent.state.getBlankNeighbor(cell) != null)// check if cell can move
					children.add(new Node(move(cell,parent.state),parent,parent.cost+1));
		
		return children;
	}
	
	/**
	 * Method used to set a time delay. Used mainly just to slow down animation when debugging.
	 * 
	 * @param milli		Milliseconds
	 */
	public void sleep(int milli){
		try {
			TimeUnit.MILLISECONDS.sleep(milli);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to display the solution (path from root position to the FINAL_STATE)
	 * 
	 */
	public void showSolution(Node solutionNode){
		Stack<Node> path = pathToNode(solutionNode);
		current = path.pop();
		grid.map = current.state;
		grid.repaint();
		sleep(1000);
		while(!path.empty()){
			current = path.pop();
			grid.map = current.state;
			grid.repaint();
			sleep(400);
		}
	}
	
	/**
	 * Returns a Node's backward path to a Parent (the path from the root to the node). 
	 * 
	 * @param n 	Node
	 * @return		Stack<Node> containing the path from the root to the Node
	 */
	public Stack<Node> pathToNode(Node n){
		Stack<Node> path = new Stack<Node>();
		path.push(n);
		while(n.parent!=null){
			n = n.parent;
			path.push(n);
		}
		
		return path;
	}
		
	/**
	 * Since the assignment needs to check the result of more than just one move, it'd be beneficial to simply return the "potential" new state and
	 * allow the option to choose whether to go to the state or not.
	 * 
	 * @param a
	 * @param b
	 * @return 			The resulting grid from swapping the values of Cells a and b.
	 */
	public TileMap swap(Cell a, Cell b,TileMap map){
		TileMap newState = new TileMap(map);		
		int tmp = a.value;
		newState.set(a, b.value);
		newState.set(b, tmp);
				
		return newState;
	}
	
	/**
	 * This method simply receives a single Cell for input and will move it to the "blank" spot if and only if the blank spot is its neighbor.
	 * 
	 * @param cell 		The Cell to be moved
	 * @return 			The resulting grid from moving the cell to the blank position.
	 */
	public TileMap move(Cell cell, TileMap map){
		Cell blank = map.getBlankNeighbor(cell);
		
		if(blank != null)
			return swap(cell,blank,map); // This cell has a blank neighbor cell. Move there
		
		return map;
	}
	
	/**
	 * This method finds a Cell at random in the grid, determines if it can move and moves it when true.
	 * 
	 * @return			The resulting grid from moving a random cell to the blank position.
	 */
	public TileMap move(TileMap map){
		Random randy = new Random();
		while(true){
			Cell cell = map.cells[randy.nextInt(COLS)][randy.nextInt(ROWS)];
			Cell blank = map.getBlankNeighbor(cell);
			if(blank != null)				
				return swap(cell,blank,map);			
		}
				
	}
	
	
	/**
	 * Method used for debugging
	 */
	public String toString(){
		String str = "";
		for(int y=0;y<ROWS;y++)
			for(int x=0;x<COLS;x++)							
				str+=current.state.cells[x][y].value;
		return str;
	}
}
