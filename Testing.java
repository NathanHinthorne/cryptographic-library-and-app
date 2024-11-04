
public class Testing {

        public static void main(String[] args) {
                System.out.println("TESTING SHA3SHAKE.java\n");

                // SHA3SHAKE sha3shake = new SHA3SHAKE();

                System.out.println("Input Message:");
                // byte[] message = Testing.hexStringToByteArray(byteString);
                byte[] message = { (byte) 0xe9 };
                for (byte b : message) {
                        System.out.printf("%02X ", b);
                }

                System.out.println("\n");
                byte[] out = SHA3SHAKE.SHA3(256, message, null);
                // byte[] out = SHA3SHAKE.SHAKE(128, message, 20, null);
                // byte[] out = sha3shake.keccakF(message);
                // String hexResult = "";
                // for (byte b : out) {
                // System.out.printf("%02X ", b);
                // hexResult += String.format("%02X", b).toLowerCase();
                // }
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
