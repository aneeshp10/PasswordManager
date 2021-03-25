import java.util.NoSuchElementException;

import java.util.LinkedList;

/**
 * This class models and implements a HashTable KeyType key and ValueType value
 * 
 * @author Aneesh Patil
 */
public class HashTableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

	/**
	 * This class creates instance variables and constructors for the HashEntry to
	 * be used while adding elements
	 * 
	 * @author Aneesh Patil
	 */
	class HashEntry<KeyType, ValueType> {
		private KeyType key;
		private ValueType value;
		private HashEntry<KeyType, ValueType> next;

		/**
		 * Constructor that assigns values to instance variables
		 * 
		 */
		public HashEntry(KeyType key, ValueType value) {
			this.key = key;
			this.value = value;
			this.next = null;
		}
	}

	private int capacity;
	private int size;
	private HashEntry<KeyType, ValueType>[] hashtable;

	/**
	 * Constructs an empty array with the initial capacity as passed to constructor
	 * 
	 */
	public HashTableMap(int capacity) {
		this.capacity = capacity;
		this.hashtable = new HashEntry[this.capacity];
	}

	/**
	 * Constructs an empty array with the initial capacity 10
	 * 
	 */
	public HashTableMap() {
		// assign capacity of 10 if uninitialized
		this.capacity = 10;
		this.hashtable = new HashEntry[this.capacity];
	}

	@Override
	/**
	 * This method adds a new key-value pair to the HashTableMap. This method calls
	 * the hashFunction() method which hashes the key based on the capacity. This
	 * method checks if the current size is exceeding the load factor ratio. If it
	 * is a new array with double capacity is created and rehashed with new
	 * capacity. Based on the returned hash index, the key-value pair is stored in
	 * that position. If there is a duplicate key, HashTable is unchanged and
	 * returns false. If two different keys have same hash index (collision),
	 * LinkedLists are used to store the key-value pairs also known as chaining.
	 * 
	 * @param key: the key to be stored that will be hashed value
	 * @param: value: the value associated with the key
	 * 
	 * @return true if key-value pair is added false if key-value pair is not added
	 *         due to duplicate key
	 */
	public boolean put(KeyType key, ValueType value) {

		int hash = calcIndex(key); // store hash key's hash value
		HashEntry<KeyType, ValueType> addPair = new HashEntry<KeyType, ValueType>(key, value);

		// checks if load factor exceeded, double and rehash if true
		if (checkEightyPercentCap() == true) {
			resize();
		}

		// if index is null add the pair increment size
		if (hashtable[hash] == null) {
			hashtable[hash] = addPair;
			size++;
			if (checkEightyPercentCap() == true) {
				resize();
			}
			return true;

			// if duplicate return false
		} else if (key.equals(hashtable[hash].key)) {
			if (checkEightyPercentCap() == true) {
				resize();
			}
			return false;
		}

		// different key, same hashcode, chaining using linkedlist
		else {
			HashEntry<KeyType, ValueType> curr = hashtable[hash]; // store first element in LinkedList in variable
																	// 'curr'

			// traverse through the LinkedList till last element
			while (curr.next != null) {
				// if key being added is same as a pre-existing key, return false and don't
				// change anything in HashTable
				curr = curr.next;
				if (key.equals(curr.key)) {
					return false;
				}
			}
			curr.next = addPair; // store the new key-value pair at the tail
			size++; // increment size
			if (checkEightyPercentCap() == true) {
				resize();
			}
			return true;

		}
	}

	@Override
	/**
	 * This method returns the value associated with a particular key passed to the
	 * method. Key is hashed to check for array's index and compare the key at the
	 * given index with key passed. If there's chaining at the given index, the
	 * method traverses through the list till it finds the key and return the value
	 * associated. If the hashed index is null or the passed key does not exist the
	 * method throws NoSuchElementException.
	 * 
	 * 
	 * @param: key: key whose value is to be returned
	 * 
	 * @return value associated with the key of type ValueType
	 * 
	 * @throws NoSuchElementException
	 */
	public ValueType get(KeyType key) throws NoSuchElementException {
		int hash = calcIndex(key); // calculate index (modulus hash function)

		// throw NoSuchElementException if calculated index is null
		if (hashtable[hash] == null) {
			throw new NoSuchElementException();
		}

		HashEntry<KeyType, ValueType> curr = hashtable[hash].next;

		// return value if key at index equals key passed to method
		if (hashtable[hash].key.equals(key)) {
			return hashtable[hash].value;
		}
		// else traverse through the chain, throw NoSuchElementException if not found
		else {
			while (curr != null) {
				if (curr.key.equals(key)) {
					return curr.value;
				} else {
					curr = curr.next;
				}
			}
		}

		throw new NoSuchElementException();
	}

	@Override
	/**
	 * Returns the number of key-value pairs
	 * 
	 * @return amount of pairs present in the array
	 */
	public int size() {
		return this.size;
	}

	@Override
	/**
	 * This method checks if a particular key exists in the HashTable or not.
	 * 
	 * @param key: the key to be checked if it exists
	 * 
	 * @return true if key exists, false otherwise
	 */
	public boolean containsKey(KeyType key) {
		int hash = calcIndex(key);

		if (hashtable[hash] == null) {
			return false;
		}

		// check if key at index equals key passed
		if (hashtable[hash].key.equals(key)) {
			return true;
		}

		// traverse through chain looking for key, return false if not found
		else {
			HashEntry<KeyType, ValueType> nxtcurr = hashtable[hash].next;
			while (nxtcurr != null) {
				if (nxtcurr.key.equals(key)) {
					return true;
				} else {
					nxtcurr = nxtcurr.next;
				}
			}
			return false;
		}

	}

	@Override
	/**
	 * Removes the key-value pair having given the particular key.
	 * 
	 * @param key: the key to be removed
	 * 
	 * @return value associated with the key being removed
	 */
	public ValueType remove(KeyType key) {
		// TODO Auto-generated method stub

		int hash = calcIndex(key);
		HashEntry<KeyType, ValueType> curr = hashtable[hash];

		// if the element doesn't exist return null
		if (!containsKey(key)) {
			return null;
		}

		// if key is at the given index without chaining, store value of the key in
		// valOut, reassign head of the linkedList, decrement size
		if (curr.key.equals(key)) {
			ValueType valOut = curr.value;
			hashtable[hash] = curr.next;
			size--;
			return valOut;
		}
		// if chaining exists, traverse through the list till key is found and remove,
		// decrement size
		else {
			while (curr.next != null) {
				if (curr.key.equals(key)) {
					ValueType valOut = curr.value;
					curr = curr.next;
					size--;
					return valOut;

				} else {
					curr = curr.next;
				}
			}
		}

		return null;
	}

	@Override
	/**
	 * This method clears the entire HashTable by creating a new empty array and
	 * reinitializing size to be zero
	 */
	public void clear() {
		// TODO Auto-generated method stub
		hashtable = new HashEntry[this.capacity];
		size = 0;

	}

	/**
	 * This method checks if the array has to be doubled or not. Calculates load
	 * factor ratio by size/capacity and if it exceeds 0.8 returns true, false
	 * otherwise.
	 * 
	 * @return true if HashTable has exceeded load factor, false otherwise
	 */
	private boolean checkEightyPercentCap() {
		double loadFactor = ((double) size) / this.capacity; // calculate current load factor
		// return true if it's greater than or equal to 80%
		if (loadFactor >= 0.8) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method doubles the capacity of the array while also rehashing the
	 * pre-existing elements and adding them to the new array. Creates a new array
	 * with double the current capacity, traverses through current HashTable to
	 * calculate new indexes and storing them in newly made table. Reassign instance
	 * variables capacity and hashtable in the end
	 */
	private void resize() {

		int newSize = this.capacity * 2;
		this.capacity = newSize;

		HashEntry<KeyType, ValueType>[] newTable = new HashEntry[newSize];
		for (HashEntry<KeyType, ValueType> ent : this.hashtable) {
			if (ent != null) {
				while (ent != null) {
					int newIndex = calcIndex(ent.key);
					if (newTable[newIndex] == null) {
						newTable[newIndex] = new HashEntry<KeyType, ValueType>(ent.key, ent.value);
					} else {
						HashEntry<KeyType, ValueType> newEntry = newTable[newIndex];
						while (newEntry.next != null) {
							newEntry = newEntry.next;
						}
						newEntry.next = new HashEntry<KeyType, ValueType>(ent.key, ent.value);
					}
					if (ent.next == null) {
						break;
					}
					ent = ent.next;
				}

			}
		}

		this.hashtable = newTable;
	}

	/**
	 * This method calculates the index for a particular key-value pair to be stored
	 * using the modulo operator. The key is first hashed using Java's in-built
	 * .hashCode() method. Taking the modulus (remainder) of the positive value of
	 * the hashcode with the capacity gives the index.
	 * 
	 * @param key: the key to be hashed
	 * @return index the key-value pair will be stored
	 */
	private int calcIndex(KeyType key) {
		int hash = (Math.abs(key.hashCode()) % this.capacity);
		return hash;
	}

}
