import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VectorTest {
    static class TestCase {
        int length;
        String message;
        String expectedMD;

        TestCase(int length, String message, String expectedMD) {
            this.length = length;
            this.message = message;
            this.expectedMD = expectedMD;
        }
    }

    public static void main(String[] args) {
        System.out.println("SHA3-224 Test Vector Validation\n");

        List<TestCase> testCases = new ArrayList<>();
        String filename = "SHA3_224ShortMsg.txt"; // CHANGE THIS
        String filepath = "sha-3bytetestvectors/" + filename;

        // Parse test vectors
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            TestCase currentTest = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Skip comments and empty lines
                if (line.startsWith("#") || line.isEmpty()) {
                    continue;
                }

                if (line.startsWith("Len = ")) {
                    // Start of new test case
                    if (currentTest != null) {
                        testCases.add(currentTest);
                    }
                    int length = Integer.parseInt(line.substring(6));
                    currentTest = new TestCase(length, null, null);
                } else if (line.startsWith("Msg = ")) {
                    if (currentTest != null) {
                        currentTest.message = line.substring(6);
                    }
                } else if (line.startsWith("MD = ")) {
                    if (currentTest != null) {
                        currentTest.expectedMD = line.substring(5);
                    }
                }
            }
            // Add the last test case
            if (currentTest != null) {
                testCases.add(currentTest);
            }
        } catch (IOException e) {
            System.err.println("Error reading test vector file: " + e.getMessage());
            return;
        }

        // Run tests
        int passCount = 0;
        int totalTests = testCases.size();

        for (int i = 0; i < testCases.size(); i++) {
            TestCase test = testCases.get(i);
            System.out.println("\nTest Case " + (i + 1));
            System.out.println("Input length: " + test.length + " bits");
            System.out.println("Input message: " + test.message);

            // Convert hex string to byte array
            byte[] message = hexStringToByteArray(test.message);

            System.out.print("Input bytes: ");
            for (byte b : message) {
                System.out.printf("%02X ", b);
            }
            System.out.println();

            // Compute SHA3-224
            byte[] output = SHA3SHAKE.SHA3(224, message, null);

            // Convert output to hex string
            String hexResult = bytesToHexString(output);

            System.out.println("Expected: " + test.expectedMD);
            System.out.println("Got:      " + hexResult);

            if (hexResult.equals(test.expectedMD)) {
                System.out.println("Result: PASS");
                passCount++;
            } else {
                System.out.println("Result: FAIL");
            }
        }

        // Print summary
        System.out.println("\nTest Summary");
        System.out.println("============");
        System.out.println("Total Tests: " + totalTests);
        System.out.println("Passed: " + passCount);
        System.out.println("Failed: " + (totalTests - passCount));
        System.out.printf("Success Rate: %.2f%%\n", (passCount * 100.0 / totalTests));
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

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}