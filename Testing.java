
public class Testing {

        public static void main(String[] args) {
                System.out.println("TESTING SHA3SHAKE.java\n");

                // SHA3SHAKE sha3shake = new SHA3SHAKE();
                // sha3shake.init(256);

                // System.out.println("\n--Theta Step Mapping Result--");
                // sha3shake.stepMapTheta();
                // sha3shake.printStateMatrix();

                // System.out.println("\n--Rho Step Mapping Result--");
                // sha3shake.stepMapRho();
                // sha3shake.printStateMatrix();

                // System.out.println("\n--Pi Step Mapping Result--");
                // sha3shake.stepMapPi();
                // sha3shake.printStateMatrix();

                // System.out.println("\n--Chi Step Mapping Result--");
                // sha3shake.stepMapChi();
                // sha3shake.printStateMatrix();

                String byteString = "e9";
                String correctByteStringOutput = "";

                System.out.println("Input Message:");
                // byte[] message = "Hello, World!".getBytes();
                byte[] message = Testing.hexStringToByteArray(byteString);
                for (byte b : message) {
                        System.out.printf("%02X ", b);
                }

                System.out.println("\n");
                byte[] out = SHA3SHAKE.SHA3(224, message, null);
                // byte[] out = SHA3SHAKE.SHAKE(128, message, 20, null);
                // String hexResult = "";
                // for (byte b : out) {
                // System.out.printf("%02X ", b);
                // hexResult += String.format("%02X", b).toLowerCase();
                // }

                // System.out.println("\n\ntest results:");
                // System.out.println("Expected: " + correctByteStringOutput);
                // System.out.println("Got: " + hexResult);
                // if (hexResult.equals(correctByteStringOutput)) {
                // System.out.println("SUCCESS");
                // } else {
                // System.out.println("FAIL");
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
