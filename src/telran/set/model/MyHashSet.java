package telran.set.model;

import java.util.Iterator;
import java.util.LinkedList;

public class MyHashSet<E> implements ISet<E> {

	private LinkedList<E>[] hashset;
	private int size;
	private int capacity;
	private double loadFactor;

	@SuppressWarnings("unchecked")
	public MyHashSet(int capacity, double loadFactor) {
		this.hashset = new LinkedList[capacity];
		this.capacity = capacity;
		this.loadFactor = loadFactor;
	}

	public MyHashSet(int capacity) {
		this(capacity, 0.75);
	}

	public MyHashSet() {
		this(16, 0.75);
	}

	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {


			int index = 0, hashIndex = 0, counter = 0;
			@Override
			public boolean hasNext() {


				return  counter < size;
			}

			@Override
			public E next() {

				if (index == 15)
					index = 15;
				if(hashset[index] != null && hashIndex < hashset[index].size())
					hashIndex++;

				while ((index  < capacity && (hashset[index] == null || hashset[index].size() == 0)) || hashIndex == hashset[index].size()) {
					if (hashIndex != 0)
							hashIndex = 0;
					index++;
				}

				E data = hashset[index].get(hashIndex);
				counter++;
				return data;
			}
		};
	}

	@Override
	public boolean add(E element) {
		if (size >= capacity * loadFactor) {
			rebuildArray();
		}
		int index = getIndex(element);
		if (hashset[index] == null) {
			hashset[index] = new LinkedList<>();
		}
		if (hashset[index].contains(element)) {
			return false;
		}
		hashset[index].add(element);
		size++;
		return true;
	}

	private void rebuildArray() {
		capacity = capacity * 2;
		LinkedList<E>[] newHashSet = new LinkedList[capacity];
		for (int i = 0; i < hashset.length; i++) {
			if (hashset[i] != null) {
				for (E e : hashset[i]) {
					int index = getIndex(e);
					if (newHashSet[index] == null) {
						newHashSet[index] = new LinkedList<>();
					}
					newHashSet[index].add(e);
				}
			}
		}
		hashset = newHashSet;
	}

	private int getIndex(E element) {
		int hashCode = element.hashCode();
		hashCode = hashCode >= 0 ? hashCode : -hashCode;
		return hashCode % capacity;
	}

	@Override
	public boolean remove(E element) {
		int index = getIndex(element);
		if (hashset[index] == null) {
			return false;
		}
		boolean res = hashset[index].remove(element);
		if (res) {
			size--;
		}
		return res;
	}

	@Override
	public boolean contains(E element) {
		int index = getIndex(element);
		if (hashset[index] == null) {
			return false;
		}
		return hashset[index].contains(element);
	}

	@Override
	public int size() {
		return size;
	}

}
