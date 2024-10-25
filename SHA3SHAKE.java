import java.nio.ByteBuffer;
import java.security.SecureRandom;

/**
 * The SHA3SHAKE class will enable users to securely hash data, extract hash
 * values, and customize the hashing process according to their specific
 * requirements.
 * 
 * Note: This implementaton of SHA3SHAKE uses booleans to represent bits. This
 * makes the code easier to read and understand, but results in slower
 * performance.
 */
public class SHA3SHAKE {

    /**
     * A cryptographically secure random number generator.
     */
    private final SecureRandom random = new SecureRandom();

    // DATA STRUCTURES AND PARAMETERS

    /**
     * The state matrix in KECCAK is a 1600-bit (5x5x64) matrix that serves as the
     * core structure for the algorithm's operations. It is used to store
     * intermediate values during the absorbing (input) and squeezing (output)
     * phases and undergoes multiple permutations to ensure security.
     * 
     * The information in it is converted from a byte string.
     */
    private long[][] stateMatrix = new long[5][5];

    /**
     * The length of the z axis long the state matrix (aka the length of a lane).
     */
    private int w;

    /**
     * The capacity of a KECCAK-p permutation in bits.
     */
    private int c;

    /**
     * The rate of a KECCAK-p permutation in bits.
     */
    private int r;

    /**
     * The length of the digest of a hash function or the requested length of the
     * output of an XOF, in bits.
     */
    private int d;

    /**
     * The input string to a SHA-3 hash or XOF function.
     */
    private String M = "";

    /**
     * The input string of the sponge.
     */
    private String N = "";

    /**
     * [add description for P]
     */
    private String P = "";

    public SHA3SHAKE() {
    }

    /**
     * REQUIRED:
     * Initialize the SHA-3/SHAKE sponge.
     * The suffix must be one of 224, 256, 384, or 512 for SHA-3, or one of 128 or
     * 256 for SHAKE.
     * 
     * @param suffix SHA-3/SHAKE suffix (SHA-3 digest bitlength = suffix, SHAKE sec
     *               level = suffix)
     */
    public void init(int suffix) {

        stateMatrix = new long[][] {
                { 0x0000000000000000L,
                        0x0000000000000000L,
                        0x0000000000000000L,
                        0x0000000000000000L,
                        0x0000000000000000L },

                { 0x0000000000000000L,
                        0x0000000000000000L,
                        0x0000000000000000L,
                        0x0000000000000000L,
                        0x0000000000000000L },

                { 0x0000000000000000L,
                        0x0000000000000000L,
                        0x0000000000000000L,
                        0x0000000000000000L,
                        0x0000000000000000L },

                { 0x0000000000000000L,
                        0x0000000000000000L,
                        0x0000000000000000L,
                        0x0000000000000000L,
                        0x0000000000000000L },

                { 0x0000000000000000L,
                        0x0000000000000000L,
                        0x0000000000000000L,
                        0x0000000000000000L,
                        0x0000000000000000L }
        };

        // Do the sponge construction algorithm here.

        /*
         * Steps:
         * 
         * 1. Let P=N || pad(r, len(N)).
         * 
         * 2. Let n=len(P)/r.
         * 
         * 3. Let c=b-r.
         * 
         * 4. Split P into n blocks of length r.
         * 
         * 5. Let S=0^b.
         * 
         * 6. For i=0 to n-1: S=f(S ⊕ (P[i] ⊕ 0^c)).
         * 
         * 7. Let Z be the empty string.
         * 
         * 8. Let Z=Z || Trunc_r(S).
         * 
         * 9. If d≤|Z|, then return Trunc_d(Z); else continue.
         * 
         * Let S=f(S), and continue with Step 8.
         */
    }

    /*
     * ------------------- Absorbing Phase -------------------
     * 
     * The input message is divided into blocks (based on the rate r).
     * 
     * Each block is XORed with the first r bits of the state matrix (the rest of
     * the
     * state, c bits, is untouched during this phase). This is done to inject the
     * input data into the state, effectively combining the message with the current
     * internal state through XOR.
     * 
     * After XORing, the entire state matrix undergoes a permutation process that
     * shuffles the bits around in a complex but deterministic way.
     */

    /**
     * REQUIRED:
     * Update the SHAKE sponge with a byte-oriented data chunk.
     *
     * @param data byte-oriented data buffer
     * @param pos  initial index to hash from
     * @param len  byte count on the buffer
     */
    public void absorb(byte[] data, int pos, int len) {
        /* … */
    }

    /**
     * REQUIRED:
     * Update the SHAKE sponge with a byte-oriented data chunk.
     *
     * @param data byte-oriented data buffer
     * @param len  byte count on the buffer (starting at index 0)
     */
    public void absorb(byte[] data, int len) {
        /* … */
    }

