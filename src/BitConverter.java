
public class BitConverter {
	int[] intToBinary (int a, int dimension) {
		String temp = Integer.toBinaryString(a);
		int[] ret = new int[dimension]; //8 temp.length()
		while (temp.length() != dimension) { //8 temp.length()
			temp = "0" + temp;
		}
		
		for (int i = 0; i < dimension; i++) { //8 temp.length()
			ret[i] = Character.getNumericValue(temp.charAt(i));
		}
		
		return ret;
	}
}
