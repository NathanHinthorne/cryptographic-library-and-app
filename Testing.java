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
                { 0x1110000000000000L, 0x2220000000000000L, 0x3330000000000000L,
                        0x4440000000000000L, 0x9990000000000000L },

                { 0x1110000000000000L, 0x2220000000000000L, 0x3330000000000000L,
                        0x4440000000000000L, 0x5550000000000000L },

                { 0x1110000000000000L, 0x2220000000000000L, 0x3330000000000000L,
                        0x4440000000000000L, 0x5550000000000000L },

                { 0x1110000000000000L, 0x2220000000000000L, 0x3330000000000000L,
                        0x4440000000000000L, 0x5550000000000000L },

                { 0x1110000000000000L, 0x2220000000000000L, 0x3330000000000000L,
                        0x4440000000000000L, 0x5550000000000000L } };

        long[][] newStateMatrix = sha3shake.stepMapTheta(stateMatrix);
        assertEquals(0x1110000000000000L, newStateMatrix[0][0]);
    }

}
