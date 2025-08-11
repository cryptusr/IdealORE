import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
	public static String hashFunction(String k, int i, int[] binaryX) throws NoSuchAlgorithmException {
        // Extract the first (i - 1) bits of x_1...x_n
        StringBuilder prefixBuilder = new StringBuilder("");
        for (int index = 0; index < Math.min(i - 1, binaryX.length); index++) {
            prefixBuilder.append(binaryX[index]);
        }
        String prefix = prefixBuilder.toString();

        // Create a string of zeros of length (n - i + 1)
        int n = binaryX.length;
        StringBuilder zeros = new StringBuilder();
        for (int j = 0; j < n - i + 1; j++) {
            zeros.append("0");
        }

        // Add x_i if i is within the bounds of binaryX
        String x_i = (i <= n) ? Integer.toString(binaryX[i - 1]) : "";

        // Concatenate k, i, prefix, zeros, and x_i
        String input = k + "," + i + "," + prefix + zeros + x_i;

        // Generate the hash using SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        // Convert the hash to a hexadecimal string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
	
	public static String hashFunctionPlus(String k, int i, int[] binaryX) throws NoSuchAlgorithmException {
        // Extract the first (i - 1) bits of x_1...x_n
        StringBuilder prefixBuilder = new StringBuilder("");
        for (int index = 0; index < Math.min(i - 1, binaryX.length); index++) {
            prefixBuilder.append(binaryX[index]);
        }
        String prefix = prefixBuilder.toString();

        // Create a string of zeros of length (n - i + 1)
        int n = binaryX.length;
        StringBuilder zeros = new StringBuilder();
        for (int j = 0; j < n - i + 1; j++) {
            zeros.append("0");
        }

        // Add x_i if i is within the bounds of binaryX
        String x_i = (i < n) ? Integer.toString(binaryX[i - 1]+1) : "";

        // Concatenate k, i, prefix, zeros, and x_i
        String input = k + "," + i + "," + prefix + zeros + x_i;

        // Generate the hash using SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        // Convert the hash to a hexadecimal string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