    /**
     * REQUIRED:
     * Update the SHAKE sponge with a byte-oriented data chunk.
     *
     * @param data byte-oriented data buffer
     */
    public void absorb(byte[] data) {
        /* … */
    }

    /*
     * ------------------- Squeezing Phase -------------------
     * 
     * Once all the input data is absorbed, the squeezing phase starts.
     * 
     * In this phase, the algorithm takes the first r bits from the state matrix as
     * part of the output.
     * 
     * The permutation function is applied again to the state, and another r bits
     * are taken.
     * 
     * This process continues until the desired output length (hash size) is
     * reached.
     */

    /**
     * REQUIRED:
     * Squeeze a chunk of hashed bytes from the sponge.
     * Call this method as many times as needed to extract the total desired number
     * of bytes.
     *
     * @param out hash value buffer
     * @param len desired number of squeezed bytes
     * @return the val buffer containing the desired hash value
     */
    public byte[] squeeze(byte[] out, int len) {
        /* … */

        return new byte[] { 0 };
    }

    /**
     * REQUIRED:
     * Squeeze a chunk of hashed bytes from the sponge.
     * Call this method as many times as needed to extract the total desired number
     * of bytes.
     *
     * @param len desired number of squeezed bytes
     * @return newly allocated buffer containing the desired hash value
     */
    public byte[] squeeze(int len) {
        /* … */

        return new byte[] { 0 };
    }

    /*
     * ------------------- Digesting -------------------
     * 
     * Digesting is the final step of the algorithm.
     * It will use the squeezing phase to extract the final hash value.
     */

    /**
     * REQUIRED:
     * Squeeze a whole SHA-3 digest of hashed bytes from the sponge.
     *
     * @param out hash value buffer
     * @return the val buffer containing the desired hash value
     */
    public byte[] digest(byte[] out) {
        /* … */
        return new byte[] { 0 };
    }

    /**
     * REQUIRED:
     * Squeeze a whole SHA-3 digest of hashed bytes from the sponge.
     *
     * @return the desired hash value on a newly allocated byte array
     */
    public byte[] digest() {
        /* … */
        return new byte[] { 0 };
    }

    // helper functions

