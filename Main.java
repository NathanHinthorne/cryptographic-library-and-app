import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Scanner;

public class Main {

    /**
     * A cryptographically secure random number generator.
     */
    private static final SecureRandom random = new SecureRandom();

    /**
     * Read a hex value from a string into a byte array.
     * 
     * @param s the string containing the hex value
     * @return the hex value as a byte array
     */
    private static byte[] parseHexString(String s) {
        byte[] result = new byte[s.length() / 2];

        for (int i = 0; i < result.length; i++) {
            System.out.println(s.substring(2 * i, 2 * i + 2));
            result[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }

        return result;
    }

    public static void main(String[] args) {
        String service = args[0];
        String outPath = args[1];

        if (!(service.equals("hash") || service.equals("mac") || service.equals("encrypt") || service.equals("decrypt"))) {
            System.out.println("Invalid service: \"" + service + "\". Must be one of hash, mac, encrypt, or decrypt.");
            return;
        }

        int secLvl = 0;
        if (service.equals("hash") || service.equals("mac")) {
            String secLvlInput = args[1];

            if (!(secLvlInput.equals("224") || secLvlInput.equals("256") || secLvlInput.equals("384") || secLvlInput.equals("512"))) {
                System.out.println("Invalid security level: \"" + secLvlInput + "\". Must be one of one of 224, 256, 384, or 512.");
                return;
            }
            
            secLvl = Integer.parseInt(secLvlInput);
        }
        
        // TODO: improve file not found error messages
        if (service.equals("hash")) {
            try (Scanner scanner = new Scanner(new File(args[3])); 
                PrintWriter writer = new PrintWriter(new File(outPath))) {

                //byte[] data = parseHexString(scanner.useDelimiter("\\Z").next());
                byte[] data = scanner.useDelimiter("\\Z").next().getBytes();
                            
                byte[] hash = SHA3SHAKE.SHA3(secLvl, data, null);
                
                for (byte b : hash) {
                    writer.printf("%x", b);
                }
            } catch (FileNotFoundException e) {
                System.out.println("Output file " + outPath + " not found.");
            }
            
        } else if (service.equals("mac")) {
            try (Scanner dataScanner = new Scanner(new File(args[3]));
                //Scanner passScanner = new Scanner(new File(args[4])); 
                PrintWriter writer = new PrintWriter(new File(outPath))) {

                //byte[] passphrase = passScanner.useDelimiter("\\Z").next().getBytes();
                byte[] passphrase = args[4].getBytes();
                byte[] data = dataScanner.useDelimiter("\\Z").next().getBytes();
                            
                SHA3SHAKE sponge = new SHA3SHAKE();
                sponge.init(secLvl);
                sponge.absorb(passphrase);
                sponge.absorb(data);
                //TODO: sponge.absorb(T);????
                //TODO: error check squeeze len
                byte[] result = sponge.squeeze(Integer.parseInt(args[5]));

                for (byte b : result) {
                    writer.printf("%x", b);
                }
            } catch (FileNotFoundException e) {
                System.out.println("Output file " + outPath + " not found.");
            }

        } else if (service.equals("encrypt")) {
            try (Scanner dataScanner = new Scanner(new File(args[2]));
                PrintWriter writer = new PrintWriter(new File(outPath))) {

                byte[] passphrase = args[3].getBytes();
                byte[] data = dataScanner.useDelimiter("\\Z").next().getBytes();
                            
                byte[] key = SHA3SHAKE.SHAKE(128, passphrase, 128, null);
                byte[] nonce = new byte[16];
                random.nextBytes(nonce);

                SHA3SHAKE sponge = new SHA3SHAKE();
                sponge.init(128);
                sponge.absorb(nonce);
                sponge.absorb(key);
        
                byte[] mask = sponge.squeeze(data.length);
                for (int i = 0; i < data.length; i++) {
                    data[i] ^= mask[i];
                }

                for (byte b : data) {
                    writer.printf("%x", b);
                }
                writer.println();
                for (byte b : nonce) {
                    writer.printf("%x", b);
                }
            } catch (FileNotFoundException e) {
                System.out.println("Output file " + outPath + " not found: " + e);
            }

        } else if (service.equals("decrypt")) {
            try (Scanner scanner = new Scanner(new File(args[2]));
                    PrintWriter writer = new PrintWriter(new File(outPath))) {

                byte[] passphrase = args[3].getBytes();
                byte[] ciphertext = parseHexString(scanner.nextLine());
                byte[] nonce = parseHexString(scanner.nextLine());

                byte[] key = SHA3SHAKE.SHAKE(128, passphrase, 128, null);

                SHA3SHAKE sponge = new SHA3SHAKE();
                sponge.init(128);
                sponge.absorb(nonce);
                sponge.absorb(key);

                byte[] mask = sponge.squeeze(ciphertext.length);
                for (int i = 0; i < ciphertext.length; i++) {
                    ciphertext[i] ^= mask[i];
                }

                writer.println(new String(ciphertext, StandardCharsets.UTF_8));
                // for (byte b : ciphertext) {
                //     writer.printf("%x", b);
                // }
            } catch (FileNotFoundException e) {
                System.out.println("Output file " + outPath + " not found: " + e);
            }
        }
    }

}
