package bindecdiagram;

public class HashCreator {

	private static HashCreator instance;
	
	private HashCreator() {}
	
	public static HashCreator instance() {
		if(instance == null) instance = new HashCreator();
		return instance;
	}
	
	public int hash(String key, int tableSize) {
		
		long sum = 0;
		
		for(int i = 0; i < key.length(); i++) sum += ((int) key.charAt(i)) * (sum + 1) + 31 * i;
		return Math.abs((int)(23 * sum + 197) % tableSize);
	}
}


