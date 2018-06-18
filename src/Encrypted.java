import java.util.*;

//This class was created so that both a decryption key and the encrypted message (List) can be returned
//and only contains straightforward accessor and mutator methods
public class Encrypted {
	public String key;
	private List<Integer> code;
	
	public Encrypted(){
		key = "";
		code = new ArrayList<Integer>();
	}
	
	public void setCode(List<Integer> list){
		code = new ArrayList<Integer>(list);
	}
	
	public List<Integer> getCode(){
		return code;
	}
	
	public void printCode(){
		System.out.println(code);
	}
	
	public String getKey(){
		return key;
	}
	
	public void setKey(String s){
		key = s;
	}
	
	public void addKey(String s){
		setKey(getKey() + s + "\n");
	}
	
	public void printKey(){
		System.out.println(key);
	}
}
