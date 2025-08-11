import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class Test {
	public static int decrypt (Field<Element> Zr, Field<Element> G1, Field<Element> G2, Field<Element> GT, Element g1, Element g2, Element[][] K11, Element[][] K12, Element[] C11, Element[] C12, Element[][] K21, Element[][] K22, Element[] C21, Element[] C22, int dimension) {
		
		Element[] left = new Element[dimension];
		Element[] right = new Element[dimension];
		
		for (int i = 0; i < dimension; i++) {
			left[i] = GT.newOneElement();
			right[i] = GT.newOneElement();
		}
		
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				left[i].mul(Setup.pairing.pairing(K11[i][j], C22[j]));
				right[i].mul(Setup.pairing.pairing(K12[i][j], C21[j]));	
			}
		}
		
		
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (left[i].isEqual(right[j])) {
					return 1;
				}
			}
		}
		return 0;
		
	}
}


/*
public class Test {
	public static int decrypt (Field<Element> Zr, Field<Element> G1, Field<Element> G2, Field<Element> GT, Element g1, Element g2, Element[] K11, Element[][] K12, Element[] C11, Element[][] C12, Element[] K21, Element[][] K22, Element[] C21, Element[][] C22, int dimension) {
		
		Element[] left = new Element[dimension];
		Element[] right = new Element[dimension];
		
		for (int i = 0; i < dimension; i++) {
			left[i] = GT.newOneElement();
			right[i] = GT.newOneElement();
		}
		
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				left[i].mul(Setup.pairing.pairing(K11[j], C22[j][i]));
				right[i].mul(Setup.pairing.pairing(K12[i][j], C21[j]));	
			}
		}
		
		
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (left[i].isEqual(right[j])) {
					return 1;
				}
			}
		}
		return 0;
		
	}
}
*/
