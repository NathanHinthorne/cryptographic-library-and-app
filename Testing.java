
public class Testing {

        public static void main(String[] args) {
                System.out.println("TESTING SHA3SHAKE.java\n");

                SHA3SHAKE sha3shake = new SHA3SHAKE();
                sha3shake.init(256);

                System.out.println("--ORIGINAL STATE MATRIX--");
                sha3shake.printStateMatrix();

                System.out.println("\n--Theta Step Mapping Result--");
                sha3shake.stepMapTheta();
                sha3shake.printStateMatrix();

                System.out.println("\n--Rho Step Mapping Result--");
                sha3shake.stepMapRho();
                sha3shake.printStateMatrix();

                System.out.println("\n--Pi Step Mapping Result--");
                sha3shake.stepMapPi();
                sha3shake.printStateMatrix();

                System.out.println("\n--Chi Step Mapping Result--");
                sha3shake.stepMapChi();
                sha3shake.printStateMatrix();

                System.out.println("\n--Iota Step Mapping Result--");
                sha3shake.stepMapIota();
                sha3shake.printStateMatrix();
        }
}
