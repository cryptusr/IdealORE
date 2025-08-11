import java.util.Random;

public class RandomString {

    public static String generateRandomString(int length) {
        // ����� ���� ���� (���ĺ� �빮��, �ҹ���, ���� ����)
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        // ������ ���̸�ŭ ���� ���� �߰�
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(index));
        }

        return stringBuilder.toString();
    }
    /*
    public static void main(String[] args) {
        int length = 10; // ���� ���ڿ� ����
        String randomString = generateRandomString(length);
        System.out.println("Generated Random String: " + randomString);
    }
    */
}
