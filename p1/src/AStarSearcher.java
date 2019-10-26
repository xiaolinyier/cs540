import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * A* algorithm search
 * 
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public AStarSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main a-star search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {

		// FILL THIS METHOD

		// explored list is a Boolean array that indicates if a state associated with a given position in the maze has already been explored. 
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		// ...
		// Initialization
		for (int i = 0; i < maze.getNoOfRows() ;i++) {
			for (int j = 0; j < maze.getNoOfCols();j++) {
				explored[i][j] = false;
			}
		}

		PriorityQueue<StateFValuePair> frontier = new PriorityQueue<StateFValuePair>();

		// TODO initialize the root state and add
		// to frontier list
		// ...
		State root_state = new State(maze.getPlayerSquare(),null,0,0);
		double h_root = eucDis(root_state);
		StateFValuePair root = new StateFValuePair(root_state,h_root + root_state.getGValue());
		frontier.add(root);
		this.maxSizeOfFrontier++;

		while (!frontier.isEmpty()) {
			//expanding
			State cur_state = frontier.poll().getState();
			explored[cur_state.getX()][cur_state.getY()] = true;
			this.noOfNodesExpanded++;
			if(cur_state.getDepth() > this.maxDepthSearched) {
				this.maxDepthSearched = cur_state.getDepth();
			}
			ArrayList<State> suc_states = cur_state.getSuccessors(explored, maze);
			// TODO return true if a solution has been found
			if(cur_state.isGoal(maze)) {
				State goal = cur_state;
				cur_state = goal.getParent();
				this.cost++;
				while(cur_state.getParent() != null) {
					Square cur_square = cur_state.getSquare();
					maze.setOneSquare(cur_square, '.');
					this.cost++;
					cur_state = cur_state.getParent();
				}
				return true;
			}
			
			// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			
			// Add suc_pair to frontier
			for(int i = 0; i < suc_states.size(); i++) {
				StateFValuePair suc_pair;
				double h = eucDis(suc_states.get(i));
				suc_pair = new StateFValuePair(suc_states.get(i),h + suc_states.get(i).getGValue());
				
				if(isValid(suc_pair,frontier)) {
					frontier.add(suc_pair);
				}
			}
			
			
			if(frontier.size() > this.getMaxSizeOfFrontier()) {
				this.maxSizeOfFrontier = frontier.size();
			}
			
			
			
			// TODO update the maze if a solution found

			// use frontier.poll() to extract the minimum stateFValuePair.
			// use frontier.add(...) to add stateFValue pairs
		}

		// TODO return false if no solution
		return false;
	}
	
	
	/**
	 * heuristic function
	 * @param current state
	 * @return heuristic value
	 */
	private double eucDis(State cur) {
		int x_goal = maze.getGoalSquare().X;
		int y_goal = maze.getGoalSquare().Y;
		int x_cur = cur.getX();
		int y_cur = cur.getY();
		double result = java.lang.Math.sqrt( (x_cur-x_goal)*(x_cur-x_goal)+(y_cur-y_goal)*(y_cur-y_goal) );
		return result;
	}
	/**
	 * 
	 * @param suc_pair
	 * @param frontier
	 * @return 
	 */
	private boolean isValid(StateFValuePair suc_pair,PriorityQueue<StateFValuePair> frontier) {
		java.util.Iterator<StateFValuePair> it = frontier.iterator();
		while(it.hasNext()) {
			StateFValuePair cur = it.next();
			int x = cur.getState().getX();
			int y = cur.getState().getY();
			// suc_state is already in Frontier, check gvalue
			if(x == suc_pair.getState().getX() && y == suc_pair.getState().getY()) {
				if(suc_pair.getState().getGValue() < cur.getState().getGValue()) {
					it.remove();
					return true;
				}else {
					return false;
				}
			}
		}
		return true;
	}
}
