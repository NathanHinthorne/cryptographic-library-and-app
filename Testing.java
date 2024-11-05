
public class Testing {

        public static void main(String[] args) {

                byte[] message = { (byte) 0xe9 };

                byte[] out = SHA3SHAKE.SHA3(256, message, null);
        }
}
