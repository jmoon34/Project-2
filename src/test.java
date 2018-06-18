import java.util.*;
public class test {
	public static void main(String[] args){
		String s = "Hello my name is Julian123!? I think \'Modules act on rings\' but I say \"Isomorphism theorem\"";
		Scanner scanner = new Scanner(System.in);
		Encrypted msg = Encryption.MasterEncrypt(s, scanner);
		System.out.println();

		System.out.println(Decryption.MasterDecryption(msg));
		
	}
}
