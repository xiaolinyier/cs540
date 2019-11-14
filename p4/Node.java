import java.util.*;

/**
 * Class for internal organization of a Neural Network.
 * There are 5 types of nodes. Check the type attribute of the node for details.
 * Feel free to modify the provided function signatures to fit your own implementation
 */

public class Node {
    private int type = 0; //0=input,1=biasToHidden,2=hidden,3=biasToOutput,4=Output
    public ArrayList<NodeWeightPair> parents = null; //Array List that will contain the parents (including the bias node) with weights if applicable

    private double inputValue = 0.0;
    private double outputValue = 0.0;
    private double outputGradient = 0.0;
    private double delta = 0.0; //input gradient

    //Create a node with a specific type
    Node(int type) {
        if (type > 4 || type < 0) {
            System.out.println("Incorrect value for node type");
            System.exit(1);

        } else {
            this.type = type;
        }

        if (type == 2 || type == 4) {
            parents = new ArrayList<>();
        }
    }

    //For an input node sets the input value which will be the value of a particular attribute
    public void setInput(double inputValue) {
        if (type == 0) {    //If input node
            this.inputValue = inputValue;
        }
    }

    /**
     * Calculate the output of a node.
     * You can get this value by using getOutput()
     */
    public void calculateOutput(ArrayList<Node> outputNodes) {
        if (type == 2 || type == 4) {   //Not an input or bias node
            // TODO: add code here
        	if(type == 2) {
        		this.outputValue = getRelu();
        	}else if(type == 4) {
        		this.outputValue = getSoftmax(outputNodes);
        	}
        }
    }

    //Gets the output value
    public double getOutput() {

        if (type == 0) {    //Input node
            return inputValue;
        } else if (type == 1 || type == 3) {    //Bias node
            return 1.00;
        } else {
            return outputValue;
        }

    }
    // get input value
    public double getInput() {
    	return this.inputValue;
    }
    // get delta
    public double getDelta() {
    	if(type==2||type==4) {
    		return this.delta;
    	}
    	return 0;
    }
    //Calculate the delta value of a node.
    public void calculateDelta(double targetValue, ArrayList<Node> outputNodes, int nodeIndex) {
        if (type == 2 || type == 4)  {
            // TODO: add code here
        	double delta = 0;
        	if(type == 2) {
        		delta = getPrimeRelu() * calcWeightedOutputDelta(outputNodes, nodeIndex);
        	}else if(type == 4){
        		delta = targetValue - this.outputValue;
        	}
        	this.delta = delta;
        }
    }


    //Update the weights between parents node and current node
    public void updateWeight(double learningRate) {
        if (type == 2 || type == 4) {
            // TODO: add code here
        	for(NodeWeightPair parentPair: this.parents) {
        		double deltaW = learningRate * parentPair.node.getOutput() * this.delta;
        		parentPair.weight+=deltaW;
        	}
        }
    }
    
    private double getPrimeRelu() {
    	
    	if(type == 2) {
    		if(this.inputValue <= 0) {
    			return 0;
    		}else {
    			return 1;
    		}
    	}
    	return -1;
    }
    
    private double getRelu() {
    	double result = Math.max(0, this.inputValue);
    	return result;
    }
    
    private double getSoftmax(ArrayList<Node> outputNodes) {
    	double sum = 0;
    	double result = Math.exp(inputValue);
		for(Node node : outputNodes) {
			sum+=Math.exp(node.getInput());
		}
		return result/sum;
    }
    // set input value;
    public void calcWeightedInputSum() {
    	if(type == 2||type==4) {
    		double value = 0;
    		for(NodeWeightPair pair:parents) {
    			value+= pair.node.getOutput() * pair.weight;
    		}
    		inputValue = value;    		
    	}
    }
    
    public double calcWeightedOutputDelta(ArrayList<Node> outputNodes,int nodeIndex) {
    	double result = 0;
    	for(Node node: outputNodes) {
    		result+= node.parents.get(nodeIndex).weight*node.getDelta();
    	}
    	return result;
    }
}


