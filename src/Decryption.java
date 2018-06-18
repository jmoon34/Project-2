import java.util.*;
import java.util.Map.Entry;
public class Decryption {
	
	//Method for decrypting an Encrypted object according to the key parameter of the object
	public static String MasterDecryption(Encrypted msg){
		boolean translated = false;
		boolean basechanged = false;
		boolean permuted = false;
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		List<Integer> permutation = new ArrayList<Integer>();
		Scanner keyreader = new Scanner(msg.getKey());
		int linecount = 1;
		int translatefactor = 0;
		int base = 0;
		while(keyreader.hasNextLine()){
			String line = keyreader.nextLine();
			Scanner linereader = new Scanner(line);
			if(linecount == 2){
				map = readMap(linereader);
			}
			while(linereader.hasNext()){
				String token = linereader.next();
				if(token.equals("translated")){
					translated = true;
					translatefactor = linereader.nextInt();
					System.out.println("translation factor: " + translatefactor);
				}
				if(token.equals("toDecimal")){
					basechanged = true;
					base = linereader.nextInt();
					System.out.println("changed from base: " + base);
				}
				if(token.equals("Permute")){
					permuted = true;
					permutation = readList(keyreader);
				}
			}
			linereader.close();
			linecount++;
		}
		keyreader.close();
		System.out.println("Permutation: " + permutation);
		System.out.println("Steps");
		System.out.println(msg.getCode());
		if(permuted){
			InversePermutation(msg.getCode(),permutation);
			System.out.println(msg.getCode());
		}
		if(basechanged){
			toDecimal(msg.getCode(), base);
			System.out.println(msg.getCode());
		}
		List<Character> clist = inttoChar(msg.getCode(), map);
		System.out.println(clist);
		if(translated){
			translate(clist, translatefactor);
			System.out.println(clist);
		}
		return chartoString(clist);
	}
	
	//Method that generates the identity permutation for a given permutation
	//The identity permutation returns a permuted List of objects back into its original sequence
	public static List<Integer> InversePermutation(List<Integer> permutation){
		for(int i=1;i<=(permutation.size()-1)/2;i++){
			int temp = permutation.get(i);
			permutation.set(i, permutation.get(permutation.size()-i));
			permutation.set(permutation.size()-i,temp);
		}
		return permutation;
	}
	
	//Overloaded version of the method above modified to take in a List as a parameter and undoes the permutation
	public static void InversePermutation(List<Integer> array, List<Integer> permutation){
		InversePermutation(permutation);
		Encryption.permute(array, permutation);
	}
	
	//Three methods using overloading and recursion to convert an integer given in base n back into base 10
	public static int toDecimal(int n, int base){
		return toDecimal(n, base, 0);
	}
	
	public static int toDecimal(int n, int base, int count){
		if(n == 0) return 0;
		int coeff = n%10;
		return (int)(coeff*Math.pow(base, count)) + toDecimal(n/10, base, count+1);
	}
	
	public static void toDecimal(List<Integer> list, int base){
		for(int i=0;i<list.size();i++){
			list.set(i, toDecimal(list.get(i), base));
		}
	}

	//Method that reverses a Character -> Integer map and returns a Integer -> Character map
	public static Map<Integer, Character> reverseMap(Map<Character, Integer> map){
		Map<Integer, Character> reverseMap = new HashMap<Integer, Character>();
		Set<Entry<Character, Integer>> entrySet = map.entrySet();
		Iterator<Entry<Character, Integer>> itr = entrySet.iterator();
		while(itr.hasNext()){
			Entry<Character, Integer> e = itr.next();
			reverseMap.put(e.getValue(), e.getKey());
		}
		return reverseMap;
	}
	
	//Method that maps a List of integers to a List of Character according to the given parameter map
	public static List<Character> inttoChar(List<Integer> list, Map<Character, Integer> map){
		Map<Integer, Character> reverseMap = reverseMap(map);
		List<Character> c = new ArrayList<Character>();
		for(int i=0;i<list.size();i++){
			c.add(reverseMap.get(list.get(i)));
		}
		return c;
	}
	
	//Method that returns a String that contains the combination of Characters in the passed List parameter
	public static String chartoString(List<Character> c){
		String s = "";
		for(int i=0;i<c.size();i++){
			s += c.get(i).toString();
		}
		return s;
	}
	
	//Method that uses a Scanner that reads a string and returns a map from it
	public static Map<Character, Integer> readMap(Scanner s){
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		String line = s.nextLine();
		line = line.substring(1, line.length());
		Scanner s2 = new Scanner(line);
		map.put(' ', 62);
		s2.next();
		while(s2.hasNext()){
			String token = s2.next();
			token = token.substring(0, token.length()-1);
			int n = 0;
			if(token.length() == 4){
				n = Integer.parseInt(token.substring(2,4));
			} else{
				n = Integer.parseInt(token.substring(2,3));
			}
			map.put(token.charAt(0), n);
			
		}
		s2.close();
		return map;
	}
	
	//Analogous to the method above, this method also reads a String with a scanner and returns a List
	public static List<Integer> readList(Scanner s){
		List<Integer> list = new ArrayList<Integer>();
		String line = s.nextLine();
		line = line.substring(1, line.length());
		Scanner s2 = new Scanner(line);
		while(s2.hasNext()){
			String token = s2.next();
			token = token.substring(0, token.length()-1);
			int n = 0;
			if(token.length() == 2){
				n = Integer.parseInt(token.substring(0,2));
			} else{
				n = Integer.parseInt(token.substring(0,1));
			}
			list.add(n);
		}
		s2.close();
		return list;
	}
	
	//Method that undoes the translation done in Encryption.translate
	public static void translate(List<Character> clist, int k){
		Encryption.translate(clist, 26-k);
	}
}
