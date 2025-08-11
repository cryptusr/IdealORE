import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class EncryptRight {
	static Pairing pairing = PairingFactory.getPairing("d224.properties");
	static Element beta = pairing.getZr().newRandomElement();
	
	public Element[] setC1(Field<Element> Zr, Field<Element> G1, Field<Element> G2, Field<Element> GT, Element g1, Element g2, Element[] y, Element[][] B) {
		Element[] C1 = new Element[y.length];
		Element[] tmp = new Element[y.length];
		
		Element[][] per = new Element[y.length][y.length];
		
		per = MatrixOps.generatePermutationMatrix(y.length);
		
		for (int i = 0; i < y.length; i++) {
			C1[i] = G2.newElement();
			C1[i].set(g2);
		}
		
		tmp = MatrixOps.multiplyMatrixArray( MatrixOps.multiplyMatrices(B, per), y);
		
		for (int i = 0; i < y.length; i++) {
			tmp[i].mulZn(beta);
			C1[i].powZn(tmp[i]);
		}
			
		return C1;
	}
	
	
	public Element[] setC2(Field<Element> Zr, Field<Element> G1, Field<Element> G2, Field<Element> GT,
            Element g1, Element g2, Element[] y, Element[][] BInverse) {

		final int n = y.length;
		
		// 1) 하나의 퍼뮤테이션 행렬 S_{X,3}
		Element[][] S3 = MatrixOps.generatePermutationMatrix(n);
		
		// 2) η ∈ [n] 선택, 표준기저 e_η * β_X 만들기
		int eta = new java.util.Random().nextInt(n);  // 0..n-1
		Element[] e_eta_beta = new Element[n];
		for (int i = 0; i < n; i++) {
		e_eta_beta[i] = Zr.newZeroElement();
		}
		e_eta_beta[eta] = Zr.newElement();
		e_eta_beta[eta].set(beta); // β_X
		
		// 3) u = S3 * e_eta_beta  (n×1 벡터)
		Element[] u = MatrixOps.multiplyMatrixArray(S3, e_eta_beta);
		
		// 4) w = M^{-1} * u
		Element[] w = MatrixOps.multiplyMatrixArray(BInverse, u);
		
		// 5) g2 지수승으로 변환
		Element[] C2 = new Element[n];
		for (int i = 0; i < n; i++) {
		C2[i] = G2.newElement();
		C2[i].set(g2);
		C2[i].powZn(w[i]);
		}
		
		return C2; // n-차원 벡터
	}

	
	
	/*
	public Element[][] setC2(Field<Element> Zr, Field<Element> G1, Field<Element> G2, Field<Element> GT, Element g1, Element g2, Element[] y, Element[][] BInverse) {
		Element[][] C2 = new Element[y.length][y.length];
		Element[][] tmp = new Element[y.length][y.length];
		Element[][] tmp1 = new Element[y.length][y.length];
		
		Element[][] per = new Element[y.length][y.length];
		
		per = MatrixOps.generatePermutationMatrix(y.length);
		
		for (int i = 0; i < y.length; i++) {
			for (int j = 0; j < y.length; j++) {
				if (i == j) {
					tmp[i][j] = Zr.newOneElement();
					tmp[i][j].mulZn(beta);
				}
				else {
					tmp[i][j] = Zr.newZeroElement();
				}
			}
		}
		
		
		tmp1 = MatrixOps.multiplyMatrices( MatrixOps.multiplyMatrices(BInverse, per), tmp);
		
		for (int i = 0; i < y.length; i++) {
			for (int j = 0; j < y.length; j++) {
				C2[i][j] = G2.newElement();
				C2[i][j].set(g2);
				C2[i][j].powZn(tmp1[i][j]);
				//C2[i][j].powZn(BInverse[i][j]);
			}
		}
			
		return C2;
	}
	*/
}
