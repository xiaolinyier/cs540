public class Helper {
	
	/** 
    * Class constructor.
    */
	private Helper () {}

	/**
	* This method is used to check if a number is prime or not
	* @param x A positive integer number
	* @return boolean True if x is prime; Otherwise, false
	*/
	public static boolean isPrime(int x) {
		
		// TODO Add your code here
		if(x == 1) {
			return false;
		}
		for(int i = 2; i <= x/2; i++) {
			if(x % i == 0) {
				return false;
			}
		}

		return true;
	}

	/**
	* This method is used to get the largest prime factor 
	* (In this case factor can't be itself)
	* @param x A positive integer number
	* @return int The largest prime factor of x
	*/
	public static int getLargestPrimeFactor(int x) {

    	// TODO Add your code here
		int factor = 2;
		for(int i = x-1; i >= 2;i--) {
			if(isPrime(i) && x%i == 0) {
				factor = i;
				return factor;
			}
		}
		return factor;

    }
}