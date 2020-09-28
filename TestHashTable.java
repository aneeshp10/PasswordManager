import java.util.NoSuchElementException;

// --== CS400 File Header Information ==--
// Name: Aneesh Patil
// Email: apatil6@wisc.edu
// Team: GA
// TA: Daniel Kiel
// Lecturer: Gary Dahl
// Notes to Grader: NA

public class TestHashTable {

	/**
	 * This method checks mainly for the correctness of the HashTableMap. A new
	 * HashTableMap is created with types Integer, Integer. Checks for put method
	 * specifically
	 * 
	 * @return true when this test verifies a correct functionality, and false
	 *         otherwise
	 */
	public static boolean test1() {

		HashTableMap<Integer, Integer> table = new HashTableMap<Integer, Integer>(10);

		// check by adding Integer, Integer key-value pairs
		if (!table.put(6, 8)) {
			return false;
		}
		if (!table.put(8, 25)) {
			return false;
		}
		if (!table.put(9, 35)) {
			return false;
		}
		if (!table.put(10, 8)) {
			return false;
		}
		if (!table.put(15, 25)) {
			return false;
		}
		if (!table.put(55, 25)) {
			return false;
		}

		if (table.size() != 6) {
			return false;
		}

		// check for duplicates
		if (table.put(15, 17638)) {
			return false;
		}
		if (table.put(55, 3)) {
			return false;
		}

		if (table.size() != 6) {
			return false;
		}

		// check by adding String, Integer key-value pairs
		HashTableMap<String, Integer> hashtable2 = new HashTableMap<String, Integer>(10);
		if (!hashtable2.put("John", 1234)) {
			return false;
		}
		if (!hashtable2.put("Jack", 1235)) {
			return false;
		}
		if (!hashtable2.put("Aneesh", 124)) {
			return false;
		}
		if (!hashtable2.put("Gary", 12346)) {
			return false;
		}
		if (!hashtable2.put("Kingsley", 234)) {
			return false;
		}
		if (!hashtable2.put("Lionel", 234)) {
			return false;
		}

		if (hashtable2.size() != 6) {
			return false;
		}

		if (hashtable2.put("Lionel", 1)) {
			return false;
		}

		if (hashtable2.size() != 6) {
			return false;
		}

		return true;
	}

	/**
	 * This method checks mainly for the correctness of the HashTableMap. A new
	 * HashTableMap is created with types Integer, Integer. Checks for containsKey
	 * method specifically
	 * 
	 * @return true when this test verifies a correct functionality, and false
	 *         otherwise
	 */
	public static boolean test2() {
		HashTableMap<Integer, Integer> pairArray = new HashTableMap<Integer, Integer>(5);
		pairArray.put(50, 792383);
		pairArray.put(55, 1762738);
		pairArray.put(65, 792383);
		pairArray.put(75, 17638);
		pairArray.put(85, 7923);
		pairArray.put(1, 138);

		if (pairArray.size() != 6) {
			return false;
		}

		if (!pairArray.containsKey(50)) { // exists
			return false;
		}

		if (!pairArray.containsKey(55)) { // exists
			return false;
		}

		if (pairArray.containsKey(2)) { // doesn't exist
			return false;
		}

		if (pairArray.containsKey(1000)) {
			return false;
		}

		if (pairArray.containsKey(Integer.MAX_VALUE)) {
			return false;
		}

		HashTableMap<Integer, String> secondPairArray = new HashTableMap<Integer, String>(15);
		secondPairArray.put(12345, "Football");
		secondPairArray.put(123, "Soccer");
		secondPairArray.put(12, "Hockey");
		secondPairArray.put(122345, "Basketball");
		secondPairArray.put(2345, "Cricket");
		if (!secondPairArray.containsKey(12345)) {
			return false;
		}
		if (!secondPairArray.containsKey(12)) {
			return false;
		}
		if (!secondPairArray.containsKey(2345)) {
			return false;
		}

		return true;
	}

