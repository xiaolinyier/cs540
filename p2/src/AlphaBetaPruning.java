import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AlphaBetaPruning {
	int minDepth = Integer.MAX_VALUE;
	boolean first = true;
	int Move;//
	double Value;//
	int visited;//
	int evaluated;
	int depthreached;
	double avgbranch;
	DecimalFormat df1 = new DecimalFormat("0.0");

	public AlphaBetaPruning() {
		Move = 0;
		Value = 0.0;
		visited = 0;
		evaluated = 0;
		depthreached = 0;
		avgbranch = 0.0;
	}

	/**
	 * This function will print out the information to the terminal, as specified in
	 * the homework description.
	 */
	public void printStats() {
		// TODO Add your code here
		avgbranch = (visited - 1) / (double) (visited - evaluated);
		System.out.println("Move: " + Move);
		System.out.println("Value: " + df1.format(Value));
		System.out.println("Number of Nodes Visited: " + visited);
		System.out.println("Number of Nodes Evaluated: " + evaluated);
		System.out.println("Max Depth Reached: " + depthreached);
		System.out.println("Avg Effective Branching Factor: " + df1.format(avgbranch));

	}

	/**
	 * This function will start the alpha-beta search
	 * 
	 * @param state This is the current game state
	 * @param depth This is the specified search depth
	 */
	public void run(GameState state, int depth) {
		// TODO Add your code here
		// initialization
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;
		int count = 0;
		for (int i = 1; i <= state.getSize(); i++) {
			if (!state.getStone(i))
				count++;
		}
		boolean maxPlayer;
		if (count % 2 == 0)
			maxPlayer = true;
		else
			maxPlayer = false;

		alphabeta(state, depth, alpha, beta, maxPlayer);
	}

	/**
	 * This method is used to implement alpha-beta pruning for both 2 players
	 * 
	 * @param state     This is the current game state
	 * @param depth     Current depth of search
	 * @param alpha     Current Alpha value
	 * @param beta      Current Beta value
	 * @param maxPlayer True if player is Max Player; Otherwise, false
	 * @return int This is the number indicating score of the best next move
	 */
	private double alphabeta(GameState state, int depth, double alpha, double beta, boolean maxPlayer) {
		visited++;
		GameState state1 = state;
		if (maxPlayer) {
			Value = maxvalue(state1, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, maxPlayer, true);
			
			List<Integer> nodes = state.getMoves();
			for(int i = 0; i< nodes.size();i++) {
				GameState sub = new GameState(state);
				sub.removeStone(nodes.get(i));
				double childvalue = minvalue(sub, depth-1,  Double.NEGATIVE_INFINITY,  Double.POSITIVE_INFINITY, !maxPlayer,false);
				
				if(childvalue == Value) {
					Move = nodes.get(i);
					break;
				}
			}
		}

		else {
			Value = minvalue(state1, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, maxPlayer, true);
			
			List<Integer> nodes = state.getMoves();
		
			for(int i = 0; i< nodes.size();i++) {
				GameState sub = new GameState(state);
				sub.removeStone(nodes.get(i));
				double childvalue = maxvalue(sub, depth-1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, !maxPlayer,false);
				
				if(childvalue == Value) {
					Move = nodes.get(i);
 					break;
				}
			}

	
		}
		depthreached = depth - minDepth + 1;

		return Value;
	}

	double maxvalue(GameState state, int depth, double alpha, double beta, boolean maxPlayer, boolean record) {
	
		if (state.getSuccessors().size() == 0 || depth == 0) {
			if (record)
				evaluated++;
			if (!maxPlayer)
				return state.evaluate();
			else
				return -state.evaluate();
		}
		if (record) {
			if (depth < minDepth) {
				minDepth = depth;
			}
		}
		double v = Double.NEGATIVE_INFINITY;

		for (GameState a : state.getSuccessors()) {
			if (record)
				visited++;
			double temp = minvalue(a, depth - 1, alpha, beta, !maxPlayer, record);

			
			v = Math.max(v, temp);
			
			if (v >= beta) {
				
				return v;
			}
			alpha = Math.max(alpha, v);
		}
		return v;
	}

	double minvalue(GameState state, int depth, double alpha, double beta, boolean maxPlayer, boolean record) {
		if (state.getSuccessors().size() == 0 || depth == 0) {
			if (record)
				evaluated++;
			if (!maxPlayer)
				return state.evaluate();
			else
				return -state.evaluate();
		}
		if (record) {
			if (depth < minDepth) {
				minDepth = depth;
			}
		}
		double v = Double.POSITIVE_INFINITY;
		for (GameState a : state.getSuccessors()) {
			if (record)
				visited++;
			double temp = maxvalue(a, depth - 1, alpha, beta, !maxPlayer, record);

			
			v = Math.min(v, temp);
			
			if (v <= alpha) {
				
				return v;
			}
			beta = Math.min(beta, v);
		}
		return v;
	}
}
