import java.security.NoSuchAlgorithmException;
import java.util.Date;
import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.util.Random;

public class Main {
	public static void main(String[] args) throws NoSuchAlgorithmException  {
		long start, end;
        Date today;
        long timeMilli;
        
        // Setup phase---------------------------------------------------------------------------------
        Pairing pairing = PairingFactory.getPairing("d224.properties");
		Field<Element> Zr = pairing.getZr();
		Field<Element> G1 = pairing.getG1();
		Field<Element> G2 = pairing.getG2();
		Field<Element> GT = pairing.getGT();
		Element g1 = G1.newRandomElement();
		Element g2 = G2.newRandomElement();
		
		//Set the plaintext bit length
		int dimension = 10;
		BitConverter converter = new BitConverter();
		
		//----Two messages-----
		int[] x = converter.intToBinary(185,dimension);
		int[] y = converter.intToBinary(185,dimension);
		
		String key = "wfjn9834ygh8734einrgubkjef";
		Hash hash = new Hash();
        
		Element[][] B = new Element[dimension][dimension];
		B = MatrixOps.identityMatrix(dimension);
		
		//for (int i = 0; i < B.length; i++) 
			//for (int j = 0; j < B.length; j++) 
				//B[i][j] = Zr.newRandomElement();		
		
		Element[][] BInverse = new Element[dimension][dimension];
		//BInverse = Setup.setBInverse(Zr, G1, G2, GT, g1, g2, B);
		BInverse = MatrixOps.identityMatrix(dimension);
		
		// Encrypt phase-------------------------------------------------------------------------------
		EncryptLeft trapdoor = new EncryptLeft();
		EncryptRight enc = new EncryptRight();
		
		////////////////////////// m0 encryption ///////////////////////////////////////
		String[] xToStr = new String[dimension];
		String[] xpToStr = new String[dimension];
		for (int i = 0; i < dimension; i++) {
			xToStr[i] = hash.hashFunction(key, i+1, x);
			xpToStr[i] = hash.hashFunctionPlus(key, i+1, x);
		}
		Element[] m0 = new Element[dimension];
		Element[] m0Plus = new Element[dimension];
		for (int i = 0; i < dimension; i++) {
			m0[i] = Zr.newElement(xToStr[i].hashCode());
		}
		m0[0] = Zr.newOneElement();
		
		for (int i = 0; i < dimension; i++) {
			m0Plus[i] = Zr.newElement(xpToStr[i].hashCode());
		}
		
				
		Element[][] K11 = new Element[dimension][dimension];
		K11 = trapdoor.setK1(Zr, G1, G2, GT, g1, g2, m0, B);
		Element[][] K12 = new Element[dimension][dimension];
		K12 = trapdoor.setK2(Zr, G1, G2, GT, g1, g2, m0, BInverse);	
		
				
		Element[] C11 = new Element[dimension];
		C11 = enc.setC1(Zr, G1, G2, GT, g1, g2, m0Plus, B);	
		Element[] C12 = new Element[dimension];
		C12 = enc.setC2(Zr, G1, G2, GT, g1, g2, m0Plus, BInverse);	
		
		
		////////////////////////// m1 encryption //////////////////////////////////////
		String[] yToStr = new String[dimension];
		String[] ypToStr = new String[dimension];
		for (int i = 0; i < dimension; i++) {
			yToStr[i] = hash.hashFunction(key, i+1, y);
			ypToStr[i] = hash.hashFunctionPlus(key, i+1, y);
		}
		Element[] m1 = new Element[dimension];
		Element[] m1Plus = new Element[dimension];
		for (int i = 0; i < dimension; i++) {
			m1[i] = Zr.newElement(yToStr[i].hashCode());
		}
		for (int i = 0; i < dimension; i++) {
			m1Plus[i] = Zr.newElement(ypToStr[i].hashCode());
		}
		
		
		
		Element[][] K21 = new Element[dimension][dimension];
		K21 = trapdoor.setK1(Zr, G1, G2, GT, g1, g2, m1, B);
		Element[][] K22 = new Element[dimension][dimension];
		K22 = trapdoor.setK2(Zr, G1, G2, GT, g1, g2, m1, BInverse);	
		
		
		Element[] C21 = new Element[dimension];
		C21 = enc.setC1(Zr, G1, G2, GT, g1, g2, m1Plus, B);	
		Element[] C22 = new Element[dimension];
		C22 = enc.setC2(Zr, G1, G2, GT, g1, g2, m1Plus, BInverse);	
		
		// Decrypt phase-------------------------------------------------------------------------------
		if (Test.decrypt(Zr, G1, G2, GT, g1, g2, K11, K12, C11, C12, K21, K22, C21, C22, dimension) == 1)
			System.out.println("x > y");
		else if (Test.decrypt(Zr, G1, G2, GT, g1, g2, K21, K22, C21, C22, K11, K12, C11, C12, dimension) == 1)
			System.out.println("x < y");
		else
			System.out.println("x = y");
		
		/*
		System.out.println("Zr length= " + Zr.newRandomElement().getLengthInBytes());
		System.out.println("G1 length= " + G1.newRandomElement().getLengthInBytes());
        System.out.println("G2 length= " + G2.newRandomElement().getLengthInBytes());
        
        String strToEncrypt = "88888888";
        String strPssword = "rhrtj657iyijktrt";
        AES.setKey(strPssword);

        
        double[] avg = {0,0,0,0,0}; // e1, e2, p, cmp, F
        Element ex = Zr.newRandomElement();
        Element e2 = G2.newElement();
        e2.set(g2);
        for (int i = 0; i < 0; i++) { // JVM의 클래스 로딩 및 초기화
            AES.encrypt(strToEncrypt.trim());
        }
        
        
        for (int a = 0; a < 0; a++) {
        	//String str1 = RandomString.generateRandomString(16);
        	//String str2 = RandomString.generateRandomString(16);
			today = new Date();
	        //timeMilli = System.nanoTime();
			timeMilli = today.getTime();
	        start = timeMilli;
	        pairing.pairing(G1.newRandomElement(), G2.newRandomElement());
	        //G1.newRandomElement().powZn(Zr.newRandomElement());
	        //str1.compareTo(str2);
	        //Zr.newRandomElement().powZn(Zr.newRandomElement());
			//AES.encrypt(strToEncrypt.trim());
			today = new Date();
	        //timeMilli = System.nanoTime();
			timeMilli = today.getTime();
	        end = timeMilli;
	        avg[0] += (double)(end - start)/1000;
		}
        //System.out.println(avg[0] + " seconds"); // Print query time
        //System.out.println(Zr.newRandomElement().getLengthInBytes());
        */
    }
}
