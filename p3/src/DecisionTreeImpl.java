import java.util.*;

/**
 * Fill in the implementation details of the class DecisionTree using this file. Any methods or
 * secondary classes that you want are fine but we will only interact with those methods in the
 * DecisionTree framework.
 */
public class DecisionTreeImpl {
	public DecTreeNode root;
	public List<List<Integer>> trainData;
	public int maxPerLeaf;
	public int maxDepth;
	public int numAttr;

	// Build a decision tree given a training set
	DecisionTreeImpl(List<List<Integer>> trainDataSet, int mPerLeaf, int mDepth) {
		this.trainData = trainDataSet;
		this.maxPerLeaf = mPerLeaf;
		this.maxDepth = mDepth;
		if (this.trainData.size() > 0) this.numAttr = trainDataSet.get(0).size() - 1;
		this.root = buildTree();
	}
	
	private DecTreeNode buildTree() {
		// TODO: add code here
		List<List<Integer>> examples = this.trainData;
		// Note: All attributes can be split on multiple times
		List<Integer> attributes = new ArrayList<Integer>();
		for(int i = 0; i < this.numAttr; i++) {
			attributes.add(i);
		}
	
		int deafult_label = majority(examples);
		int depth = 0;
		DecTreeNode tree = getSubtree(examples, attributes, deafult_label,depth);
		
		return tree;
	}
	
	private DecTreeNode getSubtree(List<List<Integer>> examples,List<Integer> attributes,int deafult_label, int depth) {
		
		List<String> list = best_AttrAndThreshold(examples, attributes);
		int attr = Integer.parseInt(list.get(0));
		int threshold = Integer.parseInt(list.get(1));
		double maxInfoGain = Double.parseDouble(list.get(2));
		// Case: create leaf node
		if(examples.size() <= this.maxPerLeaf || depth == this.maxDepth || maxInfoGain == 0) {
			//System.out.println("debug3 "+ examples.size());
			return new DecTreeNode(deafult_label,-1,-1);
			// Q: what if every instance in examples has same label
			// A: their maxInfoGain will be zero
			
		}
		
		DecTreeNode tree = new DecTreeNode(deafult_label, attr, threshold); 
		
			List<List<List<Integer>>> subExs = createsubExs(examples,attr,threshold);
			List<List<Integer>> leftEx = subExs.get(0);
			List<List<Integer>> rightEx = subExs.get(1);
			DecTreeNode left = getSubtree(leftEx,attributes,majority(leftEx),depth+1);
			DecTreeNode right = getSubtree(rightEx,attributes,majority(rightEx),depth+1);
			tree.left = left;
			tree.right = right;
		return tree;
	}
	
	/**
	 * subset of the examples classified by given attribute and threshold
	 * @param examples
	 * @param attr
	 * @return List<examples> {leftEx, rightEx}
	 */
	private List<List<List<Integer>>> createsubExs(List<List<Integer>> examples, int attr, int threshold) {
		List<List<Integer>> leftExs = new ArrayList<List<Integer>>();
		List<List<Integer>> rightExs = new ArrayList<List<Integer>>();
		for(List<Integer> instance:examples) {
			if(instance.get(attr) <= threshold) {
				leftExs.add(instance);
			}else if(instance.get(attr) > threshold){
				rightExs.add(instance);
			}
		}
		List<List<List<Integer>>> list = new ArrayList<List<List<Integer>>>();
		list.add(leftExs);
		list.add(rightExs);
		return list;
	}
	
	/**
	 * Majority vote of the examples
	 * If there are an equal number of instances labeled 0 and 1, return 1; 
	 * @param examples
	 * @return 0 or 1
	 */
	private int majority(List<List<Integer>> examples) {
		int label = 1;
		int count_1 = 0;
		int count_0 = 0;
		for(List<Integer> ins:examples) {
			if(ins.get(ins.size()-1) == 1) {
				count_1++;
			}else {
				count_0++;
			}
		}
		if(count_0 > count_1) {
			label = 0;
		}
		return label;
	}
	
	/**
	 * Calculate the best attribute and threshold to split with maximal information gain.
	 * @param List<List<Integer>> examples
	 * @param List<Integer> attributes {1,2,...,9}
	 * @return List<String> {"attribute", "threshold, "maxInfoGain"}
	 */
	private List<String> best_AttrAndThreshold(List<List<Integer>> examples, List<Integer> attributes) {
		List<String> result = new ArrayList<String>();
		int best_attr = -1;
		int attr_threshold = -1;
		//System.out.println("best_AttrAndThreshold: 1");
		double maxInfoGain = 0;
		//System.out.println(attributes.size());
		for(Integer attr : attributes) {
			int threshold = best_threshold(examples, attr);
			double temp = infoGain(examples, attr, threshold);
			//System.out.println("best_AttrAndThreshold: "+ threshold);
			if(temp > maxInfoGain) {
				maxInfoGain = temp;
				best_attr = attr;
				attr_threshold = threshold;
			}
		}
		result.add(Integer.toString(best_attr));
		result.add(Integer.toString(attr_threshold));
		result.add(Double.toString(maxInfoGain));
		
		return result;
	}
	
	/**
	 * Return the best threshold (with max information gain) for a given attribute
	 * In this homework, candidate splits are {1,2,...,9}
	 * @param List<List<Integer>> examples
	 * @param int attr
	 * @return int best
	 */
	private int best_threshold(List<List<Integer>> examples, int attr) {
		int best = -1;
		double maxInfoGain = 0;
		for(int threshold = 1; threshold <= 9; threshold++) {
			
			double temp = infoGain(examples, attr, threshold);
			//System.out.println("best_threshold: "+temp );
			if(temp > maxInfoGain) {
				
				maxInfoGain = temp;
				best = threshold;
			}
		}
		return best;
	}
	
	
	
	
	
