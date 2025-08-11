import java.util.Random;

public class RandomString {

    public static String generateRandomString(int length) {
        // 사용할 문자 집합 (알파벳 대문자, 소문자, 숫자 포함)
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        // 지정된 길이만큼 랜덤 문자 추가
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(index));
        }

        return stringBuilder.toString();
    }
    /*
    public static void main(String[] args) {
        int length = 10; // 랜덤 문자열 길이
        String randomString = generateRandomString(length);
        System.out.println("Generated Random String: " + randomString);
    }
    */
}
