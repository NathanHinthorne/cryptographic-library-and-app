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
     * 
     * Should it be 5 x 5 x w, where w is given later during initialization?
     */
    private final boolean[][][] stateMatrix = new boolean[5][5][64];

    /**
     * The length of the z axis long the state matrix (aka the length of a lane).
     */
    private int w;

    /**
     * The width of a KECCAK-p permutation in bits.
     */
    private int b;

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

        // Do the sponge construction algorithm here.
    }

    /*
     * Note: The ^ operator is XOR and is short for (~p ^ q) v (p ^ ~q) in logic
     * notation.
     */

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
     * Convert 3D matrix of bits to a byte array.
     * 
     * @param stateMatrix 3D matrix of bits
     * @return byte array
     */
    private byte[] convertToByteString(boolean[][][] stateMatrix) {
        return new byte[] { 0 };
    }

    private boolean[][][] convertToBooleanMatrix(byte[] byteString) {
        return new boolean[5][5][64];
    }

    private void printStateMatrix(boolean[][][] stateMatrix) {
        // Print the state matrix for debugging purposes
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print("Layer " + i + ", lane " + j + ": ");
                for (int k = 0; k < 64; k++) {
                    boolean bit = stateMatrix[i][j][k];
                    System.out.print(bit ? "1" : "0");
                    if ((k + 1) % 8 == 0) {
                        System.out.print(" ");
                    }
                    if ((j + 1) % 5 == 0) {
                        System.out.println();
                    }
                    System.out.println();
                }
                System.out.println();
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
     * Theta (θ) - Diffusion Step.
     * 
     * Functionality: Provides mixing between all bits in the state,
     * creating diffusion across the rows and columns. It applies a parity check
     * across columns of the 5x5 matrix of slices in the state.
     * 
     * Effect: Theta ensures that each bit is affected by the bits of every column,
     * propagating local changes across the entire state.
     * 
     * @param stateMatrix 3D matrix of bits
     * @return 3D matrix of bits
     */
    private boolean[][][] stepMapTheta(boolean[][][] stateMatrix) {
        boolean[][][] newStateMatrix = stateMatrix;

        // NOTE: Matrix A[x, y, z] from the paper will need to be written as A[z][y][x]
        // in java.

        boolean[][] C = new boolean[5][w];
        boolean[][] D = new boolean[5][w];

        // step 1: XOR every bit in a column -> get result C
        // find every C
        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < w; z++) {
                for (int y = 0; y < 5; y++) {
                    C[x][z] = stateMatrix[z][y][x] ^ C[x][z];
                }
            }
        }

        // step 2: for each C, XOR the C of nearby columns (x-1, z) and (x+1, z-1) with
        // each other -> get result D
        // find every D
        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < w; z++) {
                // we use % to ensure it's cyclical
                boolean c1 = C[(x - 1) % 5][z];
                boolean c2 = C[(x + 1) % 5][(z - 1) % 5];
                D[x][z] = c1 ^ c2;
            }
        }

        // step 3: for every bit in matrix, XOR it with the D of the column it's in ->
        // get result newStateMatrix
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                for (int z = 0; z < w; z++) {
                    newStateMatrix[z][y][x] = stateMatrix[z][y][x] ^ D[x][z];
                }
            }
        }

        return newStateMatrix;
    }

    /**
     * Rho (ρ) - Bitwise Rotation.
     * 
     * Functionality: Rotates the bits of each lane (individual segments of the
     * state matrix) by a position-dependent number of steps.
     * 
     * Effect: This step provides non-linearity by rotating bits in different ways
     * for each lane, contributing to the complexity of the hash.
     */
    private void stepMapRho() {
        // NOTE: Matrix A[x, y, z] from the paper will need to be written as A[z][y][x]
        // in java.
    }

    /**
     * Pi (π) - Transposition (Permutation).
     * 
     * Functionality: This step rearranges the positions of the bits within the
     * 3D state matrix.
     * 
     * Effect: Pi ensures that bits are mixed across different lanes.
     */
    private void stepMapPi() {
        // NOTE: Matrix A[x, y, z] from the paper will need to be written as A[z][y][x]
        // in java.
    }

    /**
     * Chi (χ) - Nonlinear Mixing.
     * 
     * Functionality: It introduces non-linearity into the state by XORing each bit
     * with a combination of other bits in the same row.
     * 
     * Effect: Chi introduces non-linearity, which is critical for creating a
     * secure cryptographic transformation that resists linear attacks.
     */
    private void stepMapChi() {
        // NOTE: Matrix A[x, y, z] from the paper will need to be written as A[z][y][x]
        // in java.
    }

    /**
     * Iota (ι) - Round Constant Addition.
     * 
     * Functionality: This step injects a round-dependent constant into the state to
     * break symmetry and ensure that each round is different.
     * 
     * Effect: Iota ensures that the permutations applied in each round differ,
     * preventing any symmetry or structure from weakening the hash function.
     */
    private void stepMapIota() {
        // NOTE: Matrix A[x, y, z] from the paper will need to be written as A[z][y][x]
        // in java.
    }

    /*
     * ----------------------------------------------------------
     * Utility methods
     * These methods don't rely on any instance-specific state).
     * Instead, they are "shortcuts" to the main steps of the Keccak algorithm.
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

        // We'll need to create a new instance of SHA3SHAKE here with specific
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

        // We'll need to create a new instance of SHA3SHAKE here with specific
        // parameters here.
        SHA3SHAKE shake = new SHA3SHAKE();

        return new byte[] { 0 };
    }

}