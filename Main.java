import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Scanner;

public class Main {
    /**
     * A cryptographically secure random number generator.
     */
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Read a hex value from a string into a byte array.
     * 
     * @param s the string containing the hex value
     * @return the hex value as a byte array
     */
    private static byte[] parseHexString(String s) {
        byte[] result = new byte[s.length() / 2];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }
        return result;
    }

    /**
     * Compute the hash of the input data using SHA-3.
     */
    private static void computeHash(String inputPath, String outPath, int securityLevel) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(inputPath));
                PrintWriter writer = new PrintWriter(new File(outPath))) {

            // byte[] data = parseHexString(scanner.useDelimiter("\Z").next());
            byte[] data = scanner.useDelimiter("\\Z").next().getBytes();
            byte[] hash = SHA3SHAKE.SHA3(securityLevel, data, null);

            for (byte b : hash) {
                writer.printf("%x", b);
            }
        }
    }

    /**
     * Compute the MAC of the input data using SHA-3.
     */
    private static void computeMAC(String inputPath, String outPath, int securityLevel,
            String passphrase, int macLength) throws FileNotFoundException {
        try (Scanner dataScanner = new Scanner(new File(inputPath));
                PrintWriter writer = new PrintWriter(new File(outPath))) {

            // byte[] passphrase = passScanner.useDelimiter("\Z").next().getBytes();
            byte[] passphraseBytes = passphrase.getBytes();
            byte[] data = dataScanner.useDelimiter("\\Z").next().getBytes();

            SHA3SHAKE sponge = new SHA3SHAKE();
            sponge.init(securityLevel);
            sponge.absorb(passphraseBytes);
            sponge.absorb(data);
            sponge.absorb("T".getBytes());
            byte[] result = sponge.squeeze(macLength);

            for (byte b : result) {
                writer.printf("%x", b);
            }
        }
    }

    /**
     * Encrypt the input data using XOR with a key derived from the passphrase.
     * 
     * @throws IOException if an I/O error occurs
     */
    private static void encrypt(String inputPath, String outPath,
            String passphrase) throws IOException {

        // reads the raw bytes directly, preserving the exact data without any text
        // interpretation.
        try (FileInputStream fileInput = new FileInputStream(inputPath);
                FileOutputStream fileOutput = new FileOutputStream(outPath)) {

            byte[] passphraseBytes = passphrase.getBytes();
            byte[] data = fileInput.readAllBytes();

            // debug
            System.out.println("input data (as text): ");
            for (byte b : data) {
                char c = (char) b;
                System.out.printf("%c", c);
            }
            System.out.println();
            System.out.println();

            System.out.println("input data: ");
            for (byte b : data) {
                System.out.printf("%x", b);
            }
            System.out.println();
            System.out.println();

            byte[] key = SHA3SHAKE.SHAKE(128, passphraseBytes, 128, null);
            byte[] nonce = new byte[16];
            RANDOM.nextBytes(nonce);

            SHA3SHAKE sponge = new SHA3SHAKE();
            sponge.init(128);
            sponge.absorb(nonce);
            sponge.absorb(key);

            byte[] mask = sponge.squeeze(data.length);
            for (int i = 0; i < data.length; i++) {
                data[i] ^= mask[i];
            }

            fileOutput.write(nonce);
            fileOutput.write(data);
        }
    }

    /**
     * Decrypt the input ciphertext using XOR with a key derived from the
     * passphrase.
     * 
     * @throws IOException
     */
    private static void decrypt(String inputPath, String outPath,
            String passphrase) throws IOException {

        try (FileInputStream fileInput = new FileInputStream(inputPath);
                FileOutputStream fileOutput = new FileOutputStream(outPath)) {

            byte[] passphaseBytes = passphrase.getBytes();
            byte[] nonce = fileInput.readNBytes(16);
            byte[] ciphertext = fileInput.readAllBytes();

            byte[] key = SHA3SHAKE.SHAKE(128, passphaseBytes, 128, null);

            SHA3SHAKE sponge = new SHA3SHAKE();
            sponge.init(128);
            sponge.absorb(nonce);
            sponge.absorb(key);

            byte[] mask = sponge.squeeze(ciphertext.length);
            for (int i = 0; i < ciphertext.length; i++) {
                ciphertext[i] ^= mask[i];
            }

            fileOutput.write(ciphertext);
        }
    }

    public static void main(String[] args) throws IOException {
        String service = args[0];

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--help":
                    switch (service) {
                        case "hash":
                            System.out.println(
                                "Usage: \n\t java Main hash <input_file> <output_file> <security_level> [options]\n\n"
                                + "Description: \n"
                                + "\tHash the provided message.\n"
                                + "\nArguments: \n"
                                + "\tinput_file: Path to the input file.\n"
                                + "\toutput_file: Path to the output file.\n"
                                + "\tsecurity_level: One of 224, 256, 384, or 512. \n"
                                + "\nOptions: \n"
                                + "\t--help: Show help.\n"
                            );
                            return;
                        case "mac":
                            System.out.println(
                                "Usage: \n\t java Main mac <input_file> <output_file> <passphrase> <security_level> <mac_length> [options]\n\n"
                                + "Description: \n"
                                + "\tCompute a MAC for the provided message.\n"
                                + "\nArguments: \n"
                                + "\tinput_file: Path to the input file.\n"
                                + "\toutput_file: Path to the output file. \n"
                                + "\tpassphrase: Passphrase to compute the MAC with. \n"
                                + "\tsecurity_level: One of 224, 256, 384, or 512. \n"
                                + "\tmac_length: Length of computed MAC; must be > 0.\n"
                                + "\nOptions: \n"
                                + "\t--help: Show help.\n"
                            );
                            return;
                        case "encrypt":
                            System.out.println(
                                "Usage: \n\t java Main encrypt <input_file> <output_file> <passphrase> [options]\n\n"
                                + "Description: \n"
                                + "\tEncrypt the provided message.\n"
                                + "\nArguments: \n"
                                + "\tinput_file: Path to the input file.\n"
                                + "\toutput_file: Path to the output file. \n"
                                + "\tpassphrase: Passphrase to encrypt with. \n"
                                + "\nOptions: \n"
                                + "\t--help: Show help.\n"
                            );
                            return;
                        case "decrypt":
                            System.out.println(
                                "Usage: \n\t java Main decrypt <input_file> <output_file> <passphrase> [options]\n\n"
                                + "Description: \n"
                                + "\tDecrypt the provided cryptogram.\n"
                                + "\nArguments: \n"
                                + "\tinput_file: Path to the input file/cryptogram.\n"
                                + "\toutput_file: Path to the output file. \n"
                                + "\tpassphrase: Passphrase to decrypt with. \n"
                                + "\nOptions: \n"
                                + "\t--help: Show help.\n"
                            );
                            return;
                        default: 
                            System.out.println(
                                "Usage: \n\t java Main <command> [options]\n\n"
                                + "Commands: \n"
                                + "\thash: Compute the hash of a message.\n"
                                + "\tmac: Compute the MAC for a message.\n"
                                + "\tencrypt: Encrypt a message under a passphrase.\n"
                                + "\tdecrypt: Decrypt a message under a passphrase.\n"
                                + "\nGeneral Options: \n"
                                + "\t--help: Show help.\n"
                            );
                            return;
                    }   
            }
        }

        if (!isValidService(service)) {
            System.out.println("Invalid service: \"" + service +
                    "\". Must be one of hash, mac, encrypt, or decrypt.");
            return;
        }
        
        if (args.length < 3) {
            System.out.println("Insufficient arguments provided.");
            return;
        }

        String inPath = args[1];
        String outPath = args[2];

        try {
            if (service.equals("hash")) {
                if (args.length != 4) {
                    System.out.println("Usage: hash <input_file> <output_file> <security_level>");
                    return;
                }
                int securityLevel = Integer.parseInt(args[3]);
                if (!isValidSecurityLevel(securityLevel)) {
                    System.out.println("Invalid security level: \"" + securityLevel
                            + "\". Must be one of one of 224, 256, 384, or 512.");
                    return;
                }
                computeHash(inPath, outPath, securityLevel);
            } else if (service.equals("mac")) {
                if (args.length != 6) {
                    System.out.println(
                            "Usage: mac <input_file> <output_file> <passphrase> <security_level> <mac_length> <mac_length>");
                    return;
                }
                int securityLevel = Integer.parseInt(args[4]);
                if (!isValidSecurityLevel(securityLevel)) {
                    System.out.println("Invalid security level: \"" + securityLevel
                            + "\". Must be one of one of 224, 256, 384, or 512.");
                    return;
                }

                int macLength = Integer.parseInt(args[5]);
                if (macLength <= 0) { // || macLength > ???
                    System.out.println("MAC length must be greater than zero.");
                    return;
                }

                computeMAC(inPath, outPath, securityLevel, args[3], macLength);
            } else if (service.equals("encrypt")) {
                if (args.length != 4) {
                    System.out.println("Usage: encrypt <input_file> <output_file> <passphrase>");
                    return;
                }
                encrypt(inPath, outPath, args[3]);
            } else if (service.equals("decrypt")) {
                if (args.length != 4) {
                    System.out.println("Usage: decrypt <input_file> <output_file> <passphrase>");
                    return;
                }
                decrypt(inPath, outPath, args[3]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format: " + e.getMessage());
        }
    }

    private static boolean isValidService(String service) {
        return (service.equals("hash") || service.equals("mac") ||
                service.equals("encrypt") || service.equals("decrypt"));
    }

    private static boolean isValidSecurityLevel(int securityLevel) {
        return (securityLevel == 224) || (securityLevel == 256) ||
                (securityLevel == 384) || (securityLevel == 512);
    }
}