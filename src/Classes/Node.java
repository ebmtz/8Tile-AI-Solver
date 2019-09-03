package Classes;

public class Node{
	public TileMap state;
	boolean visited; // will need this to keep track of which nodes have been visited
	public int cost;
	Node parent;
	
	public Node(TileMap cells, Node p,int cst){
		state = cells;
		parent = p;
		cost = cst;
	}
}
