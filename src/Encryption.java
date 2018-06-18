import java.util.*;
public class Encryption {
	//Method that encrypts a String according to instructions given by the second scanner parameter
	//Returns an Encrypted object that contains a decryption key and the encrypted version of the message
	//Precondition: Character in the String must be a letters from the alphabet + numbers + , . ! ? ' " and no other special characters may be used
	public static Encrypted MasterEncrypt(String message, Scanner s){
		Encrypted msg = new Encrypted();
		System.out.println("Original Message: " + message);
		//maps from String to array of characters, then prints the array
		List<Character> clist = stringtoChar(message);
		//Prompts whether the characters will be shifted
		String prompt="";
		while(!prompt.equals("y") && !prompt.equals("n")){
			System.out.print("Shift characters? 'y' for yes 'n' for no ");
			prompt = s.next();
		}
		if(prompt.equals("y")){
			System.out.print("Shift back by how many letters? ");
			while(!s.hasNextInt()){
				s.next();
				System.out.print("Enter an integer: ");
			}
			int t = s.nextInt();
			translate(clist, t); //Translates the list of characters
			msg.addKey("translated " + t); //Adds necessary tag to the decryption key
			System.out.print("Translated character array: ");
			System.out.println(clist);
		} else{
			msg.addKey("NA");
		}
		prompt = "";
		//Generates a random mapping from each character to a number, then prints it
		Map<Character, Integer> map = randomMap();
		System.out.println("Mapping: " + map);
		msg.addKey(map.toString());
		//Maps each character according to the map
		List<Integer> intList = chartoInt(clist, map);
		msg.setCode(intList);
		System.out.println("Mapped list: " + intList);
		//Prompts whether the integers will have a change in base
		while(!prompt.equals("y") && !prompt.equals("n")){
			System.out.print("Change base? 'y' for yes 'n' for no ");
			prompt = s.next();
		}
		if(prompt.equals("y")){
			System.out.print("Change to base: ");
			while(!s.hasNextInt()){
				s.next();
				System.out.print("Enter an integer: ");
			}
			int base = s.nextInt();
			toBase(intList, base); //Changes the base of the list of integers
			msg.setCode(intList); //Updates the encrypted message
			msg.addKey("toDecimal " + base); //Adds appropriate tag to the decryption key
			System.out.println("Transformed to base " + base + ": " + intList);
		} else{
			msg.addKey("NA");
		}
		prompt = "";
		//Prompts whether a random permutation will be applied on the list
		while(!prompt.equals("y") && !prompt.equals("n")){
			System.out.print("Perform permutation with a random sequence? 'y' for yes 'n' for no ");
			prompt = s.next();
		}
		if(prompt.equals("y")){
			List<Integer> sequence = randomSequence(message.length());
			System.out.println("Permutation Sequence: " + sequence);
			permute(intList, sequence); //Permutes the encrypted message according to a randomly generated permutation
			msg.setCode(intList); //Updates the encrypted message
			//Adds appropriate tag to the decryption key
			msg.addKey("Permute"); 
			msg.addKey(sequence.toString());
			System.out.println("Permuted list: " + intList);
		} else{
			msg.addKey("NA");
		}
		return msg;
	}
	
	//Permutation method that reorganizes the elements in the first parameter according to the sequence in the second parameter
	//The sequence of numbers in the second parameter is describes how the indexes of the target array is shifted.
	//E.g. If seq = [0,1,2], the 0th element of arr replaces the first, the first element replaces the second, and the second replaces the 0th.
	public static void permute(List<Integer> arr, List<Integer> seq){
		if(arr.size() != seq.size()) throw new IllegalArgumentException("ArrayList sizes don't match");
		
		int temp2 = 0;
		int temp1 = arr.get(seq.get(1));
		arr.set(seq.get(1), arr.get(seq.get(0)));
		for(int i=2;i<arr.size()-1; i+=2){
			temp2 = arr.get(seq.get(i));
			arr.set(seq.get(i), temp1);
			temp1 = arr.get(seq.get(i+1));
			arr.set(seq.get(i+1), temp2);
		}
		if(arr.size() %2 == 0){
			arr.set(seq.get(0), temp1);
		} else{
			temp2 = arr.get(seq.get(seq.size()-1));
			arr.set(seq.get(seq.size()-1), temp1);
			arr.set(seq.get(0), temp2);
		}
	}
	
