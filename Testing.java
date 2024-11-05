
public class Testing {

        public static void main(String[] args) {

                byte[] message = { (byte) 0xe9 };

                byte[] out = SHA3SHAKE.SHA3(256, message, null);
        }

        public static byte[] hexStringToByteArray(String hexString) {
                int len = hexString.length();
                byte[] data = new byte[len / 2];
                for (int i = 0; i < len; i += 2) {
                        data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                                        + Character.digit(hexString.charAt(i + 1), 16));
                }
                return data;
        }
}
