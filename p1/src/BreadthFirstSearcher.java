import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Breadth-First Search (BFS)
 * 
 * You should fill the search() method of this class.
 */
public class BreadthFirstSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public BreadthFirstSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main breadth first search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {
		// FILL THIS METHOD

		// explored list is a 2D Boolean array that indicates if a state associated with a given position in the maze has already been explored.
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

		// ...
		// Initialization
		for (int i = 0; i < maze.getNoOfRows() ;i++) {
			for (int j = 0; j < maze.getNoOfCols();j++) {
				explored[i][j] = false;
			}
		}
		
		State start_state = new State(maze.getPlayerSquare(),null,0,0); // root node do not have parent
		State cur_state = start_state;
		explored[cur_state.getX()][cur_state.getY()] = true;
		this.noOfNodesExpanded ++;
		
		// Queue implementing the Frontier list
		LinkedList<State> queue = new LinkedList<State>();
		ArrayList<State> suc_states = cur_state.getSuccessors(explored, maze);
		
		for (int i = 0; i < suc_states.size(); i++) {
			queue.add(suc_states.get(i));
			//System.out.printf("%d, %d  ",suc_states.get(i).getX(),suc_states.get(i).getY());
		}
		this.maxSizeOfFrontier = queue.size();
		
//		// dumping
//		for (int i = 0; i < queue.size(); i++) {
//			int x = queue.get(i).getX();
//			int y = queue.get(i).getY();
//			System.out.printf("%d %d  ", x,y);
//		}
		
		//System.out.println("*****************");
		
		
		
		while (!queue.isEmpty()) {
			// TODO return true if find a solution
			// expanding
			cur_state = queue.pop();
			explored[cur_state.getX()][cur_state.getY()] = true;
			this.noOfNodesExpanded ++;
			if(cur_state.getDepth() > this.getMaxDepthSearched()) {
				this.maxDepthSearched = cur_state.getDepth();
			}
			
			if (cur_state.isGoal(maze)) {
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
			// TODO update the maze if a solution found

			// use queue.pop() to pop the queue.
			// use queue.add(...) to add elements to queue
			
			
			

			
			
			suc_states = cur_state.getSuccessors(explored, maze);

		
			
			// add successors to frontier

			
			for(int i = 0; i < suc_states.size(); i++) {
				explored[suc_states.get(i).getX()][suc_states.get(i).getY()] = true;
				queue.add(suc_states.get(i));
			}
			
			
			
//			// Debug queue dumping
//			for (int i = 0; i < queue.size(); i++) {
//				int x = queue.get(i).getX();
//				int y = queue.get(i).getY();
//				System.out.printf("%d %d  ", x,y);
//			}
			
			
			if(queue.size() > this.getMaxSizeOfFrontier()) {
				this.maxSizeOfFrontier = queue.size(); 
			}
			
			
			
			
		}

		// TODO return false if no solution
		return false;
	}
	
}
