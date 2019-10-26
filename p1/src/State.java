import java.util.ArrayList;

/**
 * A state in the search represented by the (x,y) coordinates of the square and
 * the parent. In other words a (square,parent) pair where square is a Square,
 * parent is a State.
 * 
 * You should fill the getSuccessors(...) method of this class.
 * 
 */
public class State {

	private Square square;
	private State parent;

	// Maintain the gValue (the distance from start)
	// You may not need it for the BFS but you will
	// definitely need it for AStar
	private int gValue;

	// States are nodes in the search tree, therefore each has a depth.
	private int depth;

	/**
	 * @param square
	 *            current square
	 * @param parent
	 *            parent state
	 * @param gValue
	 *            total distance from start
	 */
	public State(Square square, State parent, int gValue, int depth) {
		this.square = square;
		this.parent = parent;
		this.gValue = gValue;
		this.depth = depth;
	}

	/**
	 * @param visited
	 *            explored[i][j] is true if (i,j) is already explored
	 * @param maze
	 *            initial maze to get find the neighbors
	 * @return all the successors of the current state
	 */
	public ArrayList<State> getSuccessors(boolean[][] explored, Maze maze) {
		// FILL THIS METHOD
		
		ArrayList<State> sucs = new ArrayList<State>();
		
		// left
		Square lsq = new Square(this.getX(),this.getY() - 1);
		if (explored[lsq.X][lsq.Y] == false 
				&& maze.getSquareValue(lsq.X, lsq.Y) != '%') {
			State lst = new State(lsq,this,this.getGValue()+1, this.getDepth() +1);
			sucs.add(lst);
			//explored[lsq.X][lsq.Y] = true;
		}
		
		// down
		Square dsq = new Square(this.getX() + 1,this.getY());
		if(explored[dsq.X][dsq.Y] == false 
				&& maze.getSquareValue(dsq.X, dsq.Y) != '%') {
			State dst = new State(dsq,this,this.getGValue()+1, this.getDepth()+1);
			sucs.add(dst);
			//explored[dsq.X][dsq.Y] = true;
		}
		
		// right
		Square rsq = new Square(this.getX(),this.getY() + 1);
		if(explored[rsq.X][rsq.Y] == false 
				&& maze.getSquareValue(rsq.X, rsq.Y) != '%') {
			State rst = new State(rsq,this,this.getGValue()+1, this.getDepth()+1);
			sucs.add(rst);
			//explored[rsq.X][rsq.Y] = true;
		}
		
		// up
		Square usq = new Square(this.getX() - 1,this.getY());
		if(explored[usq.X][usq.Y] == false 
				&& maze.getSquareValue(usq.X, usq.Y) != '%') {
			State ust = new State(usq,this,this.getGValue()+1, this.getDepth()+1);
			sucs.add(ust);
			//explored[usq.X][usq.Y] = true;
		}
		
		
		// TODO check all four neighbors (up, right, down, left) 
		
		// TODO remember that each successor's depth and gValue are
		
		// +1 of this object.
		return sucs;
	}

	/**
	 * @return x coordinate of the current state
	 */
	public int getX() {
		return square.X;
	}

	/**
	 * @return y coordinate of the current state
	 */
	public int getY() {
		return square.Y;
	}

	/**
	 * @param maze initial maze
	 * @return true is the current state is a goal state
	 */
	public boolean isGoal(Maze maze) {
		if (square.X == maze.getGoalSquare().X
				&& square.Y == maze.getGoalSquare().Y)
			return true;

		return false;
	}

	/**
	 * @return the current state's square representation
	 */
	public Square getSquare() {
		return square;
	}

	/**
	 * @return parent of the current state
	 */
	public State getParent() {
		return parent;
	}

	/**
	 * You may not need g() value in the BFS but you will need it in A-star
	 * search.
	 * 
	 * @return g() value of the current state
	 */
	public int getGValue() {
		return gValue;
	}

	/**
	 * @return depth of the state (node)
	 */
	public int getDepth() {
		return depth;
	}
}
