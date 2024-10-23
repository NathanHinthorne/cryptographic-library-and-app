import org.junit.jupiter.api.Test;

// all the main junit methods
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Testing {

        public static void main(String[] args) {
                System.out.println("TESTING SHA3SHAKE.java\n");

                SHA3SHAKE sha3shake = new SHA3SHAKE();
                sha3shake.init(256);
        }

        @Test
        public void stepMapThetaTest() {
                SHA3SHAKE sha3shake = new SHA3SHAKE();
                sha3shake.init(256);
                long[][] stateMatrix = new long[][] {
                                { 0x0000000000000001L, 0x0000000000000002L, 0x0000000000000003L, 0x0000000000000004L,
                                                0x0000000000000005L },

                                { 0x0000000000000006L, 0x0000000000000007L, 0x0000000000000008L, 0x0000000000000009L,
                                                0x000000000000000AL },

                                { 0x000000000000000BL, 0x000000000000000CL, 0x000000000000000DL, 0x000000000000000EL,
                                                0x000000000000000FL },

                                { 0x0000000000000010L, 0x0000000000000011L, 0x0000000000000012L, 0x0000000000000013L,
                                                0x0000000000000014L },

                                { 0x0000000000000015L, 0x0000000000000016L, 0x0000000000000017L, 0x0000000000000018L,
                                                0x0000000000000019L }
                };
                System.out.println("--ORIGINAL STATE MATRIX--");
                sha3shake.printStateMatrix(stateMatrix);

                System.out.println("\n--Theta Step Mapping Result--");
                long[][] matrixAfterTheta = sha3shake.stepMapTheta(stateMatrix);
                sha3shake.printStateMatrix(matrixAfterTheta);

                System.out.println("\n--Rho Step Mapping Result--");
                long[][] matrixAfterRho = sha3shake.stepMapRho(stateMatrix);
                sha3shake.printStateMatrix(matrixAfterRho);

                System.out.println("\n--Pi Step Mapping Result--");
                long[][] matrixAfterPi = sha3shake.stepMapPi(matrixAfterRho);
                sha3shake.printStateMatrix(matrixAfterPi);
        }

}