	/**
	 * This method checks mainly for the correctness of the HashTableMap. A new
	 * HashTableMap is created with types Integer, Integer. Checks for get() method
	 * specifically
	 * 
	 * @return true when this test verifies a correct functionality, and false
	 *         otherwise
	 */
	public static boolean test3() {
		// Subtest 1 with Integer, String
		HashTableMap<Integer, String> pairArray = new HashTableMap<Integer, String>(15);
		pairArray.put(12345, "Football");
		pairArray.put(123, "Soccer");
		pairArray.put(12, "Hockey");
		pairArray.put(122345, "Basketball");
		pairArray.put(2345, "Cricket");

		if (pairArray.size() != 5) {
			return false;
		}

		if (!pairArray.get(2345).equals("Cricket")) {
			return false;
		}

		if (!pairArray.get(12).equals("Hockey")) {
			return false;
		}

		if (!pairArray.get(12345).equals("Football")) {
			return false;
		}

		// attempting to get invalid keys
		try {
			pairArray.get(11);
		} catch (NoSuchElementException e) {
			return true;
		}
		// attempting to get invalid keys
		try {
			pairArray.get(99);
		} catch (NoSuchElementException e) {
			return true;
		}

		// Subtest 2 with String, Double
		HashTableMap<String, Double> pairArray2 = new HashTableMap<String, Double>(15);
		pairArray2.put("Drake", 42.5);
		pairArray2.put("Weeknd", 35.9);
		pairArray2.put("KSI", 99.67);
		pairArray2.put("Stormzy", 1567.5);
		pairArray2.put("Travis Scott", 2.5);

		if (pairArray2.size() != 5) {
			return false;
		}

		if (pairArray2.get("Drake") != 42.5) {
			return false;
		}
		if (pairArray2.get("Weeknd") != 35.9) {
			return false;
		}
		if (pairArray2.get("Travis Scott") != 2.5) {
			return false;
		}

		try {
			pairArray2.get("Kanye West");
		} catch (NoSuchElementException e) {
			return true;
		}
		try {
			pairArray2.get("Jay-z");
		} catch (NoSuchElementException e) {
			return true;
		}
		try {
			pairArray2.get("Ed Sheeran");
		} catch (NoSuchElementException e) {
			return true;
		}

		return true; // test passed
	}

	/**
	 * This method checks mainly for the correctness of the HashTableMap. A new
	 * HashTableMap is created with types String, String. Checks remove method
	 * specifically
	 * 
	 * @return true when this test verifies a correct functionality, and false
	 *         otherwise
	 */
	public static boolean test4() {

		// adding String, String key-value pairs to hashtable
		HashTableMap<String, String> pairArray = new HashTableMap<String, String>(15);
		pairArray.put("Odell Beckham Jr", "Football");
		pairArray.put("Lionel Messi", "Soccer");
		pairArray.put("Bobby Orr", "Hockey");
		pairArray.put("Stephen Curry", "Basketball");
		pairArray.put("Sachin Tendulkar", "Cricket");
		pairArray.put("Cristiano Ronaldo", "Soccer");
		pairArray.put("Lebron James", "Basketball");
		pairArray.put("Tom Brady", "Football");

		// check current size
		if (pairArray.size() != 8) {
			return false;
		}

		// remove an element and check the value
		if (!pairArray.remove("Lionel Messi").equals("Soccer")) {
			return false;
		}

		// confirm if size decremented by one
		if (pairArray.size() != 7) {
			return false;
		}

		if (!(pairArray.remove("Bobby Orr").equals("Hockey"))) {
			return false;
		}

		// attempting to remove invalid keys
		try {
			pairArray.remove("Adele");
		} catch (NoSuchElementException e) {
			return true;
		}
		// attempting to remove invalid keys
		try {
			pairArray.remove("John Legend");
		} catch (NoSuchElementException e) {
			return true;
		}
		// attempting to remove invalid keys

		try {
			pairArray.remove("KSI");
		} catch (NoSuchElementException e) {
			return true;
		}
		// attempting to remove valid key through try-catch
		try {
			pairArray.remove("Odell Beckham Jr");
		} catch (NoSuchElementException e) {
			return false;
		}

		// checking final size in the end
		if (pairArray.size() != 5) {
			return false;
		}

		return true;
	}

	/**
	 * This method checks mainly for the correctness of the HashTableMap. A new
	 * HashTableMap is created with types String, String. Checks clear method
	 * specifically
	 * 
	 * @return true when this test verifies a correct functionality, and false
	 *         otherwise
	 */
	public static boolean test5() {
		HashTableMap<String, Double> pairArray = new HashTableMap<String, Double>(10);
		pairArray.put("Odell Beckham Jr", 35.6);
		pairArray.put("Lionel Messi", 78.4);
		pairArray.put("Bobby Orr", 98.6);
		pairArray.put("Stephen Curry", 98.611);
		pairArray.put("Sachin Tendulkar", 298.6);
		pairArray.put("Cristiano Ronaldo", 9222.6);
		
		pairArray.clear();
		
		if (pairArray.size() != 0) {
			return false;
		}
		pairArray.put("Lebron James", 5698.6);
		pairArray.put("Tom Brady", 3234.0);

		if (pairArray.containsKey("Sachin Tendulkar")) {
			return false;
		}

		if (pairArray.size() != 2) {
			return false;
		}

		pairArray.clear();

		if (pairArray.size() != 0) {
			return false;
		}

		return true;
	}

	public static void main(String[] args) {
		System.out.println("Test1 Status: " + test1());
		System.out.println("Test2 Status: " + test2());
		System.out.println("Test3 Status: " + test3());
		System.out.println("Test4 Status: " + test4());
		System.out.println("Test5 Status: " + test5());
	}

}
