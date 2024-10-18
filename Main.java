import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean continueProgram = true;
        while (continueProgram) {
            System.out.println("|==============================================|");
            System.out.println("|   Welcome to Sha-3/SHAKE Cryptographic App.  |");
            System.out.println("|==============================================|\n");
            System.out.println("Please select a function: \n");
            System.out.println("1. Custom Hashing");
            System.out.println("2. SHA-3 (224, 256, 384, 512)");
            System.out.println("3. SHAKE (128, 256)");
            String hashOption = scanner.next();
            System.out.println();

            if (hashOption.equals("1")) {
                final SHA3SHAKE customSponge = new SHA3SHAKE();

                // walk user through initialization
                System.out.println("Please enter your message:");
                String message = scanner.next();
                System.out.println();
                System.out.println("Please choose the desired output length in bits:");
                System.out.println("1. 224");
                System.out.println("2. 256");
                System.out.println("3. 384");
                System.out.println("4. 512");
                String outputLength = scanner.next();
                System.out.println();

                if (outputLength.equals("1")) {
                    // initialize the SHA-3/SHAKE sponge

                } else if (outputLength.equals("2")) {
                    // initialize the SHA-3/SHAKE sponge

                } else if (outputLength.equals("3")) {
                    // initialize the SHA-3/SHAKE sponge

                } else if (outputLength.equals("4")) {
                    // initialize the SHA-3/SHAKE sponge

                }
                // walk user through stages of absorb, squeeze, and digest
            }

            else if (hashOption.equals("2")) {
                // call utility class for SHA-3
                // byte[] hash = SHA3SHAKE.SHA3();
            }

            else if (hashOption.equals("3")) {
                // call utility class for SHAKE
                // byte[] hash = SHA3SHAKE.SHAKE();
            }

            System.out.println("\nDo you want to create another hash? (y/n)");
            String continueProgramString = scanner.next();
            System.out.println();
            if (continueProgramString.equals("n")) {
                System.out.println("Goodbye!");
                continueProgram = false;
            }
            System.out.println();
        }
    }

}
