import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;

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

    /**
     * Array of round constants to be applied to Lane(0, 0), precomputed for each
     * of the 24 rounds.
     */
    private final long[] ROUND_CONSTANTS = new long[] {
            0x0000000000000001L, 0x0000000000008082L, 0x800000000000808aL,
            0x8000000080008000L, 0x000000000000808bL, 0x0000000080000001L,
            0x8000000080008081L, 0x8000000000008009L, 0x000000000000008aL,
            0x0000000000000088L, 0x0000000080008009L, 0x000000008000000aL,
            0x000000008000808bL, 0x800000000000008bL, 0x8000000000008089L,
            0x8000000000008003L, 0x8000000000008002L, 0x8000000000000080L,
            0x000000000000800aL, 0x800000008000000aL, 0x8000000080008081L,
            0x8000000000008080L, 0x0000000080000001L, 0x8000000080008008L
    };

    // DATA STRUCTURES AND PARAMETERS

    /**
     * The state matrix in KECCAK is a 1600-bit (5x5x64) matrix that serves as the
     * core structure for the algorithm's operations. It is used to store
     * intermediate values during the absorbing (input) and squeezing (output)
     * phases and undergoes multiple permutations to ensure security.
     */
    private long[][] stateMatrix;

    /**
     * The capacity of a KECCAK-p permutation in bits.
     */
    private int capacity;

    /**
     * The rate of a KECCAK-p permutation in bits.
     */
    private int rate;

    /**
     * The length of the digest of a hash function or the requested length of the
     * output of an XOF, in bits.
     */
    private int d;

    /**
     * Holds all the input data (message, keys, random samples, etc) to be used
     * later.
     */
    public byte[] input;

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
        // Validate suffix
        if (suffix != 224 && suffix != 256 && suffix != 384 && suffix != 512 // SHA-3 variants
                && suffix != 128 && suffix != 256) { // SHAKE variants
            throw new IllegalArgumentException(
                    "Invalid suffix. Must be 224, 256, 384, or 512 for SHA-3, or 128 or 256 for SHAKE");
        }

        stateMatrix = new long[][] {
                { 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L,
                        0x0000000000000000L },
                { 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L,
                        0x0000000000000000L },
                { 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L,
                        0x0000000000000000L },
                { 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L,
                        0x0000000000000000L },
                { 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L, 0x0000000000000000L,
                        0x0000000000000000L }
        };

        // For SHA-3: capacity = 2 × output length
        // For SHAKE: capacity = 2 × security level
        capacity = 2 * suffix;

        // The rate is what remains from the 1600 bits of state after subtracting
        // capacity
        rate = 1600 - capacity;

        // For SHA-3, d is the digest length (same as suffix)
        // For SHAKE, d will be set later during squeeze based on requested output
        // length
        d = suffix;

        input = new byte[0];
    }

    /*
     * ------------------- Absorbing Phase -------------------
     * 
     * The input message is divided into blocks (based on the rate r).
     * 
     * Each block is XORed with the first r bits of the state matrix (the rest of
     * the state, c bits, is untouched during this phase). This is done to inject
     * the input data into the state, effectively combining the message with the
     * current internal state through XOR.
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
        // Check if sponge is initialized
        if (rate == 0) {
            throw new IllegalStateException("Sponge must be initialized before absorbing data");
        }

        byte[] temp = new byte[input.length + len];

        // Copy old data
        for (int i = 0; i < input.length; i++) {
            temp[i] = input[i];
        }

        // Append new data
        for (int i = 0; i < len; i++) {
            temp[input.length + i] = data[pos + i];
        }

        input = temp;
    }

    /**
     * REQUIRED:
     * Update the SHAKE sponge with a byte-oriented data chunk.
     *
     * @param data byte-oriented data buffer
     * @param len  byte count on the buffer (starting at index 0)
     */
    public void absorb(byte[] data, int len) {
        absorb(data, 0, len);
    }

    /**
     * REQUIRED:
     * Update the SHAKE sponge with a byte-oriented data chunk.
     *
     * @param data byte-oriented data buffer
     */
    public void absorb(byte[] data) {
        absorb(data, 0, data.length);
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
     * Implements the pad10*1 padding scheme as specified in the SHA3 standard.
     * 
     * @param m length of input in bits
     * @return byte array containing the padding bits
     */
    private byte[] pad(int messageLength) {
        // Calculate how many bits we need to pad
        // We want the final length to be a multiple of rate
        int remainingBits = rate - (messageLength % rate);

        // We need at least 2 bits (for the starting 1 and ending 1)
        // If we only have 1 bit left, we need to add another full block
        if (remainingBits <= 1) {
            remainingBits += rate;
        }

        // Convert to bytes (rounding up)
        int paddingBytes = (remainingBits + 7) / 8;
        byte[] padding = new byte[paddingBytes];

        // Set first bit to 1 (in the first byte)
        padding[0] = (byte) 0b10000000;

        // Set last bit to 1 (in the last byte)
        padding[paddingBytes - 1] |= 0b1;

        return padding;
    }

    /**
     * Applies the padding to a message and returns the padded result.
     * 
     * @param message the original message bytes
     * @return the padded message
     */
    private byte[] applyPadding(byte[] message) {
        int messageBitLength = message.length * 8;
        byte[] padding = pad(messageBitLength);

        // Combine message and padding
        byte[] result = new byte[message.length + padding.length];
        System.arraycopy(message, 0, result, 0, message.length);
        System.arraycopy(padding, 0, result, message.length, padding.length);

        return result;
    }

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
                // %016X is used in String.format to format the long value as a hexadecimal
                // string with leading zeros to ensure it is represented as one long.
                System.out.println("stateMatrix[" + i + "][" + j + "] = " + String.format("%016X", stateMatrix[i][j]));
            }
        }
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

            // Shift right by 1
            long remainingBits = lane >>> 1;

            // Move the LSB to the MSB position and OR it with the shifted result
            lane = remainingBits | (lastBit << 63);
        }

        return lane;
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

        long[][] newStateMatrix = Arrays.copyOf(stateMatrix, stateMatrix.length);

        // operation is done BY ROW using the logic gates given in the paper
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                // A′[x, y, z] = A[x, y, z] ⊕ ((A[(x+1) mod 5, y, z] ⊕ 1) ⋅ A[(x+2) mod 5, y,
                // z]).
                newStateMatrix[x][y] = stateMatrix[x][y]
                        ^ ((stateMatrix[(x + 1) % 5][y] ^ 1) & stateMatrix[(x + 2) % 5][y]);
            }
        }

        stateMatrix = newStateMatrix;
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
    public void stepMapIota(int round) {
        // adds asymmetric, round specific CONSTANTS to the (0,0) lane

        stateMatrix[0][0] ^= ROUND_CONSTANTS[round];
    }

    public void executeRound(int round) {
        stepMapTheta();
        stepMapRho();
        stepMapPi();
        stepMapChi();
        stepMapIota(round);
    }

    public byte[] keccakP(int numRounds, byte[] byteString) { // might not need to pass byteString
        stateMatrix = byteStringToStateMatrix(byteString);

        for (int round = 0; round < numRounds; round++) {
            executeRound(round);
        }

        return stateMatrixToByteString(stateMatrix);
    }

    public byte[] keccakF(byte[] byteString) { // might not need to pass byteString
        return keccakP(24, byteString);
    }

    /*
     * ----------------------------------------------------------
     * Utility methods
     * These methods are specific implementations of the keccak algorithm
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