	/**
	 * Calculate the information gain when splitting using given attribute and threshold
	 * @param List<List<Integer>>examples
	 * @param int attr
	 * @param int threshold
	 * @return double result
	 */
	private double infoGain(List<List<Integer>> examples, int attr, int threshold) {
		double result = entro(examples) - conEntro(examples, attr, threshold);
		return result;
	}
	
	/**
	 * The entropy of given examples with class_label{0,1}
	 * @param List<List<Integer>> examples
	 * @return double result
	 */
	private double entro(List<List<Integer>> examples) {
		double result;
		int size = examples.size();
		if(size == 0) {
			System.err.println("Method name: entro/ empty examples!");
		}
		double count_0 = 0;
		double count_1 = 0;
		for(List<Integer> instance : examples) {
			if(instance.get(instance.size()-1) == 0) {
				count_0++;
			}else {
				count_1++;
			}
		}
		double p0 = count_0/size;
		double p1 = count_1/size;
		double x1, x2;
		x1 = x2 = 0;
		if(p0 != 0) {
			x1 = p0*Math.log(p0)/Math.log(2);
		}
		if(p1 != 0) {
			x2 = p1*Math.log(p1)/Math.log(2);
		}
		result = -(x1 + x2);
		//System.out.println("entro"+result );
		return result;
	}
	
	/**
	 * The entropy of examples after splitting with given attribute and threshold
	 * Note: using the definition of conditional probability
	 * pL: probability of (attr<=threshold)
	 * p0L : probability of class_label-0 and L
	 * p0_L : conditional probability of class_label-0 given in L
	 * p0_L = p0L/pL
	 * @param List<List<Integer>> examples
	 * @param int attr
	 * @param int threshold
	 * @return double result
	 */
	private double conEntro(List<List<Integer>> examples, int attr, int threshold) {
		double result;
		double size = examples.size();
		double p0_L,p1_L,p0_R,p1_R;
		double pL,pR;
		double p0L,p0R,p1L,p1R;
		double count_L,count_R, count_L0, count_L1, count_R0, count_R1 = 0;
		count_L = count_R = count_L0 = count_L1 = count_R0 = count_R1 = 0;
		for(List<Integer> instance : examples) {
			if(instance.get(attr) <= threshold) {
				count_L++;
				if(instance.get(instance.size()-1) == 0) {
					count_L0++;
				}else {
					count_L1++;
				}
			}else {
				count_R++;
				if(instance.get(instance.size()-1) == 0) {
					count_R0++;
				}else {
					count_R1++;
				}
			}
		}
		pL = count_L/size;
		pR = count_R/size;
		p0L = count_L0/size;
		p1L = count_L1/size;
		p0R = count_R0/size;
		p1R = count_R1/size;
		
		p0_L = p0L/pL; //conditional probability
		p0_R = p0R/pR;
		p1_L = p1L/pL;
		p1_R = p1R/pR;
		
		double x1,x2,x3,x4;
		x1 = x2 = x3 = x4 = 0;
		if(p0_L != 0) {
			x1 = p0_L*Math.log(p0_L);
		}
		if(p1_L != 0) {
			x2 = p1_L*Math.log(p1_L);
		}
		if(p0_R != 0) {
			x3 = p0_R*Math.log(p0_R);
		}
		if(p1_R != 0) {
			x4 = p1_R*Math.log(p1_R);
		}
		result = pL*(x1 + x2)/Math.log(2);
		result += pR*(x3 + x4)/Math.log(2);
		result = -result;
		//System.out.println("conEntro"+result );
		return result;
	}
	
	/**
	 * Use the built decision tree to find the prediction class label
	 * @param instance
	 * @return prediction class label
	 */
	public int classify(List<Integer> instance) {
		// TODO: add code here
		// Note that the last element of the array is the label.
		DecTreeNode node = this.root;
		while(!node.isLeaf()) {
			if(instance.get(node.attribute) <= node.threshold) {
				node = node.left;
			}else {
				node = node.right;
			}
		}
		
		return node.classLabel;
	}
	

	
	
	
	
	
	
	
	
	
	
	// Print the decision tree in the specified format
	public void printTree() {
		printTreeNode("", this.root);
	}
	
	
	

	public void printTreeNode(String prefixStr, DecTreeNode node) {
		String printStr = prefixStr + "X_" + node.attribute;
		System.out.print(printStr + " <= " + String.format("%d", node.threshold));
		if(node.left.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.left.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.left);
		}
		System.out.print(printStr + " > " + String.format("%d", node.threshold));
		if(node.right.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.right.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.right);
		}
	}
	
	public double printTest(List<List<Integer>> testDataSet) {
		int numEqual = 0;
		int numTotal = 0;
		for (int i = 0; i < testDataSet.size(); i ++)
		{
			int prediction = classify(testDataSet.get(i));
			int groundTruth = testDataSet.get(i).get(testDataSet.get(i).size() - 1);
			System.out.println(prediction);
			if (groundTruth == prediction) {
				numEqual++;
			}
			numTotal++;
		}
		double accuracy = numEqual*100.0 / (double)numTotal;
		System.out.println(String.format("%.2f", accuracy) + "%");
		return accuracy;
	}
}
