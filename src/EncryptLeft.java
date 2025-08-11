import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import java.util.*;

public class EncryptLeft {
    static Pairing pairing = PairingFactory.getPairing("d224.properties");
    static Element alpha = pairing.getZr().newRandomElement(); // α_X

    // K1 = g1^{ alpha * U_X * M }
    public Element[][] setK1(Field<Element> Zr, Field<Element> G1, Field<Element> G2, Field<Element> GT,
            Element g1, Element g2, Element[] m, Element[][] B) {

	final int n = m.length;
	
	// 1) n개의 서로 다른 퍼뮤테이션 π_1,...,π_n
	List<int[]> perms = distinctPermutations(n);
	
	// 2) U_X 구성: j번째 열 = m 벡터를 π_j 로 재배열
	Element[][] UX = new Element[n][n]; // Z_p 원소
	for (int col = 0; col < n; col++) {
	int[] pi = perms.get(col);
	for (int row = 0; row < n; row++) {
	UX[row][col] = Zr.newElement();
	UX[row][col].set(m[pi[row]]);
	}
	}
	
	// 3) UX * M
	Element[][] UXM = MatrixOps.multiplyMatrices(UX, B);
	
	// 4) α 배율
	for (int i = 0; i < n; i++) {
	for (int j = 0; j < n; j++) {
	UXM[i][j].mulZn(alpha);
	}
	}
	
	// 5) g1 지수승 → K1 (n×n)
	Element[][] K1 = new Element[n][n];
	for (int i = 0; i < n; i++) {
	for (int j = 0; j < n; j++) {
	K1[i][j] = G1.newElement();
	K1[i][j].set(g1);
	K1[i][j].powZn(UXM[i][j]);
	}
	}
	return K1;
}


    // K2 = g1^{ alpha * S_{X,0} * M^{-1} }
    public Element[][] setK2(Field<Element> Zr, Field<Element> G1, Field<Element> G2, Field<Element> GT,
                             Element g1, Element g2, Element[] m, Element[][] BInverse) {

        final int n = m.length;

        // 1) 한 개의 퍼뮤테이션 π, 그리고 S_{X,0} = (e_{π(1)}^T, ..., e_{π(n)}^T)
        int[] pi = samplePermutation(n);
        Element[][] S0 = permutationMatrixFromE(Zr, n, pi); // e-기저 정의 그대로 생성

        // 2) (α * S0) * M^{-1}
        //    (0은 그대로 0이므로 1 위치에만 α를 곱해도 동일)
        for (int i = 0; i < n; i++) {
            int j = pi[i];
            S0[i][j].mulZn(alpha); // 원소가 1인 위치에만 α 곱
        }
        Element[][] tmp = MatrixOps.multiplyMatrices(S0, BInverse);

        // 3) g1 지수승
        Element[][] K2 = new Element[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                K2[i][j] = G1.newElement();
                K2[i][j].set(g1);
                K2[i][j].powZn(tmp[i][j]);
            }
        }
        return K2;
    }

    // ===== helpers =====

    // e-기저 정의에 맞춘 퍼뮤테이션 행렬 S: S[i][pi[i]] = 1, else 0 (Zp에서)
    private Element[][] permutationMatrixFromE(Field<Element> Zr, int n, int[] pi) {
        Element[][] S = new Element[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                S[i][j] = (i == piIndexOf(pi, i, j))
                        ? Zr.newOneElement()
                        : Zr.newZeroElement();
            }
        }
        // 위 분기는 살짝 난해하므로 명시적으로 다시 작성:
        for (int i = 0; i < n; i++) {
            Arrays.fill(S[i], Zr.newZeroElement()); // 이 줄이 JPBC에서는 얕은복사 될 수 있어 주석처리 가능
        }
        for (int i = 0; i < n; i++) {
            int j = pi[i];
            S[i][j] = Zr.newOneElement();
        }
        return S;
    }
    private int piIndexOf(int[] pi, int i, int j) { return -1; } // 사용 안 함 (위에서 재작성)

    private int[] samplePermutation(int n) {
        int[] a = identity(n);
        Random rnd = new Random();
        for (int i = n - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int t = a[i]; a[i] = a[j]; a[j] = t;
        }
        return a;
    }

    private List<int[]> distinctPermutations(int n) {
        Set<String> seen = new HashSet<>();
        List<int[]> res = new ArrayList<>(n);
        while (res.size() < n) {
            int[] p = samplePermutation(n);
            String key = Arrays.toString(p);
            if (seen.add(key)) res.add(p);
        }
        return res;
    }

    private int[] identity(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        return a;
    }
}




/*
public class EncryptLeft {
	static Pairing pairing = PairingFactory.getPairing("d224.properties");
	static Element alpha = pairing.getZr().newRandomElement();
	
	public Element[] setK1(Field<Element> Zr, Field<Element> G1, Field<Element> G2, Field<Element> GT, Element g1, Element g2, Element[] m,  Element[][] B) {
		Element[] K1 = new Element[m.length];
		Element[] tmp = new Element[m.length];
		Element[] tmp1 = new Element[m.length];
		
		Element[][] per = new Element[m.length][m.length];
		
		per = MatrixOps.generatePermutationMatrix(m.length);
		
		for (int i = 0; i < m.length; i++) {
			tmp[i] = Zr.newOneElement();
			tmp[i].mulZn(alpha);
			tmp[i].mul(m[i]);
		}
		
		tmp1 = MatrixOps.multiplyArrayMatrix(tmp, MatrixOps.multiplyMatrices(per, B) );
		
		for (int i = 0; i < m.length; i++) {
			K1[i] = G1.newElement();
			K1[i].set(g1);
			K1[i].powZn(tmp1[i]);
		}
		return K1;
	}
	
	public Element[][] setK2(Field<Element> Zr, Field<Element> G1, Field<Element> G2, Field<Element> GT, Element g1, Element g2, Element[] m,  Element[][] BInverse) {
		Element[][] K2 = new Element[m.length][m.length];
		Element[][] tmp = new Element[m.length][m.length];
		Element[][] tmp1 = new Element[m.length][m.length];
		
		Element[][] per = new Element[m.length][m.length];
		
		per = MatrixOps.generatePermutationMatrix(m.length);
		
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m.length; j++) {
				if (i == j) {
					tmp[i][j] = Zr.newOneElement();
					tmp[i][j].mulZn(alpha);
				}
				else {
					tmp[i][j] = Zr.newZeroElement();
				}
			}
		}
		
		tmp1 = MatrixOps.multiplyMatrices(tmp, MatrixOps.multiplyMatrices(per, BInverse) );
		
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m.length; j++) {
				K2[i][j] = G1.newElement();
				K2[i][j].set(g1);
				K2[i][j].powZn(tmp1[i][j]);
			}
		}
		return K2;
	}
}
*/
