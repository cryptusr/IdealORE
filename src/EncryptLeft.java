import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import java.util.*;

public class EncryptLeft {
    static Pairing pairing = PairingFactory.getPairing("d224.properties");
    static Element alpha = pairing.getZr().newRandomElement(); // ��_X

    // K1 = g1^{ alpha * U_X * M }
    public Element[][] setK1(Field<Element> Zr, Field<Element> G1, Field<Element> G2, Field<Element> GT,
            Element g1, Element g2, Element[] m, Element[][] B) {

	final int n = m.length;
	
	// 1) n���� ���� �ٸ� �۹����̼� ��_1,...,��_n
	List<int[]> perms = distinctPermutations(n);
	
	// 2) U_X ����: j��° �� = m ���͸� ��_j �� ��迭
	Element[][] UX = new Element[n][n]; // Z_p ����
	for (int col = 0; col < n; col++) {
	int[] pi = perms.get(col);
	for (int row = 0; row < n; row++) {
	UX[row][col] = Zr.newElement();
	UX[row][col].set(m[pi[row]]);
	}
	}
	
	// 3) UX * M
	Element[][] UXM = MatrixOps.multiplyMatrices(UX, B);
	
	// 4) �� ����
	for (int i = 0; i < n; i++) {
	for (int j = 0; j < n; j++) {
	UXM[i][j].mulZn(alpha);
	}
	}
	
	// 5) g1 ������ �� K1 (n��n)
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

        // 1) �� ���� �۹����̼� ��, �׸��� S_{X,0} = (e_{��(1)}^T, ..., e_{��(n)}^T)
        int[] pi = samplePermutation(n);
        Element[][] S0 = permutationMatrixFromE(Zr, n, pi); // e-���� ���� �״�� ����

        // 2) (�� * S0) * M^{-1}
        //    (0�� �״�� 0�̹Ƿ� 1 ��ġ���� �Ḧ ���ص� ����)
        for (int i = 0; i < n; i++) {
            int j = pi[i];
            S0[i][j].mulZn(alpha); // ���Ұ� 1�� ��ġ���� �� ��
        }
        Element[][] tmp = MatrixOps.multiplyMatrices(S0, BInverse);

        // 3) g1 ������
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

    // e-���� ���ǿ� ���� �۹����̼� ��� S: S[i][pi[i]] = 1, else 0 (Zp����)
    private Element[][] permutationMatrixFromE(Field<Element> Zr, int n, int[] pi) {
        Element[][] S = new Element[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                S[i][j] = (i == piIndexOf(pi, i, j))
                        ? Zr.newOneElement()
                        : Zr.newZeroElement();
            }
        }
        // �� �б�� ��¦ �����ϹǷ� ��������� �ٽ� �ۼ�:
        for (int i = 0; i < n; i++) {
            Arrays.fill(S[i], Zr.newZeroElement()); // �� ���� JPBC������ �������� �� �� �־� �ּ�ó�� ����
        }
        for (int i = 0; i < n; i++) {
            int j = pi[i];
            S[i][j] = Zr.newOneElement();
        }
        return S;
    }
    private int piIndexOf(int[] pi, int i, int j) { return -1; } // ��� �� �� (������ ���ۼ�)

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
