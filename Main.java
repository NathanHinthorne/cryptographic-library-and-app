import java.util.Scanner;

public class Main {

    private static final SHA3SHAKE lib = new SHA3SHAKE();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean continueProgram = true;
        while (continueProgram) {
            System.out.println("|==============================================|");
            System.out.println("|   Welcome to Sha-3/SHAKE Cryptographic App.  |");
            System.out.println("|==============================================|\n");
            System.out.println("Please select a function: \n");
            String functionName = scanner.nextLine();
            System.out.println("You choose: " + functionName);

            System.out.println("\nDo you want to continue? (y/n)");
            String continueProgramString = scanner.nextLine();
            if (continueProgramString.equals("n")) {
                System.out.println("Goodbye!");
                continueProgram = false;
            }
            System.out.println();
        }
    }

}