	//A method that generates a random Character -> Integer map according to a randomly generated sequence.
	//The empty space character (' ') always gets mapped to 53.
	public static Map<Character, Integer> randomMap(){
		List<Integer> sequence = randomSequence(52);
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		int count = 0;
		for(int i='a';i<='z';i++){
			map.put((char)i, sequence.get(count));
			count++;
		}
		for(int i='A';i<='Z';i++){
			map.put((char)i, sequence.get(count));
			count++;
		}
		for(int i=1;i<=9;i++){
			map.put(Integer.toString(i).charAt(0), 52+i);
		}
		map.put(' ', 62);
		map.put('.', 63);
		map.put(',', 64);
		map.put('!', 65);
		map.put('?', 66);
		map.put('\'', 67);
		map.put('\"', 68);
		return map;
	}
	
	//A method that a character to an integer according to a map
	public static int chartoInt(char c, Map<Character, Integer> map){
		if(!map.containsKey(c)) throw new IllegalArgumentException("character is not in the alphabet");
		return map.get(c);
	}
	//An overloaded version of the method above, modified to take in a List of Characters as a parameter
	public static List<Integer> chartoInt(List<Character> clist, Map<Character, Integer> map){
		List<Integer> intlist = new ArrayList<Integer>();
		for(int i=0;i<clist.size();i++){
			intlist.add(chartoInt(clist.get(i), map));
		}
		return intlist;
	}
	
	//A method that generates a random non-repeating sequence of numbers with the parameter being the maximum number
	//Uses the Fisher-Yates shuffle working in O(N)
	public static List<Integer> randomSequence(int max){
		List<Integer> sequence = new ArrayList<Integer>();
		for(int i=0;i<max;i++){
			sequence.add(i);
		}
		Collections.shuffle(sequence);
		return sequence;
	}
	
	//Recursive method that changes a given integer n to a different base given by the second parameter
	public static int toBase(int n, int base){
		if(base > 10) throw new IllegalArgumentException("base cannot be greater than 10");
		if(n < base) return n;
		int num = base;
		int count = 1;
		int coeff = 1;
		while(num*base <= n){
			num *= base;
			count++;
		}
		n -= num;
		if(n >= num){
			while(n >= num){
				n -= num;
				coeff++;
			}
		}
		return coeff*(int)Math.pow(10, count) + toBase(n, base);
	}
	
	//Overloaded form of the method above modified to take Lists as a parameter
	public static void toBase(List<Integer> list, int base){
		for(int i=0;i<list.size();i++){
			list.set(i, toBase(list.get(i), base));
		}
	}
	
	//Method for converting a String into a List of Characters
	public static List<Character> stringtoChar(String s){
		List<Character> list = new ArrayList<Character>();
		for(int i=0;i<s.length();i++){
			list.add(s.charAt(i));
		}
		return list;
	}
	
	//Method for shifting back letters of the alphabet by the second parameter k.
	//E.g. If 'd' is shifted back by 2, the method would return 'b'.
	//Precondition: Character in the list must be a letter from the alphabet + numbers + , . ! ? ' " and no other special characters may be used
	public static void translate(List<Character> list, int k){
		
		if(k > 26) throw new IllegalArgumentException("k > 26");
		for(int i=0;i<list.size();i++){
			boolean isNumber = false;
			if(list.get(i) == ' '  || list.get(i) == ',' || list.get(i) == '.' || list.get(i) == '!' || list.get(i) == '?' || list.get(i) == '\'' || list.get(i) == '\"') continue;
			for(int j=1;j<=9;j++){
				if(list.get(i) == Integer.toString(j).charAt(0)){
					isNumber = true;
				}
			}
			if(isNumber) continue;
			if(list.get(i) > 96 && list.get(i) - k < 91){
				list.set(i, (char)(list.get(i)+26-k));
			}else{
				list.set(i, (char)(list.get(i)-k));	
			}
			if(list.get(i) < 65){
				list.set(i, (char)(list.get(i)+26));	
			}
			if(list.get(i) > 90 && list.get(i) < 97){
				list.set(i, (char)(list.get(i)+26));	
			}
		}
	}
}
