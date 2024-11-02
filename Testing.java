
public class Testing {

        public static void main(String[] args) {
                System.out.println("TESTING SHA3SHAKE.java\n");

                SHA3SHAKE sha3shake = new SHA3SHAKE();
                sha3shake.init(256);

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

                byte[] message = "Hello, World!".getBytes();
                sha3shake.absorb(message);

                System.out.println("message being absorbed:");
                for (byte b : message) {
                        System.out.printf("%02X ", b);
                }

                System.out.println("\n\nsponge's new input array:");
                for (byte b : sha3shake.input) {
                        System.out.printf("%02X ", b);
                }

                byte[] message2 = "Goodbye, Cruel World...".getBytes();
                sha3shake.absorb(message2); // Absorb the second message

                System.out.println("\n\nmessage being absorbed:");
                for (byte b : message2) {
                        System.out.printf("%02X ", b);
                }

                System.out.println("\n\nsponge's new input array:");
                for (byte b : sha3shake.input) {
                        System.out.printf("%02X ", b);
                }

        }
}
