import java.util.*;

public class debug {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<List<Integer>> examples = new ArrayList<List<Integer>>();
		List<Integer> instance_0 = Arrays.asList(1,2,3,0);
		List<Integer> instance_1 = Arrays.asList(3,2,3,1);
		List<Integer> instance_x = Arrays.asList(3,2,3,0);
		examples.add(instance_0);
		examples.add(instance_1);
		examples.add(instance_x);
		double result = conEntro(examples,0,2);
		//System.out.println(result);
		List<Integer> x = Arrays.asList(1,2,3,4);
		for(Integer i : x) {
			System.out.println(i);
		}
	}
	public static double conEntro(List<List<Integer>> examples, int attr, int threshold) {
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
		return result;
	}
	
	public static double entro(List<List<Integer>> examples) {
		double result;
		double size = examples.size();
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
		System.out.println(count_0);
		double p0 = count_0/size;
		double p1 = count_1/size;
		System.out.println(p0);
		System.out.println(p1);
		result = -((p0*Math.log(p0)/Math.log(2)) + (p1*Math.log(p1)/Math.log(2)));
		return result;
	}
}
