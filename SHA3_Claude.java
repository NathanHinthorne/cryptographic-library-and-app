public class SHA3_Claude {
    private static final int KECCAK_ROUNDS = 24;
    private static final long[] KECCAK_ROUND_CONSTANTS = {
            0x0000000000000001L, 0x0000000000008082L, 0x800000000000808aL,
            0x8000000080008000L, 0x000000000000808bL, 0x0000000080000001L,
            0x8000000080008081L, 0x8000000000008009L, 0x000000000000008aL,
            0x0000000000000088L, 0x0000000080008009L, 0x000000008000000aL,
            0x000000008000808bL, 0x800000000000008bL, 0x8000000000008089L,
            0x8000000000008003L, 0x8000000000008002L, 0x8000000000000080L,
            0x000000000000800aL, 0x800000008000000aL, 0x8000000080008081L,
            0x8000000080008080L, 0x0000000080000001L, 0x8000000080008008L
    };

    private static final int[] KECCAK_RHO_OFFSETS = {
            0, 1, 62, 28, 27, 36, 44, 6, 55, 20, 3, 10, 43,
            25, 39, 41, 45, 15, 21, 8, 18, 2, 61, 56, 14
    };

    // State array
    private final long[][] state = new long[5][5];
    private final int rate;
    private final int capacity;
    private final byte[] buffer;
    private int bufferPos;
    private final int digestSize;

    public SHA3_Claude(int digestSize) {
        this.digestSize = digestSize;
        this.rate = (1600 - (digestSize * 8 * 2)) / 8;
        this.capacity = 1600 - rate * 8;
        this.buffer = new byte[rate];
        this.bufferPos = 0;
    }

    public void update(byte[] input) {
        update(input, 0, input.length);
    }

    public void update(byte[] input, int offset, int length) {
        int remaining = length;
        int currentOffset = offset;

        while (remaining > 0) {
            int toBeCopied = Math.min(remaining, rate - bufferPos);
            System.arraycopy(input, currentOffset, buffer, bufferPos, toBeCopied);
            bufferPos += toBeCopied;
            currentOffset += toBeCopied;
            remaining -= toBeCopied;

            if (bufferPos == rate) {
                absorb();
                bufferPos = 0;
            }
        }
    }

    public byte[] digest() {
        // Padding
        buffer[bufferPos++] = 0x06; // SHA3 padding
        if (bufferPos == rate) {
            absorb();
            bufferPos = 0;
        }

        // Add final bit and zeros
        buffer[rate - 1] |= 0x80;
        absorb();

        // Squeeze
        byte[] result = new byte[digestSize];
        int offset = 0;
        int remaining = digestSize;

        while (remaining > 0) {
            int toExtract = Math.min(remaining, rate);
            squeeze(result, offset, toExtract);
            offset += toExtract;
            remaining -= toExtract;

            if (remaining > 0) {
                keccakF1600();
            }
        }

        reset();
        return result;
    }

    private void printBuffer() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.printf("%016X ", state[i][j]);
            }
            System.out.println();
        }
    }

    private void reset() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                state[i][j] = 0L;
            }
        }
        bufferPos = 0;
    }

    private void absorb() {
        // XOR input into state
        for (int i = 0; i < rate / 8; i++) {
            int pos = i * 8;
            long lane = 0L;
            for (int j = 0; j < Math.min(8, rate - pos); j++) {
                lane |= ((long) (buffer[pos + j] & 0xFF)) << (8 * j);
            }
            state[i % 5][i / 5] ^= lane;
        }
        keccakF1600();
    }

    private void squeeze(byte[] output, int offset, int length) {
        for (int i = 0; i < length / 8; i++) {
            long lane = state[i % 5][i / 5];
            for (int j = 0; j < 8 && (i * 8 + j) < length; j++) {
                output[offset + i * 8 + j] = (byte) (lane >>> (8 * j));
            }
        }
    }

    private void keccakF1600() {
        long[][] B = new long[5][5]; // Changed to 2D array
        long[] C = new long[5];

        for (int round = 0; round < KECCAK_ROUNDS; round++) {
            // θ step
            for (int x = 0; x < 5; x++) {
                C[x] = state[x][0] ^ state[x][1] ^ state[x][2] ^ state[x][3] ^ state[x][4];
            }

            for (int x = 0; x < 5; x++) {
                long D = C[(x + 4) % 5] ^ rotateLeft(C[(x + 1) % 5], 1);
                for (int y = 0; y < 5; y++) {
                    state[x][y] ^= D;
                }
            }
            if (round == 0) {
                System.out.println();
                System.out.println("After theta:");
                printBuffer();
                System.out.println();
            }

            // ρ and π steps
            int x = 1, y = 0;
            long current = state[x][y];
            for (int i = 0; i < 24; i++) {
                int newX = y;
                int newY = (2 * x + 3 * y) % 5;
                long temp = state[newX][newY];
                state[newX][newY] = rotateLeft(current, KECCAK_RHO_OFFSETS[i + 1]);
                current = temp;
                x = newX;
                y = newY;
            }
            if (round == 0) {
                System.out.println();
                System.out.println("After pi/rho:");
                printBuffer();
                System.out.println();
            }

            // χ step
            for (int y1 = 0; y1 < 5; y1++) {
                for (int x1 = 0; x1 < 5; x1++) {
                    B[x1][y1] = state[x1][y1];
                }
            }

            for (int x1 = 0; x1 < 5; x1++) {
                for (int y1 = 0; y1 < 5; y1++) {
                    state[x1][y1] = B[x1][y1] ^
                            (~B[(x1 + 1) % 5][y1] &
                                    B[(x1 + 2) % 5][y1]);
                }
            }
            if (round == 0) {
                System.out.println();
                System.out.println("After chi:");
                printBuffer();
                System.out.println();
            }

            // ι step
            state[0][0] ^= KECCAK_ROUND_CONSTANTS[round];
            if (round == 0) {
                System.out.println();
                System.out.println("After iota:");
                printBuffer();
                System.out.println();
            }
        }
    }

    private static long rotateLeft(long x, int n) {
        return (x << n) | (x >>> (64 - n));
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public static void main(String[] args) {
        // byte[] message = { (byte) 0xe9 };
        byte[] message = { (byte) 0x00 };
        SHA3_Claude sha3_256 = new SHA3_Claude(32); // 256 bits = 32 bytes
        sha3_256.update(message);
        byte[] hash = sha3_256.digest();
        System.out.println("SHA3-256: " + bytesToHex(hash));
    }
}