    /**
     * Flatten a 5x5 matrix of longs into a linear byte array.
     * 
     * @param stateMatrix 2D array of longs
     * @return byte array
     */
    private byte[] stateMatrixToByteString(long[][] stateMatrix) {
        byte[] byteString = new byte[200]; // Need 1,600 bits. 8 bits per byte.

        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        int byteStringIndex = 0;
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                long lane = stateMatrix[x][y];

                // Reset the buffer's position to zero before putting a new long value
                buffer.clear();
                buffer.putLong(lane);
                buffer.flip(); // Prepare the buffer for reading

                // Copy the bytes from the buffer to the byteString array
                byte[] destArray = new byte[Long.BYTES];
                buffer.get(destArray);
                System.arraycopy(destArray, 0, byteString, byteStringIndex, Long.BYTES);
                byteStringIndex += Long.BYTES;
            }
        }

        return byteString;
    }

    private long[][] byteStringToStateMatrix(byte[] byteString) {
        long[][] stateMatrix = new long[5][5];

        // real formula is: A[x, y, z] = S[w(5y + x) + z]

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                for (int z = 0; z < 8; z++) {
                    stateMatrix[x][y] = stateMatrix[x][y] << 8 ^ byteString[8 * (5 * y + x) + z];
                }
            }
        }

        return stateMatrix;
    }

    public void printStateMatrix() {
        // Print the state matrix for debugging purposes
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                // convert each long into hex to visualize better
                // System.out.println("stateMatrix[" + i + "][" + j + "] = " +
                // Long.toHexString(stateMatrix[i][j]));

                // %016X is used in String.format to format the long value as a hexadecimal
                // string with leading zeros to ensure it is represented as one long.
                System.out.println("stateMatrix[" + i + "][" + j + "] = " + String.format("%016X", stateMatrix[i][j]));
            }
        }
    }

    /**
     * Create a padding which will be appended to the end of a message. The padding
     * starts and ends with a 1 bit. The middle is composed of 0 bits.
     * 
     * @param x The rate
     * @param m ???
     * @return The padded plain-text message in byte array
     */
    private byte[] pad(int x, int m) {
        int j = (-m - 2) % x;

        byte[] P = new byte[j + 2]; // is this size okay?

        P[0] = (char) 1; // 1 bit to start

        // put j amount of 0 bits between the 1 bits
        for (int i = 1; i < j + 1; i++) {
            P[i] = (char) 0;
        }

        P[j + 1] = (char) 1; // 1 bit to end

        return P;
    }

    /**
     * Circular Right Shift. Preforms a circular right shift on a long value, while
     * preserving the least significant bit and moving it to the most significant
     * bit position.
     * 
     * @param value The long value to be shifted
     * @return The shifted long value
     */
    private long circularRightShift(long value, int shiftAmount) {
        long lane = value;
        for (int i = 0; i < shiftAmount; i++) {
            // Mask to isolate the least significant bit
            long lastBit = lane & 1L;
            // System.out.println("last bit: " + Long.toBinaryString(lastBit) + " at i #" +
            // i);

            // Shift right by 1
            long remainingBits = lane >>> 1;
            // System.out.println("remaining bits: " + Long.toBinaryString(remainingBits) +
            // " at i #" + i);

            // Move the LSB to the MSB position and OR it with the shifted result
            lane = remainingBits | (lastBit << 63);
            // System.out.println("result: " + Long.toBinaryString(lane) + " at i #" + i);
        }

        return lane;
    }

    private long circularLeftShift(long value, int shiftAmount) {
        return circularRightShift(value, 64 - shiftAmount);
    }

    /**
     * Theta (θ) - Diffusion Step.
     * 
     * Functionality: Provides mixing between all bits in the state,
     * creating diffusion across the rows and columns. It applies a parity check
     * across columns of the 5x5 matrix of slices in the state.
     * 
     * Effect: Ensures that each bit is affected by the bits of every column,
     * propagating local changes across the entire state.
     * 
     * @param stateMatrix 3D matrix of bits
     * @return 3D matrix of bits
     */
    public void stepMapTheta() {

        long[] resultLaneC = new long[5]; // lanes for each x
        long[] resultLaneD = new long[5]; // lanes for each x

        for (int x = 0; x < 5; x++) {
            // Step 1: XOR every bit in a column
            for (int y = 0; y < 5; y++) {
                resultLaneC[x] ^= stateMatrix[x][y];
            }
        }

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {

                // NOTE: zero indexed arrays, so instead of (x-1) % 5, we use (x+4) % 5

                // Step 2: XOR neighboring columns (x-1, z) and (x+1, z-1)
                long neighborLane1 = resultLaneC[(x + 4) % 5];

                // System.out.println("neighbor lane 1: " + Long.toBinaryString(neighborLane1));
                // System.out.println("neighbor lane 2: " + Long.toBinaryString(resultLaneC[(x +
                // 1) % 5]));

                long neighborLane2 = circularRightShift(resultLaneC[(x + 1) % 5], 1);
                // System.out.println("neighbor lane 2 (bitshifted): " +
                // Long.toBinaryString(neighborLane2) + "\n");

                resultLaneD[x] = neighborLane1 ^ neighborLane2;

                // Step 3: XOR each bit with resultLaneD
                stateMatrix[x][y] = stateMatrix[x][y] ^ resultLaneD[x];
            }
        }
    }

    /**
     * Rho (ρ) - Bitwise Rotation.
     * 
     * Functionality: Rotates the bits of each lane (individual segments of the
     * state matrix) by a position-dependent number of steps.
     * 
     * Effect: Provides non-linearity by rotating bits in different ways
     * for each lane.
     */
    public void stepMapRho() {
        // Step 1: Keep A'[0, 0] the same as A[0, 0]
        // stateMatrix[0][0] = stateMatrix[0][0];

        // Step 2: Initialize (x, y) to (1, 0)
        int x = 1;
        int y = 0;

        // Step 3: Perform rotation 24 times
        for (int t = 0; t < 24; t++) {
            int offset = ((t + 1) * (t + 2)) / 2; // Calculate offset

            // System.out.println("offset: " + offset);

            // System.out.println("stateMatrix[" + x + "][" + y + "] = " +
            // Long.toBinaryString(stateMatrix[x][y]));

            // "Rotate" the bits by bitshifting
            stateMatrix[x][y] = circularRightShift(stateMatrix[x][y], offset);

            // System.out.println(
            // "newStateMatrix[" + x + "][" + y + "] = " +
            // Long.toBinaryString(newStateMatrix[x][y]) + "\n");

            // Update (x, y) as per the given rule
            int newX = y;
            int newY = (2 * x + 3 * y) % 5;
            x = newX;
            y = newY;
        }
    }

    /**
     * Pi (π) - Transposition (Permutation).
     * 
     * Functionality: Rearranges the positions of the bits within the
     * 3D state matrix.
     * 
     * Effect: Ensures that bits are mixed across different lanes.
     */
    public void stepMapPi() {
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                stateMatrix[x][y] = stateMatrix[(x + 3 * y) % 5][x];
            }
        }
    }

    /**
     * Chi (χ) - Nonlinear Mixing.
     * 
     * Functionality: XORs each bit with a combination of other bits in the same
     * row.
     * 
     * Effect: Introduces non-linearity, which is critical for creating a
     * secure cryptographic transformation that resists linear attacks.
     */
    public void stepMapChi() {
        // operation is done BY ROW using the logic gates given in the paper

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                // A′[x, y, z] = A[x, y, z] ⊕ ((A[(x+1) mod 5, y, z] ⊕ 1) ⋅ A[(x+2) mod 5, y,
                // z]).
                stateMatrix[x][y] = stateMatrix[x][y]
                        ^ ((stateMatrix[(x + 1) % 5][y] ^ 1) & stateMatrix[(x + 2) % 5][y]);
            }
        }
    }

    /**
     * Iota (ι) - Round Constant Addition.
     * 
     * Functionality: Injects a round-dependent constant into the state to
     * break symmetry and ensure that each round is different.
     * 
     * Effect: Ensures that the permutations applied in each round differ,
     * preventing any symmetry or structure from weakening the hash function.
     */
    public void stepMapIota() {
        // adds asymmetric, round specific CONSTANTS to the (0,0) lane

        // FOR TRAE AND TRAE ALONE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // ¯\_(ツ)_/¯ ¯\_(ツ)_/¯ ¯\_(ツ)_/¯ ¯\_(ツ)_/¯ ¯\_(ツ)_/¯

        // P.S. please finish this soon Trae :/

        long RC = 0L;
        for (int j = 0; j <= l; j++) {
            // call roundConstant somewhere in here
        }
    }

    private boolean roundConstant(int t) {
        if (t % 255 == 0) {
            return true;
        }

        // bitmask for R[0]
        // bitmask for R[4]
        // bitmask for R[5]
        // bitmask for R[6]
        // bitmask for R[8]

        byte R = (byte) 0b10000000;
        for (int i = 1; i <= t % 255; i++) {
            // prepend a zero to the right of R (left shift)
            R = (byte) (R << 1);

            R ^= ((R & 0b10000000) >> 7); // XOR MSB to R[0]
            R ^= ((R & 0b10000000) >> 7) << 4; // XOR MSB to R[4]
            R ^= ((R & 0b10000000) >> 7) << 5; // XOR MSB to R[5]
            R ^= ((R & 0b10000000) >> 7) << 6; // XOR MSB to R[6]
        }

        boolean hasZero = (R & 0b10000000) == 0;

        return hasZero;
    }

    public void keccak() {
        // call all 5 step mapping functions

    }

    /*
     * ----------------------------------------------------------
     * Utility methods
     * These methods are "shortcuts" to the main steps of the Keccak algorithm.
     * Most of their state is hardcoded.
     * ----------------------------------------------------------
     */

    /**
     * REQUIRED:
     * Compute the streamlined SHA-3-<224,256,384,512> on input X.
     *
     * @param suffix desired output length in bits (one of 224, 256, 384, 512)
     * @param X      data to be hashed
     * @param out    hash value buffer (if null, this method allocates it with the
     *               required size)
     * @return the out buffer containing the desired hash value.
     */
    public static byte[] SHA3(int suffix, byte[] X, byte[] out) {
        /*
         * SHA-3: Produces a fixed output length. It is a standard cryptographic hash
         * function with variants such as SHA3-224, SHA3-256, SHA3-384, and SHA3-512,
         * each providing outputs of exactly 224, 256, 384, and 512 bits, respectively.
         */

        // This utility method will act like a controller.

        // We'll need to create a new instance of SHA3SHAKE with specific
        // parameters here.
        SHA3SHAKE sha3 = new SHA3SHAKE();

        return new byte[] { 0 };
    }

    /**
     * REQUIRED:
     * Compute the streamlined SHAKE-<128,256> on input X with output bitlength L.
     *
     * @param suffix desired security level (either 128 or 256)
     * @param X      data to be hashed
     * @param L      desired output length in bits (must be a multiple of 8)
     * @param out    hash value buffer (if null, this method allocates it with the
     *               required size)
     * @return the out buffer containing the desired hash value.
     */
    public static byte[] SHAKE(int suffix, byte[] X, int L, byte[] out) {
        /*
         * SHAKE (Secure Hash Algorithm Keccak): An extendable-output function (XOF),
         * which means the output length can be set to any desired size. SHAKE is
         * suitable when you need a variable-length hash or a longer output for
         * applications such as key generation or padding.
         */

        // This utility method will act like a controller.

        // We'll need to create a new instance of SHA3SHAKE with specific
        // parameters here.
        SHA3SHAKE shake = new SHA3SHAKE();

        return new byte[] { 0 };
    }

}